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

import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.game.tpg.GameMove;
import net.eiroca.j2me.game.tpg.GameTable;

/**
 * The Class ReversiTable.
 */
public final class ReversiTable implements GameTable {

  /** Two bits for every place: 00: nothing 01: 1 10: 2 11: oops. */
  protected byte[] board;

  /** The pass num. */
  protected int passNum;

  /**
   * Gets the player item.
   * 
   * @param player the player
   * @return the player item
   */
  public static byte getPlayerItem(final byte player) {
    return (byte) (player + 1);
  }

  /**
   * Instantiates a new reversi table.
   */
  public ReversiTable() {
    board = new byte[64];
    passNum = 0;
    setItem(3, 3, (byte) 2);
    setItem(4, 4, (byte) 2);
    setItem(3, 4, (byte) 1);
    setItem(4, 3, (byte) 1);
  }

  /**
   * Instantiates a new reversi table.
   * 
   * @param byteArray the byte array
   * @param offset the offset
   */
  public ReversiTable(final byte[] byteArray, final int offset) {
    board = new byte[64];
    passNum = byteArray[offset];
    System.arraycopy(byteArray, offset + 1, board, 0, 64);
  }

  /**
   * Instantiates a new reversi table.
   * 
   * @param table the table
   */
  public ReversiTable(final ReversiTable table) {
    board = new byte[64];
    System.arraycopy(table.board, 0, board, 0, 64);
    passNum = table.passNum;
  }

  /**
   * Convert to int array.
   * 
   * @param array the array
   */
  public void convertToIntArray(final int[][] array) {
    for (int i = 0; i < 8; ++i) {
      for (int j = 0; j < 8; ++j) {
        array[i][j] = getItem(i, j);
      }
    }
  }

  /**
   * Copy data from.
   * 
   * @param table the table
   */
  public void copyDataFrom(final GameTable table) {
    final ReversiTable rtable = (ReversiTable) table;
    System.arraycopy(rtable.board, 0, board, 0, 64);
    passNum = rtable.passNum;
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.tpg.GameTable#copyFrom()
   */
  public GameTable copyFrom() {
    final ReversiTable rtable = new ReversiTable(this);
    return rtable;
  }

  /**
   * Flip.
   * 
   * @param row the row
   * @param col the col
   */
  public void flip(final int row, final int col) {
    setItem(row, col, (byte) (3 - getItem(row, col)));
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.tpg.GameTable#getEmptyMove()
   */
  public GameMove getEmptyMove() {
    return new ReversiMove(0, 0);
  }

  /**
   * Gets the item.
   * 
   * @param row the row
   * @param col the col
   * @return the item
   */
  public byte getItem(final int row, final int col) {
    return board[row * 8 + col];
  }

  /**
   * Get the value of passNum.
   * @return Value of passNum.
   */
  public int getPassNum() {
    return passNum;
  }

  /**
   * Sets the item.
   * 
   * @param row the row
   * @param col the col
   * @param value the value
   */
  public void setItem(final int row, final int col, final byte value) {
    board[row * 8 + col] = value;
  }

  /**
   * Set the value of passNum.
   * @param v Value to assign to passNum.
   */
  public void setPassNum(final int v) {
    passNum = v;
  }

  /**
   * To byte array.
   * 
   * @return the byte[]
   */
  public byte[] toByteArray() {
    final byte[] byteArray = new byte[65];
    toByteArray(byteArray, 0);
    return byteArray;
  }

  /**
   * To byte array.
   * 
   * @param byteArray the byte array
   * @param offset the offset
   */
  public void toByteArray(final byte[] byteArray, final int offset) {
    byteArray[offset] = (byte) passNum;
    System.arraycopy(board, 0, byteArray, offset + 1, board.length);
  }

  /**
   * Should use StringBuffer instead of String, but this method is only for debug purposes.
   * 
   * @return the string
   */
  public String toString() {
    final StringBuffer ret = new StringBuffer(80);
    for (int i = 0; i < 8; ++i) {
      for (int j = 0; j < 8; ++j) {
        ret.append(getItem(j, i));
      }
      ret.append(BaseApp.CR);
    }
    ret.append("pass: ").append(getPassNum()).append(BaseApp.CR);
    return ret.toString();
  }

}