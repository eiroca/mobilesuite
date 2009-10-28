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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import javax.microedition.rms.RecordFilter;

/**
 * A class for filtering items. Items can filtered out depending on whether they
 * are read/unread and/or parsed before a deadline and/or from a certain feed.
 */
public class ItemFilter implements RecordFilter {

  public static int ANY_FEEDID = -1;
  public static int NO_DEADLINE = 0;
  public static int READ_AND_UNREAD = 0;
  public static int READ = 1;
  public static int UNREAD = 2;

  private final long deadline;
  private ByteArrayInputStream bin;
  private DataInputStream din;

  /**
   * Constructor for the class.
   * @param newfeedid the id of the feed the item should be from (Use class
   *            field if any feed)
   * @param newdeadline the time in milliseconds since January 1, 1970 UTC the
   *            item should parsed BEFORE. (Use class fields if no deadline)
   * @param newread Should the item be read, unread or both. Use class fields.
   */
  public ItemFilter(final long newdeadline) {
    deadline = newdeadline;
  }

  /**
   * Checks to see if the given item is a match. Returns <code>TRUE</code> if
   * feedid is set and it matches the candidate items feedid. If the deadline is
   * set it checks that the candidate item is created BEFORE the deadline (in
   * millisecs since 1/1 1970).
   * @param candidate the candidate item as a byte[]
   * @return <code>TRUE</code> if the given candidate item is a match.
   */
  public boolean matches(final byte[] candidate) {
    try {
      boolean ismatch = true;
      bin = new ByteArrayInputStream(candidate);
      din = new DataInputStream(bin);
      din.readInt();
      final long fDeadLine = din.readLong();
      // Check deadline
      if ((deadline > ItemFilter.NO_DEADLINE) && ismatch) {
        ismatch = (fDeadLine < deadline);
      }
      // Check if read or not
      din.close();
      bin.close();
      return ismatch;
    }
    catch (final Exception e) {
      return false;
    }
  }

}
