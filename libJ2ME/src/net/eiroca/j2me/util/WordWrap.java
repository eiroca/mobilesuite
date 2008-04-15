/**
 * Copyright (C) 2006-2008 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the
 *      Free Software Foundation, Inc.,
 *      59 Temple Place, Suite 330,
 *      Boston, MA 02111-1307
 *      USA
 *
 * Copyright (C) 2002 Eugene Morozov (xonixboy@hotmail.com)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the
 *      Free Software Foundation, Inc.,
 *      59 Temple Place, Suite 330,
 *      Boston, MA 02111-1307
 *      USA
 *
 * Copyright (c) 2002,2003, Stefan Haustein, Oberhausen, Rhld., Germany
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The  above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 *
 */
package net.eiroca.j2me.util;

import javax.microedition.lcdui.Font;
import net.eiroca.j2me.app.BaseApp;

/**
 * A class supporting word wrap for MIDP.
 */

public class WordWrap {

  Font font;
  int width;
  String txt;
  int pos;

  /**
   * Initializes the WordWrap object with the given Font, the text string to be
   * wrapped, and the target width.
   * @param font: The Font to be used to calculate the character widths.
   * @param txt: The text string to be wrapped.
   * @param width: The line width.
   */
  public WordWrap(final Font font, final String txt, final int width) {
    this.font = font;
    this.txt = txt;
    this.width = width;
  }

  /**
   * returns the next line break position. If no text is left, -1 is returned.
   */
  public int next() {
    int i = pos;
    final int len = txt.length();
    if (pos >= len) { return -1; }
    final int start = pos;
    while (true) {
      while ((i < len) && (txt.charAt(i) > ' ')) {
        i++;
      }
      final int w = font.stringWidth(txt.substring(start, i));
      if (pos == start) {
        if (w > width) {
          while (font.stringWidth(txt.substring(start, --i)) > width) {
            //
          }
          pos = i;
          break;
        }
      }
      if (w <= width) {
        pos = i;
      }
      if ((w > width) || (i >= len) || (txt.charAt(i) == BaseApp.CR)) {
        break;
      }
      i++;
    }
    return pos >= len ? pos : ++pos;
  }

}
