/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.APL file.
 */

package org.apache.log4j.helpers;

import org.apache.log4j.spi.LoggingEvent;

/**
 * <p>
 * PatternConverter is an abtract class that provides the formatting functionality that derived classes need.
 * <p>
 * Conversion specifiers in a conversion patterns are parsed to individual PatternConverters. Each of which is responsible for converting a logging event in a converter specific manner.
 * @author <a href="mailto:cakalijp@Maritz.com">James P. Cakalic</a>
 * @author Ceki G&uuml;lc&uuml;
 * @since 0.8.2
 */
public abstract class PatternConverter {

  public PatternConverter next;
  int min = -1;
  int max = 0x7FFFFFFF;
  boolean leftAlign = false;
  static String[] SPACES = {
      " ", "  ", "    ", "        "
  }; // 1,2,4,8 spaces

  protected PatternConverter() {
    //
  }

  protected PatternConverter(final FormattingInfo fi) {
    min = fi.min;
    max = fi.max;
    leftAlign = fi.leftAlign;
  }

  /**
   * Derived pattern converters must override this method in order to convert conversion specifiers in the correct way.
   */
  abstract protected String convert(LoggingEvent event);

  /**
   * A template method for formatting in a converter specific way.
   */
  public void format(final StringBuffer sbuf, final LoggingEvent e) {
    final String s = convert(e);
    if (s == null) {
      if (0 < min) {
        spacePad(sbuf, min);
      }
      return;
    }
    final int len = s.length();
    if (len > max) {
      sbuf.append(s.substring(len - max));
    }
    else if (len < min) {
      if (leftAlign) {
        sbuf.append(s);
        spacePad(sbuf, min - len);
      }
      else {
        spacePad(sbuf, min - len);
        sbuf.append(s);
      }
    }
    else {
      sbuf.append(s);
    }
  }

  /**
   * Fast space padding method.
   */
  public void spacePad(final StringBuffer sbuf, int length) {
    while (length >= 8) {
      sbuf.append(PatternConverter.SPACES[3]);
      length -= 8;
    }
    for (int i = 2; i >= 0; i--) {
      if ((length & (1 << i)) != 0) {
        sbuf.append(PatternConverter.SPACES[i]);
      }
    }
  }

}
