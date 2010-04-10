/** GPL >= 3.0 + MIT
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
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
 * 
 * Copyright (C) 2004 Andrew Scott
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package keys;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * A screen GUI that prints a representation of the state of the standard phone
 * keys. The representation covers which ones are being depressed, held, and
 * released.
 */
public class KeyStateCanvas extends Canvas {

  /** The color used for the background: white. */
  private final static int backgroundColour = 0x00FFFFFF;
  /** The color used for the list box: black. */
  private final static int borderColour = 0x00000000;
  /** The color used for the text in the boxes: blue. */
  private final static int textColour = 0x001010C0;
  /** The font used for the text. */
  private final Font fonText;
  /** The list of the detected key codes and states. */
  private final KeyStateList list;
  /** The height of the box that fits the text font. */
  private final int fontHeight;
  /** The maximum number of entries in the list. */
  private final int listSize;

  /**
   * Constructs an instance of the KeyStateCanvas class.
   */
  public KeyStateCanvas() {
    super();
    fonText = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    list = new KeyStateList(this, fonText);
    listSize = list.getSize();
    fontHeight = fonText.getHeight() + 1;
  }

  /**
   * Returns a string that describes the complete aspects of the given key code,
   * both game and normal, or a key code if neither. Should normally return a
   * string no more than 9 characters long.
   * @param keyCode the code from the keyXXXed() functions
   * @return a string description
   */
  private String getFullKeyName(final int keyCode) {
    StringBuffer name;
    String gameName = null;
    boolean recognised = true;
    int gameCode;
    char keyChar = ' ';
    name = new StringBuffer(16);
    try {
      gameCode = getGameAction(keyCode);
    }
    catch (final IllegalArgumentException iae) {
      gameCode = 0;
    }
    // Find out if the key code represents a standard key
    switch (keyCode) {
      case (Canvas.KEY_NUM0): {
        keyChar = '0';
        break;
      }
      case (Canvas.KEY_NUM1): {
        keyChar = '1';
        break;
      }
      case (Canvas.KEY_NUM2): {
        keyChar = '2';
        break;
      }
      case (Canvas.KEY_NUM3): {
        keyChar = '3';
        break;
      }
      case (Canvas.KEY_NUM4): {
        keyChar = '4';
        break;
      }
      case (Canvas.KEY_NUM5): {
        keyChar = '5';
        break;
      }
      case (Canvas.KEY_NUM6): {
        keyChar = '6';
        break;
      }
      case (Canvas.KEY_NUM7): {
        keyChar = '7';
        break;
      }
      case (Canvas.KEY_NUM8): {
        keyChar = '8';
        break;
      }
      case (Canvas.KEY_NUM9): {
        keyChar = '9';
        break;
      }
      case (Canvas.KEY_POUND): {
        keyChar = '#';
        break;
      }
      case (Canvas.KEY_STAR): {
        keyChar = '*';
        break;
      }
      default: {
        recognised = false;
        break;
      }
    }
    // Add the game name, if any, to the string
    switch (gameCode) {
      case UP: {
        gameName = "UP";
        break;
      }
      case DOWN: {
        gameName = "DOWN";
        break;
      }
      case LEFT: {
        gameName = "LEFT";
        break;
      }
      case RIGHT: {
        gameName = "RIGHT";
        break;
      }
      case FIRE: {
        gameName = "FIRE";
        break;
      }
      case GAME_A: {
        gameName = "GAME-A";
        break;
      }
      case GAME_B: {
        gameName = "GAME-B";
        break;
      }
      case GAME_C: {
        gameName = "GAME-C";
        break;
      }
      case GAME_D: {
        gameName = "GAME-D";
        break;
      }
      default: {
        break;
      }
    }
    if (recognised) {
      if (gameName != null) {
        name.append('(');
        name.append(gameName);
        name.append(')');
        name.append(' ');
      }
      name.append(keyChar);
    }
    else {
      name.append("Key ");
      name.append(keyCode);
    }
    return name.toString();
  }

  /**
   * Returns a string that describes the states held in the given key state.
   * Should normally return a string no more than 8 characters long.
   * @param keyState the combined state of the key, from KeyStateList
   * @return a string description
   */
  private String getStateName(final KeyState keyState) {
    StringBuffer name;
    name = new StringBuffer(9);
    if (keyState.isPressed()) {
      name.append("Dn ");
    }
    if (keyState.isRepeated()) {
      name.append(".. ");
    }
    if (keyState.isReleased()) {
      name.append("Up ");
    }
    return name.toString();
  }

  /**
   * Draws on the given Graphics object, a box with the given entry in it, at
   * the given position.
   * @param g the Graphics object
   * @param y the y-coordinate for the top of the box
   * @param keyCode the code of the entry
   * @param keyState the state of the entry
   */
  private void drawEntry(final Graphics g, final int y, final int keyCode, final KeyState keyState) {
    int width, width2;
    width = getWidth();
    width2 = width / 2;
    g.setColor(KeyStateCanvas.borderColour);
    g.drawRect(0, y, width - 1, fontHeight);
    g.setColor(KeyStateCanvas.textColour);
    g.drawString(getFullKeyName(keyCode), width2 - 1, y + 1, Graphics.TOP | Graphics.RIGHT);
    g.drawString(getStateName(keyState), width2 + 1, y + 1, Graphics.TOP | Graphics.LEFT);
  }

  /**
   * Draws the list on this canvas.
   * @param g the Graphics object for this Canvas
   */
  public void paint(final Graphics g) {
    int i, num, width, height, entryHeight;
    synchronized (list) {
      width = getWidth();
      height = getHeight();
      entryHeight = height / listSize;
      height--; // ensure bottom of entry boxes fits on the screen
      g.setColor(KeyStateCanvas.backgroundColour);
      g.fillRect(0, 0, width, height);
      g.setFont(fonText);
      // Draw each element of the list starting at the bottom of the screen
      num = list.getNumEntries();
      for (i = 0; i < num; i++) {
        height = height - entryHeight;
        drawEntry(g, height, list.getCode(i), list.getState(i));
      }
    }
  }

  /**
   * Intercept a pressed key.
   * @param keyCode the code of the key in question
   */
  protected void keyPressed(final int keyCode) {
    synchronized (list) {
      list.addEntry(keyCode, KeyState.PRESSED);
    }
    repaint();
  }

  /**
   * Intercept a held key.
   * @param keyCode the code of the key in question
   */
  protected void keyRepeated(final int keyCode) {
    synchronized (list) {
      list.addEntry(keyCode, KeyState.REPEATED);
    }
    repaint();
  }

  /**
   * Intercept a released key.
   * @param keyCode the code of the key in question
   */
  protected void keyReleased(final int keyCode) {
    synchronized (list) {
      list.addEntry(keyCode, KeyState.RELEASED);
    }
    repaint();
  }

}
