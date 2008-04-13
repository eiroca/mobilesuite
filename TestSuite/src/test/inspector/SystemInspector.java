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
package test.inspector;

import java.util.TimeZone;
import test.AbstractProcessor;

public class SystemInspector extends AbstractProcessor {

  public static final String PREFIX = "system.";
  public static final String CATEGORY = "System";

  public SystemInspector() {
    super(SystemInspector.CATEGORY, SystemInspector.PREFIX);
  }

  public void execute() {
    // get the timezone id
    final TimeZone tz = TimeZone.getDefault();
    final String[] timeZoneIDs = java.util.TimeZone.getAvailableIDs();
    final StringBuffer timeZonesBuffer = new StringBuffer(64);
    for (int i = 0; i < timeZoneIDs.length; i++) {
      if (i > 0) {
        timeZonesBuffer.append(", ");
      }
      timeZonesBuffer.append(timeZoneIDs[i]);
    }
    Runtime.getRuntime().gc();
    addResult("mem.total", Long.toString(Runtime.getRuntime().totalMemory()));
    addResult("mem.free", Long.toString(Runtime.getRuntime().freeMemory()));
    addResult("timezone", tz.getID());
    addResult("timezone.available", timeZonesBuffer.toString());
  }

}
