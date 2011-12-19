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

import iddb.core.DAOException;
import iddb.core.model.Server;
import iddb.exception.EntityDoesNotExistsException;

import java.util.Date;
import java.util.List;

public interface ServerDAO {

	public abstract void save(Server server) throws DAOException;
	
	public abstract void savePermissions(Server server) throws DAOException;

	public abstract void saveBanPermissions(Server server) throws DAOException;
	
	public abstract List<Server> findAll(int offset, int limit, int[] count);

	public abstract Server findByUid(String uid);

	public abstract Server get(Long server) throws EntityDoesNotExistsException, DAOException;
	
	public abstract Server get(Long server, boolean fetchPermissions) throws EntityDoesNotExistsException, DAOException;
	
	public abstract List<Server> listNotUpdatedSince(Date date); 
	
	public abstract List<Server> findEnabled(int offset, int limit, int[] count);

	public abstract void loadPermissions(Server server) throws DAOException; 

}