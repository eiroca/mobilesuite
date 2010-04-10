/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.txt file.  */

package org.apache.log4j.helpers;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Formats a {@link Date} in the format "dd MMM YYYY HH:mm:ss,SSS" for example, "06 Nov 1994 15:49:37,459".
 * @author Ceki G&uuml;lc&uuml;
 * @since 0.7.5
 */
// Modifiers: Witmate [Nov,2004: Modified for log4j2me]
public class DateTimeDateFormat extends AbsoluteTimeDateFormat {

  String[] shortMonths = {
      "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Agu", "Sep", "Oct", "Nov", "Dec"
  };

  public DateTimeDateFormat() {
    super();
  }

  public DateTimeDateFormat(final TimeZone timeZone) {
    this();
    calendar = Calendar.getInstance(timeZone);
  }

  /**
   * Appends to <code>sbuf</code> the date in the format "dd MMM YYYY HH:mm:ss,SSS" for example, "06 Nov 1994 08:49:37,459".
   * @param sbuf the string buffer to write to
   */
  public StringBuffer format(final Date date, final StringBuffer sbuf, final Object fieldPosition) {

    calendar.setTime(date);

    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    if (day < 10) {
      sbuf.append('0');
    }
    sbuf.append(day);
    sbuf.append(' ');
    sbuf.append(shortMonths[calendar.get(Calendar.MONTH)]);
    sbuf.append(' ');

    final int year = calendar.get(Calendar.YEAR);
    sbuf.append(year);
    sbuf.append(' ');

    return super.format(date, sbuf, fieldPosition);
  }

  /**
   * This method does not do anything but return <code>null</code>.
   */
  public Date parse(final java.lang.String s, final Object pos) {
    return null;
  }
}
