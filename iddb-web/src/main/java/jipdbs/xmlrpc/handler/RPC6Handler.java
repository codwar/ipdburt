package jipdbs.xmlrpc.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import iddb.api.Events;
import iddb.core.IDDBService;
import iddb.core.model.Penalty;
import iddb.info.PenaltyInfo;
import iddb.info.PlayerInfo;
import iddb.legacy.python.date.DateUtils;

public class RPC6Handler extends RPC5Handler {

	protected static final String REMOTEID = "REMOTEID";
	protected static final String ADMINID = "ADMINID";
	protected static final String ADMIN = "ADMIN";
	protected static final String REASON = "REASON";
	protected static final String DURATION = "DURATION";
	protected static final String CREATED = "CREATED";
	protected static final String EXTRA = "EXTRA";
	protected static final String UPDATED = "UPDATED";
	protected static final String LEVEL = "LEVEL";
	protected static final String IP = "IP";
	protected static final String ID = "ID";
	protected static final String GUID = "GUID";
	protected static final String NAME = "NAME";
	protected static final String PBID = "PBID";
	protected static final String EVENT = "EVENT";

	private static final Logger log = LoggerFactory.getLogger(RPC6Handler.class);

	protected static Map<String, Integer> DATAIX;
	protected static Map<String, Integer> EXTRA_BANIX;
	protected static Map<String, Integer> EXTRA_NOTEIX;
	
	static {
		DATAIX = new HashMap<String, Integer>();
		DATAIX.put(EVENT, 0);
		DATAIX.put(NAME, 1);
		DATAIX.put(PBID, 2);
		DATAIX.put(GUID, 3);
		DATAIX.put(ID, 4);
		DATAIX.put(IP, 5);
		DATAIX.put(LEVEL, 6);
		DATAIX.put(UPDATED, 7);
		DATAIX.put(EXTRA, 8);
		
		EXTRA_BANIX = new HashMap<String, Integer>();
		EXTRA_BANIX.put(CREATED, 1);
		EXTRA_BANIX.put(DURATION, 2);
		EXTRA_BANIX.put(REASON, 3);
		EXTRA_BANIX.put(ADMIN, 4);
		EXTRA_BANIX.put(ADMINID, 5);
		
		EXTRA_NOTEIX = new HashMap<String, Integer>();
		EXTRA_NOTEIX.put(CREATED, 0);
		EXTRA_NOTEIX.put(REASON, 1);
		EXTRA_NOTEIX.put(ADMIN, 2);
		EXTRA_NOTEIX.put(ADMINID, 3);
		EXTRA_NOTEIX.put(REMOTEID, 4);
	}
	
	public RPC6Handler(IDDBService app) {
		super(app);
	}

	protected Object getData(Object[] values, String id) {
		return values[DATAIX.get(id)];
	}
	
	protected Object getBanData(Object[] values, String id) {
		return values[EXTRA_BANIX.get(id)];
	}

	protected Object getNoteData(Object[] values, String id) {
		return values[EXTRA_NOTEIX.get(id)];
	}
	
	@Override
	protected PlayerInfo processEventInfo(String uid, Object o, Long timediff)
			throws Exception {
		Object[] values = ((Object[]) o);
		if (log.isDebugEnabled()) log.debug("EventInfo: {}", Arrays.toString(values));
		String event = (String) getData(values, EVENT);
		PlayerInfo playerInfo = new PlayerInfo(event);
		playerInfo.setName((String) getData(values, NAME));
		playerInfo.setPbid((String) getData(values, PBID));
		playerInfo.setGuid((String) getData(values, GUID));
		playerInfo.setClientId(parseLong(getData(values, ID)));
		playerInfo.setIp((String) getData(values, IP));
		playerInfo.setLevel(parseLong(getData(values, LEVEL)));
		playerInfo.setHash(getClientHash(uid, playerInfo.getGuid()));
		if (values.length > DATAIX.get(UPDATED)) {
			Date updated;
			if (getData(values, UPDATED) instanceof Date) {
				updated = (Date) getData(values, UPDATED);
			} else {
				try {
					updated = DateUtils.timestampToDate((Integer) getData(values, UPDATED) + timediff); 
				} catch (Exception e) {
					log.error(e.getMessage());
					updated = new Date();
				}
			}
			playerInfo.setUpdated(updated);
		}
		if (Events.BAN.equals(event)) {
			Object[] data = (Object[]) getData(values, EXTRA);
			if (log.isDebugEnabled()) log.debug("BanInfo: {}", Arrays.toString(data));
			PenaltyInfo penalty = new PenaltyInfo();
			penalty.setType(Penalty.BAN);
			penalty.setCreated(DateUtils.timestampToDate(parseLong(getBanData(data, CREATED)) + timediff));
			penalty.setDuration(parseLong(getBanData(data, DURATION)));
			penalty.setReason((String) getBanData(data, REASON));
			penalty.setAdmin((String) getBanData(data, ADMIN));
			penalty.setAdminId(getClientHash(uid, smartCast(getBanData(data, ADMINID))));
			playerInfo.setPenaltyInfo(penalty);
		} else if (Events.ADDNOTE.equals(event)) {
			Object[] data = (Object[]) getData(values, EXTRA);
			if (log.isDebugEnabled()) log.debug("NoteInfo: {}", Arrays.toString(data));
			PenaltyInfo penalty = new PenaltyInfo();
			penalty.setType(Penalty.NOTICE);
			penalty.setCreated(DateUtils.timestampToDate(parseLong(getNoteData(data, CREATED)) + timediff));
			penalty.setReason((String) getNoteData(data, REASON));
			penalty.setAdmin((String) getNoteData(data, ADMIN));
			penalty.setAdminId(getClientHash(uid, smartCast(getNoteData(data, ADMINID))));
			try {
				penalty.setRemoteId((String) getNoteData(data, REMOTEID));
			} catch (ArrayIndexOutOfBoundsException e) {
			}
			playerInfo.setPenaltyInfo(penalty);			
		} else if (Events.DELNOTE.equals(event)) {
			try {
				PenaltyInfo penalty = new PenaltyInfo();
				penalty.setRemoteId((String)((Object[]) getData(values, EXTRA))[0]);
				playerInfo.setPenaltyInfo(penalty);
			} catch (ArrayIndexOutOfBoundsException e) {
			}
		}
		if (log.isDebugEnabled()) log.debug("{}: {}", event, playerInfo.toString());
		return playerInfo;
	}
}
