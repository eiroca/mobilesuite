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
package test.inspector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import net.eiroca.j2me.app.BaseApp;
import test.AbstractProcessor;

/**
 * The Class CanvasInspector.
 */
public class CanvasInspector extends AbstractProcessor {

  /** The Constant PREFIX. */
  public static final String PREFIX = "C.";
  
  /** The Constant CATEGORY. */
  public static final String CATEGORY = "Canvas";

  /** The Constant RES_ID01. */
  public static final String RES_ID01 = "S.W";
  
  /** The Constant RES_ID02. */
  public static final String RES_ID02 = "S.H";
  
  /** The Constant RES_ID03. */
  public static final String RES_ID03 = "S.FW";
  
  /** The Constant RES_ID04. */
  public static final String RES_ID04 = "S.FH";
  
  /** The Constant RES_ID05. */
  public static final String RES_ID05 = "S.B";
  
  /** The Constant RES_ID06. */
  public static final String RES_ID06 = "S.CD";
  
  /** The Constant RES_ID07. */
  public static final String RES_ID07 = "S.IC";
  
  /** The Constant RES_ID08. */
  public static final String RES_ID08 = "S.AL";
  
  /** The Constant RES_ID09. */
  public static final String RES_ID09 = "F.D.H";
  
  /** The Constant RES_ID10. */
  public static final String RES_ID10 = "F.S.H";
  
  /** The Constant RES_ID11. */
  public static final String RES_ID11 = "F.S.B.H";
  
  /** The Constant RES_ID12. */
  public static final String RES_ID12 = "F.M.H";
  
  /** The Constant RES_ID13. */
  public static final String RES_ID13 = "F.M.B.H";
  
  /** The Constant RES_ID14. */
  public static final String RES_ID14 = "F.L.H";
  
  /** The Constant RES_ID15. */
  public static final String RES_ID15 = "F.L.B.H";
  
  /** The Constant RES_ID16. */
  public static final String RES_ID16 = "H.P";
  
  /** The Constant RES_ID17. */
  public static final String RES_ID17 = "H.M";
  
  /** The Constant RES_ID18. */
  public static final String RES_ID18 = "H.H";
  
  /** The Constant RES_ID19. */
  public static final String RES_ID19 = "K.GAME.A";
  
  /** The Constant RES_ID20. */
  public static final String RES_ID20 = "K.GAME.B";
  
  /** The Constant RES_ID21. */
  public static final String RES_ID21 = "K.GAME.C";
  
  /** The Constant RES_ID22. */
  public static final String RES_ID22 = "K.GAME.D";
  
  /** The Constant RES_ID23. */
  public static final String RES_ID23 = "K.UP";
  
  /** The Constant RES_ID24. */
  public static final String RES_ID24 = "K.DOWN";
  
  /** The Constant RES_ID25. */
  public static final String RES_ID25 = "K.FIRE";
  
  /** The Constant RES_ID26. */
  public static final String RES_ID26 = "K.LEFT";
  
  /** The Constant RES_ID27. */
  public static final String RES_ID27 = "K.RIGHT";

  /** The canvas. */
  private Canvas canvas;
  
  /** The canvas full. */
  private Canvas canvasFull;

  /**
   * Instantiates a new canvas inspector.
   */
  public CanvasInspector() {
    super(CanvasInspector.CATEGORY, CanvasInspector.PREFIX);
  }

  /**
   * Test key.
   * 
   * @param desc the desc
   * @param key the key
   */
  final private void testKey(final String desc, final int key) {
    addResult(desc, canvas.getKeyName(canvas.getKeyCode(key)));
  }

  /**
   * Test int.
   * 
   * @param desc the desc
   * @param x the x
   */
  final private void testInt(final String desc, final int x) {
    addResult(desc, new Integer(x));
  }

  /**
   * Test bool.
   * 
   * @param desc the desc
   * @param val the val
   */
  final private void testBool(final String desc, final boolean val) {
    addResult(desc, (val ? new Boolean(true) : new Boolean(false)));
  }

  /**
   * Test font.
   * 
   * @param desc the desc
   * @param f the f
   */
  final private void testFont(final String desc, final Font f) {
    addResult(desc, new Integer(f.getHeight()));
  }

  /* (non-Javadoc)
   * @see test.AbstractProcessor#execute()
   */
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
