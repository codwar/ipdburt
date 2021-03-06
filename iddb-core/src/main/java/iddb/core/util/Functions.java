/**
 *   Copyright(c) 2010-2011 CodWar Soft
 * 
 *   This file is part of IPDB UrT.
 *
 *   IPDB UrT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This software is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this software. If not, see <http://www.gnu.org/licenses/>.
 */
package iddb.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class Functions {

	private static int IP_SEED = 111;

	static {
		// A cheap way to obtain a seed with a per deployed version lifecycle.
		try {
			String build = SystemProperties.applicationVersion().getBuild();
			if (!build.equals("0")) {
				IP_SEED = Integer.parseInt(StringUtils.right(build, 4)) % 1000;
			}
		} catch (Exception e) {
			// Swallow, will keep 111.
		}
	}

	public static final String maskIpAddress(String ip) {
		if (ip == null || ip.length() == 0)
			return "";
		String[] parts = ip.split("\\.");
		Integer n = IP_SEED;
		n += Integer.parseInt(parts[0]);
		n -= Integer.parseInt(parts[1]);
		n += Integer.parseInt(parts[2]);
		n -= Integer.parseInt(parts[3]);
		n = Math.abs(n);
		return parts[0] + "." + parts[1] + "." + parts[2] + "."
				+ Integer.toHexString(n).toUpperCase();
	}

	public static final Long ipToDecimal(String ip) {
		if (ip == null || ip.length() == 0)
			return 0l;
		String[] parts = ip.split("\\.");
		Long n = 16777216 * Long.parseLong(parts[0]);
		n += 65536 * Long.parseLong(parts[1]);
		n += 256 * Long.parseLong(parts[2]);
		n += Long.parseLong(parts[3]);
		return n;
	}

	public static final String decimalToIp(Long number) {
		if (number == null || number.equals(0l))
			return "";
		StringBuilder s = new StringBuilder();
		s.append(number / 256 / 65536);
		s.append(".");
		s.append((number / 65536) % 256);
		s.append(".");
		s.append((number / 256) % 256);
		s.append(".");
		s.append(number % 256);
		return s.toString();
	}

	public static String fixIp(String query, boolean range) {
		String[] parts = query.split("\\.");
		String[] r = new String[4];
		int l;
		if (range) l = 3;
		else l = 4;
		for (int i = 0; i < l; i++) {
			if (i < parts.length)
				r[i] = parts[i];
			else
				r[i] = "*";
		}
		if (range) r[3] = "*";	
		if ("*".equals(r[0]))
			return "0.0.0.0";
		return join(r, ".");
	}

	public static String join(Collection<String> list, String token) {
		return join(list.toArray(new String[0]), token);
	}
	
	public static String join(String[] list, String token) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.length; i++) {
			builder.append(list[i]);
			if (i < (list.length - 1))
				builder.append(token);
		}
		return builder.toString();
	}

	public static Long[] getIpRange(String query) {
		Long[] result = new Long[2];
		if (query.contains("*")) {
			String start = query.replaceAll("\\*", "0");
			String end = query.replaceAll("\\*", "255");
			result[0] = ipToDecimal(start);
			result[1] = ipToDecimal(end);
		} else {
			result[0] = ipToDecimal(query);
			result[1] = result[0];
		}
		return result;
	}

	public static List<Integer> range(int min, int max) {
		return range(min, max, 0);
	}

	public static List<Integer> range(int min, int max, int sum) {
		List<Integer> list = new LinkedList<Integer>();
		if (max > 0) {
			for (int i = min; i < max; i++) {
				list.add(new Integer(i + sum));
			}
		} else {
			for (int i = min; i > max; i--) {
				list.add(new Integer(i + sum));
			}
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List sublist(List list, int size) {
		List result = new ArrayList();
		for (int i : Range.range(0, list.size(), size)) {
			if (i + size > list.size() ) {
				result.add(list.subList(i, i+ (list.size() - i)));	
			} else {
				result.add(list.subList(i, i + size));	
			}
		}
		return result;
	}
	
	public static Long time2minutes(String time) {
		if (time == null || "".trim().equals(time)) return 0L;
		
		Float value;
		
		char last = time.charAt(time.length()-1);
		try {
			switch (last) {
			case 'h':
				value = Float.parseFloat(time.substring(0, time.length()-1)) * 60;
				break;
			case 'm':
				value = Float.parseFloat(time.substring(0, time.length()-1));
				break;
			case 's':
				value = Float.parseFloat(time.substring(0, time.length()-1)) / 60;
				break;
			case 'd':
				value = Float.parseFloat(time.substring(0, time.length()-1)) * 60 * 24;
				break;
			case 'w':
				value = Float.parseFloat(time.substring(0, time.length()-1)) * 60 * 24 * 7;
				break;
			case 'M':
				value = Float.parseFloat(time.substring(0, time.length()-1)) * 60 * 24 * 31;
				break;
			case 'y':
				value = Float.parseFloat(time.substring(0, time.length()-1)) * 60 * 24 * 365;
				break;				
			default:
				value = Float.parseFloat(time);
				break;
			}
		} catch (NumberFormatException e) {
			value = 0f;
		}
		return value.longValue();
	}
	
	public static String minutes2Str(Long minutes) {
		Float num;
		String suffix = "";
		if (minutes < 60) {
			num = new Float(minutes);
		} else if (minutes < 1440) {
			num = Float.valueOf(minutes) / 60;
			suffix = "h";
		} else if (minutes < 10080) {
			num = Float.valueOf(minutes) / 1440;
			suffix = "d";
		} else if (minutes < 525600) {
			num = Float.valueOf(minutes) / 10080;
			suffix = "w";
		} else {
			num = Float.valueOf(minutes) / 525600;
			suffix = "y";
		}
		if ("0".equals(num.toString().substring(num.toString().indexOf(".")+1))) {
			return String.format("%d%s", num.intValue(), suffix);
		}
		return String.format("%.2g%s", num, suffix); // 2=1 decimal
	}

	public static String normalizeCleanUp(String text) {
		char[] v = text.toLowerCase().toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < v.length ; i++) {
			char c = v[i];
			char r;
			switch (c) {
			case ' ':
			case '`':
			case '*':
			case '>':
			case '<':
			case '{':
			case '}':
			case '[':
			case ']':
			case '.':
			case ',':
			case ':':
			case '_':
			case '-':
			case '"':
			case '\'':
			case '#':
				r = '\b';
				break;
			default:
				r = c;
			}
			if (r != '\b') {
				if (i > 0) {
					if (c != v[i-1]) {
						sb.append(r);
					}
				} else {
					sb.append(r);
				}
			}
		}
		return sb.toString();		
	}
	
	public static String normalizeReplace(String text) {
		char[] v = text.toLowerCase().toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < v.length ; i++) {
			char c = v[i];
			char r;
			switch (c) {
			case '@':
				r = 'a';
				break;
			case '|':
			case '!':
				r = 'i';
				break;
			case '&':
				r = 'y';
				break;
			case '0':
				r = 'o';
				break;
			case '1':
				r = 'i';
				break;
			case '3':
				r = 'e';
				break;
			case '4':
				r = 'a';
				break;
			case '5':
				r = 's';
				break;
			case '7':
				r = 't';
				break;
			default:
				r = c;
			}
			if (r != '\b') {
				if (i > 0) {
					if (c != v[i-1]) {
						sb.append(r);
					}
				} else {
					sb.append(r);
				}
			}
		}
		return sb.toString();
	}
	
	public static String normalize(String text) {
		return normalizeReplace(normalizeCleanUp(text));
	}
	
	public static String createNameIndex(String name) {
//		Collection<String> n = NGrams.ngrams(name, Parameters.INDEX_MIN_LENGTH);
//		n.addAll(NGrams.ngrams(Functions.normalize(name), Parameters.INDEX_MIN_LENGTH));
//		n.add(Functions.normalize(name));
//		return Functions.join(new HashSet<String>(n), " ");
		Set<String> n = new HashSet<String>();
		n.add(name.toLowerCase());
		n.add(normalizeCleanUp(name));
		n.add(normalizeReplace(name));
		n.add(normalize(name));
		return join(n.toArray(new String[0]), " ");
	}
	
	public static void main(String[] args) {
		System.out.println(createNameIndex("[uy].Juan"));
		
//		System.out.println(normalize(">pr0.frankillo"));
//		System.out.println(normalize("H'ace"));
//		System.out.println(normalize("Fatal1ty"));
//		System.out.println(normalize("[Her0]-.{T0bA_1"));
//		System.out.println(normalize("pok_lol!!!"));
//		System.out.println(normalize("Ca|*Sniper_Depre"));
//		System.out.println(normalize("P3P3"));
//		System.out.println(normalize("R0S4M0N73"));
//		System.out.println(normalize("[xXxXx]heXen"));
//		System.out.println(normalize("Macca*_____*"));
//		System.out.println(normalize("lVZLAl#VCTR#"));
//		System.out.println(createNameIndex("&amp;amp;gt;WD&amp;amp;gt;Mr.Popo"));
//		String s = "125.68.67.66";
//		Long v = ipToDecimal(s);
//		System.out.println(s);
//		System.out.println(v);
//		System.out.println(decimalToIp(v));
//		System.out.println(getIpRange("127.0.0.*"));
//		System.out.println(range(0, -10));
//
//		for (int i : Range.range(0, 2000, 100)) {
//			System.out.println(i);
//		}
//		
//		List<String> list = new ArrayList<String>();
//		list.add("1");
//		list.add("2");
//		list.add("3");
//		list.add("4");
//		list.add("5");
//		list.add("6");
//		list.add("7");
//		list.add("8");
//		list.add("9");
//		
//		for (Iterator<?> it = sublist(list, 5).iterator(); it.hasNext();) {
//			System.out.println(it.next());
//		}
		
	}
}


