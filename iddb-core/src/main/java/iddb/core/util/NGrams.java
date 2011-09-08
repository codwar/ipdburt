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

	public static Collection<String> ngrams(String s, int min) {

		Set<String> ngrams = new HashSet<String>();

		if (s.trim().length() <= min) {
			ngrams.add(s.toLowerCase());
			return ngrams;
		}
		
		for (int i = 0; i < s.length() - min; i++) {
			for (int j = s.length(); j > i + (min-1); j--) {
				ngrams.add(s.substring(i, j).toLowerCase());
			}		
		}

		return ngrams;
	}
	
	public static Collection<String> ngrams(String s) {
		return ngrams(s,2);
	}

	public static void main(String[] args) {
		System.out.println(ngrams("TextoDePrueba1"));
		System.out.println(ngrams("TextoDePrueba1",2));
		System.out.println(ngrams("TextoDePrueba1",3));
		System.out.println(ngrams("TextoDePrueba1",4));
		System.out.println(ngrams("TextoDePrueba1",5));
		System.out.println(ngrams("Texto", 5));
	}

}
