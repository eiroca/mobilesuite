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

import java.util.Vector;
import net.eiroca.j2me.game.tpg.GameMinMax;
import net.eiroca.j2me.game.tpg.GameMove;
import net.eiroca.j2me.game.tpg.GameTable;
import net.eiroca.j2me.game.tpg.TwoPlayerGame;

/**
 * The Class ReversiGame.
 */
public final class ReversiGame extends TwoPlayerGame {

  /** The eval num. */
  protected int evalNum = 0;

  /** The heur matrix. */
  protected int[][] heurMatrix;

  /** The liberty penalty. */
  protected int libertyPenalty;

  /** The num first free neighbours. */
  protected int numFirstFreeNeighbours;

  /** The num second free neighbours. */
  protected int numSecondFreeNeighbours;

  /** The num first player. */
  public int numFirstPlayer;

  /** The num second player. */
  public int numSecondPlayer;

  /** The point. */
  protected int point;

  /** The point first player. */
  protected int pointFirstPlayer;

  /** The point second player. */
  protected int pointSecondPlayer;

  /** The s01. */
  protected int s01;

  /** The s11. */
  protected int s11;

  /** The s bonus. */
  protected int sBonus;

  /** The square erase. */
  protected boolean squareErase;

  /** The r player. */
  protected byte rPlayer;

  /** The r table. */
  protected ReversiTable rTable;

  /** The table int array. */
  protected int[][] tableIntArray = new int[8][8];

  /**
   * Instantiates a new reversi game.
   * 
   * @param heurMatrix the heur matrix
   */
  public ReversiGame(final int[][] heurMatrix) {
    this(heurMatrix, 0, 0, false);
  }

  /**
   * Instantiates a new reversi game.
   * 
   * @param heurMatrix the heur matrix
   * @param libertyPenalty the liberty penalty
   * @param sBonus the s bonus
   * @param squareErase the square erase
   */
  public ReversiGame(final int[][] heurMatrix, final int libertyPenalty, final int sBonus, final boolean squareErase) {
    this.heurMatrix = heurMatrix;
    this.libertyPenalty = libertyPenalty;
    this.sBonus = sBonus;
    this.squareErase = squareErase;
  }

  /**
   * _turn.
   * 
   * @param table the table
   * @param player the player
   * @param move the move
   * @param newTable the new table
   * @param animated the animated
   * @return the game table[]
   */
  private GameTable[] _turn(final ReversiTable table, final byte player, final ReversiMove move, final ReversiTable newTable, final boolean animated) {
    final int row = move.row;
    final int col = move.col;
    if ((row != 8) && (table.getItem(row, col) != 0)) { return null; }
    Vector vTables = null;
    GameTable tables[];
    if (animated) {
      vTables = new Vector();
    }
    newTable.copyDataFrom(table);
    if (row == 8) {
      // pass
      newTable.setPassNum(newTable.getPassNum() + 1);
      tables = new GameTable[1];
      tables[0] = newTable;
      return tables;
    }
    newTable.setPassNum(0);
    newTable.setItem(row, col, ReversiTable.getPlayerItem(player));
    if (animated) {
      vTables.addElement(new ReversiTable(newTable));
    }
    boolean flipped = false;
    for (int dirrow = -1; dirrow <= 1; ++dirrow) {
      for (int dircol = -1; dircol <= 1; ++dircol) {
        if ((dirrow == 0) && (dircol == 0)) {
          continue;
        }
        int c = 1;
        while (ReversiMove.valid(row + c * dirrow, col + c * dircol) && (newTable.getItem(row + c * dirrow, col + c * dircol) == ReversiTable.getPlayerItem((byte) (1 - player)))) {
          ++c;
        }
        if ((c > 1) && ReversiMove.valid(row + c * dirrow, col + c * dircol) && (newTable.getItem(row + c * dirrow, col + c * dircol) == ReversiTable.getPlayerItem(player))) {
          flipped = true;
          for (int s1 = 1; s1 < c; ++s1) {
            newTable.flip(row + s1 * dirrow, col + s1 * dircol);
            if (animated) {
              vTables.addElement(new ReversiTable(newTable));
            }
          }
        }
      }
    }
    if (flipped) {
      if (animated) {
        tables = new GameTable[vTables.size()];
        for (int i = 0; i < vTables.size(); ++i) {
          tables[i] = (GameTable) vTables.elementAt(i);
        }
      }
      else {
        tables = new GameTable[1];
        tables[0] = newTable;
      }
      return tables;
    }
    newTable.setItem(row, col, (byte) 0);
    return null;
  }

  /*
   * @Override
   * @see net.eiroca.j2me.minmax.TwoPlayerGame#animatedTurn(net.eiroca.j2me.minmax.Table,
   *      byte, net.eiroca.j2me.minmax.Move, net.eiroca.j2me.minmax.Table)
   */
  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.tpg.TwoPlayerGame#animatedTurn(net.eiroca.j2me.game.tpg.GameTable, byte, net.eiroca.j2me.game.tpg.GameMove, net.eiroca.j2me.game.tpg.GameTable)
   */
  public GameTable[] animatedTurn(final GameTable table, final byte player, final GameMove move, final GameTable newt) {
    return _turn((ReversiTable) table, player, (ReversiMove) move, (ReversiTable) newt, true);
  }

  /**
   * Erase square heuristic.
   * 
   * @param i the i
   * @param j the j
   * @param id the id
   * @param jd the jd
   */
  protected void eraseSquareHeuristic(final int i, final int j, final int id, final int jd) {
    s01 = heurMatrix[i][j + jd];
    s11 = heurMatrix[i + id][j + jd];
    heurMatrix[i][j + jd] = 0;
    heurMatrix[i + id][j] = 0;
    heurMatrix[i + id][j + jd] = 0;
  }

  /**
   * Eval.
   * 
   * @param fullProcess the full process
   */
  protected void eval(final boolean fullProcess) {
    rTable.convertToIntArray(tableIntArray);
    final boolean lazyProcess = !fullProcess || isGameEnded() || (numFirstPlayer + numSecondPlayer > 58);
    numFirstPlayer = 0;
    numSecondPlayer = 0;
    pointFirstPlayer = 0;
    pointSecondPlayer = 0;
    numFirstFreeNeighbours = 0;
    numSecondFreeNeighbours = 0;
    if (!lazyProcess && squareErase) {
      if (tableIntArray[0][0] != 0) {
        eraseSquareHeuristic(0, 0, 1, 1);
      }
      if (tableIntArray[0][7] != 0) {
        eraseSquareHeuristic(0, 7, 1, -1);
      }
      if (tableIntArray[7][7] != 0) {
        eraseSquareHeuristic(7, 7, -1, -1);
      }
      if (tableIntArray[7][0] != 0) {
        eraseSquareHeuristic(7, 0, -1, 1);
      }
    }
    for (int i = 0; i < 8; ++i) {
      for (int j = 0; j < 8; ++j) {
        final int item = tableIntArray[i][j];
        switch (item) {
          case 1:
            ++numFirstPlayer;
            if (!lazyProcess) {
              pointFirstPlayer += heurMatrix[i][j];
              if (libertyPenalty != 0) {
                numFirstFreeNeighbours += freeNeighbours(i, j);
              }
            }
            break;
          case 2:
            ++numSecondPlayer;
            if (!lazyProcess) {
              pointSecondPlayer += heurMatrix[i][j];
              if (libertyPenalty != 0) {
                numSecondFreeNeighbours += freeNeighbours(i, j);
              }
            }
            break;
        }
      }
    }
    if (!lazyProcess && squareErase) {
      restoreSquareHeuristic(0, 0, 1, 1);
      restoreSquareHeuristic(0, 7, 1, -1);
      restoreSquareHeuristic(7, 7, -1, -1);
      restoreSquareHeuristic(7, 0, -1, 1);
    }
    int squareBonusPoint = 0;
    if (!lazyProcess && (sBonus != 0)) {
      squareBonusPoint = squareBonus();
    }
    if (lazyProcess) {
      point = numFirstPlayer - numSecondPlayer;
      if (isGameEnded()) {
        if (point > 0) {
          point += GameMinMax.MAX_POINT;
        }
        else if (point < 0) {
          point -= GameMinMax.MAX_POINT;
        }
      }
    }
    else {
      point = pointFirstPlayer - pointSecondPlayer + libertyPenalty * (numSecondFreeNeighbours - numFirstFreeNeighbours) + sBonus * squareBonusPoint;
    }
    if (rPlayer == 1) {
      point = -point;
    }
  }

  /**
   * Free neighbours.
   * 
   * @param i the i
   * @param j the j
   * @return the int
   */
  protected int freeNeighbours(final int i, final int j) {
    int freeNeighbours = 0;
    for (int id = -1; id <= 1; ++id) {
      for (int jd = -1; jd <= 1; ++jd) {
        if ((i + id >= 0) && (i + id < 8) && (j + jd >= 0) && (j + jd < 8) && (tableIntArray[i + id][j + jd] == 0)) {
          ++freeNeighbours;
        }
      }
    }
    return freeNeighbours;
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.tpg.TwoPlayerGame#getEvalNum()
   */
  public int getEvalNum() {
    return evalNum;
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.tpg.TwoPlayerGame#getGameResult()
   */
  public int getGameResult() {
    int piecediff = numFirstPlayer - numSecondPlayer;
    if (rPlayer == 1) {
      piecediff = -piecediff;
    }
    if (piecediff > 0) {
      return TwoPlayerGame.WIN;
    }
    else if (piecediff < 0) {
      return TwoPlayerGame.LOSS;
    }
    else {
      return TwoPlayerGame.DRAW;
    }
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.tpg.TwoPlayerGame#getPoint()
   */
  public int getPoint() {
    return point;
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.tpg.TwoPlayerGame#hasPossibleMove(net.eiroca.j2me.game.tpg.GameTable, byte)
   */
  public boolean hasPossibleMove(final GameTable table, final byte player) {
    final ReversiMove[] moves = (ReversiMove[]) possibleMoves(table, player);
    return (moves != null) && ((moves.length > 1) || (moves[0].row != 8));
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.tpg.TwoPlayerGame#isGameEnded()
   */
  public boolean isGameEnded() {
    if ((numFirstPlayer + numSecondPlayer == 64) || (numFirstPlayer == 0) || (numSecondPlayer == 0) || (rTable.getPassNum() == 2)) { return true; }
    return false;
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.tpg.TwoPlayerGame#possibleMoves(net.eiroca.j2me.game.tpg.GameTable, byte)
   */
  public GameMove[] possibleMoves(final GameTable table, final byte player) {
    if (!(table instanceof ReversiTable)) { return null; }
    final Vector moves = new Vector();
    if (((ReversiTable) table).getPassNum() == 2) {
      // two passes: end of the game
      return null;
    }
    final ReversiTable newTable = new ReversiTable();
    final ReversiMove move = new ReversiMove(0, 0);
    boolean hasMove = false;
    for (int row = 0; row < 8; ++row) {
      for (int col = 0; col < 8; ++col) {
        move.setCoordinates(row, col);
        if (!hasMove && (((ReversiTable) table).getItem(row, col) == 0)) {
          hasMove = true;
        }
        final boolean goodMove = turn(table, player, move, newTable);
        if (goodMove) {
          moves.addElement(new ReversiMove(move));
        }
      }
    }
    if (!hasMove) { return null; }
    if (moves.size() == 0) {
      // need to pass
      moves.addElement(new ReversiMove(8, 8));
    }
    final GameMove[] retMoves = new ReversiMove[moves.size()];
    for (int m = 0; m < moves.size(); ++m) {
      retMoves[m] = (GameMove) moves.elementAt(m);
    }
    return retMoves;
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.tpg.TwoPlayerGame#resetEvalNum()
   */
  public void resetEvalNum() {
    evalNum = 0;
  }

  /**
   * Restore square heuristic.
   * 
   * @param i the i
   * @param j the j
   * @param id the id
   * @param jd the jd
   */
  protected void restoreSquareHeuristic(final int i, final int j, final int id, final int jd) {
    heurMatrix[i][j + jd] = s01;
    heurMatrix[i + id][j] = s01;
    heurMatrix[i + id][j + jd] = s11;
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.tpg.TwoPlayerGame#setTable(net.eiroca.j2me.game.tpg.GameTable, byte, boolean)
   */
  protected void setTable(final GameTable table, final byte player, final boolean fullProcess) {
    if (!(table instanceof ReversiTable)) { throw new IllegalArgumentException(); }
    rTable = (ReversiTable) table;
    rPlayer = player;
    ++evalNum;
    eval(fullProcess);
  }

  /**
   * Square bonus.
   * 
   * @return the int
   */
  protected int squareBonus() {
    boolean c1 = true;
    boolean c2 = true;
    boolean c3 = true;
    boolean c4 = true;
    final int bonus[] = new int[3];
    bonus[1] = 0;
    bonus[2] = 0;
    final int corner1 = tableIntArray[0][0];
    if (corner1 != 0) {
      int c1r = 1;
      while ((c1r < 8) && (tableIntArray[0][c1r] == corner1)) {
        ++c1r;
      }
      bonus[corner1] += c1r - 1;
      if (c1r == 8) {
        c2 = false;
      }
    }
    final int corner2 = tableIntArray[0][7];
    if (corner2 != 0) {
      if (c2) {
        int c2l = 1;
        while ((c2l < 8) && (tableIntArray[0][7 - c2l] == corner2)) {
          ++c2l;
        }
        bonus[corner2] += c2l - 1;
        if (c2l == 8) {
          c1 = false;
        }
      }
      int c2r = 1;
      while ((c2r < 8) && (tableIntArray[c2r][7] == corner2)) {
        ++c2r;
      }
      bonus[corner2] += c2r - 1;
      if (c2r == 8) {
        c3 = false;
      }
    }
    final int corner3 = tableIntArray[7][7];
    if (corner3 != 0) {
      if (c3) {
        int c3l = 1;
        while ((c3l < 8) && (tableIntArray[7 - c3l][7] == corner3)) {
          ++c3l;
        }
        bonus[corner3] += c3l - 1;
      }
      int c3r = 1;
      while ((c3r < 8) && (tableIntArray[7][7 - c3r] == corner3)) {
        ++c3r;
      }
      bonus[corner3] += c3r - 1;
      if (c3r == 8) {
        c4 = false;
      }
    }
    final int corner4 = tableIntArray[7][0];
    if (corner4 != 0) {
      if (c4) {
        int c4l = 1;
        while ((c4l < 8) && (tableIntArray[7][c4l] == corner4)) {
          ++c4l;
        }
        bonus[corner4] += c4l - 1;
      }
      int c4r = 1;
      while ((c4r < 8) && (tableIntArray[7 - c4r][0] == corner4)) {
        ++c4r;
      }
      bonus[corner4] += c4r - 1;
      if (c4r == 8) {
        c1 = false;
      }
    }
    if ((corner1 != 0) && c1) {
      int c1l = 1;
      while ((c1l < 8) && (tableIntArray[c1l][0] == corner1)) {
        ++c1l;
      }
      bonus[corner1] += c1l - 1;
    }
    return bonus[1] - bonus[2];
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.tpg.TwoPlayerGame#turn(net.eiroca.j2me.game.tpg.GameTable, byte, net.eiroca.j2me.game.tpg.GameMove, net.eiroca.j2me.game.tpg.GameTable)
   */
  public boolean turn(final GameTable table, final byte player, final GameMove move, final GameTable newt) {
    return _turn((ReversiTable) table, player, (ReversiMove) move, (ReversiTable) newt, false) != null;
  }

}
