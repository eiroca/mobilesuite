/** GPL >= 3.0
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 * Copyright (C) Marius Rieder
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
package net.eiroca.j2me.pacman;

import PacMan;
import javax.microedition.lcdui.game.TiledLayer;
import net.eiroca.j2me.app.BaseApp;

/**
 * The Class GameField.
 */
public class GameField extends TiledLayer {

  /** The Constant WIDTH_IN_TILES. */
  private static final int WIDTH_IN_TILES = 21;

  /** The Constant HEIGHT_IN_TILES. */
  private static final int HEIGHT_IN_TILES = 17;

  /** The Constant TILE_WIDTH. */
  private static final int TILE_WIDTH = 10;

  /** The Constant TILE_HEIGHT. */
  private static final int TILE_HEIGHT = 10;

  /** The Constant TL_PILL_MAGIC1. */
  private static final int TL_PILL_MAGIC1 = 14;

  /** The Constant TL_PILL_MAGIC2. */
  private static final int TL_PILL_MAGIC2 = 15;

  /** The magic pill. */
  private static int[] magicPill = {
      GameField.TL_PILL_MAGIC1, GameField.TL_PILL_MAGIC2
  };

  /** The tick count. */
  private int tickCount = 0;

  /** The pills. */
  private int pills = 147;

  /**
   * Instantiates a new game field.
   */
  public GameField() {
    super(GameField.WIDTH_IN_TILES, GameField.HEIGHT_IN_TILES, BaseApp.createImage(PacMan.RES_FIELD), GameField.TILE_WIDTH, GameField.TILE_HEIGHT);
    createAnimatedTile(GameField.magicPill[0]);
    init();
  }

  /**
   * Inits the.
   */
  public void init() {
    BaseApp.loadTile(PacMan.RES_MAP, this);
    pills = 147;
  }

  /**
   * Tick.
   */
  public void tick() {
    final int tickState = (tickCount++ >> 4); // slow down x8
    setAnimatedTile(-1, GameField.magicPill[tickState % 2]);
  }

  /**
   * Contains impassable area.
   * 
   * @param x the x
   * @param y the y
   * @param width the width
   * @param height the height
   * @return true, if successful
   */
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

  /**
   * Can walk.
   * 
   * @param column the column
   * @param row the row
   * @return true, if successful
   */
  public boolean canWalk(int column, int row) {
    column = column < 0 ? 0 : column;
    column = column > GameField.WIDTH_IN_TILES ? GameField.WIDTH_IN_TILES : column;
    row = row < 0 ? 0 : row;
    row = row > GameField.HEIGHT_IN_TILES ? GameField.HEIGHT_IN_TILES : row;
    final int cell = getCell(column, row);
    if ((cell < 13) && (cell > 0)) { return false; }
    return true;
  }

  /**
   * Can ghost walk.
   * 
   * @param column the column
   * @param row the row
   * @return true, if successful
   */
  public boolean canGhostWalk(int column, int row) {
    column = column < 0 ? 0 : column;
    column = column > GameField.WIDTH_IN_TILES ? GameField.WIDTH_IN_TILES : column;
    row = row < 0 ? 0 : row;
    row = row > GameField.HEIGHT_IN_TILES ? GameField.HEIGHT_IN_TILES : row;
    final int cell = getCell(column, row);
    if ((cell < 12) && (cell > 0)) { return false; }
    return true;
  }

  /**
   * Eat pill.
   * 
   * @param column the column
   * @param row the row
   * @return true, if successful
   */
  public boolean eatPill(final int column, final int row) {
    final int cell = getCell(column, row);
    if (cell == 13) {
      setCell(column, row, 16);
      pills--;
      return true;
    }
    return false;
  }

  /**
   * Eat magic pill.
   * 
   * @param column the column
   * @param row the row
   * @return true, if successful
   */
  public boolean eatMagicPill(final int column, final int row) {
    final int cell = getCell(column, row);
    if (cell == -1) {
      setCell(column, row, 16);
      pills--;
      return true;
    }
    return false;
  }

  /**
   * Gets the pills.
   * 
   * @return the pills
   */
  public int getPills() {
    return pills;
  }

  /**
   * See pacman.
   * 
   * @param x the x
   * @param y the y
   * @param x2 the x2
   * @param y2 the y2
   * @return true, if successful
   */
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
