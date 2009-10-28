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

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.game.GameApp;
import net.eiroca.j2me.game.GameScreen;

public class BubbletScreen extends GameScreen {

  public static final int MSG_NAME = GameApp.MSG_USERDEF + 0;

  private static int[] COLORS = {
      0x00FF0000, 0x0000FF00, 0x000000FF, 0x00FFFF00, 0x00FF00FF, 0x0000FFFF, 0x00000000
  };

  private final BubbletGame game;
  private final int cellWidth;
  private final int cellHeight;
  private int cross_x;
  private int cross_y;
  private final int off_x;
  private final int off_y;
  private final Font f;

  public BubbletScreen(final GameApp pMidlet, final int pFieldWidth, final int pFieldHeight) {
    super(pMidlet, false, true);
    name = Application.messages[BubbletScreen.MSG_NAME];
    f = Font.getDefaultFont();
    final int resX = 1;
    final int resY = 1 + f.getHeight();
    // Set the Display of our application
    cellWidth = (screenWidth - resX) / pFieldWidth;
    cellHeight = (screenHeight - resY) / pFieldHeight;
    off_x = (screenWidth - cellWidth * pFieldWidth) / 2;
    off_y = resY;
    game = new BubbletGame(pFieldWidth, pFieldHeight);
  }

  public void init() {
    super.init();
    // Initialize cross hairs coordinates on first paint() call
    cross_x = 0;
    cross_y = 0;
    score.beginGame(1, 0, 0);
    game.startGame();
  }

  public boolean tick() {
    screen.setColor(Application.background);
    screen.fillRect(0, 0, screenWidth, screenHeight);
    drawWholeBoard(screen);
    drawCross(screen, cross_x, cross_y);
    return true;
  }

  private void drawWholeBoard(final Graphics g) {
    g.setColor(Application.foreground);
    g.drawString("Score: " + score.getScore(), off_x, 0, Graphics.TOP | Graphics.LEFT);
    for (int x = 0; x < game.fieldWidth; x++) {
      for (int y = 0; y < game.fieldHeight; y++) {
        drawField(g, x, y);
      }
    }
    final int x1 = game.fieldWidth * cellWidth;
    final int y1 = game.fieldHeight * cellHeight;
    g.setColor(Application.foreground);
    g.drawRect(off_x, off_y, x1, y1);
  }

  private void drawCross(final Graphics g, final int x, final int y) {
    final int x1 = off_x + x * cellWidth;
    final int y1 = off_y + y * cellHeight;
    final int x2 = off_x + x * cellWidth + cellWidth;
    final int y2 = off_y + y * cellHeight + cellHeight;
    final int me = game.field[x][y];
    if (me == BubbletGame.BLACK) {
      g.setColor(Application.foreground);
    }
    else {
      g.setColor(Application.background);
    }
    g.drawLine(x1, y1, x2, y2);
    g.drawLine(x2, y1, x1, y2);
  }

  private void drawField(final Graphics g, final int x, final int y) {
    // Determine cell color
    int next;
    final int me = game.field[x][y];
    if (me >= 0) {
      g.setColor(BubbletScreen.COLORS[me]);
    }
    // Draw cell
    final int x1 = off_x + x * cellWidth;
    final int y1 = off_y + y * cellHeight;
    g.fillRect(x1, y1, cellWidth, cellHeight);
    g.setColor(Application.background);
    // g.drawRect(x1, y1, cellWidth, cellHeight);
    if (x > 0) {
      next = game.field[x - 1][y];
    }
    else {
      next = me + 1;
    }
    if (next != me) {
      g.drawLine(x1, y1, x1, y1 + cellHeight);
    }
    if (x < (game.fieldWidth - 1)) {
      next = game.field[x + 1][y];
    }
    else {
      next = me + 1;
    }
    if (next != me) {
      g.drawLine(x1 + cellWidth, y1, x1 + cellWidth, y1 + cellHeight);
    }
    if (y > 0) {
      next = game.field[x][y - 1];
    }
    else {
      next = me + 1;
    }
    if (next != me) {
      g.drawLine(x1, y1, x1 + cellWidth, y1);
    }
    if (y < (game.fieldHeight - 1)) {
      next = game.field[x][y + 1];
    }
    else {
      next = me + 1;
    }
    if (next != me) {
      g.drawLine(x1, y1 + cellHeight, x1 + cellWidth, y1 + cellHeight);
    }
    if ((x > 0) && (y > 0)) {
      next = game.field[x - 1][y - 1];
      if (next != me) {
        g.drawLine(x1, y1, x1, y1);
      }
    }
  }

  public void keyPressed(int pKeyCode) {
    pKeyCode = getGameAction(pKeyCode);
    switch (pKeyCode) {
      case Canvas.UP:
        if (cross_y > 0) {
          cross_y = cross_y - 1;
        }
        break;
      case Canvas.DOWN:
        if (cross_y < (game.fieldHeight - 1)) {
          cross_y = cross_y + 1;
        }
        break;
      case Canvas.LEFT:
        if (cross_x > 0) {
          cross_x = cross_x - 1;
        }
        break;
      case Canvas.RIGHT:
        if (cross_x < (game.fieldWidth - 1)) {
          cross_x = cross_x + 1;
        }
        break;
      case Canvas.FIRE: // fire
        final int points = game.processFire(cross_x, cross_y);
        score.addScore(points);
        if (game.isGameFinished()) {
          midlet.doGameStop();
        }
        break;
      default:
        midlet.doGamePause();
        break;
    }
  }

}