/** GPL >= 3.0
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 * Copyright (C) M. Jumari
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
package net.eiroca.j2me.minesweeper.game;

import java.util.Vector;
import net.eiroca.j2me.app.BaseApp;

/**
 * The Class MineSweeperGame.
 */
public class MineSweeperGame {

  /** The Constant GE_RUNNING. */
  public static final int GE_RUNNING = 0;

  /** The Constant GE_EXPLODED. */
  public static final int GE_EXPLODED = 1;

  /** The Constant GE_RESOLVED. */
  public static final int GE_RESOLVED = 2;

  /** The Constant MINE_MIN_SIZE. */
  public static final int MINE_MIN_SIZE = 9;

  /** The Constant MINE_MAX_SIZE. */
  public static final int MINE_MAX_SIZE = 30;

  /** The Constant MINE_CHECKED. */
  public static final int MINE_CHECKED = -100;

  /** The Constant MINE_UNCHECKED. */
  public static final int MINE_UNCHECKED = -200;

  /** The Constant MINE_BOMB. */
  public static final int MINE_BOMB = -300;

  /** The Constant MIN_BOMB. */
  public static final int MIN_BOMB = 9;

  /** The Constant MAX_BOMB. */
  public static final int MAX_BOMB = 99;

  /** The size_width. */
  public int size_width = 9;

  /** The size_height. */
  public int size_height = 9;

  /** The bomb. */
  public int bomb = 10;

  /** The status. */
  public int status = MineSweeperGame.GE_RUNNING;

  /** The field. */
  public MineInfo[][] field;

  /**
   * Instantiates a new mine sweeper game.
   */
  public MineSweeperGame() {
    //
  }

  /**
   * Fungsi untuk membuat game baru dengan paramater Level B = Beginer, I = Intermediate, E = Expert.
   * 
   * @param level the level
   */
  public void newGame(final char level) {
    if (level == 'B') {
      newGame(9, 9, 10);
    }
    if (level == 'I') {
      newGame(15, 17, 40);
    }
    if (level == 'E') {
      newGame(25, 20, 99);
    }
  }

  /**
   * Fungsi untuk membuat game baru dengan parameter lebar, tinggi dan jumlah bomb.
   * 
   * @param width lebar game
   * @param height tinggi game
   * @param bomb jumlah bomb
   */
  public void newGame(final int width, final int height, final int bomb) {
    size_width = width;
    size_height = height;
    this.bomb = bomb;
    init();
    status = MineSweeperGame.GE_RUNNING;
  }

  /**
   * Fungi untuk klik kiri game.
   * 
   * @param x koordinat x
   * @param y koordinat y
   * @return the vector
   */
  public Vector checkCell(final int x, final int y) {
    final Vector result = new Vector();
    final MineInfo m = field[x][y];
    if (m.status_guess == MineSweeperGame.MINE_UNCHECKED) {
      if (m.status_real > 0) {
        m.status_guess = m.status_real;
        result.addElement(m);
      }
      else if (m.status_real == MineSweeperGame.MINE_BOMB) {
        showBomb(result);
      }
      else if (m.status_real == 0) {
        m.status_guess = m.status_real;
        result.addElement(m);
        checkMines(result);
      }
    }
    checkVictory();
    return result;
  }

  /**
   * Fungi untuk klik kanan game.
   * 
   * @param x koordinat x
   * @param y koordinat y
   * @return the vector
   */
  public Vector markBomb(final int x, final int y) {
    final Vector result = new Vector();
    final MineInfo m = field[x][y];
    if (m.status_guess == MineSweeperGame.MINE_CHECKED) {
      m.status_guess = MineSweeperGame.MINE_UNCHECKED;
      result.addElement(m);
    }
    else {
      m.status_guess = MineSweeperGame.MINE_CHECKED;
      result.addElement(m);
    }
    return result;
  }

  /**
   * Double check.
   * 
   * @param result the result
   * @param mi the mi
   */
  public void doubleCheck(final Vector result, final MineInfo mi) {
    if ((mi.status_guess == MineSweeperGame.MINE_UNCHECKED) && (mi.status_real == MineSweeperGame.MINE_BOMB)) {
      showBomb(result);
    }
    if (mi.status_guess == MineSweeperGame.MINE_UNCHECKED) {
      mi.status_guess = mi.status_real;
      result.addElement(mi);
      if (mi.status_real == 0) {
        checkMines(result);
      }
    }
  }

  /**
   * Can double click.
   * 
   * @param x the x
   * @param y the y
   * @return true, if successful
   */
  public boolean canDoubleClick(final int x, final int y) {
    final MineInfo m = field[x][y];
    boolean result = false;
    int bombs = 0;
    if (m.status_guess > 0) {
      for (int dx = -1; dx < 2; dx++) {
        final int nx = x + dx;
        if ((nx >= 0) && (nx < size_width)) {
          for (int dy = -1; dy < 2; dy++) {
            if ((dx == 0) & (dy == 0)) {
              continue;
            }
            final int ny = y + dy;
            if ((ny >= 0) && (ny < size_height)) {
              if (field[nx][ny].status_guess == MineSweeperGame.MINE_CHECKED) {
                bombs++;
              }
            }
          }
        }
      }
      result = (m.status_guess == bombs);
    }
    return result;
  }

  /**
   * Fungi untuk klik ganda game.
   * 
   * @param x koordinat x
   * @param y koordinat y
   * @return the vector
   */
  public Vector doubleClick(final int x, final int y) {
    final Vector result = new Vector();
    for (int dx = -1; dx < 2; dx++) {
      if (status == MineSweeperGame.GE_EXPLODED) {
        break;
      }
      final int nx = x + dx;
      if ((nx >= 0) && (nx < size_width)) {
        for (int dy = -1; dy < 2; dy++) {
          if ((dx == 0) & (dy == 0)) {
            continue;
          }
          final int ny = y + dy;
          if ((ny >= 0) && (ny < size_height)) {
            doubleCheck(result, field[nx][ny]);
          }
          if (status == MineSweeperGame.GE_EXPLODED) {
            break;
          }
        }
      }
    }
    checkVictory();
    return result;
  }

  /**
   * Fungsi untuk mencari jumlah kotak yang ditandai.
   * 
   * @return jumlah kotak yang ditandai dan belum terbuka
   */
  public int checked() {
    int c_checked = 0;
    for (int y = 0; y < size_height; y++) {
      for (int x = 0; x < size_width; x++) {
        if (field[x][y].status_guess == MineSweeperGame.MINE_CHECKED) {
          c_checked++;
        }
      }
    }
    return c_checked;
  }

  /**
   * Fungsi untuk mencari jumlah kotak yang belum ditandai.
   * 
   * @return jumlah kotak yang belum ditandai dan belum terbuka
   */
  private int unchecked() {
    int c_unchecked = 0;
    for (int y = 0; y < size_height; y++) {
      for (int x = 0; x < size_width; x++) {
        if (field[x][y].status_guess == MineSweeperGame.MINE_UNCHECKED) {
          c_unchecked++;
        }
      }
    }
    return c_unchecked;
  }

  /**
   * Fungsi untuk mengecek menang.
   */
  private void checkVictory() {
    if (checked() + unchecked() == bomb) {
      status = MineSweeperGame.GE_RESOLVED;
    }
  }

  /**
   * Check.
   * 
   * @param vector the vector
   * @param mi the mi
   */
  private void check(final Vector vector, final MineInfo mi) {
    if (!vector.contains(mi)) {
      if ((mi.status_guess == MineSweeperGame.MINE_UNCHECKED) && (mi.status_real >= 0)) {
        mi.status_guess = mi.status_real;
        vector.addElement(mi);
      }
    }
  }

  /**
   * Fungsi untuk membuka sekitar.
   * 
   * @param res vector temporery yang digunakan
   */
  private void checkMines(final Vector res) {
    int i = 0;
    while (true) {
      if (i >= res.size()) {
        break;
      }
      final MineInfo m = (MineInfo) res.elementAt(i);
      if (m.status_real == 0) {
        for (int dx = -1; dx < 2; dx++) {
          final int nx = m.x + dx;
          if ((nx >= 0) && (nx < size_width)) {
            for (int dy = -1; dy < 2; dy++) {
              if ((dx == 0) & (dy == 0)) {
                continue;
              }
              final int ny = m.y + dy;
              if ((ny >= 0) && (ny < size_height)) {
                check(res, field[nx][ny]);
              }
            }
          }
        }
      }
      i++;
    }
  }

  /**
   * Fungsi untuk membuka semua bomb.
   * 
   * @param result the result
   */
  private void showBomb(final Vector result) {
    status = MineSweeperGame.GE_EXPLODED;
    result.removeAllElements();
    for (int y = 0; y < size_height; y++) {
      for (int x = 0; x < size_width; x++) {
        if ((field[x][y].status_guess == MineSweeperGame.MINE_UNCHECKED) && (field[x][y].status_real == MineSweeperGame.MINE_BOMB)) {
          field[x][y].status_guess = MineSweeperGame.MINE_BOMB;
          result.addElement(field[x][y]);
        }
      }
    }
  }

  /**
   * Fungsi untuk membuat kotak game baru.
   */
  private void init() {
    // buat kotak kosong
    field = new MineInfo[size_width][size_height];
    for (int y = 0; y < size_height; y++) {
      for (int x = 0; x < size_width; x++) {
        field[x][y] = new MineInfo(x, y, MineSweeperGame.MINE_UNCHECKED, MineSweeperGame.MINE_UNCHECKED);
      }
    }
    // random lokasi bomb
    int z = 0;
    int px;
    int py;
    while (z != bomb) {
      px = BaseApp.rand(size_width);
      py = BaseApp.rand(size_height);
      if (field[px][py].status_real != MineSweeperGame.MINE_BOMB) {
        field[px][py].status_real = MineSweeperGame.MINE_BOMB;
        z++;
      }
    }
    // kasih nilai semua kotak kecuali bomb
    for (int y = 0; y < size_height; y++) {
      for (int x = 0; x < size_width; x++) {
        if (field[x][y].status_real != MineSweeperGame.MINE_BOMB) {
          field[x][y].status_real = bomb(x, y);
        }
      }
    }
  }

  /**
   * Fungsi untuk mencari jumlah bomb disekitar kotak.
   * 
   * @param x koordinat x
   * @param y koordinat y
   * @return jumlah kotak yang ditandai sebaga bomb yang ada disekitar kotak dan belum terbuka
   */
  private int bomb(final int x, final int y) {
    int result = 0;
    for (int dx = -1; dx < 2; dx++) {
      final int nx = x + dx;
      if ((nx >= 0) && (nx < size_width)) {
        for (int dy = -1; dy < 2; dy++) {
          if ((dx == 0) & (dy == 0)) {
            continue;
          }
          final int ny = y + dy;
          if ((ny >= 0) && (ny < size_height)) {
            if (field[nx][ny].status_real == MineSweeperGame.MINE_BOMB) {
              result++;
            }
          }
        }
      }
    }
    return result;
  }

}
