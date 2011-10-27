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
package iddb.api;

public interface RemotePermissions {

	public static final Integer DISABLE = 0;
	public static final Integer ADD_BAN = 1;
	public static final Integer REMOVE_BAN = 2;
	public static final Integer ADD_NOTICE = 4;
	public static final Integer REMOVE_NOTICE = 8;
	
	public static final Long DEFAULT_MAXBAN = 20160L; // 14d
	
}
