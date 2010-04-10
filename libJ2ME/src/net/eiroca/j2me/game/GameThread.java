/** GPL >= 3.0
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/
 */
package net.eiroca.j2me.game;

/**
 * The Class GameThread.
 */
public class GameThread extends Thread {

  /** The Constant MILLIS_PER_TICK. */
  private static final int MILLIS_PER_TICK = 25;

  /** The screen. */
  public GameScreen screen;

  /** The stopped. */
  public boolean stopped = false;

  /**
   * Instantiates a new game thread.
   * 
   * @param canvas the canvas
   */
  public GameThread(final GameScreen canvas) {
    screen = canvas;
  }

  /* (non-Javadoc)
   * @see java.lang.Thread#run()
   */
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
    screen = null;
  }

}
