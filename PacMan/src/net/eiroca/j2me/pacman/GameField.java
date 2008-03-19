/** GPL >= 2.0
 * Based upon Nokia PacMan game written by Marius Rieder
 *
 * Copyright (C) Marius Rieder
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
package net.eiroca.j2me.pacman;

import PacMan;
import javax.microedition.lcdui.game.TiledLayer;
import net.eiroca.j2me.app.BaseApp;

public class GameField extends TiledLayer {

  private static final int WIDTH_IN_TILES = 21;
  private static final int HEIGHT_IN_TILES = 17;
  private static final int TILE_WIDTH = 10;
  private static final int TILE_HEIGHT = 10;
  private static final int TL_PILL_MAGIC1 = 14;
  private static final int TL_PILL_MAGIC2 = 15;

  private static int[] magicPill = {
      GameField.TL_PILL_MAGIC1, GameField.TL_PILL_MAGIC2
  };

  private int tickCount = 0;

  private int pills = 147;

  public GameField() {
    super(GameField.WIDTH_IN_TILES, GameField.HEIGHT_IN_TILES, BaseApp.createImage(PacMan.RES_FIELD), GameField.TILE_WIDTH, GameField.TILE_HEIGHT);
    createAnimatedTile(GameField.magicPill[0]);
    init();
  }

  public void init() {
    BaseApp.loadTile(PacMan.RES_MAP, this);
    pills = 147;
  }

  public void tick() {
    final int tickState = (tickCount++ >> 4); // slow down x8
    setAnimatedTile(-1, GameField.magicPill[tickState % 2]);
  }

  public boolean containsImpassableArea(final int x, final int y, final int width, final int height) {
    int rowMin = (y - height / 2 + 1) / GameField.TILE_HEIGHT;
    int rowMax = (y + height / 2 - 1) / GameField.TILE_HEIGHT;
    int columnMin = (x - width / 2 + 1) / GameField.TILE_WIDTH;
    int columnMax = (x + width / 2 - 1) / GameField.TILE_WIDTH;
    rowMin = rowMin > 0 ? rowMin : 0;
    rowMax = rowMax < GameField.HEIGHT_IN_TILES ? rowMax : GameField.HEIGHT_IN_TILES - 1;
    columnMin = columnMin > 0 ? columnMin : 0;
    columnMax = columnMax < GameField.WIDTH_IN_TILES ? columnMax : GameField.WIDTH_IN_TILES - 1;
    for (int row = rowMin; row <= rowMax; ++row) {
      for (int column = columnMin; column <= columnMax; ++column) {
        final int cell = getCell(column, row);
        if ((cell < 13) && (cell > 0)) { return true; }
      }
    }
    return false;
  }

  public boolean canWalk(int column, int row) {
    column = column < 0 ? 0 : column;
    column = column > GameField.WIDTH_IN_TILES ? GameField.WIDTH_IN_TILES : column;
    row = row < 0 ? 0 : row;
    row = row > GameField.HEIGHT_IN_TILES ? GameField.HEIGHT_IN_TILES : row;
    final int cell = getCell(column, row);
    if ((cell < 13) && (cell > 0)) { return false; }
    return true;
  }

  public boolean canGhostWalk(int column, int row) {
    column = column < 0 ? 0 : column;
    column = column > GameField.WIDTH_IN_TILES ? GameField.WIDTH_IN_TILES : column;
    row = row < 0 ? 0 : row;
    row = row > GameField.HEIGHT_IN_TILES ? GameField.HEIGHT_IN_TILES : row;
    final int cell = getCell(column, row);
    if ((cell < 12) && (cell > 0)) { return false; }
    return true;
  }

  public boolean eatPill(final int column, final int row) {
    final int cell = getCell(column, row);
    if (cell == 13) {
      setCell(column, row, 16);
      pills--;
      return true;
    }
    return false;
  }

  public boolean eatMagicPill(final int column, final int row) {
    final int cell = getCell(column, row);
    if (cell == -1) {
      setCell(column, row, 16);
      pills--;
      return true;
    }
    return false;
  }

  public int getPills() {
    return pills;
  }

  public boolean seePacman(final int x, final int y, final int x2, final int y2) {
    int row = y;
    int row2 = y2 / GameField.TILE_HEIGHT;
    int column = x;
    int column2 = x2 / GameField.TILE_WIDTH;
    if (row == row2) {
      if (column2 < column) {
        final int i = column2;
        column2 = column;
        column = i;
      }
      if (column2 - column <= 1) { return true; }
      for (int i = 1; i < (column2 - column); i++) {
        final int cell = getCell(column + i, row);
        if ((cell < 13) && (cell > 0)) { return false; }
      }
      return true;
    }
    else if (column == column2) {
      if (row2 < row) {
        final int i = row2;
        row2 = row;
        row = i;
      }
      if (row2 - row <= 1) { return true; }
      for (int i = 1; i < row2 - row; i++) {
        final int cell = getCell(column, row + i);
        if ((cell < 13) && (cell > 0)) { return false; }
      }
      return true;
    }
    return false;
  }
}
