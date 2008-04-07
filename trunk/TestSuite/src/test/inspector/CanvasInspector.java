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
package test.inspector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import net.eiroca.j2me.app.BaseApp;
import test.AbstractProcessor;

public class CanvasInspector extends AbstractProcessor {

  public static final String CATEGORY = "Canvas";

  private Canvas canvas;
  private Canvas canvasFull;

  public CanvasInspector() {
    super(CanvasInspector.CATEGORY);
  }

  final private void testKey(final String desc, final int key) {
    addResult(desc, canvas.getKeyName(canvas.getKeyCode(key)));
  }

  final private void testInt(final String desc, final int x) {
    addResult(desc, new Integer(x));
  }

  final private void testBool(final String desc, final boolean val) {
    addResult(desc, (val ? Boolean.TRUE : Boolean.FALSE));
  }

  final private void testFont(final String desc, final Font f) {
    addResult(desc, new Integer(f.getHeight()));
  }

  public void execute() {
    canvas = new TestCanvas(false);
    canvasFull = new TestCanvas(true);
    final Displayable cur = BaseApp.getDisplay();
    BaseApp.setDisplay(canvas);
    try {
      Thread.sleep(10);
    }
    catch (final InterruptedException e) {
      // ignore
    }
    BaseApp.setDisplay(canvasFull);
    try {
      Thread.sleep(10);
    }
    catch (final InterruptedException e) {
      // ignore
    }
    BaseApp.setDisplay(cur);
    final Display d = BaseApp.display;
    testInt("Screen (normal) width", canvas.getWidth());
    testInt("Screen (normal) height", canvas.getHeight());
    testInt("Screen (full) width", canvasFull.getWidth());
    testInt("Screen (full) height", canvasFull.getHeight());
    testInt("Color Depth", d.numColors());
    testBool("Is grayscale", d.isColor());
    testInt("Alpha Levels", d.numAlphaLevels());
    testBool("Screen buffered", canvas.isDoubleBuffered());
    testFont("Font Default Height", Font.getDefaultFont());
    testFont("Font Small Height", Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
    testFont("Font Small Bold Height", Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
    testFont("Font Medium Height", Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
    testFont("Font Medium Bold Height", Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
    testFont("Font Large Height", Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
    testFont("Font Large Bold Height", Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE));
    testBool("Has pointer events", canvas.hasPointerEvents());
    testBool("Has motion events", canvas.hasPointerMotionEvents());
    testBool("Has key-held events", canvas.hasRepeatEvents());
    testKey("Key GAME_A", Canvas.GAME_A);
    testKey("Key GAME_B", Canvas.GAME_B);
    testKey("Key GAME_C", Canvas.GAME_C);
    testKey("Key GAME_D", Canvas.GAME_D);
    testKey("Key UP", Canvas.UP);
    testKey("Key DOWN", Canvas.DOWN);
    testKey("Key FIRE", Canvas.FIRE);
    testKey("Key LEFT", Canvas.LEFT);
    testKey("Key RIGTH", Canvas.RIGHT);
  }

}
