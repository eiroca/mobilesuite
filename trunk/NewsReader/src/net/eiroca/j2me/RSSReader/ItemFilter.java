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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import javax.microedition.rms.RecordFilter;

/**
 * A class for filtering items. Items can filtered out depending on whether they are read/unread and/or parsed before a deadline and/or from a certain feed.
 */
public class ItemFilter implements RecordFilter {

  /** The AN y_ feedid. */
  public static int ANY_FEEDID = -1;

  /** The N o_ deadline. */
  public static int NO_DEADLINE = 0;

  /** The REA d_ an d_ unread. */
  public static int READ_AND_UNREAD = 0;

  /** The READ. */
  public static int READ = 1;

  /** The UNREAD. */
  public static int UNREAD = 2;

  /** The deadline. */
  private final long deadline;

  /** The bin. */
  private ByteArrayInputStream bin;

  /** The din. */
  private DataInputStream din;

  /**
   * Constructor for the class.
   * 
   * @param newdeadline the time in milliseconds since January 1, 1970 UTC the item should parsed BEFORE. (Use class fields if no deadline)
   */
  public ItemFilter(final long newdeadline) {
    deadline = newdeadline;
  }

  /**
   * Checks to see if the given item is a match. Returns <code>TRUE</code> if feedid is set and it matches the candidate items feedid. If the deadline is set it checks that the candidate item is
   * created BEFORE the deadline (in millisecs since 1/1 1970).
   * 
   * @param candidate the candidate item as a byte[]
   * @return if the given candidate item is a match.
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
