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

import iddb.core.model.Server;
import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.ServerDAO;
import iddb.exception.UnauthorizedUpdateException;

public class ServerManager {

	private final static ServerDAO serverDAO = (ServerDAO) DAOFactory.forClass(ServerDAO.class);

	private ServerManager() {};
	
	public static Server getAuthorizedServer(String key, String remoteAddress)
			throws UnauthorizedUpdateException {
		return getAuthorizedServer(key, remoteAddress, null);
	}

	public static Server getAuthorizedServer(String key, String remoteAddress,
			String serverName) throws UnauthorizedUpdateException {

		Server server = serverDAO.findByUid(key);

		if (server == null) {

			// Compose.
			StringBuilder builder = new StringBuilder(
					"Se intenta actualizar servidor no existente.\n");
			builder.append("Key: " + key).append("\n");
			if (serverName != null)
				builder.append("Nombre: " + serverName).append("\n");
			if (remoteAddress != null)
				builder.append("IP: " + remoteAddress).append("\n");
			String message = builder.toString();

			// Throw.
			throw new UnauthorizedUpdateException(message);
		}

		if (remoteAddress != null && server.getAddress() != null && server.getAddress().length() > 0
		        && !remoteAddress.equals(server.getAddress())) {

			// Compose.
			StringBuilder builder = new StringBuilder(
					"Intento de actualizar desde IP no autorizada.\n");
			builder.append("Key: " + key).append("\n");
			if (serverName != null)
				builder.append("Nombre: " + serverName).append("\n");
			if (remoteAddress != null)
				builder.append("IP: " + remoteAddress).append("\n");
			String message = builder.toString();

			// Throw.
			throw new UnauthorizedUpdateException(message);
		}

		return server;
	}
}
