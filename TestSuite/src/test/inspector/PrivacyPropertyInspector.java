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

import test.AbstractInspector;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.Pair;

public class PrivacyPropertyInspector extends AbstractInspector {

  public static final String CATEGORY = "Privacy Properties";
  public static final String PROP_DATA = "/data_ppp.txt";

  Pair[] test;

  public PrivacyPropertyInspector() {
    super(CATEGORY);
    test = Application.readPairs(PROP_DATA, '=');
  }

  final private void testProp(Pair p) {
    Object val = Application.readProperty(p.value.toString(), null);
    addResult(p.name, (val != null ? Boolean.TRUE : null));
  }

  public void run() {
    if (test != null) {
      for (int i = 0; i < test.length; i++) {
        testProp(test[i]);
      }
    }
  }

}
