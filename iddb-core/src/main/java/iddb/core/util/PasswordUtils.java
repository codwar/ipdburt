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

import java.util.Random;

import org.apache.commons.lang.StringUtils;

public final class PasswordUtils {

	private static final String charset = "0123456789abcdefghijklmnopqrstuvwxyz";
	
	public static boolean checkPassword(String raw_password, String encoded_password) {
		if (StringUtils.isEmpty(encoded_password) || StringUtils.isEmpty(raw_password)) return false;
		if (!encoded_password.contains("$")) return false;
		// get the salt from the encoded password
		String[] part = StringUtils.split(encoded_password, "$");
		String hashPassword = GuidGenerator.getSHA1Hash(part[0] + raw_password);
		return (hashPassword.equals(part[1]));
	}
	
	public static String hashPassword(String raw_password) {
		Random random = new Random(raw_password.length() + System.currentTimeMillis());
		String salt = StringUtils.left(GuidGenerator.getSHA1Hash(Float.toHexString(random.nextFloat()) + Float.toHexString(System.currentTimeMillis())),5);
		String hashedPassword = GuidGenerator.getSHA1Hash(salt + raw_password);
		return salt + "$" + hashedPassword;
	}
	
    public static String getRandomString() {
    	int length = 10;
        Random rand = new Random(System.currentTimeMillis());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }
        return sb.toString();
    }
	
}
