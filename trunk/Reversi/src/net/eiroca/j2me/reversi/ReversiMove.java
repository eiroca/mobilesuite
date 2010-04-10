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
package net.eiroca.j2me.reversi;

import net.eiroca.j2me.game.tpg.GameMove;

/**
 * The Class ReversiMove.
 */
public final class ReversiMove implements GameMove {

  /** The col. */
  public int col;

  /** The row. */
  public int row;

  /** The point. */
  protected int point;

  /**
   * Valid.
   * 
   * @param row the row
   * @param col the col
   * @return true, if successful
   */
  public static boolean valid(final int row, final int col) {
    return (row >= 0) && (row < 8) && (col >= 0) && (col < 8);
  }

  /**
   * Instantiates a new reversi move.
   * 
   * @param row the row
   * @param col the col
   */
  public ReversiMove(final int row, final int col) {
    this.row = row;
    this.col = col;
  }

  /**
   * Instantiates a new reversi move.
   * 
   * @param move the move
   */
  public ReversiMove(final ReversiMove move) {
    row = move.row;
    col = move.col;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(final Object o) {
    if (!(o instanceof ReversiMove)) { return false; }
    final ReversiMove r = (ReversiMove) o;
    return (row == r.row) && (col == r.col);
  }

  /**
   * Get the value of point.
   * @return Value of point.
   */
  public int getPoint() {
    return point;
  }

  /**
   * Sets the coordinates.
   * 
   * @param row the row
   * @param col the col
   */
  public void setCoordinates(final int row, final int col) {
    this.row = row;
    this.col = col;
  }

  /**
   * Set the value of point.
   * @param v Value to assign to point.
   */
  public void setPoint(final int v) {
    point = v;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return new StringBuffer(32).append("ReversiMove(").append(row).append(", ").append(col).append(")").toString();
  }

}
