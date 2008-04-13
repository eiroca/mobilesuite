/**
 * Copyright (C) 2006-2008 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the
 *      Free Software Foundation, Inc.,
 *      59 Temple Place, Suite 330,
 *      Boston, MA 02111-1307
 *      USA
 *
 * Copyright (c) 2002,2003, Stefan Haustein, Oberhausen, Rhld., Germany
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The  above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 *
 */
package net.eiroca.j2me.util;

import java.io.IOException;
import java.io.InputStream;

public class BoundInputStream extends InputStream {

  int remaining;
  InputStream is;

  public BoundInputStream(final InputStream is, final int length) {
    this.is = is;
    remaining = length;
  }

  public int available() throws IOException {
    final int avail = is.available();
    return avail < remaining ? avail : remaining;
  }

  public int read() throws IOException {
    if (remaining <= 0) { return -1; }
    remaining--;
    return is.read();
  }

  public int read(final byte[] data, final int start, int max) throws IOException {
    if (max > remaining) {
      max = remaining;
    }
    final int actual = is.read(data, start, max);
    if (actual > 0) {
      remaining -= actual;
    }
    return actual;
  }

  public void close() {
    try {
      is.close();
    }
    catch (final IOException ignored) {
      //
    }
  }

}
