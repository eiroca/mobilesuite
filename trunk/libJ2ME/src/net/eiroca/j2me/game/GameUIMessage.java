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

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import net.eiroca.j2me.app.BaseApp;

public class GameUIMessage extends Canvas {

  private final String[] msg;
  Displayable next;

  public GameUIMessage(final String[] msg, final Displayable next) {
    super();
    this.msg = msg;
    setFullScreenMode(true);
    this.next = next;
  }

  public void paint(final Graphics g) {
    final int width = getWidth();
    final int height = getHeight();
    g.setColor(BaseApp.background);
    g.fillRect(0, 0, width, height);
    g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
    final int centerX = width / 2;
    final int centerY = height / 2;
    g.setColor(BaseApp.foreground);
    drawText(g, centerX, centerY - 1);
    drawText(g, centerX, centerY + 1);
    drawText(g, centerX - 1, centerY);
    drawText(g, centerX + 1, centerY);
    g.setColor(BaseApp.background);
    drawText(g, centerX, centerY);
  }

  private void drawText(final Graphics g, final int centerX, final int centerY) {
    final int l = msg.length;
    final int fontHeight = g.getFont().getHeight();
    final int textHeight = l * fontHeight;
    int topY = centerY - textHeight / 2;
    for (int i = 0; i < l; i++) {
      if (msg[i] != null) {
        g.drawString(msg[i], centerX, topY, Graphics.HCENTER | Graphics.TOP);
      }
      topY = topY + fontHeight;
    }
  }

  public void keyPressed(final int keyCode) {
    BaseApp.back(null, next, true);
  }

}
