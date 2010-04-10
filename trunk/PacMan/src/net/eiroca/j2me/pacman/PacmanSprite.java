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
import net.eiroca.j2me.game.GameApp;

/**
 * The Class PacmanSprite.
 */
public class PacmanSprite extends Sprite {

  /** The Constant WIDTH. */
  private static final int WIDTH = 8;
  
  /** The Constant HEIGHT. */
  private static final int HEIGHT = 8;
  
  /** The canvas. */
  private final PacManScreen canvas;
  
  /** The animations. */
  private final int[][] animations = {
      {
        0
      }, {
          1, 2, 3, 4, 3, 2
      }, {
          4, 3, 2, 1, 5, 6, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8
      }
  };
  
  /** The animation tick. */
  private int animationTick = 0;
  
  /** The Constant STAND. */
  private static final int STAND = 0;
  
  /** The Constant RUN. */
  private static final int RUN = 1;
  
  /** The Constant DIE. */
  private static final int DIE = 2;
  
  /** The current direction. */
  private int currentDirection = PacManScreen.LEFT;
  
  /** The eaten. */
  private int eaten;
  
  /** The dead. */
  public boolean dead;
  
  /** The map x. */
  private int mapX = 0;
  
  /** The map y. */
  private int mapY = 0;

  /**
   * Instantiates a new pacman sprite.
   * 
   * @param canvas the canvas
   */
  public PacmanSprite(final PacManScreen canvas) {
    super(BaseApp.createImage(PacMan.RES_PACMAN), PacmanSprite.WIDTH, PacmanSprite.HEIGHT);
    defineCollisionRectangle(2, 2, PacmanSprite.WIDTH - 4, PacmanSprite.WIDTH - 4);
    defineReferencePixel(PacmanSprite.WIDTH / 2, PacmanSprite.HEIGHT / 2);
    this.canvas = canvas;
    init();
  }

  /**
   * Inits the.
   */
  public void init() {
    eaten = 1;
    dead = false;
    animationTick = 0;
  }

  /**
   * Tick.
   * 
   * @param direction the direction
   */
  public void tick(final int direction) {
    animationTick++;
    if (dead) {
      int[] sequence;
      sequence = animations[PacmanSprite.DIE];
      if ((animationTick >> 2) < sequence.length) {
        setFrame(sequence[(animationTick >> 2) % sequence.length]);
      }
      else if ((animationTick >> 2) == sequence.length) {
        dead = false;
        setRefPixelPosition(11 * 10 - 5, 12 * 10 - 5);
      }
    }
    else {
      mapX = getRefPixelX() / 10;
      mapY = getRefPixelY() / 10;
      final GameField field = canvas.getField();
      boolean moving = false;
      switch (direction) {
        case PacManScreen.UP:
          if ((getY() > 10) && !field.containsImpassableArea(getRefPixelX(), getRefPixelY() - 10, getWidth() + 2, getHeight() + 2)) {
            currentDirection = direction;
          }
          break;
        case PacManScreen.LEFT:
          if ((getX() > 10) && !field.containsImpassableArea(getRefPixelX() - 10, getRefPixelY(), getWidth() + 2, getHeight() + 2)) {
            currentDirection = direction;
          }
          break;
        case PacManScreen.DOWN:
          if ((getY() + 10 < field.getHeight()) && !field.containsImpassableArea(getRefPixelX(), getRefPixelY() + 10, getWidth() + 2, getHeight() + 2)) {
            currentDirection = direction;
          }
          break;
        case PacManScreen.RIGHT:
          if ((getX() + 10 < field.getWidth()) && !field.containsImpassableArea(getRefPixelX() + 10, getRefPixelY(), getWidth() + 2, getHeight() + 2)) {
            currentDirection = direction;
          }
          break;
        default:
          break;
      }
      switch (currentDirection) {
        case PacManScreen.UP:
          if ((getY() > 0) && !field.containsImpassableArea(getRefPixelX(), getRefPixelY() - 2, getWidth() + 2, getHeight() + 2)) {
            move(0, -2);
            moving = true;
          }
          break;
        case PacManScreen.LEFT:
          if ((getX() > 0) && !field.containsImpassableArea(getRefPixelX() - 2, getRefPixelY(), getHeight() + 2, getHeight() + 2)) {
            move(-2, 0);
            moving = true;
          }
          break;
        case PacManScreen.DOWN:
          if ((getY() + getHeight() < field.getHeight()) && !field.containsImpassableArea(getRefPixelX(), getRefPixelY() + 2, getWidth() + 2, getHeight() + 2)) {
            move(0, 2);
            moving = true;
          }
          break;
        case PacManScreen.RIGHT:
          if ((getX() + getWidth() < field.getWidth()) && !field.containsImpassableArea(getRefPixelX() + 2, getRefPixelY(), getHeight() + 2, getHeight() + 2)) {
            move(2, 0);
            moving = true;
          }
          break;
        default:
          break;
      }
      if (getRefPixelX() <= 7) {
        setRefPixelPosition(field.getWidth() - 9, getRefPixelY());
      }
      if (getRefPixelX() >= field.getWidth() - 7) {
        setRefPixelPosition(9, getRefPixelY());
      }
      if (moving) {
        advanceRunningAnimation();
      }
      else {
        setStandingAnimation();
      }
      if (field.eatPill(mapX, mapY)) {
        canvas.score.addScore(1);
      }
      if (field.eatMagicPill(mapX, mapY)) {
        canvas.score.addScore(5);
        canvas.setMagicMode();
      }
      if (canvas.overlapsGhost(this)) {
        if (canvas.getMagicMode()) {
          canvas.score.addScore(eaten++ * 10);
        }
        else {
          canvas.score.killed();
          GameApp.vibrate(500);
          dead = true;
          animationTick = 0;
        }
      }
    }
  }

  /**
   * Advance running animation.
   */
  private void advanceRunningAnimation() {
    int[] sequence;
    sequence = animations[PacmanSprite.RUN];
    if (currentDirection == PacManScreen.UP) {
      setTransform(Sprite.TRANS_ROT90);
    }
    else if (currentDirection == PacManScreen.RIGHT) {
      setTransform(Sprite.TRANS_ROT180);
    }
    else if (currentDirection == PacManScreen.DOWN) {
      setTransform(Sprite.TRANS_ROT270);
    }
    else {
      setTransform(Sprite.TRANS_NONE);
    }
    setFrame(sequence[(animationTick >> 1) % sequence.length]);
  }

  /**
   * Sets the standing animation.
   */
  private void setStandingAnimation() {
    setFrame(animations[PacmanSprite.STAND][0]);
    if (currentDirection == PacManScreen.UP) {
      setTransform(Sprite.TRANS_ROT90);
    }
    else if (currentDirection == PacManScreen.RIGHT) {
      setTransform(Sprite.TRANS_ROT180);
    }
    else if (currentDirection == PacManScreen.DOWN) {
      setTransform(Sprite.TRANS_ROT270);
    }
    else {
      setTransform(Sprite.TRANS_NONE);
    }
  }

  /**
   * Gets the dead.
   * 
   * @return the dead
   */
  public boolean getDead() {
    return dead;
  }

}
