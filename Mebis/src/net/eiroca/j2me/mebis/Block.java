/** GPL >= 2.0
 * Based upon scriptris - a free j2me tetris (R) clone with bluetooth multiplayer support
 *
 * Copyright (C) 2005-2006 Michael "ScriptKiller" Arndt <scriptkiller@gmx.de> http://scriptkiller.de/
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
package net.eiroca.j2me.mebis;

public class Block {

  public int color;
  public int x;
  public int y;

  // Row r;

  public Block(final int color, final int x, final int y) {
    this.color = color;
    this.x = x;
    this.y = y;
  }

  public void update(final int x, final int y) {
    this.x = x;
    this.y = y;
  }

  public Block clone() {
    return new Block(color, x, y);
  }

}
