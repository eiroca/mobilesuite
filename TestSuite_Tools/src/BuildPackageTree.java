/** GPL >= 3.0 + MIT
 * Based upon Mobile Device Tools written by Andrew Scott
 * 
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
 * 
 * Copyright (C) 2004 Andrew Scott
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ListIterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Utility that outputs a Java class file containing an array of Strings initialized to a compressed representation of the full class names of the classes in the JAR-files specified on the command
 * line. Used as part of the build process of the ClassBrowser MIDlet.
 */
public class BuildPackageTree {

  /** Array of the different classes to be written to the output file. */
  ArrayList<String> classNamesList;

  /**
   * Constructs a BuildPackageTree object from the given array of file-names. The files are jar or zip files containing the API classes for a J2ME virtual machine implementation. Each of these class
   * names is added to the classNamesList field.
   * 
   * @param fileNames list of file-names of jar/zip files of classes
   */
  public BuildPackageTree(final String fileNames[]) {
    ZipFile classesJar;
    ZipEntry possibleClass;
    Enumeration<? extends ZipEntry> e;
    int i;
    classNamesList = new ArrayList<String>();
    for (i = 0; i < fileNames.length; i++) {
      try {
        classesJar = new ZipFile(fileNames[i]);
        e = classesJar.entries();
        while (e.hasMoreElements()) {
          possibleClass = e.nextElement();
          if (isClass(possibleClass)) {
            classNamesList.add(getClassName(possibleClass));
          }
        }
        classesJar.close();
      }
      catch (final IOException ioe) {
        System.err.println("BuildPackageTree: Problem with file " + fileNames[i] + ": " + ioe.toString());
      }
    }
  }

  /**
   * Returns a string containing the start of the Java file that's to be generated. Essentially some comments and the start of a class and field definition.
   * 
   * @param sb the sb
   * 
   * @return the Java source prefix
   */
  private void emitPrefix(final StringBuffer sb) {
  }

  /**
   * Returns a string containing the end of the file begun with getPrefix().
   * 
   * @param sb the sb
   * 
   * @return the Java source suffix
   */
  private void emitSuffix(final StringBuffer sb) {
  }

  /**
   * Writes a Java source file to stdout for the PackageTree class that contains a string array consisting of the classes in the classNamesList field.
   * 
   * @param sb the sb
   * 
   * @see mobiledevtools.cbrowser.PackageTree
   */
  public void outputJava(final StringBuffer sb) {
    if (classNamesList.size() > 0) {
      ListIterator<String> it;
      String prevName, className, abbrev;
      emitPrefix(sb);
      Collections.sort(classNamesList);
      it = classNamesList.listIterator();
      prevName = null;
      while (it.hasNext()) {
        className = it.next();
        abbrev = getAbbreviation(prevName, className);
        if (abbrev != null) {
          // skip duplicates
          sb.append(abbrev);
          sb.append('\n');
          prevName = className;
        }
      }
      emitSuffix(sb);
    }
  }

  /**
   * Tests the entry in a zip/jar file for whether it represents a class/interface or not. Returns true if it does.
   * 
   * @param currEntry the file entry in a zip/jar file
   * 
   * @return true if the entry represents a class
   */
  private boolean isClass(final ZipEntry currEntry) {
    if (currEntry.isDirectory()) { return false; }
    final String name = currEntry.getName();
    return (name.endsWith(".class")) && (name.indexOf('$') <= 0);
  }

  /**
   * Turns the full path name of an entry in a zip/jar file into a fully expanded class name.
   * 
   * @param currEntry the file entry in a zip/jar file
   * 
   * @return a String containing the entry as a class name
   */
  private String getClassName(final ZipEntry currEntry) {
    String name;
    StringBuffer result;
    char ch;
    int pos, len;
    name = currEntry.getName();
    len = name.length();
    result = new StringBuffer(len + 10);
    for (pos = 0; pos < len; pos++) {
      ch = name.charAt(pos);
      if (ch == '.') {
        break; // we've got to the ".class" suffix, so stop
      }
      else if (ch == '/') {
        result.append('.'); // turn directories into packages
      }
      else {
        result.append(ch);
      }
    }
    return result.toString();
  }

  /**
   * Given two fully expanded class names, returns a kind-of difference between them. The second class name has any package elements removed, so that the name appears to start with a sequence of dots.
   * 
   * @param prev the previous full, expanded class name of the previous class
   * @param curr the full, expanded class name of the class to output now
   * 
   * @return an abbreviated version of the class name
   */
  private String getAbbreviation(final String prev, final String curr) {
    int firstDiffPos, prevDotPos, len;
    StringBuffer abbreviation;
    char ch;
    if (prev == null) { return curr; }
    len = prev.length();
    if (curr.length() < len) {
      len = curr.length();
    }
    abbreviation = new StringBuffer();
    prevDotPos = -1;
    for (firstDiffPos = 0; firstDiffPos < len; firstDiffPos++) {
      ch = prev.charAt(firstDiffPos);
      if (curr.charAt(firstDiffPos) != ch) {
        break;
      }
      if (ch == '.') {
        abbreviation.append('.');
        prevDotPos = firstDiffPos;
      }
    }
    // if the strings are the same: it's a duplicate!
    if ((firstDiffPos == len) && (prev.length() == curr.length())) { return null; }
    abbreviation.append(curr.substring(prevDotPos + 1));
    return abbreviation.toString();
  }

  /**
   * Creates a new instance of the BuildPackageTree class for the files specified on the command line, and writes the resulting Java source file to stdout.
   * 
   * @param args the command line arguments
   */
  public static void main(final String args[]) {
    if (args.length < 1) {
      System.err.println("Use: java BuildPackageTree jarfile1 [jarfile2 ...]");
    }
    else {
      final BuildPackageTree bpt = new BuildPackageTree(args);
      final StringBuffer sb = new StringBuffer(1024);
      bpt.outputJava(sb);
      System.out.println(sb);
    }
  }

}
