/** MIT LICENSE
 * Based upon Mobile Device Tools written by Andrew Scott
 *
 * Copyright (C) 2004 Andrew Scott
 * Copyright (C) 2006-2008 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package classbrowser;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;

public class ClassBrowserHelper {

  /** The maximum number of elements in a package. */
  static int MAX_ELEMENTS = 32;

  /** The Image object with a "plus" symbol. */
  public static Image imPlus;
  /** The Image object with a "dash" symbol. */
  public static Image imDash;

  /**
   * Reset the classes List so that it displays the new package. The new package
   * path is read from sPackagePath.
   */
  public static void generateList(final List classesList, final String sPackagePath) {
    int iNumElements;
    int iClassIdx;
    int iDepth;
    String sLastElement;
    boolean fSearching;
    String rgElements[];
    String rgPackageElement[];
    int rgPackageIdx[];
    iNumElements = classesList.size();
    while (0 < iNumElements--) {
      classesList.delete(iNumElements); // deleting off the elements..
    }
    rgElements = ClassBrowserHelper.splitPackagePath(sPackagePath);
    rgPackageElement = new String[ClassBrowserHelper.MAX_ELEMENTS];
    rgPackageIdx = new int[ClassBrowserHelper.MAX_ELEMENTS];
    fSearching = true; // still looking for first relevant entry
    iDepth = 0; // index into rgElements array
    sLastElement = ""; // last element added to the list
    for (iClassIdx = 0; iClassIdx < PackageTree.Name.length;) {
      ClassBrowserHelper.expandName(PackageTree.Name[iClassIdx], rgPackageIdx, rgPackageElement);
      if (rgElements.length == 0) {
        sLastElement = ClassBrowserHelper.submitCandidate(classesList, rgPackageElement, iDepth, sLastElement);
      }
      else if (fSearching) {
        if (rgPackageElement[iDepth].equals(rgElements[iDepth])) {
          if (rgElements.length == iDepth + 1) {
            fSearching = false;
            sLastElement = ClassBrowserHelper.submitCandidate(classesList, rgPackageElement, iDepth + 1, sLastElement);
          }
          else {
            iDepth++;
            continue; // reprocess this entry at a deeper level
          }
        }
      }
      else {
        if (rgPackageElement[iDepth].equals(rgElements[iDepth])) {
          sLastElement = ClassBrowserHelper.submitCandidate(classesList, rgPackageElement, iDepth + 1, sLastElement);
        }
        else {
          break; // we've moved past any interesting candidates (they're
          // sorted)
        }
      }
      iClassIdx++;
    }
  }

  /**
   * Given a full package path, eg. java.io.Connection, split it into its
   * elements, eg. java, io, and Connection. A blank path has no elements.
   * @param sPath the full path as a string
   * @return an array of strings containing each element (with no dots)
   */
  private static String[] splitPackagePath(final String sPath) {
    int iCount, iPos, iNextPos, iIdx;
    String rgElement[];
    // Calculate how many elements in the path, ie. #dots + 1, or 0
    iCount = 0;
    if ((null != sPath) && (0 < sPath.length())) {
      iPos = 0;
      do {
        iCount++;
        iPos = sPath.indexOf('.', iPos) + 1; // note we add one to force a halt
      }
      while (0 != iPos);
    }
    // Allocate space to store the elements in an array
    rgElement = new String[iCount];
    // Copy each element into that array
    iPos = iNextPos = 0;
    for (iIdx = 0; iIdx < iCount; iIdx++) {
      if (-1 == (iNextPos = sPath.indexOf('.', iPos))) {
        iNextPos = sPath.length();
      }
      rgElement[iIdx] = sPath.substring(iPos, iNextPos);
      iPos = iNextPos + 1;
    }
    return rgElement;
  }

  /**
   * Go through the given string, and set the given int array to have the
   * positions of the dots in it, with the final position being one more than
   * the last character (ie. the length).
   * @param sEntry the string to be parsed, eg. ".lang.String"
   * @param rgIdx the array to store the dot positions
   * @return the number of elements in the string, eg. 3
   */
  private static int parseTreeEntry(final String sEntry, final int rgIdx[]) {
    int iElements = 0;
    if ((null != sEntry) && (null != rgIdx)) {
      int iIdx, iPos;
      iPos = -1;
      iIdx = 0;
      do {
        if (-1 != (iPos = sEntry.indexOf('.', iPos + 1))) {
          rgIdx[iIdx] = iPos;
        }
        else {
          rgIdx[iIdx] = sEntry.length();
        }
      }
      while ((++iIdx < rgIdx.length) && (-1 != iPos));
      iElements = iIdx;
    }
    return iElements;
  }

  /**
   * Update the package expansion array with any new elements that appear in the
   * given name from the package tree.
   * @param sName a package tree entry, eg. "..midlet.MIDlet"
   * @param rgPos an array to use as temporary storage
   * @param rgExpansion the array containing the name expansion
   */
  private static void expandName(final String sName, final int rgPos[], final String rgExpansion[]) {
    if (sName != null) {
      int i, iPos, iNumElements;
      iNumElements = ClassBrowserHelper.parseTreeEntry(sName, rgPos);
      iPos = -1;
      for (i = 0; i < iNumElements; i++) {
        if (1 < rgPos[i] - iPos) {
          rgExpansion[i] = sName.substring(iPos + 1, rgPos[i]);
        }
        iPos = rgPos[i];
      }
      if (i < rgExpansion.length) {
        rgExpansion[i] = ""; // mark the end of the expansion
      }
    }
  }

  /**
   * Turns an expanded string from expandName() into a proper string,
   * representing a fully expanded class name.
   * @param rgElements an array of package and class elements, terminated by a
   *            blank ("") string.
   * @return a string representation of the class
   */
  private static String getExpandedString(final String rgElements[]) {
    StringBuffer sb;

    sb = new StringBuffer();
    if (null != rgElements) {
      int iIdx = 0;

      while (true) {
        sb.append(rgElements[iIdx]);
        if ((++iIdx == rgElements.length) || (0 == rgElements[iIdx].length())) {
          break;
        }
        sb.append('.');
      }
    }
    return sb.toString();
  }

  /**
   * Checks to see if a given class/interface exists in this Java
   * implementation.
   * @param sName the full name of the class
   * @return true iff the class/interface exists
   */
  private static boolean isClass(final String sName) {
    boolean fFound = false;

    try {
      if (null != sName) {
        Class.forName(sName);
        fFound = true;
      }
    }
    catch (final ClassNotFoundException cnfe) {
      // do nothing
    }
    return fFound;
  }

  /**
   * Given a candidate line from the PackageTree, if the specified element is
   * worth adding, then add it to the class list lisClasses. Expands the
   * candidate to full class path and tests its presence in this JVM.
   * @param sCandidate the full line from the PackageTree
   * @param iDepth the depth we are looking at, 0 being the first element
   */
  private static String submitCandidate(final List classesList, final String rgElements[], final int iDepth, String sLast) {
    if ((rgElements != null) && (0 <= iDepth)) {
      Image im;
      String sClassName;
      if (!rgElements[iDepth].equals(sLast)) {
        sClassName = ClassBrowserHelper.getExpandedString(rgElements);
        if (ClassBrowserHelper.isClass(sClassName)) {
          sLast = rgElements[iDepth];
          if (Character.isLowerCase(sLast.charAt(0))) {
            im = ClassBrowserHelper.imPlus;
          }
          else {
            im = ClassBrowserHelper.imDash;
          }
          classesList.append(sLast, im);
        }
      }
    }
    return sLast;
  }

}
