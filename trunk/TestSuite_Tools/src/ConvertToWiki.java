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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import net.eiroca.j2me.TestSuite.Category;
import net.eiroca.j2me.TestSuite.Processor;
import net.eiroca.j2me.TestSuite.ProcessorMapping;
import net.eiroca.j2me.TestSuite.ProcessorResult;

/**
 * The Class ConvertToWiki.
 */
public class ConvertToWiki {

  /** The maps. */
  static HashMap<Category, Category> maps;

  /**
   * Convert a testsuite output to a wiki file
   * 
   * @param fin the testsuite output
   * @param fout the wiki (dokuwiki)
   * 
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static void convert(final File fin, final File fout) throws IOException {
    final Processor processor = new ProcessorResult();
    processor.readInput(new FileInputStream(fin));
    processor.transform(ConvertToWiki.maps);
    processor.writeOutput(new FileOutputStream(fout));
  }

  /**
   * The main method.
   * 
   * @param args the args
   */
  public static void main(final String[] args) {
    final ProcessorMapping mapping = new ProcessorMapping();
    File map = new File("./mapping.data");
    String base = "./";
    try {
      if (!map.exists()) {
        base = "./bin";
        map = new File("./bin/mapping.data");
      }
      if (!map.exists()) {
        System.err.println("Missing mapping.data");
        System.exit(1);
      }
      mapping.readInput(new FileInputStream(map));
    }
    catch (final IOException e1) {
      e1.printStackTrace();
    }
    ConvertToWiki.maps = mapping.getMapping();
    final File f = new File(base);
    File fin;
    File fout;
    final String[] files = f.list();
    for (final String p : files) {
      if (!p.endsWith(".txt")) {
        continue;
      }
      fin = new File(base + "/" + p);
      fout = new File(base + "/" + p.substring(0, p.length() - 3) + "wiki");
      if ((!fout.exists()) || (fout.lastModified() < fin.lastModified())) {
        try {
          System.out.println("->" + fin);
          ConvertToWiki.convert(fin, fout);
        }
        catch (final IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
