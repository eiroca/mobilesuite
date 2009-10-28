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

import NewsReader;
import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.BaseApp;

public class ShowNews extends Canvas {

  private int corr;
  private final int border = 5;
  private final int border1 = 2;
  private final int w1;
  private final int h1;
  private final int width;
  private final int height;
  private boolean isBegin;
  private boolean isEnd;
  private final Vector vect;
  private final RSSFeed feed;

  public ShowNews(final NewsReader newMidlet, final RSSFeed feed, final RSSItem rssItem) {
    this.feed = feed;
    isBegin = true;
    isEnd = true;
    corr = 0;
    width = getWidth();
    height = getHeight();
    w1 = width - 2 * border1;
    h1 = height - 2 * border1;
    final String txt = "<tit>" + rssItem.title + "</tit>" + rssItem.description;
    vect = RenderedWord.createWordList(txt, width - 2 * border, height - 2 * border, feed.colText, feed.colTitl);
    Application.setup(this, Application.cBACK, null);
    if (!BaseApp.isEmpty(rssItem.link)) {
      addCommand(NewsReader.cGO);
    }
  }

  private void rowForward() {
    corr = corr + RenderedWord.heightFont;
    if (corr > 0) {
      isBegin = false;
    }
  }

  private void rowBackward() {
    corr = corr - RenderedWord.heightFont;
    if (corr == 0) {
      isBegin = true;
    }
  }

  protected void paint(final Graphics g) {
    isEnd = true;
    g.setColor(feed.colBord);
    g.fillRect(0, 0, width, height);
    g.setColor(feed.colBckG);
    g.fillRect(border1, border1, w1, h1);
    int j = 0;
    for (int i = 0; i < vect.size(); i++) {
      final RenderedWord renderedWord = (RenderedWord) vect.elementAt(i);
      j = renderedWord.row - corr;
      if ((j >= 0) && ((j + RenderedWord.heightFont) < height - border)) {
        g.setColor(renderedWord.color);
        g.setFont(RenderedWord.font[renderedWord.style]);
        g.drawString(renderedWord.word, renderedWord.offset + border, j + border, Graphics.TOP | Graphics.LEFT);
      }
    }
    if ((j + RenderedWord.heightFont) >= height) {
      isEnd = false;
    }
  }

  protected void keyPressed(final int keyCode) {
    switch (getGameAction(keyCode)) {
      case Canvas.UP:
        if (!isBegin) {
          rowBackward();
          repaint();
        }
        break;
      case Canvas.DOWN:
        if (!isEnd) {
          rowForward();
          repaint();
        }
        break;
    }
  }

}
