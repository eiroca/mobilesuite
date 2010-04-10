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

import net.eiroca.j2me.app.BaseApp;
import test.AbstractProcessor;

/**
 * The Class MIDletInspector.
 */
public class MIDletInspector extends AbstractProcessor {

  /** The Constant CATEGORY. */
  public static final String CATEGORY = "Properties";
  
  /** The Constant PREFIX. */
  public static final String PREFIX = null;

  /**
   * Instantiates a new mI dlet inspector.
   */
  public MIDletInspector() {
    super(MIDletInspector.CATEGORY, MIDletInspector.PREFIX);
  }

  /* (non-Javadoc)
   * @see test.AbstractProcessor#execute()
   */
  public void execute() {
    final String ver = BaseApp.midlet.readAppProperty("MIDlet-Version", "1.0.0");
    addResult("TestSuite", ver);
  }

}
