package jipdbs.util;

import org.datanucleus.util.StringUtils;

public class Functions {

	private static final Integer IP_SEED = 569;
	
	public static final String maskIpAddress(String ip) {
		if (StringUtils.isEmpty(ip)) return "";
		String[] parts = StringUtils.split(ip, ".");
		Integer n = IP_SEED;
		n+=Integer.parseInt(parts[0]);
		n-=Integer.parseInt(parts[1]);
		n+=Integer.parseInt(parts[2]);
		n-=Integer.parseInt(parts[3]);
		n = Math.abs(n);
		return parts[0] + "." + parts[1] + "." + parts[2] + "." + Integer.toHexString(n).toUpperCase();
	}
	
	public static final Long ipToDecimal(String ip) {
		if (StringUtils.isEmpty(ip)) return 0l;
		String[] parts = StringUtils.split(ip, ".");
		Long n = 16777216 * Long.parseLong(parts[0]);
		n+= 65536 * Long.parseLong(parts[1]);
		n+= 256 * Long.parseLong(parts[2]);
		n+= Long.parseLong(parts[3]);
		return n;
	}
	
	public static final String decimalToIp(Long number) {
		if (number == null || number.equals(0l)) return "";
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

	public static Long[] getIpRange(String query) {
		Long[] result = new Long[2];
		if (query.contains("*")) {
			String start = StringUtils.replaceAll(query, "*", "0");
			String end = StringUtils.replaceAll(query, "*", "255");
			result[0] = ipToDecimal(start);
			result[1] = ipToDecimal(end);
		} else {
			result[0] = ipToDecimal(query);
			result[1] = result[0];
		}
		return result;
	}

	
	public static void main(String[] args) {
		String s = "125.68.67.66";
		Long v = ipToDecimal(s);
		System.out.println(s);
		System.out.println(v);
		System.out.println(decimalToIp(v));
		System.out.println(getIpRange("127.0.0.*"));
	}
}
