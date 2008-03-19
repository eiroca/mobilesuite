/**
 * (C) 2006 eIrOcA
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 */
import javax.microedition.lcdui.game.GameCanvas;

import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.game.GameApp;
import net.eiroca.j2me.game.GameScreen;

public final class EGameScreen extends GameScreen {

  public EGameScreen(final GameApp midlet, final boolean suppressKeys, final boolean fullScreen) {
    super(midlet, suppressKeys, fullScreen);
    name = "EGame";
  }

  public void init() {
    super.init();
    score.beginGame(1, 0, 0);
  }

  public boolean tick() {
    score.addScore(1);
    final int keyStates = getKeyStates();
    if (keyStates == GameCanvas.FIRE_PRESSED) {
      midlet.doGamePause();
    }
    else if (keyStates != 0) {
      midlet.doGameStop();
    }
    draw();
    return true;
  }

  public void draw() {
    // clear screen to black
    screen.setColor(BaseApp.background);
    screen.fillRect(0, 0, screenWidth, screenHeight);
    screen.setColor(BaseApp.foreground);
    screen.fillArc(0, 0, 50, 50, 45, 270);
    screen.drawString("Score: " + score.getScore(), 0, 75, 0);
  }

}
