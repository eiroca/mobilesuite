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

  public static final String RES_ID01 = "S.W";
  public static final String RES_ID02 = "S.H";
  public static final String RES_ID03 = "S.FW";
  public static final String RES_ID04 = "S.FH";
  public static final String RES_ID05 = "S.B";
  public static final String RES_ID06 = "S.CD";
  public static final String RES_ID07 = "S.IC";
  public static final String RES_ID08 = "S.AL";
  public static final String RES_ID09 = "F.D.H";
  public static final String RES_ID10 = "F.S.H";
  public static final String RES_ID11 = "F.S.B.H";
  public static final String RES_ID12 = "F.M.H";
  public static final String RES_ID13 = "F.M.B.H";
  public static final String RES_ID14 = "F.L.H";
  public static final String RES_ID15 = "F.L.B.H";
  public static final String RES_ID16 = "H.P";
  public static final String RES_ID17 = "H.M";
  public static final String RES_ID18 = "H.H";
  public static final String RES_ID19 = "K.GAME_A";
  public static final String RES_ID20 = "K.GAME_B";
  public static final String RES_ID21 = "K.GAME_C";
  public static final String RES_ID22 = "K.GAME_D";
  public static final String RES_ID23 = "K.UP";
  public static final String RES_ID24 = "K.DOWN";
  public static final String RES_ID25 = "K.FIRE";
  public static final String RES_ID26 = "K.LEFT";
  public static final String RES_ID27 = "K.RIGHT";

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
    testInt(CanvasInspector.RES_ID01, canvas.getWidth());
    testInt(CanvasInspector.RES_ID02, canvas.getHeight());
    testInt(CanvasInspector.RES_ID03, canvasFull.getWidth());
    testInt(CanvasInspector.RES_ID04, canvasFull.getHeight());
    testBool(CanvasInspector.RES_ID05, canvas.isDoubleBuffered());
    testInt(CanvasInspector.RES_ID06, d.numColors());
    testBool(CanvasInspector.RES_ID07, d.isColor());
    testInt(CanvasInspector.RES_ID08, d.numAlphaLevels());
    testFont(CanvasInspector.RES_ID09, Font.getDefaultFont());
    testFont(CanvasInspector.RES_ID10, Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
    testFont(CanvasInspector.RES_ID11, Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
    testFont(CanvasInspector.RES_ID12, Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
    testFont(CanvasInspector.RES_ID13, Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
    testFont(CanvasInspector.RES_ID14, Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE));
    testFont(CanvasInspector.RES_ID15, Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE));
    testBool(CanvasInspector.RES_ID16, canvas.hasPointerEvents());
    testBool(CanvasInspector.RES_ID17, canvas.hasPointerMotionEvents());
    testBool(CanvasInspector.RES_ID18, canvas.hasRepeatEvents());
    testKey(CanvasInspector.RES_ID19, Canvas.GAME_A);
    testKey(CanvasInspector.RES_ID20, Canvas.GAME_B);
    testKey(CanvasInspector.RES_ID21, Canvas.GAME_C);
    testKey(CanvasInspector.RES_ID22, Canvas.GAME_D);
    testKey(CanvasInspector.RES_ID23, Canvas.UP);
    testKey(CanvasInspector.RES_ID24, Canvas.DOWN);
    testKey(CanvasInspector.RES_ID25, Canvas.FIRE);
    testKey(CanvasInspector.RES_ID26, Canvas.LEFT);
    testKey(CanvasInspector.RES_ID27, Canvas.RIGHT);
  }

}
