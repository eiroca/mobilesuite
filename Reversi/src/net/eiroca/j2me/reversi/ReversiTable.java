/** GPL >= 2.0
 * Based upon jtReversi game written by Jataka Ltd.
 *
 * Copyright (C) 2002-2004 Salamon Andras
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
package net.eiroca.j2me.reversi;

import net.eiroca.j2me.game.tpg.GameMove;
import net.eiroca.j2me.game.tpg.GameTable;

public final class ReversiTable implements GameTable {

  /**
   * Two bits for every place: 00: nothing 01: 1 10: 2 11: oops
   */
  protected byte[] board;
  protected int passNum;

  public static byte getPlayerItem(final byte player) {
    return (byte) (player + 1);
  }

  public ReversiTable() {
    board = new byte[64];
    passNum = 0;
    setItem(3, 3, (byte) 2);
    setItem(4, 4, (byte) 2);
    setItem(3, 4, (byte) 1);
    setItem(4, 3, (byte) 1);
  }

  public ReversiTable(final byte[] byteArray, final int offset) {
    board = new byte[64];
    passNum = byteArray[offset];
    System.arraycopy(byteArray, offset + 1, board, 0, 64);
  }

  public ReversiTable(final ReversiTable table) {
    board = new byte[64];
    System.arraycopy(table.board, 0, board, 0, 64);
    passNum = table.passNum;
  }

  public void convertToIntArray(final int[][] array) {
    for (int i = 0; i < 8; ++i) {
      for (int j = 0; j < 8; ++j) {
        array[i][j] = getItem(i, j);
      }
    }
  }

  public void copyDataFrom(final GameTable table) {
    final ReversiTable rtable = (ReversiTable) table;
    System.arraycopy(rtable.board, 0, board, 0, 64);
    passNum = rtable.passNum;
  }

  public GameTable copyFrom() {
    final ReversiTable rtable = new ReversiTable(this);
    return rtable;
  }

  public void flip(final int row, final int col) {
    setItem(row, col, (byte) (3 - getItem(row, col)));
  }

  public GameMove getEmptyMove() {
    return new ReversiMove(0, 0);
  }

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

  public byte[] toByteArray() {
    final byte[] byteArray = new byte[65];
    toByteArray(byteArray, 0);
    return byteArray;
  }

  public void toByteArray(final byte[] byteArray, final int offset) {
    byteArray[offset] = (byte) passNum;
    System.arraycopy(board, 0, byteArray, offset + 1, board.length);
  }

  /**
   * Should use StringBuffer instead of String, but this method is only for debug purposes.
   */
  public String toString() {
    final StringBuffer ret = new StringBuffer(80);
    for (int i = 0; i < 8; ++i) {
      for (int j = 0; j < 8; ++j) {
        ret.append(getItem(j, i));
      }
      ret.append('\n');
    }
    ret.append("pass: ").append(getPassNum()).append('\n');
    return ret.toString();
  }

}