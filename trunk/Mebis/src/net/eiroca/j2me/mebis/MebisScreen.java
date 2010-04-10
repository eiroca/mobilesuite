/** GPL >= 3.0
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 * Copyright (C) 2005-2006 Michael "ScriptKiller" Arndt <scriptkiller@gmx.de> http://scriptkiller.de/
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

package net.eiroca.j2me.mebis;

import Mebis;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.game.GameApp;
import net.eiroca.j2me.game.GameScreen;

/**
 * The Class MebisScreen.
 */
public final class MebisScreen extends GameScreen {

  /** The Constant ROTATE_LEFT. */
  public static final int ROTATE_LEFT = 1;

  /** The Constant ROTATE_RIGHT. */
  public static final int ROTATE_RIGHT = 2;

  /** The Constant LEFT. */
  public static final int LEFT = 3;

  /** The Constant RIGHT. */
  public static final int RIGHT = 4;

  /** The Constant STEP. */
  public static final int STEP = 5;

  /** The Constant DROP. */
  public static final int DROP = 6;

  /** The Constant COLS. */
  public static final int COLS = 12;

  /** The Constant ROWS. */
  public static final int ROWS = 18;

  /** The brick. */
  private Brick brick;

  /** The rows. */
  private Row rows[];

  /** The show lost. */
  private boolean showLost = false;

  /** The show won. */
  private boolean showWon = false;

  /** The screen font. */
  private final Font screenFont;

  /** The font height. */
  private int fontHeight;

  /** The last step. */
  long lastStep;

  /** The step_time. */
  int step_time;

  /** The block width. */
  private int blockWidth;

  /** The block height. */
  private int blockHeight;

  /** The game area width. */
  private int gameAreaWidth;

  /** The game area height. */
  private int gameAreaHeight;

  /** The game area off x. */
  private final int gameAreaOffX;

  /** The game area off y. */
  private final int gameAreaOffY;

  /** The score width. */
  private final int scoreWidth;

  /** The score height. */
  private final int scoreHeight;

  /** The score off x. */
  private final int scoreOffX;

  /** The score off y. */
  private int scoreOffY = 0;

  /** The font anchor x. */
  private final int fontAnchorX;

  /** The font anchor y. */
  private final int fontAnchorY;

  /**
   * Instantiates a new mebis screen.
   * 
   * @param midlet the midlet
   */
  public MebisScreen(final GameApp midlet) {
    super(midlet, false, true);
    name = Application.messages[Mebis.MSG_NAME];
    screenFont = screen.getFont();
    fontHeight = screenFont.getBaselinePosition();
    if (fontHeight <= 2) {
      // returns wrong values on 6230i
      fontHeight = screenFont.getHeight();
    }
    /* game-area */
    gameAreaWidth = (screenWidth * 2) / 3;
    gameAreaHeight = screenHeight;
    /* try to get the maximum out of available space first try maximum width */
    blockWidth = gameAreaWidth / MebisScreen.COLS;
    blockHeight = blockWidth;
    if (blockHeight * MebisScreen.ROWS > gameAreaHeight) {
      /* bigger than maximum height? => try maximum height */
      blockHeight = gameAreaHeight / MebisScreen.ROWS;
      blockWidth = blockHeight;
    }
    gameAreaWidth = blockWidth * MebisScreen.COLS;
    gameAreaHeight = blockHeight * MebisScreen.ROWS;
    gameAreaOffX = 0;
    gameAreaOffY = (screenHeight - gameAreaHeight) / 2;
    scoreWidth = screenWidth - gameAreaWidth;
    scoreHeight = gameAreaHeight;
    scoreOffX = gameAreaWidth + 1;
    scoreOffY = gameAreaOffY;
    fontAnchorX = gameAreaOffX + blockWidth * MebisScreen.COLS / 2;
    fontAnchorY = gameAreaOffY + blockHeight * MebisScreen.ROWS / 2;
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.GameScreen#init()
   */
  public void init() {
    super.init();
    score.beginGame(1, 0, 0);
    lastStep = 0;
    step_time = 500;
    newBrick();
    rows = new Row[MebisScreen.ROWS];
    for (int i = 0; i < rows.length; i++) {
      rows[i] = new Row(i, MebisScreen.COLS);
    }
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.GameScreen#tick()
   */
  public boolean tick() {
    final long now = System.currentTimeMillis();
    if ((now - lastStep) > step_time) {
      lastStep = now;
      brickTransition(MebisScreen.STEP);
    }
    draw();
    return true;
  }

  /**
   * Draw.
   */
  public void draw() {
    /* background */
    screen.setColor(Application.background);
    screen.fillRect(0, 0, screenWidth, screenHeight);
    /* game-area */
    screen.setColor(Application.background);
    screen.fillRect(gameAreaOffX, gameAreaOffY, gameAreaWidth, gameAreaHeight);
    /* draw small lines */
    screen.setColor(0x999999);
    /* cols */
    for (int i = 0; i <= MebisScreen.COLS; i++) {
      screen.drawLine(gameAreaOffX + i * blockWidth, gameAreaOffY, gameAreaOffX + i * blockWidth, gameAreaOffY + gameAreaHeight);
    }
    /* rows */
    for (int i = 0; i <= MebisScreen.ROWS; i++) {
      screen.drawLine(gameAreaOffX, gameAreaOffY + i * blockHeight, gameAreaOffX + gameAreaWidth, gameAreaOffY + i * blockHeight);
    }
    if (!showLost && !showWon) {
      /* paint rows */
      for (int y = 0; y < rows.length; y++) {
        for (int x = 0; x < rows[y].blocks.length; x++) {
          if (rows[y].blocks[x] == null) {
            continue;
          }
          final int color = rows[y].blocks[x].color;
          screen.setColor(color);
          screen.fillRect(gameAreaOffX + x * blockWidth, gameAreaOffY + y * blockHeight, blockWidth, blockHeight);
        }
      }
      /* paint brick */
      for (int i = 0; i < brick.blocks.length; i++) {
        final int color = brick.blocks[i].color;
        final int x = brick.blocks[i].x;
        final int y = brick.blocks[i].y;
        screen.setColor(color);
        screen.fillRect(gameAreaOffX + x * blockWidth, gameAreaOffY + y * blockHeight, blockWidth, blockHeight);
      }
    }
    if (showLost) {
      drawCenteredTextBox(fontAnchorX, fontAnchorY, Application.messages[Mebis.MSG_LOOSE]);
    }
    else if (showWon) {
      drawCenteredTextBox(fontAnchorX, fontAnchorY, Application.messages[Mebis.MSG_WIN]);
    }
    /* score-area */
    screen.setColor(Application.background);
    screen.fillRect(scoreOffX, scoreOffY, scoreWidth, scoreHeight);
    screen.setColor(Application.foreground);
    screen.drawString(Application.messages[Mebis.MSG_SCORE], scoreOffX, scoreOffY, Graphics.TOP | Graphics.LEFT);
    screen.drawString(String.valueOf(score.getScore()), scoreOffX + 10, scoreOffY + fontHeight, Graphics.TOP | Graphics.LEFT);
    screen.drawString(Application.messages[Mebis.MSG_LINES], scoreOffX, scoreOffY + 40, Graphics.TOP | Graphics.LEFT);
    screen.drawString(String.valueOf(score.getLevel()), scoreOffX + 10, scoreOffY + 40 + fontHeight, Graphics.TOP | Graphics.LEFT);
  }

  /* (non-Javadoc)
   * @see javax.microedition.lcdui.Canvas#keyPressed(int)
   */
  public void keyPressed(final int keyCode) {
    switch (getGameAction(keyCode)) {
      case Canvas.UP:
        brickTransition(MebisScreen.ROTATE_LEFT);
        break;
      case Canvas.DOWN:
        brickTransition(MebisScreen.ROTATE_RIGHT);
        break;
      case Canvas.LEFT:
        brickTransition(MebisScreen.LEFT);
        break;
      case Canvas.RIGHT:
        brickTransition(MebisScreen.RIGHT);
        break;
      case Canvas.FIRE:
        brickTransition(MebisScreen.DROP);
        break;
      default:
        switch (keyCode) {
          case Canvas.KEY_NUM1:
            brickTransition(MebisScreen.ROTATE_LEFT);
            break;
          case Canvas.KEY_NUM3:
            brickTransition(MebisScreen.ROTATE_RIGHT);
            break;
          case Canvas.KEY_NUM4:
            brickTransition(MebisScreen.LEFT);
            break;
          case Canvas.KEY_NUM6:
            brickTransition(MebisScreen.RIGHT);
            break;
          case Canvas.KEY_NUM5:
            brickTransition(MebisScreen.STEP);
            break;
          case Canvas.KEY_NUM7:
            brickTransition(MebisScreen.DROP);
            break;
          default:
            midlet.doGamePause();
            break;
        }
        break;
    }
  }

  /**
   * Brick transition.
   * 
   * @param type the type
   */
  public synchronized void brickTransition(final int type) {
    /* make a copy of brick */
    final Brick temp = brick.clone();
    if (type == MebisScreen.ROTATE_LEFT) {
      /* rotate copy */
      temp.rotate(true);
      /* now check for collisions */
      if (!brickCollisionCheck(temp)) {
        /* no collision? -> rotate real brick */
        brick.rotate(true);
      }
    }
    else if (type == MebisScreen.ROTATE_RIGHT) {
      /* rotate copy */
      temp.rotate(false);
      /* now check for collisions */
      if (!brickCollisionCheck(temp)) {
        /* no collision? -> rotate real brick */
        brick.rotate(false);
      }
    }
    else if (type == MebisScreen.LEFT) {
      temp.left();
      if (!brickCollisionCheck(temp)) {
        brick.left();
      }
    }
    else if (type == MebisScreen.RIGHT) {
      temp.right();
      if (!brickCollisionCheck(temp)) {
        brick.right();
      }
    }
    else if (type == MebisScreen.STEP) {
      temp.step();
      if (!brickCollisionCheck(temp)) {
        brick.step();
      }
      else {
        dropBrick();
      }
    }
    else if (type == MebisScreen.DROP) {
      dropBrick();
    }
  }

  /**
   * Drop brick.
   */
  public void dropBrick() {
    while (!brickCollisionCheck(brick)) {
      brick.step();
    }
    // brick is now solid, get a new one
    addBrickToRows(brick);
    rowCompleteCheck();
    newBrick();
    /* game lost? */
    if (brickCollisionCheck(brick)) {
      midlet.doGameStop();
    }
  }

  /**
   * Create new random brick.
   */
  public void newBrick() {
    brick = new Brick(BaseApp.rand(7));
    brick.setPosition((MebisScreen.COLS / 2) - 2, 0);
  }

  /**
   * Add the brick to the rows-Objects so that brick-Object may be destroyed.
   * 
   * @param b the b
   */
  public void addBrickToRows(final Brick b) {
    int x;
    int y;
    for (int i = 0; i < b.blocks.length; i++) {
      x = b.blocks[i].x;
      y = b.blocks[i].y;
      if (y <= 0) { return; }
      rows[y - 1].blocks[x] = b.blocks[i];
    }
  }

  /**
   * Brick collision check.
   * 
   * @param b the b
   * @return true, if successful
   */
  public boolean brickCollisionCheck(final Brick b) {
    int i;
    int y;
    int x;
    for (i = 0; i < b.blocks.length; i++) {
      y = b.blocks[i].y;
      x = b.blocks[i].x;
      /* collision with ground */
      if (y >= MebisScreen.ROWS) { return true; }
      /* collision with left or right border */
      if ((x < 0) || (x >= MebisScreen.COLS)) { return true; }
      /* collision with rows */
      if (rows[y].blocks[x] != null) { return true; }
    }
    return false;
  }

  /**
   * Row complete check.
   */
  public void rowCompleteCheck() {
    /* count number of completed rows in one step */
    int count = 0;
    for (int y = 0; y < rows.length; y++) {
      boolean hasnull = false;
      for (int x = 0; x < rows[y].blocks.length; x++) {
        if (rows[y].blocks[x] == null) {
          hasnull = true;
          break;
        }
      }
      if (!hasnull) {
        /* row completed */
        /* remove this row */
        /* move other rows "down" (_in_crease y!) */
        for (int i = y; i > 0; i--) {
          rows[i] = rows[i - 1];
        }
        /* add new row on top */
        rows[0] = new Row(0, MebisScreen.COLS);
        /* increase counter */
        count++;
      }
    }
    /* add score */
    score.nextLevel(count);
    switch (count) {
      case 4:
        score.addScore(1200);
        break;
      case 3:
        score.addScore(300);
        break;
      case 2:
        score.addScore(100);
        break;
      case 1:
        score.addScore(40);
        break;
    }
  }

  /**
   * Show lost-game screen .
   */
  public void showLost() {
    showLost = true;
  }

  /**
   * Show won-game screen.
   */
  public void showWon() {
    showWon = true;
  }

  /**
   * Add 'count' lines at bottom of game.
   * 
   * @param count the count
   */
  public void addRandomRows(final int count) {
    Row r;
    for (int i = 0; i < count; i++) {
      /* new row at ypos ROWS-1 (bottom) */
      r = new Row(MebisScreen.ROWS - 1, MebisScreen.COLS);
      for (int z = 0; z < r.blocks.length; z++) {
        /* random: block here? */
        int rr = BaseApp.rand(6);
        if (rr != 0) {
          rr = BaseApp.rand(7);
          final int color = Brick.colors[rr];
          r.blocks[z] = new Block(color, z, MebisScreen.ROWS - 1);
        }
      }
      /* move other rows up */
      for (int y = 0; y < MebisScreen.ROWS - 1; y++) {
        rows[y] = rows[y + 1];
      }
      /* replace it */
      rows[MebisScreen.ROWS - 1] = r;
    }
  }

  /**
   * Draw centered text box.
   * 
   * @param fontAnchorX the font anchor x
   * @param fontAnchorY the font anchor y
   * @param s the s
   */
  public void drawCenteredTextBox(final int fontAnchorX, final int fontAnchorY, final String s) {
    /* background */
    screen.setColor(0xFFFFFF);
    screen.fillRect(fontAnchorX - screenFont.stringWidth(s) / 2 - 5, fontAnchorY - 5, screenFont.stringWidth(s) + 10, fontHeight + 10);
    /* draw box outline */
    screen.setColor(0x999999);
    screen.drawRect(fontAnchorX - screenFont.stringWidth(s) / 2 - 5, fontAnchorY - 5, screenFont.stringWidth(s) + 10, fontHeight + 10);
    /* draw string */
    screen.setColor(0x000000);
    screen.drawString(s, fontAnchorX, fontAnchorY, Graphics.TOP | Graphics.HCENTER);
  }

}
