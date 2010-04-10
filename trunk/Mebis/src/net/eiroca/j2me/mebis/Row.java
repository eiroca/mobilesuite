/** GPL >= 3.0
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 * Copyright (C) 2005-2006 Michael "ScriptKiller" Arndt <scriptkiller@gmx.de> http://scriptkiller.de/
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
package net.eiroca.j2me.mebis;

/**
 * The Class Row.
 */
public class Row {

  /** ypos of this row. */
  public int ypos;

  /** All Block objects that belong to this row. */
  public Block blocks[];

  /**
   * Instantiates a new row.
   * 
   * @param ypos the ypos
   * @param cols the cols
   */
  public Row(final int ypos, final int cols) {
    this.ypos = ypos;
    blocks = new Block[cols];
    for (int i = 0; i < blocks.length; i++) {
      blocks[i] = null;
    }
  }

}
