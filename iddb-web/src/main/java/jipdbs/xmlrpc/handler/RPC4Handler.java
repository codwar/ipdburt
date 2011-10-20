package jipdbs.xmlrpc.handler;

import iddb.api.Events;
import iddb.api.ServerManager;
import iddb.core.IDDBService;
import iddb.core.model.Penalty;
import iddb.core.model.Server;
import iddb.core.util.HashUtils;
import iddb.exception.UnauthorizedUpdateException;
import iddb.exception.UpdateApiException;
import iddb.info.PenaltyInfo;
import iddb.info.PlayerInfo;
import iddb.legacy.python.date.DateUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jipdbs.xmlrpc.JIPDBSXmlRpc4Servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPC4Handler extends RPC3Handler {

	private static final Logger log = LoggerFactory.getLogger(RPC4Handler.class);
	
	/**
	 * @param app
	 */
	public RPC4Handler(IDDBService app) {
		super(app);
	}

	/* (non-Javadoc)
	 * @see jipdbs.xmlrpc.handler.RPC3Handler#getClientAddress()
	 */
	@Override
	public String getClientAddress() {
		return JIPDBSXmlRpc4Servlet.getClientIpAddress();
	}
	
	public void updateName(String key, String name, Object[] data) {
		this.updateApi.updateName(key, name, (String) data[0], (Integer) data[1], (Integer) data[2], getClientAddress());
	}

	public String getClientHash(String key, String guid) {
		if (guid == null || "".equals(guid)) return "";
		return HashUtils.getSHA1Hash(guid + key);
	}
	
	public void update(String key, Object[] plist, Long timestamp) throws UpdateApiException, Exception {
		try {
			Server server = ServerManager.getAuthorizedServer(key, getClientAddress());

			List<PlayerInfo> list = new ArrayList<PlayerInfo>();
			for (Object o : plist) {
				try {
					list.add(processEventInfo(server.getUid(), o, timestamp));
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
			if (list.size() > 0) {
				if (list.size() > maxListSize) {
					log.warn("List size is " + Integer.toString(list.size()));
					// this is too much to process
					list = list.subList(list.size() - maxListSize, list.size());
				} else {
					log.info("List size is " + Integer.toString(list.size()));
				}
				updateApi.updatePlayer(server, list);
			} else {
				if (server.getOnlinePlayers() > 0 || server.getDirty()) {
					log.debug("Cleaning server " + server.getName());
					updateApi.cleanServer(server);
				}
			}
		} catch (UnauthorizedUpdateException e) {
			log.warn(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.error(w.getBuffer().toString());
			throw e;
		}
	}

	public Integer register(String key, String userid, Object[] data) throws UpdateApiException, Exception {
		try {
			Server server = ServerManager.getAuthorizedServer(key, getClientAddress());
			PlayerInfo playerInfo = new PlayerInfo(Events.REGISTER);
			playerInfo.setName((String) data[0]);
			playerInfo.setGuid((String) data[1]);
			playerInfo.setClientId(parseLong(data[2]));
			playerInfo.setIp((String) data[3]);
			playerInfo.setLevel(parseLong(data[4]));
			playerInfo.setHash(getClientHash(server.getUid(), playerInfo.getGuid()));
			
			return this.updateApi.linkUser(server, userid, playerInfo);
			
		} catch (UnauthorizedUpdateException e) {
			log.warn(e.getMessage());
			throw new UpdateApiException(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
			StringWriter w = new StringWriter();
			e.printStackTrace(new PrintWriter(w));
			log.error(w.getBuffer().toString());
			throw e;
		}
	}
	
	protected PlayerInfo processEventInfo(String uid, Object o, Long timestamp) throws Exception {
		Long timediff = DateUtils.dateToTimestamp(new Date()) - timestamp;
		Object[] values = ((Object[]) o);
		if (log.isDebugEnabled()) log.debug("EventInfo: {}", Arrays.toString(values));
		String event = (String) values[0];
		PlayerInfo playerInfo = new PlayerInfo(event);
		playerInfo.setName((String) values[1]);
		playerInfo.setGuid((String) values[2]);
		playerInfo.setClientId(parseLong(values[3]));
		playerInfo.setIp((String) values[4]);
		playerInfo.setLevel(parseLong(values[5]));
		playerInfo.setHash(getClientHash(uid, playerInfo.getGuid()));
		if (values.length > 6) {
			Date updated;
			if (values[6] instanceof Date) {
				updated = (Date) values[6];
			} else {
				try {
					updated = DateUtils.timestampToDate((Integer) values[6] + timediff); 
				} catch (Exception e) {
					log.error(e.getMessage());
					updated = new Date();
				}
			}
			playerInfo.setUpdated(updated);
		}
		if (Events.BAN.equals(event)) {
			Object[] data = (Object[]) values[7];
			if (log.isDebugEnabled()) log.debug("BanInfo: {}", Arrays.toString(data));
			PenaltyInfo penalty = new PenaltyInfo();
			penalty.setType(Penalty.BAN);
			penalty.setCreated(DateUtils.timestampToDate(parseLong(data[1]) + timediff));
			penalty.setDuration(parseLong(data[2]));
			penalty.setReason((String) data[3]);
			penalty.setAdmin((String) data[4]);
			penalty.setAdminId(getClientHash(uid, smartCast(data[5])));
			playerInfo.setPenaltyInfo(penalty);
		} else if (Events.ADDNOTE.equals(event)) {
			Object[] data = (Object[]) values[7];
			if (log.isDebugEnabled()) log.debug("NoteInfo: {}", Arrays.toString(data));
			PenaltyInfo penalty = new PenaltyInfo();
			penalty.setType(Penalty.NOTICE);
			penalty.setCreated(DateUtils.timestampToDate(parseLong(data[0]) + timediff));
			penalty.setReason((String) data[1]);
			penalty.setAdmin((String) data[2]);
			penalty.setAdminId(getClientHash(uid, smartCast(data[3])));
			playerInfo.setPenaltyInfo(penalty);			
		}
		return playerInfo;
	}

}