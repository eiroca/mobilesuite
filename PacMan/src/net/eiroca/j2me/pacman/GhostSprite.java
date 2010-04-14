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
import javax.microedition.lcdui.game.Sprite;
import net.eiroca.j2me.app.BaseApp;

/**
 * The Class GhostSprite.
 */
public class GhostSprite extends Sprite {

  /** The Constant WIDTH. */
  private static final int WIDTH = 8;

  /** The Constant HEIGHT. */
  private static final int HEIGHT = 8;

  /** The canvas. */
  private final PacManScreen canvas;

  /** The animations. */
  private final int[][][] animations = {
      {
          {
            0
          }, {
            1
          }
      }, // orange
      {
          {
            2
          }, {
            3
          }
      }, // red
      {
          {
            4
          }, {
            5
          }
      }, // violet
      {
          {
            6
          }, {
            7
          }
      }, // green
      {
          {
            8
          }, {
            9
          }
      }, // blue
      {
          {
            10
          }, {
            11
          }
      }
  }; // eye

  /** The animation tick. */
  private int animationTick;

  /** The animation. */
  private int animation;

  /** The current direction. */
  private int currentDirection = PacManScreen.LEFT;

  /** The old direction. */
  private int oldDirection = PacManScreen.LEFT;

  /** The tracked. */
  private boolean tracked = false;

  /** The ghost. */
  private int ghost = 0;

  /** The pm_x. */
  private int pm_x = -1;

  /** The pm_y. */
  private int pm_y = -1;

  /** The map x. */
  private int mapX = 0;

  /** The map y. */
  private int mapY = 0;

  /** The ways. */
  private final int ways[] = {
      0, 0, 0, 0
  };

  /** The num of way. */
  private int numOfWay = 0;

  /** The eyeonly. */
  public boolean eyeonly = false;

  /**
   * Instantiates a new ghost sprite.
   * 
   * @param canvas the canvas
   * @param num the num
   */
  public GhostSprite(final PacManScreen canvas, final int num) {
    super(BaseApp.createImage(PacMan.RES_GHOST), GhostSprite.WIDTH, GhostSprite.HEIGHT);
    defineCollisionRectangle(2, 2, GhostSprite.WIDTH - 4, GhostSprite.WIDTH - 4);
    defineReferencePixel(GhostSprite.WIDTH / 2, GhostSprite.HEIGHT / 2);
    this.canvas = canvas;
    ghost = num;
    animation = num;
    setFrame(animations[num][1][0]);
  }

  /**
   * Tick.
   */
  public void tick() {
    // ghosts are 2x as slow in magic-mode
    if (canvas.getMagicMode() && ((animationTick++ % 2) != 0)) { return; }
    if ((eyeonly == true) && (animationTick > 50) && !canvas.getMagicMode()) {
      setRefPixelPosition(11 * 10 - 4, 8 * 10 - 4);
      eyeonly = false;
    }
    final GameField field = canvas.getField();
    mapX = getRefPixelX() / 10;
    mapY = getRefPixelY() / 10;
    if ((getX() % 10 == 2) && (getY() % 10 == 2)) {
      numOfWay = 0;
      if (field.canGhostWalk(mapX, mapY - 1)) {
        ways[PacManScreen.UP] = 1;
        numOfWay++;
      }
      else {
        ways[PacManScreen.UP] = 0;
      }
      if (field.canGhostWalk(mapX - 1, mapY)) {
        ways[PacManScreen.LEFT] = 1;
        numOfWay++;
      }
      else {
        ways[PacManScreen.LEFT] = 0;
      }
      if (field.canGhostWalk(mapX, mapY + 1)) {
        ways[PacManScreen.DOWN] = 1;
        numOfWay++;
      }
      else {
        ways[PacManScreen.DOWN] = 0;
      }
      if (field.canGhostWalk(mapX + 1, mapY)) {
        ways[PacManScreen.RIGHT] = 1;
        numOfWay++;
      }
      else {
        ways[PacManScreen.RIGHT] = 0;
      }
      if (field.seePacman(mapX, mapY, canvas.getPacmanX(), canvas.getPacmanY()) && !canvas.getPacmanDead()) {
        tracked = true;
        pm_x = canvas.getPacmanX();
        pm_y = canvas.getPacmanY();
      }
      if (numOfWay > 2) {
        oldDirection = currentDirection;
        do {
          currentDirection = BaseApp.rand(4);
        }
        while ((ways[currentDirection] == 0) && ((currentDirection + 2) % 4 != oldDirection));
      }
      if (tracked) {
        tracked = false;
        final int dx = pm_x - getX();
        final int dy = pm_y - getY();
        if (Math.abs(dy) > Math.abs(dx)) {
          if (dy > 0) {
            currentDirection = PacManScreen.DOWN;
          }
          else {
            currentDirection = PacManScreen.UP;
          }
        }
        else {
          if (dx > 0) {
            currentDirection = PacManScreen.RIGHT;
          }
          else {
            currentDirection = PacManScreen.LEFT;
          }
        }
        if (canvas.getMagicMode() || eyeonly) {
          currentDirection = (currentDirection + 2) % 4;
        }
      }
      if (ways[currentDirection] == 0) {
        do {
          currentDirection = BaseApp.rand(4);
        }
        while (ways[currentDirection] == 0);
      }
    }
    switch (currentDirection) {
      case PacManScreen.UP:
        move(0, -2);
        break;
      case PacManScreen.LEFT:
        move(-2, 0);
        break;
      case PacManScreen.DOWN:
        move(0, 2);
        break;
      case PacManScreen.RIGHT:
        move(2, 0);
        break;
      default:
        break;
    }
    if (getRefPixelX() <= 5) {
      setRefPixelPosition(field.getWidth() - 6, getRefPixelY());
    }
    if (getRefPixelX() >= field.getWidth() - 5) {
      setRefPixelPosition(6, getRefPixelY());
    }
    if (canvas.overlapsPacman(this)) {
      if (canvas.getMagicMode()) {
        eyeonly = true;
        animationTick = 0;
      }
    }
    setStandingAnimation();
  }

  /**
   * Sets the standing animation.
   */
  private void setStandingAnimation() {
    animation = ghost;
    if (canvas.getMagicMode()) {
      animation = 4;
    }
    if (eyeonly) {
      animation = 5;
    }
    if (currentDirection == PacManScreen.UP) {
      setFrame(animations[animation][1][0]);
      setTransform(Sprite.TRANS_NONE);
    }
    else {
      setFrame(animations[animation][0][0]);
      setTransform(Sprite.TRANS_NONE);
    }
  }

}
