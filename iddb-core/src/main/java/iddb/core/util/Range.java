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

import java.util.Iterator;

/**
 * Class that generates immutable sequences (ranges) as Iterable<Integer>
 * objects. A range represents a start (0 if not given), an stop (mandatory) and
 * an optional step (1 by default). The start value is included in the range,
 * the stop value is exclusive. Every range is handled by an Iterable<Integer>
 * which can by used in an extended for loop.
 * 
 * <pre>
 * for ( int i : range( 0, 10, 3 ) )
 *   System.out.print( i + " " ); // 0 3 6 9
 * </pre>
 *
 */
public class Range {
	
	public static Iterable<Integer> range(final int start, final int stop,
			final int step) {
		if (step <= 0)
			throw new IllegalArgumentException("step > 0 isrequired!");

		return new Iterable<Integer>() {
			public Iterator<Integer> iterator() {
				return new Iterator<Integer>() {
					private int counter = start;

					public boolean hasNext() {
						return counter < stop;
					}

					public Integer next() {
						try {
							return counter;
						} finally {
							counter += step;
						}
					}

					public void remove() {
					}
				};
			}
		};
	}

	public static Iterable<Integer> range(final int start, final int stop) {
		return range(start, stop, 1);
	}

	public static Iterable<Integer> range(final int stop) {
		return range(0, stop, 1);
	}
}