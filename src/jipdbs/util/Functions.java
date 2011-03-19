package jipdbs.util;

import org.datanucleus.util.StringUtils;

public class Functions {

	private static final Integer IP_SEED = 569;
	
	public static final String maskIpAddress(String ip) {
		if (ip.isEmpty()) return "";
		String[] parts = StringUtils.split(ip, ".");
		Integer n = IP_SEED;
		n+=Integer.parseInt(parts[0]);
		n-=Integer.parseInt(parts[1]);
		n+=Integer.parseInt(parts[2]);
		n-=Integer.parseInt(parts[3]);
		n = Math.abs(n);
		return parts[0] + "." + parts[1] + "." + parts[2] + "." + Integer.toHexString(n).toUpperCase();
	}
	
	public static void main(String[] args) {
		System.out.println(maskIpAddress("127.0.0.1"));
		System.out.println(maskIpAddress("192.168.50.99"));
		System.out.println(maskIpAddress("190.210.25.185"));
		System.out.println(maskIpAddress("190.210.1.999"));
	}
}
