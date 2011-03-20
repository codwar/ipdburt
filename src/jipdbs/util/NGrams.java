package jipdbs.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class NGrams {

	public static List<String> bigrams(String s) {

		if (s.trim().length() < 2)
			return Collections.emptyList();

		List<String> bigrams = new LinkedList<String>();

		for (int i = 1; i < s.length(); i++) {

			char a = s.charAt(i - 1);
			char b = s.charAt(i);

			if (Character.isWhitespace(a) || Character.isWhitespace(b))
				continue;

			String bigram = new String(new char[] { Character.toLowerCase(a),
					Character.toLowerCase(b) });

			bigrams.add(bigram);
		}

		return bigrams;
	}

	public static void main(String[] args) {
		System.out.println(bigrams("[+ter]Shonaka"));
		System.out.println(bigrams("[+ter]Sho naka"));
	}

}
