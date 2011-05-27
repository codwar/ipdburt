package jipdbs.api;

import jipdbs.core.model.Server;
import jipdbs.core.model.dao.ServerDAO;
import jipdbs.core.model.dao.cached.ServerCachedDAO;
import jipdbs.core.model.dao.impl.ServerDAOImpl;
import jipdbs.exception.UnauthorizedUpdateException;

import org.datanucleus.util.StringUtils;

public class ServerManager {

	protected final ServerDAO serverDAO = new ServerCachedDAO(
			new ServerDAOImpl());

	public Server getAuthorizedServer(String key, String remoteAddress)
			throws UnauthorizedUpdateException {
		return getAuthorizedServer(key, remoteAddress, null);
	}

	public Server getAuthorizedServer(String key, String remoteAddress,
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

		if (remoteAddress != null && StringUtils.notEmpty(server.getAddress())
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
