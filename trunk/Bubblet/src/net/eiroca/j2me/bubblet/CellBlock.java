/** GPL >= 2.0
 * Based upon Bubblet game written by Juan Antonio Agudo.
 *
 * Copyright (C) Juan Antonio Agudo
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
package net.eiroca.j2me.bubblet;

public class CellBlock {

  int xval;
  int yval;
  int color;

  public CellBlock(final int x, final int y) {
    xval = x;
    yval = y;
  }

  public CellBlock(final int x, final int y, final int col) {
    xval = x;
    yval = y;
    color = col;
  }

  public boolean equals(final Object o) {
    if (o == this) { return true; }
    if ((o instanceof CellBlock)) {
      final CellBlock c = (CellBlock) o;
      if ((c.xval == xval) && (c.yval == yval)) { return true; }
    }
    return false;
  }

}