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

import java.util.Random;

/**
 * The Class MathBenchmarkAbstract.
 */
abstract public class MathBenchmarkAbstract extends BenchmarkAbstract {

  /** The result. */
  public int result;

  /** The Constant NUMBER_OF_OPS. */
  protected final static int NUMBER_OF_OPS = 5000000;

  /** The array a. */
  protected final int arrayA[];
  
  /** The array b. */
  protected final int arrayB[];

  /** The static a. */
  protected static int staticA;
  
  /** The static b. */
  protected static int staticB;

  /** The instance a. */
  protected int instanceA;
  
  /** The instance b. */
  protected int instanceB;

  /** The random. */
  protected final Random random = new Random();

  /**
   * Instantiates a new math benchmark abstract.
   * 
   * @param suite the suite
   */
  public MathBenchmarkAbstract(final SuiteAbstract suite) {
    super(suite);
    do {
      instanceA = random.nextInt();
    }
    while (instanceA == 0);
    do {
      instanceB = random.nextInt();
    }
    while (instanceB == 0);
    do {
      MathBenchmarkAbstract.staticA = random.nextInt();
    }
    while (MathBenchmarkAbstract.staticA == 0);
    do {
      MathBenchmarkAbstract.staticB = random.nextInt();
    }
    while (MathBenchmarkAbstract.staticB == 0);
    arrayA = new int[100];
    arrayB = new int[100];
    final Random r = new Random();
    for (int i = 0; i < 100; i++) {
      do {
        arrayA[i] = r.nextInt();
      }
      while (arrayA[i] == 0);
      do {
        arrayB[i] = r.nextInt();
      }
      while (arrayB[i] == 0);
    }

  }

}
