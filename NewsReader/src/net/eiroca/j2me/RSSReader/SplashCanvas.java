/** GPL >= 2.0
 * Based upon RSS Reader MIDlet
 * Copyright (C) 2004 Gösta Jonasson <gosta(at)brothas.net>
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
package net.eiroca.j2me.RSSReader;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/*
 * Simple splash screen that scrolls text from the top and bottom to the center
 * and stops.
 */
public class SplashCanvas extends Canvas implements Runnable {

  private final String SPLASH_TEXT1 = "News";
  private final String SPLASH_TEXT2 = "Reader";
  private final String SPLASH_SPACE = " ";
  private final int UPDATE_TIME = 50;
  private final int STEPS = 16;

  private int y = 0;
  private int i = 0;
  private final int w;
  private final int h;
  private final int fonth;
  private final int text1x;
  private final int text2x;
  private final int stopy;
  private final int yinc;
  private final Font font;
  private boolean isdone;

  /**
   * Constructor for the class. Makes some initial calculations for the
   * animation.
   */
  public SplashCanvas() {
    isdone = false;
    w = getWidth();
    h = getHeight();
    font = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
    final int totsize = font.stringWidth(SPLASH_TEXT1 + SPLASH_SPACE + SPLASH_TEXT2);
    text1x = (w - totsize) / 2;
    text2x = text1x + font.stringWidth(SPLASH_TEXT1 + SPLASH_SPACE);
    fonth = font.getHeight();
    stopy = h / 2 - fonth / 2;
    yinc = (h / 2) / STEPS;
  }

  /**
   * Catches key presses and then sets isDone to TRUE.
   * @param keyCode code for the key pressed
   */
  protected void keyPressed(final int keyCode) {
    isdone = true;
  }

  /**
   * Paint method. Draws the string and the text on the given Graphics object.
   * @param g The Graphics object to paint on
   */
  public void paint(final Graphics g) {
    /* White background */
    g.setColor(0xFFFFFF);
    g.fillRect(0, 0, w, h);
    g.setColor(0x000000);
    g.setFont(font);
    /* Print the text */
    g.drawString(SPLASH_TEXT1, text1x, y, Graphics.TOP | Graphics.LEFT);
    g.drawString(SPLASH_TEXT2, text2x, h - y - fonth, Graphics.TOP | Graphics.LEFT);
    i++;
    if (i < STEPS) {
      y += yinc;
    }
    else if (i == STEPS) {
      // To make the last "image" perfect
      y = stopy;
    }
    else {
      isdone = true;
    }
  }

  /**
   * The running method that calls repaint() every UPDATE_TIME milliseconds.
   */
  public void run() {
    while (!isdone) {
      try {
        Thread.sleep(UPDATE_TIME);
      }
      catch (final Exception e) {
        //
      }
      repaint();
    }
  }

  /**
   * Returns weather the splash screen is finished or not.
   * @return TRUE if the splash screen is finished
   */
  public boolean isDone() {
    return isdone;
  }

}
