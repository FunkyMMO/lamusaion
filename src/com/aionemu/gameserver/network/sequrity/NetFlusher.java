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
package com.aionemu.gameserver.network.sequrity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author NB4L1
 */
public final class NetFlusher {
	private static final Timer _timer = new Timer(NetFlusher.class.getName(), true);

	public static void add(final Runnable runnable, long interval) {
		_timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					runnable.run();
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
			}
		}, interval, interval);
	}
}