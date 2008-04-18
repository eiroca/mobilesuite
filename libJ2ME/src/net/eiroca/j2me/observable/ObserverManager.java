/** GPL >= 2.0
 *
 * Copyright (C) M. Serhat Cinar, http://graviton.de
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
package net.eiroca.j2me.observable;

import java.util.Vector;

public class ObserverManager {

  private final Vector observers;

  public ObserverManager() {
    observers = new Vector();
  }

  public void addObserver(final Observer observer) {
    if (!observers.contains(observer)) {
      observers.addElement(observer);
    }
  }

  public void removeObserver(final Observer observer) {
    if (observers.contains(observer)) {
      observers.removeElement(observer);
    }
  }

  public void notifyObservers(final Observable observable) {
    for (int i = 0; i < observers.size(); i++) {
      ((Observer) observers.elementAt(i)).changed(observable);
    }
  }

}
