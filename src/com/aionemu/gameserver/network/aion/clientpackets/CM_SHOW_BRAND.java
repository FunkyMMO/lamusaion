/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
 *
 *  Encom is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;

/**
 * @author Sweetkr
 * @author Simple
 */
public class CM_SHOW_BRAND extends AionClientPacket {

	@SuppressWarnings("unused")
	private int action;
	private int brandId;
	private int targetObjectId;

	/**
	 * @param opcode
	 */
	public CM_SHOW_BRAND(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		action = readD();
		brandId = readD();
		targetObjectId = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();

		if (player.isInGroup2() && player.getPlayerGroup2().isLeader(player)) {
			PlayerGroupService.showBrand(player, targetObjectId, brandId);
		}
		if (player.isInAlliance2() && player.getPlayerAlliance2().isSomeCaptain(player)) {
			PlayerAllianceService.showBrand(player, targetObjectId, brandId);
		}
	}
}