/** GPL >= 3.0
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 * Copyright (C) M. Jumari
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
package net.eiroca.j2me.minesweeper.game;

/**
 * The Class MineInfo.
 */
public class MineInfo {

  /** The x. */
  public int x;

  /** The y. */
  public int y;

  /** The status_real. */
  public int status_real;

  /** The status_guess. */
  public int status_guess;

  /**
   * Constructor class MineKotak.
   * 
   * @param x coordinate row
   * @param y coordinate column
   * @param status informasi nilai kotak
   * @param status_guess informasi suatu kotak di tandai atau tidak
   */
  public MineInfo(final int x, final int y, final int status, final int status_guess) {
    this.x = x;
    this.y = y;
    status_real = status;
    this.status_guess = status_guess;
  }

}
