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

import java.util.Enumeration;
import java.util.Hashtable;
import net.eiroca.j2me.app.BaseApp;
import test.AbstractProcessor;

/**
 * The Class APIsInspector.
 */
public class APIsInspector extends AbstractProcessor {

  /** The Constant CATEGORY. */
  public static final String CATEGORY = "APIs";
  
  /** The Constant PREFIX. */
  public static final String PREFIX = "A.";
  
  /** The Constant PROP_DATA. */
  public static final String PROP_DATA = "data_class.txt";

  /** The test. */
  Hashtable test;

  /**
   * Instantiates a new aP is inspector.
   */
  public APIsInspector() {
    super(APIsInspector.CATEGORY, APIsInspector.PREFIX);
    test = BaseApp.readMap(APIsInspector.PROP_DATA, '=');
  }

  /**
   * Test class.
   * 
   * @param name the name
   * @param clazz the clazz
   */
  final private void testClass(final String name, final String clazz) {
    addResult(name, BaseApp.isClass(clazz) ? new Boolean(true) : new Boolean(false));
  }

  /* (non-Javadoc)
   * @see test.AbstractProcessor#execute()
   */
  public void execute() {
    if (test != null) {
      for (final Enumeration e = test.keys(); e.hasMoreElements();) {
        final String k = (String) e.nextElement();
        String v = (String) test.get(k);
        if (v == null) {
          v = k;
        }
        if (k != null) {
          testClass(v, k);
        }
      }
    }
  }

}
