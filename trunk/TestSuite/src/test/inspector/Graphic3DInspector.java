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
import javax.microedition.m3g.Graphics3D;
import net.eiroca.j2me.app.BaseApp;
import test.AbstractProcessor;

/**
 * The Class Graphic3DInspector.
 */
public class Graphic3DInspector extends AbstractProcessor {

  /** The Constant PREFIX. */
  public static final String PREFIX = "G.";
  
  /** The Constant CATEGORY. */
  public static final String CATEGORY = "Graphic3D";

  /**
   * Instantiates a new graphic3 d inspector.
   */
  public Graphic3DInspector() {
    super(Graphic3DInspector.CATEGORY, Graphic3DInspector.PREFIX);
  }

  /* (non-Javadoc)
   * @see test.AbstractProcessor#execute()
   */
  public void execute() {
    if (BaseApp.isClass("javax.microedition.m3g.Graphics3D")) {
      final Hashtable props = Graphics3D.getProperties();
      Object key;
      Object val;
      for (final Enumeration e = props.keys(); e.hasMoreElements();) {
        key = e.nextElement();
        val = props.get(key);
        addResult("3." + key, val);
      }

    }
  }

}
