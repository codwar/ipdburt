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
package iddb.core.model.dao.cached;

import iddb.core.model.LogModel;
import iddb.core.model.dao.LogModelDAO;
import iddb.exception.EntityDoesNotExistsException;

import java.util.Date;
import java.util.List;

public class LogModelDAOCached extends CachedDAO implements LogModelDAO {

	private final LogModelDAO impl;
	
	/**
	 * @param namespace
	 */
	public LogModelDAOCached(LogModelDAO impl) {
		super("logmodel");
		this.impl = impl;
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.LogModelDAO#save(iddb.core.model.LogModel)
	 */
	public void save(LogModel model) {
		this.impl.save(model);

	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.LogModelDAO#findByDate(java.util.Date, java.util.Date)
	 */
	public List<LogModel> findByDate(Date dateFrom, Date dateTo) {
		return this.impl.findByDate(dateFrom, dateTo);
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.LogModelDAO#get(java.lang.Long)
	 */
	public LogModel get(Long id) throws EntityDoesNotExistsException {
		return this.impl.get(id);
	}

	/* (non-Javadoc)
	 * @see iddb.core.model.dao.LogModelDAO#delete(java.lang.Long)
	 */
	public void delete(Long id) throws EntityDoesNotExistsException {
		this.impl.delete(id);
	}

}
