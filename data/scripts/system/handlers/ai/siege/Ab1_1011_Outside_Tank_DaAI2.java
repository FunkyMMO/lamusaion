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
package ai.siege;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("Ab1_1011_Outside_Tank_Da")
public class Ab1_1011_Outside_Tank_DaAI2 extends NpcAI2 {

  protected int startBarAnimation = 1;
  protected int cancelBarAnimation = 2;

  @Override
  protected void handleDialogStart(Player player) {
    handleUseItemStart(player);
  }

  protected void handleUseItemStart(final Player player) {
    final int delay = getTalkDelay();
    if (delay != 0) {
      final ItemUseObserver observer = new ItemUseObserver() {
        @Override
        public void abort() {
          player.getController().cancelTask(TaskId.ACTION_ITEM_NPC);
          PacketSendUtility.broadcastPacket(
            player,
            new SM_EMOTION(player, EmotionType.END_QUESTLOOT, 0, getObjectId()),
            true
          );
          PacketSendUtility.sendPacket(
            player,
            new SM_USE_OBJECT(
              player.getObjectId(),
              getObjectId(),
              0,
              cancelBarAnimation
            )
          );
          player.getObserveController().removeObserver(this);
        }
      };
      player.getObserveController().attach(observer);
      PacketSendUtility.sendPacket(
        player,
        new SM_USE_OBJECT(
          player.getObjectId(),
          getObjectId(),
          getTalkDelay(),
          startBarAnimation
        )
      );
      PacketSendUtility.broadcastPacket(
        player,
        new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0, getObjectId()),
        true
      );
      player
        .getController()
        .addTask(
          TaskId.ACTION_ITEM_NPC,
          ThreadPoolManager
            .getInstance()
            .schedule(
              new Runnable() {
                @Override
                public void run() {
                  PacketSendUtility.broadcastPacket(
                    player,
                    new SM_EMOTION(
                      player,
                      EmotionType.END_QUESTLOOT,
                      0,
                      getObjectId()
                    ),
                    true
                  );
                  PacketSendUtility.sendPacket(
                    player,
                    new SM_USE_OBJECT(
                      player.getObjectId(),
                      getObjectId(),
                      getTalkDelay(),
                      cancelBarAnimation
                    )
                  );
                  player.getObserveController().removeObserver(observer);
                  handleUseItemFinish(player);
                }
              },
              delay
            )
        );
    } else {
      handleUseItemFinish(player);
    }
  }

  protected void handleUseItemFinish(Player player) {
    SkillEngine
      .getInstance()
      .applyEffectDirectly(21593, player, player, 7200000 * 1); //Board The Weapon.
    AI2Actions.deleteOwner(this);
    AI2Actions.scheduleRespawn(this);
  }

  protected int getTalkDelay() {
    return getObjectTemplate().getTalkDelay() * 1000;
  }
}
