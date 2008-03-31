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

public class APIsInspector extends AbstractInspector {

  public static final String CATEGORY = "APIs";
  public static final String PROP_DATA = "/data_class.txt";

  Pair[] test;

  public APIsInspector() {
    super(CATEGORY);
    test = Application.readPairs(PROP_DATA, '=');
  }

  final private void testClass(Pair p) {
    addResult(p.name, Application.isClass(p.value.toString()) ? Boolean.TRUE : Boolean.FALSE);
  }

  public void run() {
    if (test != null) {
      for (int i = 0; i < test.length; i++) {
        testClass(test[i]);
      }
    }
  }

}
