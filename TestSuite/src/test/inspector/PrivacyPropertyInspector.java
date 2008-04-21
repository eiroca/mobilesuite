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

import java.util.Enumeration;
import java.util.Hashtable;
import net.eiroca.j2me.app.BaseApp;
import test.AbstractProcessor;

public class PrivacyPropertyInspector extends AbstractProcessor {

  public static final String PREFIX = "X.";
  public static final String CATEGORY = "Privacy Properties";
  public static final String PROP_DATA = "data_ppp.txt";

  Hashtable test;

  public PrivacyPropertyInspector() {
    super(PrivacyPropertyInspector.CATEGORY, PrivacyPropertyInspector.PREFIX);
    test = BaseApp.readMap(PrivacyPropertyInspector.PROP_DATA, '=');
  }

  final private void testProp(final String name, final String prop) {
    final Object val = BaseApp.readProperty(prop, null);
    addResult(name, (val != null ? Boolean.TRUE : null));
  }

  public void execute() {
    if (test != null) {
      for (final Enumeration e = test.keys(); e.hasMoreElements();) {
        final String k = (String) e.nextElement();
        String v = (String) test.get(k);
        if (v == null) {
          v = k;
        }
        if (k != null) {
          testProp(v, k);
        }
      }
    }
  }

}
