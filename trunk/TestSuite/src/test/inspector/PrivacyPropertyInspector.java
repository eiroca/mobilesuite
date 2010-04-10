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
 * The Class PrivacyPropertyInspector.
 */
public class PrivacyPropertyInspector extends AbstractProcessor {

  /** The Constant PREFIX. */
  public static final String PREFIX = "X.";
  
  /** The Constant CATEGORY. */
  public static final String CATEGORY = "Privacy Properties";
  
  /** The Constant PROP_DATA. */
  public static final String PROP_DATA = "data_ppp.txt";

  /** The test. */
  Hashtable test;

  /**
   * Instantiates a new privacy property inspector.
   */
  public PrivacyPropertyInspector() {
    super(PrivacyPropertyInspector.CATEGORY, PrivacyPropertyInspector.PREFIX);
    test = BaseApp.readMap(PrivacyPropertyInspector.PROP_DATA, '=');
  }

  /**
   * Test prop.
   * 
   * @param name the name
   * @param prop the prop
   */
  final private void testProp(final String name, final String prop) {
    final Object val = BaseApp.readProperty(prop, null);
    addResult(name, (val != null ? new Boolean(true) : null));
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
          testProp(v, k);
        }
      }
    }
  }

}
