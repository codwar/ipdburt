package jipdbs.xmlrpc.handler;

import iddb.api.Events;
import iddb.api.ServerManager;
import iddb.api.SimpleMapEntry;
import iddb.core.IDDBService;
import iddb.core.model.Penalty;
import iddb.core.model.PenaltyHistory;
import iddb.core.model.Player;
import iddb.core.model.Server;
import iddb.core.util.HashUtils;
import iddb.exception.EntityDoesNotExistsException;
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
import java.util.Map.Entry;

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

	public void updateName(String key, String name, Object[] data) {
		this.updateApi.updateName(key, name, (String) data[0], (Integer) data[1], getClientAddress());
		if (log.isDebugEnabled()) {
			log.debug("Update Server {} - {}", name, Arrays.toString(data));	
		}
	}
	
	/* (non-Javadoc)
	 * @see jipdbs.xmlrpc.handler.RPC3Handler#getClientAddress()
	 */
	@Override
	public String getClientAddress() {
		return JIPDBSXmlRpc4Servlet.getClientIpAddress();
	}

	public String getClientHash(String key, String guid) {
		if (guid == null || "".equals(guid)) return "";
		return HashUtils.getSHA1Hash(guid + key);
	}
	
	/**
	 * 
	 * @param key
	 * @param plist
	 * @param timestamp
	 * @throws UpdateApiException
	 * @throws Exception
	 */
	public void update(String key, Object[] plist, Integer timestamp) throws UpdateApiException, Exception {
		try {
			Server server = ServerManager.getAuthorizedServer(key, getClientAddress());

			log.info("Update {} - {}", server.getName(), plist.length);
			
			if (plist.length > maxListSize) {
				log.warn("Too many items to process [{}].", plist.length);
				plist = Arrays.copyOfRange(plist, 0, maxListSize);
			}

			Long timediff = DateUtils.dateToTimestamp(new Date()) - timestamp;
			log.debug("Server timestamp: {} - diff {}", timestamp, timediff);
			
			List<PlayerInfo> list = new ArrayList<PlayerInfo>();
			for (Object o : plist) {
				try {
					list.add(processEventInfo(server.getUid(), o, timediff));
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
			
			if (list.size() > 0) {
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
			
			log.debug("Register {} - User {}", server.getName(), userid);
			
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
	
	protected PlayerInfo processEventInfo(String uid, Object o, Long timediff) throws Exception {
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List eventQueue(String key) {
		List list = new ArrayList();
		List<PenaltyHistory> events = new ArrayList<PenaltyHistory>();
		try {
			Server server = ServerManager.getAuthorizedServer(key, getClientAddress());
			
			log.debug("Query Event Queue {}", server.getName());
			
			List<Penalty> penalties = app.listPendingEvents(server.getKey());
			for (Penalty p : penalties) {
				String adminId = "0";
				PenaltyHistory his = app.getLastPenaltyHistory(p);
				Player client;
				try {
					client = app.getPlayer(p.getPlayer());
				} catch (EntityDoesNotExistsException e) {
					log.error(e.getMessage());
					continue;
				}
				try {
					Player admin = app.getPlayer(p.getAdmin());
					adminId = admin.getClientId().toString();
				} catch (EntityDoesNotExistsException e) {
					log.error(e.getMessage());
				}
				String[] values;
				if (p.getType().equals(Penalty.BAN)) {
					if (his.getFuncId().equals(PenaltyHistory.FUNC_ID_ADD)) {
						values = new String[]{Events.BAN, his.getKey().toString(), client.getClientId().toString(), p.getDuration().toString(), p.getReason(), adminId};	
					} else {
						values = new String[]{Events.UNBAN, his.getKey().toString(), client.getClientId().toString()};
					}
				} else {
					if (his.getFuncId().equals(PenaltyHistory.FUNC_ID_ADD)) {
						values = new String[]{Events.ADDNOTE, his.getKey().toString(), p.getKey().toString() , client.getClientId().toString(), p.getReason(), adminId};
					} else {
						values = new String[]{Events.DELNOTE, his.getKey().toString(), p.getKey().toString()};
					}
				}
				list.add(values);
				events.add(his);
			}
			
			if (log.isDebugEnabled()) {
				log.debug("Events {}", list.toString());
			}
			
			app.updatePenaltyHistory(events);
		} catch (UnauthorizedUpdateException e) {
			log.warn(e.getMessage());
		}
		return list;
	}
	
	/**
	 * 
	 * @param key
	 * @param list
	 */
	public void confirmEvent(String key, Object[] list) {
		try {
			Server server = ServerManager.getAuthorizedServer(key, getClientAddress());
			
			if (log.isDebugEnabled()) {
				log.debug("Confirm Event {} - {}", server.getName(), Arrays.toString(list));
			}
			
			List<Entry<Long, String>> eventList = new ArrayList<Entry<Long,String>>();
			for (Object o : list) {
				Object[] data = (Object[]) o;
				Entry<Long, String> entry = new SimpleMapEntry<Long, String>(parseLong(data[0]), (String) data[1]);
				eventList.add(entry);
			}
			if (eventList.size() > 0) {
				app.confirmRemoteEvent(eventList);
			}
		} catch (UnauthorizedUpdateException e) {
			log.warn(e.getMessage());
		}
		
	}
}