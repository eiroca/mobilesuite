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

public class Score {

  public String name;
  protected int score = 0;
  protected int level = 0;
  private int lives = 0;

  public Score() {
    name = "-";
  }

  public Score(final String name, final int level, final int score) {
    this.name = name;
    this.level = level;
    this.score = score;
  }

  public void beginGame(final int aLives, final int aLevel, final int aScore) {
    score = aScore;
    lives = aLives;
    level = aLevel;
  }

  public void endGame() {
    //
  }

  public void nextLevel() {
    level++;
  }

  public void nextLevel(final int step) {
    level = level + step;
  }

  public void addScore(final int val) {
    score += val;
  }

  public int getLevel() {
    return level;
  }

  public boolean killed() {
    lives--;
    return (lives > 0);
  }

  public int getLives() {
    return lives;
  }

  public int getScore() {
    return Math.abs(score);
  }

}
