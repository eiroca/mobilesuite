/** GPL >= 3.0
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 * Copyright (C) 2004 Gösta Jonasson
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
package net.eiroca.j2me.RSSReader;

import NewsReader;
import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.BaseApp;

/**
 * The Class ShowNews.
 */
public class ShowNews extends Canvas {

  /** The corr. */
  private int corr;

  /** The border. */
  private final int border = 5;

  /** The border1. */
  private final int border1 = 2;

  /** The w1. */
  private final int w1;

  /** The h1. */
  private final int h1;

  /** The width. */
  private final int width;

  /** The height. */
  private final int height;

  /** The is begin. */
  private boolean isBegin;

  /** The is end. */
  private boolean isEnd;

  /** The vect. */
  private final Vector vect;

  /** The feed. */
  private final RSSFeed feed;

  /**
   * Instantiates a new show news.
   * 
   * @param newMidlet the new midlet
   * @param feed the feed
   * @param rssItem the rss item
   */
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

  /**
   * Row forward.
   */
  private void rowForward() {
    corr = corr + RenderedWord.heightFont;
    if (corr > 0) {
      isBegin = false;
    }
  }

  /**
   * Row backward.
   */
  private void rowBackward() {
    corr = corr - RenderedWord.heightFont;
    if (corr == 0) {
      isBegin = true;
    }
  }

  /* (non-Javadoc)
   * @see javax.microedition.lcdui.Canvas#paint(javax.microedition.lcdui.Graphics)
   */
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

  /* (non-Javadoc)
   * @see javax.microedition.lcdui.Canvas#keyPressed(int)
   */
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
