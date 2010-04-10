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

import java.util.Vector;
import javax.microedition.lcdui.Font;
import net.eiroca.j2me.app.BaseApp;

/**
 * The Class RenderedWord.
 */
public class RenderedWord {

  /** The height font. */
  public static int heightFont;

  /** The font. */
  public static Font[] font;

  /** The font width. */
  public static int[] fontWidth;

  /** The offset. */
  public int offset;

  /** The row. */
  public int row;

  /** The style. */
  public int style;

  /** The color. */
  public int color;

  /** The word. */
  public String word;

  static {
    RenderedWord.font = new Font[7];
    RenderedWord.fontWidth = new int[7];
    Font f;
    for (int i = 0; i < 7; i++) {
      f = Font.getFont(Font.FACE_PROPORTIONAL, i, Font.SIZE_MEDIUM);
      RenderedWord.font[i] = f;
      RenderedWord.fontWidth[i] = f.stringWidth(" ");
    }
    RenderedWord.heightFont = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM).getHeight() + 3;
  }

  /**
   * Instantiates a new rendered word.
   * 
   * @param offset the offset
   * @param row the row
   * @param style the style
   * @param color the color
   * @param word the word
   */
  public RenderedWord(final int offset, final int row, final int style, final int color, final String word) {
    this.offset = offset;
    this.style = style;
    this.word = word;
    this.color = color;
    this.row = row;
  }

  /**
   * Creates the word list.
   * 
   * @param text the text
   * @param width the width
   * @param height the height
   * @param colTxt the col txt
   * @param colTit the col tit
   * @return the vector
   */
  public static final Vector createWordList(final String text, final int width, final int height, final int colTxt, final int colTit) {
    final Vector wordList = new Vector();
    RenderedWord oldWord;
    final String tmpText = text.replace(BaseApp.CR, ' ');
    int color = colTxt;
    int index = 0;
    final int lung = tmpText.length();
    int row = 0;
    int offset = 0;
    int style = 0;
    boolean fine = false;
    String tag = "";
    oldWord = null;
    while (!fine) {
      int spaceIndex;
      int tagIndex;
      int endTagIndex;
      String word = "";
      if (tmpText.charAt(index) == ' ') {
        index++;
        if (index == lung) {
          fine = true;
        }
        word = " ";
      }
      else {
        if (tmpText.charAt(index) == '<') {
          endTagIndex = tmpText.indexOf(">", index);
          tag = tmpText.substring(index + 1, endTagIndex).trim().toLowerCase();
          if (tag.equals("tit")) {
            color = colTit;
            style = style + Font.STYLE_BOLD;
          }
          if (tag.equals("/tit")) {
            color = colTxt;
            style = style - Font.STYLE_BOLD;
            row = row + 14 * RenderedWord.heightFont / 10;
            offset = 0;
          }
          if (tag.equals("b")) {
            style = style + Font.STYLE_BOLD;
          }
          if (tag.equals("/b")) {
            style = style - Font.STYLE_BOLD;
          }
          if (tag.equals("i")) {
            style = style + Font.STYLE_ITALIC;
          }
          if (tag.equals("/i")) {
            style = style - Font.STYLE_ITALIC;
          }
          if (tag.equals("u")) {
            style = style + Font.STYLE_UNDERLINED;
          }
          if (tag.equals("/u")) {
            style = style - Font.STYLE_UNDERLINED;
          }
          if ((tag.equals("br")) || (tag.equals("br/"))) {
            row = row + RenderedWord.heightFont;
            offset = 0;
          }
          if ((style < 0) || (style > 7)) {
            style = 0;
          }
          index = endTagIndex + 1;
          if ((index == lung) || (index == -1)) {
            fine = true;
          }
        }
        else {
          spaceIndex = tmpText.indexOf(" ", index);
          tagIndex = tmpText.indexOf("<", index);
          if ((spaceIndex == -1) && (tagIndex == -1)) {
            word = tmpText.substring(index);
            fine = true;
          }
          else {
            if ((spaceIndex == -1) && (tagIndex != -1)) {
              word = tmpText.substring(index, tagIndex);
              index = tagIndex;
            }
            else {
              if ((spaceIndex != -1) && (tagIndex == -1)) {
                word = tmpText.substring(index, spaceIndex);
                index = spaceIndex;
              }
              else {
                if (spaceIndex < tagIndex) {
                  word = tmpText.substring(index, spaceIndex);
                  index = spaceIndex;
                }
                else {
                  word = tmpText.substring(index, tagIndex);
                  index = tagIndex;
                }
              }
            }
          }
          if (!BaseApp.isEmpty(word)) {
            final int l = RenderedWord.font[style].stringWidth(word);
            int pos = offset;
            if ((offset + l) < width) {
              offset = offset + l + RenderedWord.fontWidth[style];
            }
            else {
              if (offset != 0) {
                row = row + RenderedWord.heightFont;
              }
              pos = 0;
              offset = l + RenderedWord.fontWidth[style];
            }
            if (oldWord == null) {
              oldWord = new RenderedWord(pos, row, style, color, word);
            }
            else {
              if ((oldWord.row == row) && (oldWord.style == style) && (oldWord.color == color)) {
                oldWord.word = oldWord.word + " " + word;
              }
              else {
                wordList.addElement(oldWord);
                oldWord = new RenderedWord(pos, row, style, color, word);
              }
            }
          }
        }
      }
    }
    if (oldWord != null) {
      wordList.addElement(oldWord);
    }
    return wordList;
  }

}
