/*
 * This file is part of Encom.
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
package admincommands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.WeddingService;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.world.World;

/**
 * @author SheppeR
 * @rework Eloann
 */
public class cmd_divorce extends PlayerCommand {

  public cmd_divorce() {
    super("divorce");
  }

  @Override
  public void execute(Player admin, String... params) {
    if (params == null || params.length != 2) {
      PacketSendUtility.sendMessage(
        admin,
        "syntax .divorce <characterName1> <characterName2>"
      );
      return;
    }

    Player partner1 = World
      .getInstance()
      .findPlayer(Util.convertName(params[0]));
    Player partner2 = World
      .getInstance()
      .findPlayer(Util.convertName(params[1]));

    final int ap1 = partner1.getAbyssRank().getAp();
    final int ap2 = partner2.getAbyssRank().getAp();

    if (partner1 == null || partner2 == null) {
      PacketSendUtility.sendMessage(
        admin,
        "The specified player is not online."
      );
      return;
    }
    if (partner1.equals(partner2)) {
      PacketSendUtility.sendMessage(
        admin,
        "You can't cancel marry player on himself."
      );
      return;
    }
    if (
      partner1.getWorldId() == 510010000 ||
      partner1.getWorldId() == 520010000 ||
      partner2.getWorldId() == 510010000 ||
      partner2.getWorldId() == 520010000
    ) {
      PacketSendUtility.sendMessage(admin, "One of the players is in prison.");
      return;
    }

    WeddingService.getInstance().unDoWedding(partner1, partner2);
    ItemService.addItem(partner1, 182400001, 0);
    ItemService.addItem(partner2, 182400001, -0);
    AbyssPointsService.addAp(partner1, -((ap1 * 20) / 100));
    AbyssPointsService.addAp(partner2, -((ap2 * 20) / 100));
    PacketSendUtility.sendMessage(admin, "Married canceled.");
  }

  private boolean hasItem(Player player, int itemId) {
    return player.getInventory().getItemCountByItemId(itemId) > 0;
  }

  @Override
  public void onFail(Player admin, String message) {
    PacketSendUtility.sendMessage(
      admin,
      "syntax .divorce <characterName1> <characterName2>"
    );
  }
}
