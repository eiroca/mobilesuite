/** GPL >= 3.0
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 * Copyright (C) 2002 Eugene Morozov
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
package net.eiroca.j2me.sm.util;

/**
 * The <code>StoreObserver</code> interface defines the way for the store observers to be notified of any of change in the store content (objects added/removed).
 */
public interface StoreObserver {

  /** The Constant DEL. */
  public static final int DEL = 2;
  
  /** The Constant ADD. */
  public static final int ADD = 1;

  /**
   * This method is called when information about an Store which was previously requested using an asynchronous interface becomes available.
   * 
   * @param action the action
   * @param obj the obj
   * @param store the store
   */
  public void actionDone(int action, Object obj, Store store);

}
