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
package test.benchmark;

import java.util.Random;

abstract public class MathBenchmarkAbstract extends BenchmarkAbstract {

  public int result;

  protected final static int NUMBER_OF_OPS = 5000000;

  protected final int arrayA[];
  protected final int arrayB[];

  protected static int staticA;
  protected static int staticB;

  protected int instanceA;
  protected int instanceB;

  protected final Random random = new Random();

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
