/**
 *   Copyright(c) 2010-2012 CodWar Soft
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
package sgt.utils.sql.builder;

import java.util.Date;

/**
 * @author 12072245
 *
 */
public class QueryBuilder {
	
	private StringBuilder builder = new StringBuilder();
	
	private void build(String name, Object value, String comparison) {
		if (value instanceof String) {
			builder.append(String.format("%s %s '%s'", name, comparison, value));
		} else if (value instanceof Date) {
			builder.append(String.format("%s %s '%3$tY-%3$tm-%3$td'", name, comparison,  value));
		} else {
			builder.append(String.format("%s %s %s", name, comparison, value));
		}
	}
	
	public QueryBuilder and(String name, Object value) {
		return and(name, value, "=");
	}

	public QueryBuilder and(String name, Object value, String comparison) {
		if (builder.length() > 0) builder.append(" AND ");
		build(name, value, comparison);
		return this;
	}
	
	public QueryBuilder or(String name, Object value) {
		return or(name, value, "=");
	}

	public QueryBuilder or(String name, Object value, String comparison) {
		if (builder.length() > 0) builder.append(" OR ");
		build(name, value, comparison);
		return this;
	}

	public QueryBuilder and(QueryBuilder query) {
		if (builder.length() > 0) builder.append(" AND ");
		builder.append(String.format("(%s)", query.toString())); 
		return this;
	}

	public QueryBuilder or(QueryBuilder query) {
		if (builder.length() > 0) builder.append(" OR ");
		builder.append(String.format("(%s)", query.toString())); 
		return this;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return builder.toString();
	}
}
