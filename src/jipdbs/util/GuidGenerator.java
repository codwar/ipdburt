package jipdbs.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.UUID;

public class GuidGenerator {

	/**
	 * Returns a random generated uuid encoded with SHA1
	 * If text is supplied it is used as a 'seed' to generate the hash.
	 * 
	 * @param String
	 * 			text (optional)
	 * @return random generated uuid
	 */
	public static String generate(String text) {
		UUID uuid = UUID.randomUUID();
		StringBuffer buffer = new StringBuffer(uuid.toString());
		if (text != null) {
			buffer.append(text);
		}
		String hash = getSHA1Hash(buffer.toString());
		if (hash == null) {
			hash = uuid.toString();
		}
		return hash;
	}

	/**
	 * Return a SHA1 hash representation of text
	 * @param String
	 * 			text
	 * @return SHA1 hash
	 */
	public static String getSHA1Hash(String text) {
		try {
			MessageDigest md;
			md = MessageDigest.getInstance("SHA1");
			md.update(text.getBytes());
			StringBuffer hash = new StringBuffer();
			for (byte aux : md.digest()) {
				int b = aux & 0xff;
				if (Integer.toHexString(b).length() == 1)
					hash.append("0");
				hash.append(Integer.toHexString(b));
			}
			return hash.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
}
