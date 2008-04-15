/**
 * Copyright (C) 2006-2008 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 * Copyright (C) 2002 Eugene Morozov (xonixboy@hotmail.com)
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
package net.eiroca.j2me.app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.Stack;
import java.util.TimeZone;
import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.game.TiledLayer;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordListener;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public abstract class BaseApp extends MIDlet implements CommandListener, ItemCommandListener {

  public static final String NL = "\n\r";
  public static final String sCR = "\n";
  public static final char CR = '\n';
  public static final char LF = '\r';

  /*
   * Mathematics
   */

  private static final Random randomgenerator = new Random();

  public static int roll(final int many, final int size) {
    int s = 0;
    for (int i = 0; i < many; i++) {
      s += (BaseApp.randomgenerator.nextInt() & 0x7FFFFFFF) % size;
    }
    return s;
  }

  public static int rand(final int size) {
    return (BaseApp.randomgenerator.nextInt() & 0x7FFFFFFF) % size;
  }

  /*
   * Enconding
   */

  public static final String SERIALIZATION_ENCODING = "UTF-8";
  public static final int LONG_LENGTH = 8;
  public static final int INT_LENGTH = 4;
  private final static char[] charTab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

  /**
   * Encodes int to byte array using the given offset.
   *
   * @param integer The integer to be encoded.
   * @param bytes The byte array to encode to.
   * @param bytesOffset The offset to start the encoding.
   */
  public static void encodeIntegerToBytes(final int integer, final byte[] bytes, int bytesOffset) {
    bytes[bytesOffset++] = (byte) (integer >> 24);
    bytes[bytesOffset++] = (byte) (integer >> 16);
    bytes[bytesOffset++] = (byte) (integer >> 8);
    bytes[bytesOffset] = (byte) (integer);
  }

  /**
   * Encodes the byte array to integer. Note: implementation doesn't check for
   * ArrayIndexOutOfBounds
   *
   * @param bytes The byte[] to be encoded.
   * @param bytesOffset The byte[] offset.
   * @return The int representation of byte[].
   */
  public static int encodeBytesToInteger(final byte[] bytes, int bytesOffset) {
    return ((bytes[bytesOffset++]) << 24) + ((bytes[bytesOffset++] & 0xff) << 16) + ((bytes[bytesOffset++] & 0xff) << 8) + (bytes[bytesOffset] & 0xff);
  }

  /**
   * Encodes long to byte[].
   *
   * @param longValue The long to be encoded.
   * @param bytes The byte[] to encode into.
   * @param bytesOffset The byte[] offset.
   */
  public static void encodeLongToBytes(final long longValue, final byte[] bytes, int bytesOffset) {
    bytes[bytesOffset++] = (byte) (longValue >> 56);
    bytes[bytesOffset++] = (byte) (longValue >> 48);
    bytes[bytesOffset++] = (byte) (longValue >> 40);
    bytes[bytesOffset++] = (byte) (longValue >> 32);
    bytes[bytesOffset++] = (byte) (longValue >> 24);
    bytes[bytesOffset++] = (byte) (longValue >> 16);
    bytes[bytesOffset++] = (byte) (longValue >> 8);
    bytes[bytesOffset] = (byte) (longValue);
  }

  /**
   * Encodes byte[] to long.
   *
   * @param bytes The byte[] to be encoded.
   * @param bytesOffset The byte[] offset.
   * @return The encoded long value.
   */
  public static long encodeBytesToLong(final byte[] bytes, int bytesOffset) {
    return ((bytes[bytesOffset++] & 0xffL) << 56) | ((bytes[bytesOffset++] & 0xffL) << 48) | ((bytes[bytesOffset++] & 0xffL) << 40) | ((bytes[bytesOffset++] & 0xffL) << 32)
        | ((bytes[bytesOffset++] & 0xffL) << 24) | ((bytes[bytesOffset++] & 0xffL) << 16) | ((bytes[bytesOffset++] & 0xffL) << 8) | (bytes[bytesOffset] & 0xffL);
  }

  /**
   * Encodes the bytes to the Hex String. Used for the key conversion in the UI.
   *
   * @param bytes The byte[] to be encoded.
   * @return The encoded String.
   */
  public static String encodeToHexString(final byte[] bytes) {
    // Note: In this initial implementation the length of the key string is
    // 32 characters in Hex, so initialization of the StringBuffer
    // with 32 chars length can help to boost the performance slightly.
    final StringBuffer result = new StringBuffer(32);
    final int bytesLength = bytes.length;
    for (int i = 0; i < bytesLength; i++) {
      result.append(Integer.toString((bytes[i] >> 4) & 0x0f, 16));
      result.append(Integer.toString(bytes[i] & 0x0f, 16));
    }
    return result.toString();
  }

  /**
   * Decodes the Hex string into the byte array. Used for the key conversion in
   * the UI.
   *
   * @param string The String to be decoded.
   * @param bytes The byte[] to decode into.
   */
  public static void decodeHexString(final String string, final byte[] bytes) {
    final int length = Math.min(string.length(), bytes.length * 2);
    int index;
    byte charValue;
    for (int i = 0; i < length; i++) {
      index = i / 2;
      charValue = (byte) Character.digit(string.charAt(i), 16);
      bytes[index] = (byte) (bytes[index] | ((charValue < 0) ? 0 : charValue << (((i + 1) % 2) * 4)));
    }
  }

  /**
   * @param data
   * @return
   */
  public static String base64Encode(final byte[] data) {
    return BaseApp.base64Encode(data, 0, data.length, null).toString();
  }

  /**
   * Decodes the given Base64 encoded String to a new byte array. The byte array
   * holding the decoded data is returned.
   */
  public static byte[] base64Decode(final String s) {
    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      BaseApp.base64Decode(s, bos);
      bos.close();
    }
    catch (final IOException e) {
      throw new RuntimeException();
    }
    return bos.toByteArray();
  }

  /**
   * Encodes the part of the given byte array denoted by start and len to the
   * Base64 format. The encoded data is appended to the given StringBuffer. If
   * no StringBuffer is given, a new one is created automatically. The
   * StringBuffer is the return value of this method.
   */
  public static StringBuffer base64Encode(final byte[] data, final int start, final int len, StringBuffer buf) {
    if (buf == null) {
      buf = new StringBuffer(data.length * 3 / 2);
    }
    final int end = len - 3;
    int i = start;
    int n = 0;
    while (i <= end) {
      final int d = ((data[i] & 0x0ff) << 16) | ((data[i + 1] & 0x0ff) << 8) | (data[i + 2] & 0x0ff);
      buf.append(BaseApp.charTab[(d >> 18) & 63]);
      buf.append(BaseApp.charTab[(d >> 12) & 63]);
      buf.append(BaseApp.charTab[(d >> 6) & 63]);
      buf.append(BaseApp.charTab[d & 63]);
      i += 3;
      if (n++ >= 14) {
        n = 0;
        buf.append(BaseApp.LF);
      }
    }
    if (i == start + len - 2) {
      final int d = ((data[i] & 0x0ff) << 16) | ((data[i + 1] & 255) << 8);
      buf.append(BaseApp.charTab[(d >> 18) & 63]);
      buf.append(BaseApp.charTab[(d >> 12) & 63]);
      buf.append(BaseApp.charTab[(d >> 6) & 63]);
      buf.append("=");
    }
    else if (i == start + len - 1) {
      final int d = (data[i] & 0x0ff) << 16;
      buf.append(BaseApp.charTab[(d >> 18) & 63]);
      buf.append(BaseApp.charTab[(d >> 12) & 63]);
      buf.append("==");
    }
    return buf;
  }

  /**
   * decode a char in base64
   *
   * @param c
   * @return
   */
  private static int base64Decode(final char c) {
    if ((c >= 'A') && (c <= 'Z')) {
      return c - 65;
    }
    else if ((c >= 'a') && (c <= 'z')) {
      return c - 97 + 26;
    }
    else if ((c >= '0') && (c <= '9')) {
      return c - 48 + 26 + 26;
    }
    else {
      switch (c) {
        case '+':
          return 62;
        case '/':
          return 63;
        case '=':
          return 0;
        default:
          throw new RuntimeException("unexpected code: " + c);
      }
    }
  }

  /**
   * decode a base 64 string
   *
   * @param s
   * @param os
   * @throws IOException
   */
  public static void base64Decode(final String s, final OutputStream os) throws IOException {
    int i = 0;
    final int len = s.length();
    while (true) {
      while ((i < len) && (s.charAt(i) <= ' ')) {
        i++;
      }
      if (i == len) {
        break;
      }
      final int tri = (BaseApp.base64Decode(s.charAt(i)) << 18) + (BaseApp.base64Decode(s.charAt(i + 1)) << 12) + (BaseApp.base64Decode(s.charAt(i + 2)) << 6)
          + (BaseApp.base64Decode(s.charAt(i + 3)));
      os.write((tri >> 16) & 255);
      if (s.charAt(i + 2) == '=') {
        break;
      }
      os.write((tri >> 8) & 255);
      if (s.charAt(i + 3) == '=') {
        break;
      }
      os.write(tri & 255);
      i += 4;
    }
  }

  /*
   * Strings
   */

  /**
   * Removes non-numeric characters from the phone number.
   *
   * @param telNum The address string to process
   * @return The processed address string.
   */
  public static String normalizeTelNum(final String telNum) {
    // Note: no NullPointer checks.
    if (telNum == null) { return null; }
    final int telNumLen = telNum.length();
    final StringBuffer res = new StringBuffer(telNumLen);
    char ch;
    for (int i = 0; i < telNumLen; i++) {
      ch = telNum.charAt(i);
      if (Character.isDigit(ch)) {
        res.append(ch);
      }
      else if (ch == '+') {
        res.append(ch);
      }
    }
    return res.toString();
  }

  /**
   * Count lines (CR as separator)
   *
   * @param message
   * @return
   */
  public static int lineBreaks(final String message) {
    int breaks = 0;
    int index = 0;
    while ((index = message.indexOf(BaseApp.CR, index)) != -1) {
      ++breaks;
      ++index;
    }
    return breaks;
  }

  /**
   * Return max width of a line of a string (CR as line separator)
   *
   * @param f
   * @param message
   * @return
   */
  public static int maxSubWidth(final Font f, final String message) {
    int startIndex;
    int endIndex = -1;
    int maxWidth = 0;
    int messageWidth;
    while (endIndex < message.length()) {
      startIndex = endIndex + 1;
      endIndex = message.indexOf(BaseApp.CR, startIndex);
      if (endIndex == -1) {
        endIndex = message.length();
      }
      final String submessage = message.substring(startIndex, endIndex);
      messageWidth = f.stringWidth(submessage);
      if (maxWidth < messageWidth) {
        maxWidth = messageWidth;
      }
    }
    return maxWidth;
  }

  /**
   * Remove any tag
   *
   * @param text
   * @return
   */
  public static String removeHtml(final String text) {
    int htmlStartIndex;
    int htmlEndIndex;
    if (text == null) { return null; }
    htmlStartIndex = text.indexOf("<");
    if (htmlStartIndex == -1) { return text; }
    final StringBuffer plainText = new StringBuffer(text.length());
    String htmlText = text.trim();
    while (htmlStartIndex >= 0) {
      plainText.append(htmlText.substring(0, htmlStartIndex));
      if (htmlText.substring(htmlStartIndex + 1, htmlStartIndex + 3).toLowerCase().equals("br")) {
        plainText.append(BaseApp.CR);
      }
      htmlEndIndex = htmlText.indexOf(">", htmlStartIndex);
      htmlText = htmlText.substring(htmlEndIndex + 1);
      htmlStartIndex = htmlText.indexOf("<", 0);
    }
    htmlText = plainText.toString();
    return htmlText;
  }

  /**
   * If string is null of "" true is returned
   *
   * @param s
   * @return
   */
  public static boolean isEmpty(final String s) {
    return (s == null) || (s.equals(""));
  }

  /**
   * Return the value of a string
   *
   * @param s string with the number
   * @param def default value
   * @param radix of the number
   * @return
   */
  public static int val(final String s, final int def, final int radix) {
    try {
      return Integer.parseInt(s, radix);
    }
    catch (final Exception e) {
      return def;
    }
  }

  /*
   * Formatters
   */

  public static final int DATE = 1;
  public static final int TIME = 2;
  public static final int DATE_TIME = 3;

  /**
   * Left pad a string
   */
  public static String lpad(final String data, final String add, final int len) {
    final StringBuffer tmp = new StringBuffer();
    for (int i = 0; i < len - data.length(); i++) {
      tmp.append(add);
    }
    tmp.append(data);
    return tmp.toString();
  }

  /**
   * add two digit number
   *
   * @param buf
   * @param i
   */
  public static void dd(final StringBuffer buf, final int i) {
    buf.append((char) ('0' + i / 10));
    buf.append((char) ('0' + i % 10));
  }

  /**
   * Format a date dd/mm/yyyy
   *
   * @param c
   * @return
   */
  public StringBuffer formatDate(final Calendar c, final StringBuffer sb) {
    sb.append(c.get(Calendar.DATE)).append('/').append((c.get(Calendar.MONTH) + 1)).append('/').append(c.get(Calendar.YEAR));
    return sb;
  }

  /**
   * Format Time hh:mm:ss
   *
   * @param c
   * @param sec
   * @return
   */
  public StringBuffer formatTime(final Calendar c, final boolean sec, final StringBuffer sb) {
    sb.append(c.get(Calendar.HOUR_OF_DAY)).append(':').append(c.get(Calendar.MINUTE));
    if (sec) {
      sb.append(':').append(c.get(Calendar.SECOND));
    }
    return sb;
  }

  /**
   * convert date into iso date string
   *
   * @param date
   * @param type DATE, TIME or DATE_TIME
   * @return
   */
  public static String dateToString(final Date date, final int type) {
    final Calendar calendar = Calendar.getInstance();
    calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
    calendar.setTime(date);
    final StringBuffer buf = new StringBuffer(26);
    if ((type & BaseApp.DATE) != 0) {
      final int year = calendar.get(Calendar.YEAR);
      BaseApp.dd(buf, year / 100);
      BaseApp.dd(buf, year % 100);
      buf.append('-');
      BaseApp.dd(buf, calendar.get(Calendar.MONTH) - Calendar.JANUARY + 1);
      buf.append('-');
      BaseApp.dd(buf, calendar.get(Calendar.DAY_OF_MONTH));
      if (type == BaseApp.DATE_TIME) {
        buf.append("T");
      }
    }
    if ((type & BaseApp.TIME) != 0) {
      BaseApp.dd(buf, calendar.get(Calendar.HOUR_OF_DAY));
      buf.append(':');
      BaseApp.dd(buf, calendar.get(Calendar.MINUTE));
      buf.append(':');
      BaseApp.dd(buf, calendar.get(Calendar.SECOND));
      buf.append('.');
      final int ms = calendar.get(Calendar.MILLISECOND);
      buf.append((char) ('0' + (ms / 100)));
      BaseApp.dd(buf, ms % 100);
      buf.append('Z');
    }
    return buf.toString();
  }

  /**
   * convert iso date string into date
   *
   * @param text
   * @param type
   * @return
   */
  public static Date stringToDate(String text, final int type) {
    final Calendar calendar = Calendar.getInstance();
    if ((type & BaseApp.DATE) != 0) {
      calendar.set(Calendar.YEAR, Integer.parseInt(text.substring(0, 4)));
      calendar.set(Calendar.MONTH, Integer.parseInt(text.substring(5, 7)) - 1 + Calendar.JANUARY);
      calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(text.substring(8, 10)));
      if ((type != BaseApp.DATE_TIME) || (text.length() < 11)) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
      }
      text = text.substring(11);
    }
    else {
      calendar.setTime(new Date(0));
    }
    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(text.substring(0, 2)));
    calendar.set(Calendar.MINUTE, Integer.parseInt(text.substring(3, 5)));
    calendar.set(Calendar.SECOND, Integer.parseInt(text.substring(6, 8)));
    int pos = 8;
    if ((pos < text.length()) && (text.charAt(pos) == '.')) {
      int ms = 0;
      int f = 100;
      while (true) {
        final char d = text.charAt(++pos);
        if ((d < '0') || (d > '9')) {
          break;
        }
        ms += (d - '0') * f;
        f /= 10;
      }
      calendar.set(Calendar.MILLISECOND, ms);
    }
    else {
      calendar.set(Calendar.MILLISECOND, 0);
    }
    if (pos < text.length()) {
      if ((text.charAt(pos) == '+') || (text.charAt(pos) == '-')) {
        calendar.setTimeZone(TimeZone.getTimeZone("GMT" + text.substring(pos)));
      }
      else if (text.charAt(pos) == 'Z') {
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
      }
      else {
        throw new RuntimeException("illegal time format!");
      }
    }
    return calendar.getTime();
  }

  /**
   * Format a string changing $1 .. $9 with given objetcs.toString()
   *
   * @param msg
   * @param o
   * @return
   */
  public static final String format(final String msg, final Object[] o) {
    final StringBuffer sb = new StringBuffer(msg.length());
    char ch;
    for (int i = 0; i < msg.length(); i++) {
      ch = msg.charAt(i);
      if (ch == '$') {
        i++;
        ch = msg.charAt(i);
        switch (ch) {
          case '1':
          case '2':
          case '3':
          case '4':
          case '5':
          case '6':
          case '7':
          case '8':
          case '9':
            sb.append(o[ch - '1']);
            break;
          default:
            sb.append(ch);
            break;
        }
      }
      else {
        sb.append(ch);
      }
    }
    return sb.toString();
  }

  /*
   * URL
   */

  /**
   * URL encode a string
   */
  public static String URLEncode(final String s) {
    if (s == null) { return null; }
    final StringBuffer res = new StringBuffer(s.length());
    char ch;
    for (int i = 0; i < s.length(); i++) {
      ch = s.charAt(i);
      switch (ch) {
        case CR:
          res.append("%0A");
          break;
        case LF:
          res.append("%0D");
          break;
        case ' ':
          res.append("%20");
          break;
        case '#':
          res.append("%23");
          break;
        case '%':
          res.append("%25");
          break;
        case '&':
          res.append("%26");
          break;
        case '-':
          res.append("%2D");
          break;
        case '/':
          res.append("%2F");
          break;
        case ':':
          res.append("%3A");
          break;
        case ';':
          res.append("%3B");
          break;
        case '=':
          res.append("%3D");
          break;
        case '?':
          res.append("%3F");
          break;
        default:
          res.append(ch);
          break;
      }
    }
    return res.toString();
  }

  /*
   * Graphics
   */

  /**
   * Creates a new, scaled version of the given image.
   *
   * @param src: The source image
   * @param dstW: The destination (scaled) image width
   * @param dstH: The destination (scaled) image height
   * @return Image: A new Image object with the given width and height.
   */
  public static Image scaleImage(final Image src, final int dstW, final int dstH) {
    final int srcW = src.getWidth();
    final int srcH = src.getHeight();
    final Image tmp = Image.createImage(dstW, srcH);
    Graphics g = tmp.getGraphics();
    int delta = (srcW << 16) / dstW;
    int pos = delta / 2;
    for (int x = 0; x < dstW; x++) {
      g.setClip(x, 0, 1, srcH);
      g.drawImage(src, x - (pos >> 16), 0, Graphics.LEFT | Graphics.TOP);
      pos += delta;
    }
    final Image dst = Image.createImage(dstW, dstH);
    g = dst.getGraphics();
    delta = (srcH << 16) / dstH;
    pos = delta / 2;
    for (int y = 0; y < dstH; y++) {
      g.setClip(0, y, dstW, 1);
      g.drawImage(tmp, 0, y - (pos >> 16), Graphics.LEFT | Graphics.TOP);
      pos += delta;
    }
    return dst;
  }

  /*
   * Vector
   */

  /**
   * Quick Sort
   */
  private static void qsort(final Vector v, final int sx, final int dx, final Comparator c) {
    int a;
    int b;
    Object ele2x;
    a = sx;
    b = dx;
    final Object ele1x = v.elementAt((sx + dx) / 2);
    do {
      while (c.compare(v.elementAt(a), ele1x) < 0) {
        a++;
      }
      while (c.compare(ele1x, v.elementAt(b)) < 0) {
        b--;
      }
      if (a <= b) {
        ele2x = v.elementAt(a);
        v.setElementAt(v.elementAt(b), a);
        v.setElementAt(ele2x, b);
        a++;
        b--;
      }
    }
    while (a <= b);
    if (sx < b) {
      BaseApp.qsort(v, sx, b, c);
    }
    if (a < dx) {
      BaseApp.qsort(v, a, dx, c);
    }
  }

  /**
   * Sort a vector according to comparator c
   *
   * @param v
   * @param c
   */
  public static void sort(final Vector v, final Comparator c) {
    if ((v != null) && (v.size() > 1)) {
      BaseApp.qsort(v, 0, v.size() - 1, c);
    }
  }

  /**
   * Find a element o in the vector v using the comparator c
   *
   * @param v
   * @param o
   * @param c
   * @return
   */
  public static int find(final Vector v, final Object o, final Comparator c) {
    for (int i = 0; i < v.size(); i++) {
      if (c.compare(o, v.elementAt(i)) == 0) { return i; }
    }
    return -1;
  }

  /*
   * RMS
   */
  private static final Hashtable recordStores = new Hashtable();
  private static RecordListener listener;

  public static void setRecordListener(final RecordListener listener) {
    BaseApp.listener = listener;
  }

  /**
   * Get / create a RecordStore. Boolean save is used to sore the RecordStore
   * into the Hashtable or not. Program will release them in destroyApp().
   *
   * @param name, the name of the RecordStore
   * @param createIfNecessary, the RecordStore will be created if it's not
   *          existing and this boolean is set to true
   * @param save, the RecordStore will be stored into the Hashtable if save is
   *          set to true. It will only be released when destroyApp() is called
   * @return
   */
  public static RecordStore getRecordStore(final String name, final boolean createIfNecessary, final boolean save) {
    RecordStore rs = (RecordStore) BaseApp.recordStores.get(name);
    if (rs == null) {
      try {
        rs = RecordStore.openRecordStore(name, createIfNecessary);
        if (BaseApp.listener != null) {
          rs.addRecordListener(BaseApp.listener);
        }
        if (save) {
          BaseApp.recordStores.put(name, rs);
        }
      }
      catch (final RecordStoreException e) {
        //
      }
    }
    return rs;
  }

  /**
   *
   *
   */
  public static void closeRecordStores() {
    final Enumeration e = BaseApp.recordStores.elements();
    while (e.hasMoreElements()) {
      try {
        ((RecordStore) e.nextElement()).closeRecordStore();
      }
      catch (final RecordStoreException ex) {
        //
      }
    }
    BaseApp.recordStores.clear();
  }

  /**
   * @param rs
   * @param recordID
   * @return
   */
  public static DataInputStream readRecord(final RecordStore rs, final int recordID) {
    DataInputStream dis = null;
    if (rs != null) {
      try {
        final byte[] data = rs.getRecord(recordID);
        dis = new DataInputStream(new ByteArrayInputStream(data));
      }
      catch (final RecordStoreException e) {
        //
      }
    }
    return dis;
  }

  /**
   * @param rs
   * @param baos
   */
  public static void writeData(final RecordStore rs, final ByteArrayOutputStream baos) {
    if (rs != null) {
      final byte[] data = baos.toByteArray();
      try {
        if (rs.getNumRecords() == 0) {
          rs.addRecord(data, 0, data.length);
        }
        else {
          rs.setRecord(1, data, 0, data.length);
        }
      }
      catch (final RecordStoreException e) {
        //
      }
    }
    try {
      baos.close();
    }
    catch (final IOException e) {
      //
    }
  }

  /**
   * @param rs
   * @param is
   * @param os
   */
  public static void close(final RecordStore rs, final InputStream is, final OutputStream os) {
    if (is != null) {
      try {
        is.close();
      }
      catch (final IOException e) {
        //
      }
    }
    if (os != null) {
      try {
        os.close();
      }
      catch (final IOException e) {
        //
      }
    }
    if (rs != null) {
      try {
        rs.closeRecordStore();
      }
      catch (final RecordStoreException e) {
        //
      }
    }
  }

  /**
   * @param rsName
   * @param info
   */
  public static void getRecordStoreInfo(final String rsName, final RMSInfo info) {
    try {
      final RecordStore rs = RecordStore.openRecordStore(rsName, true);
      try {
        info.used = rs.getSize();
        info.avail = rs.getSizeAvailable();
        info.numRec = rs.getNumRecords();
      }
      finally {
        rs.closeRecordStore();
      }
    }
    catch (final RecordStoreException e) {
    }
  }

  /*
   * Resources
   */
  public static final int MD_MENUID = 0;
  public static final int MD_MENUTX = 1;
  public static final int MD_MENUAC = 2;
  public static final int MD_MENUIC = 3;

  private static final String DIR_SEP = "/";

  public static Command cBACK;
  public static Command cEXIT;
  public static Command cOK;

  public static int background = 0x00000000;
  public static int foreground = 0x00FFFFFF;
  public static short[][] menu;
  public static String[] messages;
  public static Image[] icons;

  public static int pSpecial;

  private static char LINESEP = BaseApp.CR;
  private static char STRIP = BaseApp.LF;
  private static String COMMENT = "#";

  public static InputStream getInputStream(final String res) {
    StringBuffer sb = new StringBuffer(64);
    String basepath;
    final Class me = res.getClass();
    sb.append(BaseApp.DIR_SEP);
    if (BaseApp.resPrefix != null) {
      sb.append(BaseApp.resPrefix).append(BaseApp.DIR_SEP);
    }
    basepath = sb.toString();
    if (BaseApp.locale != null) {
      sb.append(BaseApp.locale).append(BaseApp.DIR_SEP);
    }
    sb.append(res);
    InputStream in = me.getResourceAsStream(sb.toString());
    if (in == null) {
      sb = new StringBuffer(basepath).append(res);
      in = me.getResourceAsStream(sb.toString());
    }
    return in;
  }

  /**
   * Read a resource file into a Pair[], the format is name&lt;sep&gt;value.
   * Line separator is CR
   *
   * @param res
   * @param sep
   * @return
   */
  public static Pair[] readPairs(final String res, final char sep) {
    final Vector s = new Vector();
    Pair p = new Pair();
    StringBuffer sb = new StringBuffer(60);
    boolean inName = true;
    boolean sepFound = false;
    int ch;
    try {
      final InputStream in = BaseApp.getInputStream(res);
      do {
        ch = in.read();
        if (ch == -1) {
          if (sb.length() > 0) {
            if (!inName) {
              p.value = sb.toString();
            }
            else {
              p.name = sb.toString();
              p.value = null;
            }
            s.addElement(p);
          }
          break;
        }
        else if ((ch == sep) && (!sepFound)) {
          sepFound = true;
          p.name = sb.toString();
          p.value = null;
          sb = new StringBuffer(60);
          inName = false;
        }
        else if (ch == BaseApp.LINESEP) {
          sepFound = false;
          if (!inName) {
            p.value = sb.toString();
          }
          else {
            p.name = sb.toString();
            p.value = null;
          }
          s.addElement(p);
          p = new Pair();
          sb = new StringBuffer(80);
        }
        else if (ch != BaseApp.STRIP) {
          sb.append((char) ch);
        }
      }
      while (true);
    }
    catch (final IOException e) {
      s.removeAllElements();
    }
    final Pair[] out = new Pair[s.size()];
    for (int i = 0; i < s.size(); i++) {
      out[i] = (Pair) s.elementAt(i);
    }
    return out;
  }

  /**
   * Read a resource file into a String[]. Line separator is CR.
   *
   * @param res
   * @return
   */
  public static String[] readStrings(final String res) {
    final Vector s = new Vector();
    String txt;
    StringBuffer sb = new StringBuffer(80);
    int ch;
    try {
      final InputStream in = BaseApp.getInputStream(res);
      do {
        ch = in.read();
        if (ch == -1) {
          if (sb.length() > 0) {
            s.addElement(sb);
          }
          break;
        }
        else if (ch == BaseApp.LINESEP) {
          txt = sb.toString();
          if (!txt.startsWith(BaseApp.COMMENT)) {
            s.addElement(txt);
          }
          sb = new StringBuffer(80);
        }
        else if (ch != BaseApp.STRIP) {
          sb.append((char) ch);
        }
      }
      while (true);
    }
    catch (final IOException e) {
      s.removeAllElements();
    }
    final String[] out = new String[s.size()];
    for (int i = 0; i < s.size(); i++) {
      out[i] = s.elementAt(i).toString();
    }
    return out;
  }

  /**
   * Read a resource file into a String
   *
   * @param res
   * @return
   */
  public static String readString(final String res) {
    final StringBuffer sb = new StringBuffer(1024);
    int ch;
    try {
      final InputStream in = BaseApp.getInputStream(res);
      do {
        ch = in.read();
        if (ch == -1) {
          break;
        }
        sb.append((char) ch);
      }
      while (true);
      return sb.toString();
    }
    catch (final IOException e) {
      return null;
    }
  }

  /**
   * Split one large image into an Images array.
   *
   * @param image, the Image source
   * @param count, the count of the return array
   * @param width, the width of the split Image
   * @param height, the height of the split Image
   * @return
   */
  public static Image[] splitImages(final String res, final int count, final int width, final int height) {
    final Image[] images = new Image[count];
    try {
      Graphics g;
      final InputStream in = BaseApp.getInputStream(res);
      final Image image = Image.createImage(in);
      for (int i = 0; i < count; i++) {
        images[i] = Image.createImage(width, height);
        g = images[i].getGraphics();
        g.drawImage(image, -i * width, 0, 0);
      }
      g = null;
    }
    catch (final IOException ex) {
      System.err.println("missing " + res);
    }
    return images;
  }

  /**
   * Load an Image from a resource file
   *
   * @param res
   * @return
   */
  public static Image createImage(final String res) {
    Image image = null;
    try {
      final InputStream in = BaseApp.getInputStream(res);
      image = Image.createImage(in);
    }
    catch (final IOException ex) {
      System.err.println("missing " + res);
    }
    return image;
  }

  /**
   * Load a tile from a resource file
   *
   * @param res
   * @param tile
   * @return
   */
  public static boolean loadTile(final String res, final TiledLayer tile) {
    boolean ok = true;
    try {
      final InputStream in = BaseApp.getInputStream(res);
      final DataInputStream rs = new DataInputStream(in);
      final int sr = rs.readByte();
      final int sc = rs.readByte();
      for (int r = 0; r < sr; r++) {
        for (int c = 0; c < sc; c++) {
          tile.setCell(c, r, rs.readByte());
        }
      }
    }
    catch (final IOException e) {
      ok = false;
    }
    return ok;
  }

  /**
   * @param cl
   * @param title
   * @param textRes
   * @return
   */
  public static Displayable getTextForm(final int title, final String textRes) {
    final Form form = new Form(BaseApp.messages[title]);
    final String msg = BaseApp.readString(textRes);
    if (msg != null) {
      form.append(msg);
    }
    BaseApp.setup(form, BaseApp.cBACK, null);
    return form;
  }

  /**
   * @param cl
   * @param title
   * @param textRes
   * @param o
   * @return
   */
  public static Displayable getTextForm(final int title, final String textRes, final Object[] o) {
    final Form form = new Form(BaseApp.messages[title]);
    final String msg = BaseApp.readString(textRes);
    if (msg != null) {
      form.append(BaseApp.format(msg, o));
    }
    BaseApp.setup(form, BaseApp.cBACK, null);
    return form;
  }

  /**
   * Displays the alert.
   */
  public static void showAlert(final int alertTitle, final int alertMessage, final Image alertImage, final AlertType alertType, final Displayable alertNext, final int timeOut) {
    final Alert alert = new Alert(BaseApp.messages[alertTitle], BaseApp.messages[alertMessage], alertImage, alertType);
    alert.setTimeout(timeOut);
    BaseApp.back(alert, alertNext, true);
  }

  /**
   * @param msg
   * @param o
   * @return
   */
  public static String format(final int msg, final Object[] o) {
    return BaseApp.format(BaseApp.messages[msg], o);
  }

  /*
   * UI Manager
   */

  /**
   * Go and back for all Displayables.
   */
  private static final Stack displayableStack = new Stack();
  private static final Hashtable commands = new Hashtable();
  private static final Hashtable listItems = new Hashtable();

  public static final int AC_NONE = 0;
  public static final int AC_BACK = -101;
  public static final int AC_EXIT = -100;

  /**
   * Whenever go() or back() is called. This method will be invoked too. It lets
   * developer knows A Displayable is changed to B Displayble.
   *
   * @param previous, previous Displayable
   * @param next, next Displayable
   * @param returnCode
   */
  public void afterChange(final Displayable previous, final Displayable next) {
    //
  }

  /**
   * @param previous
   * @param next
   */
  public void beforeChange(final Displayable previous, final Displayable next) {
    //
  }

  /**
   * Go back to previous Displayable with one return code.
   *
   * @param alert
   * @param returnCode,
   * @return
   */
  public static Displayable back(final Alert alert) {
    // Back to A Displayable from B Displayable, so the Stack's size must be
    // more or equals to 2.
    if (BaseApp.displayableStack.size() >= 2) {
      final Displayable previous = (Displayable) BaseApp.displayableStack.pop();
      // get the instance of the previous one but remain it in the stack.
      final Displayable next = (Displayable) BaseApp.displayableStack.peek();
      BaseApp.midlet.beforeChange(previous, next);
      if (alert == null) {
        BaseApp.setDisplay(next);
      }
      else {
        BaseApp.setDisplay(alert, next);
      }
      BaseApp.midlet.afterChange(previous, next);
      return next;
    }
    return null;
  }

  /**
   * Go back to specify Displayable. It's same like calling go() to add one new
   * Displayable, if the next is not existing in the stack.
   *
   * @param alert
   * @param next
   */
  public static void back(final Alert alert, final Displayable next, final boolean keepPrevious) {
    if (!BaseApp.displayableStack.empty()) {
      final int index = BaseApp.displayableStack.search(next);
      final Displayable previous = (Displayable) BaseApp.displayableStack.pop();
      for (int i = index - 1; i >= 1; i--) {
        BaseApp.displayableStack.pop();
      }
      if (keepPrevious) {
        BaseApp.displayableStack.push(previous);
      }
      BaseApp.show(alert, next, true);
    }
  }

  /**
   * Add one new Displayable. save is used to set the Displayable to the stack
   * or not.
   *
   * @param alert
   * @param next, the new Displayable to be the current Dislayable in the
   *          screen, if next is null, it will
   * @param save, next will be saved into the Stack (for back() to return to
   *          previous Displayable) if save is set to true
   */
  public static void show(final Alert alert, Displayable next, final boolean save) {
    Displayable previous = null;
    if (!BaseApp.displayableStack.empty()) {
      previous = (Displayable) BaseApp.displayableStack.peek();
    }
    if (next == null) {
      next = BaseApp.getDisplay();
    }
    else {
      final boolean isNew = (previous == null) || (previous != next);
      boolean isAlert = (next instanceof Alert) && (((Alert) next).getTimeout() != Alert.FOREVER);
      if (save && isNew && !isAlert) {
        BaseApp.displayableStack.push(next);
      }
    }
    BaseApp.midlet.beforeChange(previous, next);
    if (alert == null) {
      BaseApp.setDisplay(next);
    }
    else {
      BaseApp.setDisplay(alert, next);
    }
    BaseApp.midlet.afterChange(previous, next);
  }

  public static Command newCommand(final int label, final int commandType, final int priority, final int action) {
    final Command cmd = new Command(BaseApp.messages[label], commandType, priority);
    BaseApp.commands.put(cmd, new Integer(action));
    return cmd;
  }

  public static Command newCommand(final int label, final int commandType, final int priority) {
    final Command cmd = new Command(BaseApp.messages[label], commandType, priority);
    return cmd;
  }

  /**
   * @param cmd
   * @param action
   */
  public static void registerCommand(final Command cmd, final int action) {
    BaseApp.commands.put(cmd, new Integer(action));
  }

  /**
   * @param cmd
   * @param next
   */
  public static void registerCommand(final Command cmd, final Displayable next) {
    BaseApp.commands.put(cmd, next);
  }

  /**
   * @param list
   * @param action
   */
  public static void registerList(final List list, final int action) {
    BaseApp.listItems.put(list, new Integer(action));
  }

  /**
   * @param list
   * @param index
   * @param action
   */
  public static void registerListItem(final List list, final int index, final int action) {
    BaseApp.listItems.put(list + "#" + index, new Integer(action));
  }

  /**
   * @param list
   * @param index
   * @param next
   */
  public static void registerListItem(final List list, final int index, final Displayable next) {
    BaseApp.listItems.put(list + "#" + index, next);
  }

  /**
   * @param action
   * @param d
   * @param cmd
   * @return
   */
  abstract public boolean handleAction(int action, Displayable d, Command cmd);

  public void process(final Command cmd, final Displayable d, final Item i) {
    // if cmd is list selection, we change cmd to actual command
    Object at = null;
    if (cmd == List.SELECT_COMMAND) {
      if ((d != null) && (d instanceof List)) {
        final List list = (List) d;
        final int index = list.getSelectedIndex();
        at = BaseApp.listItems.get(list + "#" + index);
        if (at == null) {
          at = BaseApp.listItems.get(list);
        }
      }
    }
    if ((at == null) && (cmd != null)) {
      at = BaseApp.commands.get(cmd);
    }
    if (at != null) {
      if (at instanceof Displayable) {
        BaseApp.show(null, (Displayable) at, true);
      }
      else {
        final int action = ((Integer) at).intValue();
        boolean processed = handleAction(action, d, cmd);
        if (!processed) {
          switch (action) {
            case AC_BACK: {
              BaseApp.back(null);
              break;
            }
            case AC_EXIT: {
              BaseApp.midlet.notifyDestroyed();
              break;
            }
          }
        }
      }
    }
  }

  /**
   * @param d
   * @param c1
   * @param c2
   */
  public static void setup(final Displayable d, final Command c1, final Command c2) {
    d.setCommandListener(BaseApp.midlet);
    if (c1 != null) {
      d.addCommand(c1);
    }
    if (c2 != null) {
      d.addCommand(c2);
    }
  }

  /*
   * Midlets
   */

  protected boolean initialized = false;
  public static BaseApp midlet;
  public static Display display;
  public static String locale = null;
  public static String resPrefix = null;

  /**
   * Setup midlet and display references
   */
  public BaseApp() {
    BaseApp.locale = BaseApp.readProperty("microedition.locale", null);
    if (BaseApp.locale != null) {
      final int ps = BaseApp.locale.indexOf("-");
      if (ps > 0) {
        BaseApp.locale = BaseApp.locale.substring(0, ps);
      }
      if (BaseApp.locale.length() == 0) {
        BaseApp.locale = null;
      }
    }
    BaseApp.midlet = this;
    BaseApp.display = Display.getDisplay(this);
  }

  public void commandAction(final Command cmd, final Item i) {
    process(cmd, null, i);
  }

  /**
   *
   */
  public void commandAction(final Command cmd, final Displayable d) {
    process(cmd, d, null);
  }

  /**
   * StartApp initialize or resume the application
   */
  final public void startApp() throws MIDletStateChangeException {
    if (!initialized) {
      init();
    }
    else {
      resume();
    }
  }

  /**
   * Pause the application
   */
  final public void pauseApp() {
    pause();
  }

  /**
   * Close the application
   */
  final public void destroyApp(final boolean unconditional) throws MIDletStateChangeException {
    done();
  }

  /**
   * Application initialization
   */
  protected void init() {
    initialized = true;
  }

  /**
   * Application pause
   */
  protected void pause() {
    //
  }

  /**
   * Application resume
   */
  protected void resume() {
    //
  }

  /**
   * Application destroy
   */
  protected void done() {
    BaseApp.displayableStack.removeAllElements();
    initialized = false;
  }

  /**
   * Return the current Display
   *
   * @return
   */
  public static Displayable getDisplay() {
    return BaseApp.display.getCurrent();
  }

  /**
   * Set the current display
   *
   * @param aDisplay
   */
  public static void setDisplay(final Displayable aDisplay) {
    BaseApp.display.setCurrent(aDisplay);
  }

  /**
   * @param anAlert
   * @param aNext
   */
  public static void setDisplay(final Alert anAlert, final Displayable aNext) {
    BaseApp.display.setCurrent(anAlert, aNext);
  }

  /**
   * Retrieves the system property, and returns it, or "unknown" if it is null.
   *
   * @param sName the name of the system property, eg. for System.getProperty
   * @return the contents of the property, never null
   */
  public static String readProperty(final String sName, final String def) {
    String sValue = null;
    try {
      sValue = System.getProperty(sName);
    }
    catch (final Exception e) {
    }
    return (sValue == null ? def : sValue);
  }

  public String readAppProperty(final String sName, final String def) {
    String sValue = null;
    try {
      sValue = getAppProperty(sName);
    }
    catch (final Exception e) {
    }
    return (sValue == null ? def : sValue);
  }

  /**
   * Checks to see if a given class/interface exists in this Java
   * implementation.
   *
   * @param className the full name of the class
   * @return true if the class/interface exists
   */
  public static boolean isClass(final String className) {
    boolean found = false;
    try {
      if (className != null) {
        Class.forName(className);
        found = true;
      }
    }
    catch (final ClassNotFoundException cnfe) {
      //
    }
    return found;
  }

}