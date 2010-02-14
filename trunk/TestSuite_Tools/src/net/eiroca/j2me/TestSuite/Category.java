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
package net.eiroca.j2me.TestSuite;

import java.util.StringTokenizer;
import java.util.Vector;

/**
 * The Class Category.
 */
public class Category implements Comparable<Category> {

  /** The categories. */
  public Vector<String> categories = new Vector<String>();
  
  /** The index. */
  public Integer index;

  /**
   * Instantiates a new category.
   * 
   * @param categoryName the category name
   * @param sep the sep
   */
  public Category(final String categoryName, final String sep) {
    parse(categoryName, sep);
  }

  /**
   * Split the string with a category in its elements.
   * 
   * @param categoryName the category name
   * @param sep the sep
   */
  public void parse(final String categoryName, final String sep) {
    categories.clear();
    index = null;
    String levNam;
    Integer idx = null;
    final StringTokenizer st = new StringTokenizer(categoryName, sep);
    while (st.hasMoreTokens()) {
      if (idx != null) {
        categories.add(idx.toString());
        idx = null;
      }
      levNam = st.nextToken().trim();
      try {
        final Integer vl = Integer.valueOf(levNam);
        idx = vl.intValue();
      }
      catch (final NumberFormatException e) {
        idx = null;
        categories.add(levNam);
      }
    }
    index = idx;
  }

  /**
   * Gets the category name.
   * 
   * @return the category name
   */
  public String getCategoryName() {
    return Category.getCategoryName(categories, "\t", categories.size());
  }

  /**
   * Gets the category name.
   * 
   * @param list the list
   * @param sep the sep
   * @param lev the lev
   * 
   * @return the category name
   */
  public static String getCategoryName(final Vector<String> list, final String sep, final int lev) {
    final StringBuffer buf = new StringBuffer(80);
    String nam;
    for (int i = 0; i < lev; i++) {
      if (i < list.size()) {
        nam = list.elementAt(i);
      }
      else {
        nam = "";
      }
      if (buf.length() > 0) {
        buf.append(sep);
      }
      buf.append(nam);
    }
    return buf.toString();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    final StringBuffer buf = new StringBuffer(80);
    buf.append(getCategoryName());
    if (index != null) {
      buf.append('[').append(index).append(']');
    }
    return buf.toString();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return categories.hashCode();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof Category) {
      return categories.equals(((Category) obj).categories);
    }
    else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(final Category o) {
    final int res = categories.toString().compareTo(o.categories.toString());
    // System.out.println(cats.toString()+"?"+o.toString()+"="+res);
    return res;
  }

}
