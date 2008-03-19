/** GPL >= 2.0
 * Based upon J2ME Minesweeper.
 * Copyright (C) M. Jumari
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
package net.eiroca.j2me.minesweeper.game;

public class MineInfo {

  public int x;
  public int y;
  public int status_real;
  public int status_guess;

  /**
   * Constructor class MineKotak
   * @param x coordinate row
   * @param y coordinate column
   * @param status informasi nilai kotak
   * @param status_guess informasi suatu kotak di tandai atau tidak
   */
  public MineInfo(final int x, final int y, final int status, final int status_guess) {
    this.x = x;
    this.y = y;
    this.status_real = status;
    this.status_guess = status_guess;
  }

}
