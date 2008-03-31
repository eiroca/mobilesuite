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

import javax.bluetooth.LocalDevice;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.app.Pair;
import test.AbstractInspector;

public class LocalDeviceInspector extends AbstractInspector {

  public static final String CATEGORY = "LocalDevice";
  public static final String PROP_DATA = "/data_bluetooth.txt";

  Pair[] test;

  public LocalDeviceInspector() {
    super(LocalDeviceInspector.CATEGORY);
    test = BaseApp.readPairs(LocalDeviceInspector.PROP_DATA, '=');
  }

  final private void testProp(final Pair p) {
    String val;
    try {
      val = LocalDevice.getProperty(p.value.toString());
    }
    catch (final Exception e) {
      val = null;
    }
    addResult(p.name, val);
  }

  public void run() {
    if (BaseApp.isClass("javax.bluetooth.LocalDevice")) {
      if (test != null) {
        for (int i = 0; i < test.length; i++) {
          testProp(test[i]);
        }
      }
    }
  }

}
