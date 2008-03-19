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

import java.util.Vector;
import net.eiroca.j2me.app.BaseApp;

public class BubbletGame {

  public static final int RED = 0;
  public static final int GREEN = 1;
  public static final int BLUE = 2;
  public static final int PURPLE = 3;
  public static final int YELLOW = 4;
  public static final int TURQUOISE = 5;
  public static final int BLACK = 6;

  public int fieldWidth;
  public int fieldHeight;
  public int field[][];

  public BubbletGame(final int pFieldWidth, final int pFieldHeight) {
    fieldWidth = pFieldWidth;
    fieldHeight = pFieldHeight;
    field = new int[fieldWidth][fieldHeight];
  }

  public int processFire(final int cross_x, final int cross_y) {
    int score = 0;
    final int selectedColor = field[cross_x][cross_y];
    // Is it white? then do nothing at all
    final Vector cellSet = new Vector(20);
    if (selectedColor != BubbletGame.BLACK) {
      // Find neighboring cells with same color in x and y direction
      gatherColoredNeighbors(new CellBlock(cross_x, cross_y), selectedColor, cellSet);
      score = score + (cellSet.size() * cellSet.size());
      CellBlock cell;
      // mark all erased cells
      for (int i = 0; i < cellSet.size(); i++) {
        cell = (CellBlock) cellSet.elementAt(i);
        field[cell.xval][cell.yval] = BubbletGame.BLACK;
      }
      boolean moved = true;
      while (moved) {
        moved = false;
        for (int x = 0; x < fieldWidth; x++) {
          for (int y = fieldHeight - 1; y > 0; y--) {
            if (field[x][y] == BubbletGame.BLACK) {
              if (field[x][y - 1] != BubbletGame.BLACK) {
                field[x][y] = field[x][y - 1];
                field[x][y - 1] = BubbletGame.BLACK;
                moved = true;
              }
            }
          }
        }
      }
      cellSet.removeAllElements();
      // move empty slices to the left corner
      for (int i = 0; i < fieldWidth - 1; i++) {
        if (field[i][fieldHeight - 1] == BubbletGame.BLACK) {
          moveSliceLeft(i);
          break;
        }
      }
    }
    return score;
  }

  private void moveSliceLeft(final int x) {
    if (x > fieldWidth) { return; }
    final Vector storage = new Vector(10);
    Vector slice;
    for (int i = x; i < fieldWidth; i++) {
      slice = new Vector(10);
      for (int j = 0; j < fieldHeight; j++) {
        final int tmp = field[i][j];
        if (tmp != BubbletGame.BLACK) {
          slice.addElement(new CellBlock(i, j, tmp));
          field[i][j] = BubbletGame.BLACK;
        }
        else {
          continue;
        }
      }
      if (slice.size() > 0) {
        storage.addElement(slice);
      }
    }
    for (int i = 0; i < storage.size(); i++) {
      slice = (Vector) storage.elementAt(i);
      for (int j = 0; j < slice.size(); j++) {
        final CellBlock c = (CellBlock) slice.elementAt(j);
        field[x + i][c.yval] = c.color;
      }
    }
  }

  public boolean isGameFinished() {
    for (int i = 0; i < fieldWidth; i++) {
      for (int j = 0; j < fieldHeight; j++) {
        if ((field[i][j] != BubbletGame.BLACK) && hasNeighbors(new CellBlock(i, j, field[i][j]))) { return false; }
      }
    }
    return true;
  }

  private boolean hasNeighbors(final CellBlock pCell) {
    // check for cell with same color above the current cell
    final int x = pCell.xval;
    final int y = pCell.yval;
    final int c = pCell.color;
    if ((y > 0) && (field[x][y - 1] == c)) { return true; }
    // check for cell with same color below the current cell
    if ((y + 1) < fieldHeight) {
      if (field[x][y + 1] == c) { return true; }
    }
    // check for cell with same color right from the current cell
    if ((x > 0) && (field[x - 1][y] == c)) { return true; }
    // check for cell with same color right from the current cell
    if ((x + 1) < fieldWidth) {
      if (field[x + 1][y] == c) { return true; }
    }
    return false;
  }

  // recursive method, that gathers information about
  // contiguous spaces of the same color
  private void gatherColoredNeighbors(final CellBlock pCell, final int pColor, final Vector cellSet) {
    final Vector v = new Vector(10);
    final int x = pCell.xval;
    final int y = pCell.yval;
    // check for cell with same color above the current cell
    if ((y > 0) && (field[x][y - 1] == pColor)) {
      final CellBlock c = new CellBlock(x, y - 1);
      if (!cellSet.contains(c)) {
        v.addElement(c);
        cellSet.addElement(c);
      }
    }
    // check for cell with same color below the current cell
    if ((y + 1) < fieldHeight) {
      if (field[x][y + 1] == pColor) {
        final CellBlock c = new CellBlock(x, y + 1);
        if (!cellSet.contains(c)) {
          v.addElement(c);
          cellSet.addElement(c);
        }
      }
    }
    // check for cell with same color right from the current cell
    if ((x > 0) && (field[x - 1][y] == pColor)) {
      final CellBlock c = new CellBlock(x - 1, y);
      if (!cellSet.contains(c)) {
        v.addElement(c);
        cellSet.addElement(c);
      }
    }
    // check for cell with same color right from the current cell
    if ((x + 1) < fieldWidth) {
      if (field[x + 1][y] == pColor) {
        final CellBlock c = new CellBlock(x + 1, y);
        if (!cellSet.contains(c)) {
          v.addElement(c);
          cellSet.addElement(c);
        }
      }
    }
    // recursive call for all cells of the same color that were found
    for (int i = 0; i < v.size(); i++) {
      gatherColoredNeighbors((CellBlock) v.elementAt(i), pColor, cellSet);
    }
    // if the size of CellSet equals one it means, that there is only
    // one cell, and no neighbor of that color, so nothing can be dissolved
    if (cellSet.size() == 1) {
      cellSet.removeAllElements();
    }
  }

  public void startGame() {
    // initialize sizes of basic screen elements (only once)
    for (int i = 0; i < fieldWidth; i++) {
      for (int j = 0; j < fieldHeight; j++) {
        field[i][j] = BaseApp.rand(6);
      }
    }
  }

}
