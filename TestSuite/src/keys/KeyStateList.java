/** MIT LICENSE
 * Based upon Mobile Device Tools written by Andrew Scott
 *
 * Copyright (C) 2004 Andrew Scott
 * Copyright (C) 2006-2008 eIrOcA (eNrIcO Croce & sImOnA Burzio)
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

/**
 * Manages the current list of key states for printing on the screen. It's
 * really more of a queue, as old entries drop off the top.
 */
public class KeyStateList {

  /** The maximum number of entries that can be stored in the list. */
  int listSize;
  /** The current number of entries in the list. */
  int numEntries;
  /** List of the key codes. */
  int keyCodes[];
  /** List of the key states corresponding to the key codes. */
  KeyState keyStates[];

  /**
   * Constructs an instance of the KeyStateList class. Ensures that the list
   * being maintained is large enough for the screen using the given font.
   * @param can the canvas this list will be displayed on
   * @param f the font used to write the list contents
   */
  public KeyStateList(final Canvas can, final Font f) {
    int i;
    listSize = can.getHeight() / (f.getHeight() + 1);
    keyCodes = new int[listSize];
    keyStates = new KeyState[listSize];
    numEntries = 0;
    for (i = 0; i < listSize; i++) {
      keyStates[i] = new KeyState();
    }
  }

  /**
   * Returns the maximum size of the list.
   * @return the list size
   */
  public int getSize() {
    return listSize;
  }

  /**
   * Returns the number of entries in the list.
   * @return the number of entries
   */
  public int getNumEntries() {
    return numEntries;
  }

  /**
   * Adds a new key event entry to the current list. May result in previous
   * entries in the list dropping off. If the new event corresponds to a key
   * code already in the list, then the existing entry will be modified, rather
   * than adding a new entry.
   * @param keyCode the code for the key event, e.g. from getKeyCode()
   * @param keyState a (possible) combination of PRESSED, RELEASED, etc.
   */
  public void addEntry(final int keyCode, final int keyState) {
    int i;

    // This method could be made more efficient by having a counter
    // mark the start of the queue and incrementing it, rather than
    // copying all the elements up one.
    for (i = 0; i < numEntries; i++) {
      if (keyCodes[i] == keyCode) {
        // If a key code is in the list, update the existing state
        keyStates[i].set(keyState);
        return;
      }
    }
    // If the list is full, we'll have to make space
    if (listSize == numEntries) {
      numEntries--;
    }
    // Shift the list entries along one
    for (i = numEntries + 1; 1 < i--;) {
      keyCodes[i] = keyCodes[i - 1];
      keyStates[i].reset(keyStates[i - 1]);
    }
    // Insert the new entry in at the top
    keyCodes[0] = keyCode;
    keyStates[0].reset(keyState);
    numEntries++;
  }

  /**
   * Returns the key code for the entry at the given index in the list.
   * @return key code value
   */
  public int getCode(final int idx) {
    if ((0 > idx) || (numEntries <= idx)) {
      // debugging message only
      System.err.println("getCode(" + idx + ") invalid index");
      return 0;
    }
    return keyCodes[idx];
  }

  /**
   * Returns the key state for the entry at the given index in the list.
   * @return key state object
   */
  public KeyState getState(final int idx) {
    if ((0 > idx) || (numEntries <= idx)) {
      // debugging message only
      System.err.println("getState(" + idx + ") invalid index");
      return null;
    }
    return keyStates[idx];
  }

}
