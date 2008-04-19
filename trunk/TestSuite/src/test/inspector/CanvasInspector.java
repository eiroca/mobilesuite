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

  public static final String PREFIX = "C.";
  public static final String CATEGORY = "Canvas";

  private Canvas canvas;
  private Canvas canvasFull;

  public CanvasInspector() {
    super(CanvasInspector.CATEGORY, CanvasInspector.PREFIX);
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
    testInt("screen.width", canvas.getWidth());
    testInt("screen.height", canvas.getHeight());
    testInt("screen.fullwidth", canvasFull.getWidth());
    testInt("screen.fullheight", canvasFull.getHeight());
    testBool("screen.buffered", canvas.isDoubleBuffered());
    testInt("screen.colordepth", d.numColors());
    testBool("screen.iscolor", d.isColor());
    testInt("screen.alphalevels", d.numAlphaLevels());
    testFont("font.default.height", Font.getDefaultFont());
    testFont("font.small.height", Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
    testFont("font.small.bold.height", Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
    testFont("font.medium.height", Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
    testFont("font.medium.bold.height", Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
    testFont("font.large.height", Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
    testFont("font.large.bold.height", Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE));
    testBool("has.pointer", canvas.hasPointerEvents());
    testBool("has.motion", canvas.hasPointerMotionEvents());
    testBool("has.key-held", canvas.hasRepeatEvents());
    testKey("key.GAME_A", Canvas.GAME_A);
    testKey("key.GAME_B", Canvas.GAME_B);
    testKey("key.GAME_C", Canvas.GAME_C);
    testKey("key.GAME_D", Canvas.GAME_D);
    testKey("key.UP", Canvas.UP);
    testKey("key.DOWN", Canvas.DOWN);
    testKey("key.FIRE", Canvas.FIRE);
    testKey("key.LEFT", Canvas.LEFT);
    testKey("key.RIGHT", Canvas.RIGHT);
  }

}
