/** GPL >= 2.0
 * Based upon Nokia PacMan
 *
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
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PacmanField {

  private static final int WIDTH_IN_TILES = 21;
  private static final int HEIGHT_IN_TILES = 17;

  private static int[][] cellTiles = {
      {
          8, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 9
      }, {
          7, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 7
      }, {
          7, -1, 5, 6, 3, 13, 5, 6, 3, 13, 2, 13, 5, 6, 3, 13, 5, 6, 3, -1, 7
      }, {
          7, 13, 13, 13, 13, 13, 13, 13, 13, 13, 7, 13, 13, 13, 13, 13, 13, 13, 13, 13, 7
      }, {
          7, 13, 5, 6, 3, 13, 2, 13, 5, 6, 6, 6, 3, 13, 2, 13, 5, 6, 3, 13, 7
      }, {
          7, 13, 13, 13, 13, 13, 7, 16, 16, 16, 16, 16, 16, 16, 7, 13, 13, 13, 13, 13, 7
      }, {
          11, 6, 6, 6, 3, 13, 4, 16, 8, 3, 12, 5, 9, 16, 4, 13, 5, 6, 6, 6, 10
      }, {
          16, 16, 16, 16, 16, 13, 13, 16, 7, 16, 16, 16, 7, 16, 13, 13, 16, 16, 16, 16, 16
      }, {
          8, 6, 6, 6, 3, 13, 1, 16, 11, 6, 6, 6, 10, 16, 1, 13, 5, 6, 6, 6, 9
      }, {
          7, 13, 13, 13, 13, 13, 13, 16, 16, 16, 16, 16, 16, 16, 13, 13, 13, 13, 13, 13, 7
      }, {
          7, 13, 5, 6, 9, 13, 5, 6, 3, 13, 1, 13, 5, 6, 3, 13, 8, 6, 3, 13, 7
      }, {
          7, -1, 13, 13, 7, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 7, 13, 13, -1, 7
      }, {
          7, 6, 3, 13, 4, 13, 2, 13, 5, 6, 6, 6, 3, 13, 2, 13, 4, 13, 5, 6, 7
      }, {
          7, 13, 13, 13, 13, 13, 7, 13, 13, 13, 7, 13, 13, 13, 7, 13, 13, 13, 13, 13, 7
      }, {
          7, 13, 5, 6, 6, 6, 6, 6, 3, 13, 4, 13, 5, 6, 6, 6, 6, 6, 3, 13, 7
      }, {
          7, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 7
      }, {
          11, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 10
      }
  };

  public static void main(final String[] args) {
    try {
      System.out.print("building ... ");
      final DataOutputStream out = new DataOutputStream(new FileOutputStream("level.map"));
      out.writeByte(PacmanField.HEIGHT_IN_TILES);
      out.writeByte(PacmanField.WIDTH_IN_TILES);
      for (int row = 0; row < PacmanField.HEIGHT_IN_TILES; ++row) {
        for (int column = 0; column < PacmanField.WIDTH_IN_TILES; ++column) {
          out.writeByte(PacmanField.cellTiles[row][column]);
        }
      }
      out.close();
      System.out.println("map created");
    }
    catch (final FileNotFoundException e) {
      e.printStackTrace();
    }
    catch (final IOException e) {
      e.printStackTrace();
    }
  }

}
