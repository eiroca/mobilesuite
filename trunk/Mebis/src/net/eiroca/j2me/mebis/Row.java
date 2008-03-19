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

public class Row {

  /* ypos of this row */
  public int ypos;

  /* all Block objects that belong to this row */
  public Block blocks[];

  public Row(final int ypos, final int cols) {
    this.ypos = ypos;
    blocks = new Block[cols];
    for (int i = 0; i < blocks.length; i++) {
      blocks[i] = null;
    }
  }

}
