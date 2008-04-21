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

  public static final String PREFIX = "S.";
  public static final String CATEGORY = "System";

  public static final String RES_ID1 = "M.T";
  public static final String RES_ID2 = "M.F";
  public static final String RES_ID3 = "T";
  public static final String RES_ID4x = "T.A.";

  public SystemInspector() {
    super(SystemInspector.CATEGORY, SystemInspector.PREFIX);
  }

  public void execute() {
    // Memory
    addResult(SystemInspector.RES_ID1, Long.toString(Runtime.getRuntime().totalMemory()));
    Runtime.getRuntime().gc();
    addResult(SystemInspector.RES_ID2, Long.toString(Runtime.getRuntime().freeMemory()));
    // Time Zone
    final TimeZone tz = TimeZone.getDefault();
    addResult(SystemInspector.RES_ID3, tz.getID());
    final String[] timeZoneIDs = java.util.TimeZone.getAvailableIDs();
    for (int i = 0; i < timeZoneIDs.length; i++) {
      addResult(SystemInspector.RES_ID4x + i, timeZoneIDs[i]);
    }
  }

}
