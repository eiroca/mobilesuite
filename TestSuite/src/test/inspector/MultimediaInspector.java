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

import javax.microedition.media.Manager;
import test.AbstractProcessor;

/**
 * The Class MultimediaInspector.
 */
public class MultimediaInspector extends AbstractProcessor {

  /** The Constant PREFIX. */
  public static final String PREFIX = "M.";
  
  /** The Constant CATEGORY. */
  public static final String CATEGORY = "Multimedia";

  /**
   * Instantiates a new multimedia inspector.
   */
  public MultimediaInspector() {
    super(MultimediaInspector.CATEGORY, MultimediaInspector.PREFIX);
  }

  /* (non-Javadoc)
   * @see test.AbstractProcessor#execute()
   */
  public void execute() {
    final String[] supportedProtocols = Manager.getSupportedProtocols(null);
    if (supportedProtocols != null) {
      for (int i = 0; i < supportedProtocols.length; i++) {
        final String protocol = supportedProtocols[i];
        final String[] supportedContentTypes = Manager.getSupportedContentTypes(protocol);
        for (int j = 0; j < supportedContentTypes.length; j++) {
          addResult(protocol + "." + j, supportedContentTypes[j]);
        }
      }
    }
  }

}
