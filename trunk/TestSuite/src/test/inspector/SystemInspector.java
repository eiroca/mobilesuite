/** GPL >= 3.0
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
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
package test.inspector;

import java.util.TimeZone;
import test.AbstractProcessor;

/**
 * The Class SystemInspector.
 */
public class SystemInspector extends AbstractProcessor {

  /** The Constant PREFIX. */
  public static final String PREFIX = "S.";
  
  /** The Constant CATEGORY. */
  public static final String CATEGORY = "System";

  /** The Constant RES_ID1. */
  public static final String RES_ID1 = "M.T";
  
  /** The Constant RES_ID2. */
  public static final String RES_ID2 = "M.F";
  
  /** The Constant RES_ID3. */
  public static final String RES_ID3 = "T";
  
  /** The Constant RES_ID4x. */
  public static final String RES_ID4x = "T.A.";

  /**
   * Instantiates a new system inspector.
   */
  public SystemInspector() {
    super(SystemInspector.CATEGORY, SystemInspector.PREFIX);
  }

  /* (non-Javadoc)
   * @see test.AbstractProcessor#execute()
   */
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
