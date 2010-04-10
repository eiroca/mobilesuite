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
package test.benchmark;

import test.AbstractProcessor;

/**
 * The Class SuiteAbstract.
 */
public class SuiteAbstract extends AbstractProcessor implements Runnable {

  /** The benchmark. */
  protected BenchmarkAbstract[] benchmark;
  
  /** The finished. */
  public boolean finished = false;

  /**
   * Instantiates a new suite abstract.
   * 
   * @param cat the cat
   * @param prefix the prefix
   */
  public SuiteAbstract(final String cat, final String prefix) {
    super(cat, prefix);
  }

  /* (non-Javadoc)
   * @see test.AbstractProcessor#execute()
   */
  public void execute() {
    finished = false;
    new Thread(this).start();
  }

  /* (non-Javadoc)
   * @see java.lang.Runnable#run()
   */
  public void run() {
    for (int i = 0; i < benchmark.length; i++) {
      benchmark[i].execute();
    }
    finished = true;
  }

}
