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
package iddb.core.model.dao;

import java.util.Date;
import java.util.List;

import iddb.core.model.LogModel;
import iddb.exception.EntityDoesNotExistsException;

public interface LogModelDAO {

	/**
	 * @param model
	 */
	void save(LogModel model);

	/**
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	List<LogModel> findByDate(Date dateFrom, Date dateTo);

	/**
	 * @param id
	 * @return
	 * @throws EntityDoesNotExistsException
	 */
	LogModel get(Long id) throws EntityDoesNotExistsException;

	/**
	 * @param id
	 * @throws EntityDoesNotExistsException
	 */
	void delete(Long id) throws EntityDoesNotExistsException;

}
