package jipdbs.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public final class NGrams {

	public static Collection<String> bigrams(String s) {

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

	public static Collection<String> ngrams(String s) {

		Set<String> ngrams = new HashSet<String>();

		if (s.trim().length() < 2) {
			ngrams.add(s);
			return ngrams;
		}
		
		for (int i = 0; i < s.length() - 2; i++) {
			for (int j = s.length(); j > i; j--)
				ngrams.add(s.substring(i, j).toLowerCase());
		}

		return ngrams;
	}

	public static void main(String[] args) {
		System.out.println(bigrams("[+ter]Shonaka"));
		System.out.println(bigrams("[+ter]Sho naka"));
		System.out.println(ngrams("[+ter]Shonaka"));
		System.out.println(ngrams("[+ter]Sho naka"));
	}

}
