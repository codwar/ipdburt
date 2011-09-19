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
package iddb.task.tasks;

import iddb.api.Events;
import iddb.core.model.Penalty;
import iddb.core.model.Player;
import iddb.core.model.dao.DAOFactory;
import iddb.core.model.dao.PenaltyDAO;
import iddb.core.model.dao.PlayerDAO;
import iddb.info.PenaltyInfo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class PenaltyTask implements Runnable {

	private Player player;
	private String event;
	
	public PenaltyTask(Player player, String event) {
		this.player = player;
		this.event = event;
	}
	
	private void process(Player player, String event) {
		PenaltyDAO dao = (PenaltyDAO) DAOFactory.forClass(PenaltyDAO.class);
		PlayerDAO playerDAO = (PlayerDAO) DAOFactory.forClass(PlayerDAO.class);
		
		// TODO cambiar a un future statement al procesar
		if (Events.BAN.equals(event) || Events.UNBAN.equals(event)) {
			if (player.getBanInfo() == null) {
				List<Penalty> penalties = dao.findByPlayerAndType(player.getKey(), Penalty.BAN);
				if (penalties.size() > 0) dao.delete(penalties);
			} else {
				List<Penalty> penalties = dao.findByPlayerAndTypeAndActive(player.getKey(), Penalty.BAN);
				Penalty penalty;
				if (penalties.size() > 0) {
					penalty = penalties.get(0);
				} else {
					penalty = new Penalty();
					penalty.setPlayer(player.getKey());
				}
				PenaltyInfo penaltyInfo = new PenaltyInfo();
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
				PenaltyInfo info = new PenaltyInfo();
				Penalty penalty = new Penalty();
				penalty.setPlayer(player.getKey());
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

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		process(player, event);
	}
}
