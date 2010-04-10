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
 * The Class Block.
 */
public class Block {

  /** The color. */
  public int color;

  /** The x. */
  public int x;

  /** The y. */
  public int y;

  /**
   * Instantiates a new block.
   * 
   * @param color the color
   * @param x the x
   * @param y the y
   */
  public Block(final int color, final int x, final int y) {
    this.color = color;
    this.x = x;
    this.y = y;
  }

  /**
   * Update.
   * 
   * @param x the x
   * @param y the y
   */
  public void update(final int x, final int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Clone.
   * 
   * @return the block
   */
  public Block clone() {
    return new Block(color, x, y);
  }

}
