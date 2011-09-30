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
package iddb.core.model.dao;

import iddb.core.model.UserServer;
import iddb.exception.EntityDoesNotExistsException;

import java.util.List;

public interface UserServerDAO {

	public abstract void save(UserServer userServer);

	public abstract List<UserServer> findByUser(Long user);

	public abstract List<UserServer> findByServer(Long server);
	
	public abstract UserServer get(Long userServer) throws EntityDoesNotExistsException;

	public abstract UserServer findByUserAndServer(Long user, Long server) throws EntityDoesNotExistsException;

	public abstract UserServer findByPlayerAndServer(Long player, Long server) throws EntityDoesNotExistsException;
	
}