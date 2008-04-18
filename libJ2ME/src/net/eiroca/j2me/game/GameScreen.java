/** GPL >= 2.0
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
package net.eiroca.j2me.game;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

abstract public class GameScreen extends GameCanvas {

  protected final GameApp midlet;
  protected final Graphics screen;
  protected final int screenWidth;
  protected final int screenHeight;
  protected final boolean fullScreenMode;

  protected boolean active = false;
  protected GameThread animationThread;
  public String name;
  public Score score;

  public GameScreen(final GameApp aMidlet, final boolean suppressKeys, final boolean fullScreen) {
    super(suppressKeys);
    midlet = aMidlet;
    fullScreenMode = fullScreen;
    setFullScreenMode(fullScreenMode);
    score = new Score();
    screen = getGraphics();
    screenWidth = screen.getClipWidth();
    screenHeight = screen.getClipHeight();
  }

  public void init() {
    active = true;
  }

  public void show() {
    setFullScreenMode(fullScreenMode);
    animationThread = new GameThread(this);
    animationThread.start();
  }

  abstract public boolean tick();

  public void hide() {
    if (animationThread != null) {
      animationThread.stopped = true;
      animationThread = null;
      setFullScreenMode(false);
    }
  }

  public void done() {
    hide();
    active = false;
    score.endGame();
  }

  public final boolean isActive() {
    return active;
  }

}
