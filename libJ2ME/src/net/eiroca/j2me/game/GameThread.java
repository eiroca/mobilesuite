/** GPL >= 2.0
 *
 * Copyright (C) 2006-2008 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package net.eiroca.j2me.game;

public class GameThread extends Thread {

  private static final int MILLIS_PER_TICK = 25;

  public GameScreen screen;
  public boolean stopped = false;

  public GameThread(final GameScreen canvas) {
    screen = canvas;
  }

  public void run() {
    try {
      while (!stopped) {
        final long drawStartTime = System.currentTimeMillis();
        if (screen.isShown()) {
          if (screen.tick()) {
            screen.flushGraphics();
          }
        }
        final long timeTaken = System.currentTimeMillis() - drawStartTime;
        if (timeTaken < GameThread.MILLIS_PER_TICK) {
          synchronized (this) {
            wait(GameThread.MILLIS_PER_TICK - timeTaken);
          }
        }
        else {
          Thread.yield();
        }
      }
    }
    catch (final InterruptedException e) {
      // Nothing to do
    }
  }

}
