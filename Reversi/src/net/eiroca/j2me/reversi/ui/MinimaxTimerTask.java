/** GPL >= 3.0
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 * Copyright (C) 2002-2004 Salamon Andras
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
package net.eiroca.j2me.reversi.ui;

import java.util.TimerTask;
import net.eiroca.j2me.game.tpg.GameMinMax;
import net.eiroca.j2me.game.tpg.GameTable;

/**
 * The Class MinimaxTimerTask.
 */
public class MinimaxTimerTask extends TimerTask {

  /** The ended. */
  public boolean ended;

  /** The start table. */
  protected GameTable startTable;

  /**
   * Instantiates a new minimax timer task.
   */
  public MinimaxTimerTask() {
    //
  }

  /* (non-Javadoc)
   * @see java.util.TimerTask#cancel()
   */
  public boolean cancel() {
    GameMinMax.cancel(true);
    return true;
  }

  /* (non-Javadoc)
   * @see java.util.TimerTask#run()
   */
  public void run() {
    ended = false;
    GameMinMax.foreMinimax(ReversiScreen.getActSkill(), startTable, ReversiScreen.getActPlayer(), ReversiScreen.rgame, true, 0, true, true);
    System.gc();
    ended = true;
  }

  /**
   * Sets the start table.
   * 
   * @param startTable the new start table
   */
  public void setStartTable(final GameTable startTable) {
    this.startTable = startTable;
  }

}