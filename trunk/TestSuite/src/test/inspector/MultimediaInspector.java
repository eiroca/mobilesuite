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

import javax.microedition.media.Manager;
import test.AbstractProcessor;

public class MultimediaInspector extends AbstractProcessor {

  public static final String PREFIX = "multimedia.";
  public static final String CATEGORY = "Multimedia";

  public MultimediaInspector() {
    super(MultimediaInspector.CATEGORY, MultimediaInspector.PREFIX);
  }

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
