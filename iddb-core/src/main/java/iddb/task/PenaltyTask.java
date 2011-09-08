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
package iddb.task;

import iddb.api.Events;
import iddb.core.model.Penalty;
import iddb.core.model.Player;
import iddb.core.model.dao.PenaltyDAO;
import iddb.core.model.dao.PlayerDAO;
import iddb.core.model.dao.cached.PenaltyCachedDAO;
import iddb.core.model.dao.cached.PlayerCachedDAO;
import iddb.core.model.dao.impl.PenaltyDAOImpl;
import iddb.core.model.dao.impl.PlayerDAOImpl;
import iddb.info.PenaltyInfo;

import java.util.Date;
import java.util.List;


import org.apache.commons.lang.StringUtils;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class PenaltyTask {

	private static final String URL = "/admin/task/penalty";
	
	public static void enqueue(Player player, String event) {
		Queue queue = QueueFactory.getQueue("penalty");
		queue.add(TaskOptions.Builder.withUrl(URL).param("key", KeyFactory.keyToString(player.getKey())).param("event", event));
	}
	
	public void process(Player player, String event) {
		PenaltyDAO dao = new PenaltyCachedDAO(new PenaltyDAOImpl());
		PlayerDAO playerDAO = new PlayerCachedDAO(new PlayerDAOImpl());
		
		if (Events.BAN.equals(event) || Events.UNBAN.equals(event)) {
			if (StringUtils.isEmpty(player.getBanInfo())) {
				List<Penalty> penalties = dao.findByPlayerAndType(player.getKey(), Penalty.BAN);
				if (penalties.size() > 0) dao.delete(penalties);
			} else {
				List<Penalty> penalties = dao.findByPlayerAndTypeAndActive(player.getKey(), Penalty.BAN);
				Penalty penalty;
				if (penalties.size() > 0) {
					penalty = penalties.get(0);
				} else {
					penalty = new Penalty(player.getKey());
				}
				PenaltyInfo penaltyInfo = new PenaltyInfo(player.getBanInfo());
				penalty.setType(Penalty.BAN);
				penalty.setCreated(penaltyInfo.getCreated());
				penalty.setUpdated(new Date());
				penalty.setReason(penaltyInfo.getReason());
				penalty.setDuration(penaltyInfo.getDuration());
				penalty.setSynced(true);
				penalty.setActive(true);
				try {
					if (penaltyInfo.getAdminId() != null) {
						Player admin = playerDAO.findByServerAndGuid(player.getServer(), penaltyInfo.getAdminId());
						if (admin != null) penalty.setAdmin(admin.getKey());
					}
				} catch (Exception e) {
				}
				dao.save(penalty);
			}			
		} else if (Events.ADDNOTE.equals(event) || Events.DELNOTE.equals(event)) {
			if (StringUtils.isEmpty(player.getNote())) {
				List<Penalty> penalties = dao.findByPlayerAndType(player.getKey(), Penalty.NOTICE);
				if (penalties.size() > 0) dao.delete(penalties);
			} else {
				PenaltyInfo info = new PenaltyInfo(player.getBanInfo());
				Penalty penalty = new Penalty(player.getKey());
				penalty.setType(Penalty.NOTICE);
				penalty.setCreated(info.getCreated());
				penalty.setUpdated(new Date());
				penalty.setReason(info.getReason());
				penalty.setSynced(true);
				penalty.setActive(true);
				try {
					if (info.getAdminId() != null) {
						Player admin = playerDAO.findByServerAndGuid(player.getServer(), info.getAdminId());
						if (admin != null) penalty.setAdmin(admin.getKey());
					}
				} catch (Exception e) {
				}
				dao.save(penalty);			
			}
		}
		
	}
}
