/** GPL >= 2.0
 * Based upon J2ME Minesweeper.
 * Copyright (C) M. Jumari
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
import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.StringItem;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.game.GameApp;
import net.eiroca.j2me.game.GameScreen;
import net.eiroca.j2me.game.GameUISettings;
import net.eiroca.j2me.game.Score;
import net.eiroca.j2me.minesweeper.CustomLevelForm;
import net.eiroca.j2me.minesweeper.MineSweeperScreen;
import net.eiroca.j2me.minesweeper.game.MineSweeperGame;

public class MineSweeper extends GameApp {

  public static String RES_ERROR = "error.png";
  public static String RES_SMILE = "smile.png";
  public static String RES_SMILEOK = "smile_ok.png";
  public static String RES_SMILEKO = "smile_ko.png";
  public static String RES_ICONS = "icons.png";
  public static String RES_BOMB = "bomb.wav";
  public static String RES_CLOCK = "waktu.wav";

  public static final int MSG_MENU_OPTIONS_LEVEL = GameApp.MSG_USERDEF + 0;

  public static final int MSG_TEXT_LEVEL_01 = GameApp.MSG_USERDEF + 1;
  public static final int MSG_TEXT_LEVEL_02 = GameApp.MSG_USERDEF + 2;
  public static final int MSG_TEXT_LEVEL_03 = GameApp.MSG_USERDEF + 3;
  public static final int MSG_TEXT_LEVEL_04 = GameApp.MSG_USERDEF + 4;

  public static final int MSG_NAME = GameApp.MSG_USERDEF + 5;

  public static final int MSG_CUSTOMLEVEL = GameApp.MSG_USERDEF + 6;
  public static final int MSG_CL_HEIGTH = GameApp.MSG_USERDEF + 7;
  public static final int MSG_CL_WIDTH = GameApp.MSG_USERDEF + 8;
  public static final int MSG_CL_BOMBS = GameApp.MSG_USERDEF + 9;
  public static final int MSG_CL_ERR_HEIGHT = GameApp.MSG_USERDEF + 10;
  public static final int MSG_CL_ERR_WIDTH = GameApp.MSG_USERDEF + 11;
  public static final int MSG_CL_ERR_BOMBS = GameApp.MSG_USERDEF + 12;
  public static final int MSG_CL_ERR_FRM = GameApp.MSG_USERDEF + 13;
  public static final int MSG_CL_ERR_TO = GameApp.MSG_USERDEF + 14;

  public static final int MSG_HS_LEVEL = GameApp.MSG_USERDEF + 15;

  public static int usLevel = 1;

  public static int width = 9;
  public static int height = 9;
  public static int bomb = 9;

  private CustomLevelForm gameCustomLevel;

  private ChoiceGroup opDifficulty;

  private final Image iError;

  public MineSweeper() {
    super();
    BaseApp.resPrefix = "mi";
    Application.menu = new short[][] {
        {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_CONTINUE, GameApp.GA_CONTINUE, 0
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_NEWGAME, GameApp.GA_NEWGAME, 1
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_HIGHSCORE, GameApp.GA_HIGHSCORE, 2
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_OPTIONS, GameApp.GA_OPTIONS, 3
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_SETTINGS, GameApp.GA_SETTINGS, 4
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_HELP, GameApp.GA_HELP, 5
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_ABOUT, GameApp.GA_ABOUT, 6
        }
    };
    GameApp.hsName = "MineSweeper";
    GameApp.hsMaxLevel = 3;
    GameApp.hsMaxScore = 1;
    iError = BaseApp.createImage(MineSweeper.RES_ERROR);
  }

  protected GameUISettings getSettings() {
    return new GameUISettings(this, GameApp.FT_AUDIO);
  }

  protected Displayable getOptions() {
    final Form form = new Form(Application.messages[GameApp.MSG_MENU_MAIN_OPTIONS]);
    opDifficulty = new ChoiceGroup(Application.messages[MineSweeper.MSG_MENU_OPTIONS_LEVEL], Choice.EXCLUSIVE);
    for (int i = 0; i < 4; i++) {
      opDifficulty.append(Application.messages[MineSweeper.MSG_TEXT_LEVEL_01 + i], null);
    }
    form.append(opDifficulty);
    Application.setup(form, Application.cBACK, Application.cOK);
    return form;
  }

  public void doShowOptions() {
    super.doShowOptions();
    opDifficulty.setSelectedIndex(MineSweeper.usLevel, true);
  }

  public void doApplyOptions() {
    MineSweeper.usLevel = opDifficulty.getSelectedIndex();
    if (MineSweeper.usLevel == 3) {
      if (gameCustomLevel == null) {
        gameCustomLevel = new CustomLevelForm();
        Application.setup(gameCustomLevel, Application.cOK, null);
      }
      gameCustomLevel.setInputs();
      Application.show(null, gameCustomLevel, false);
    }
    else {
      super.doApplyOptions();
    }
  }

  public void doGameStart() {
    GameApp.hsLevel = MineSweeper.usLevel;
    super.doGameStart();
  }

  public Alert makeAlert(final String err, final int min, final int max) {
    return new Alert(Application.messages[MineSweeper.MSG_CUSTOMLEVEL], err + Application.messages[MineSweeper.MSG_CL_ERR_FRM] + min + Application.messages[MineSweeper.MSG_CL_ERR_TO] + max, iError,
        AlertType.ERROR);
  }

  public void commandAction(final Command c, final Displayable d) {
    boolean processed = false;
    if (c == Application.cOK) {
      if (d == gameCustomLevel) {
        processed = true;
        gameCustomLevel.getInputs();
        boolean ok = true;
        if (ok && ((MineSweeper.height < MineSweeperGame.MINE_MIN_SIZE) || (MineSweeper.height > MineSweeperGame.MINE_MAX_SIZE))) {
          ok = false;
          Application.show(makeAlert(Application.messages[MineSweeper.MSG_CL_ERR_HEIGHT], MineSweeperGame.MINE_MIN_SIZE, MineSweeperGame.MINE_MAX_SIZE), gameCustomLevel, false);
        }
        if (ok && ((MineSweeper.width < MineSweeperGame.MINE_MIN_SIZE) || (MineSweeper.width > MineSweeperGame.MINE_MAX_SIZE))) {
          ok = false;
          Application.show(makeAlert(Application.messages[MineSweeper.MSG_CL_ERR_WIDTH], MineSweeperGame.MINE_MIN_SIZE, MineSweeperGame.MINE_MAX_SIZE), gameCustomLevel, false);
        }
        int max = (MineSweeper.height - 1) * (MineSweeper.width - 1);
        if (max > MineSweeperGame.MAX_BOMB) {
          max = MineSweeperGame.MAX_BOMB;
        }
        if (ok && ((MineSweeper.bomb < MineSweeperGame.MIN_BOMB) || (MineSweeper.bomb > max))) {
          ok = false;
          Application.show(makeAlert(Application.messages[MineSweeper.MSG_CL_ERR_BOMBS], MineSweeperGame.MIN_BOMB, max), gameCustomLevel, false);
        }
        if (ok) {
          Application.back(null);
        }
      }
    }
    if (!processed) {
      super.commandAction(c, d);
    }
  }

  protected Displayable getHighScore() {
    final Form form = new Form(Application.messages[GameApp.MSG_MENU_MAIN_HIGHSCORE]);
    final Font f = Font.getFont(Font.STYLE_BOLD);
    for (int l = 0; l < GameApp.hsMaxLevel; l++) {
      final Vector scores = GameApp.highscore.getList(l);
      final StringItem txt = new StringItem(Application.messages[MineSweeper.MSG_HS_LEVEL] + Application.messages[MineSweeper.MSG_TEXT_LEVEL_01 + l] + BaseApp.CR, null);
      txt.setFont(f);
      form.append(txt);
      if (scores.size() == 0) {
        form.append(Application.messages[GameApp.MSG_TEXT_HIGHSCORE_01]);
        form.append(BaseApp.sCR);
      }
      else {
        Score s;
        for (int i = 0; i < scores.size(); i++) {
          s = (Score) scores.elementAt(i);
          form.append("" + (i + 1) + ": " + s.getScore() + " (" + s.name + ")" + BaseApp.CR);
        }
      }
    }
    Application.setup(form, Application.cBACK, null);
    return form;
  }

  public GameScreen getGameScreen() {
    return new MineSweeperScreen(this);
  }

  public void processGameAction(final int action) {
    switch (action) {
      case GA_STARTUP: // Continue
        doStartup();
        break;
      case GA_CONTINUE: // Continue
        doGameResume();
        break;
      case GA_NEWGAME: // New game
        doGameStart();
        break;
      case GA_HIGHSCORE: // High score
        doHighScore();
        break;
      case GA_OPTIONS:
        doShowOptions();
        break;
      case GA_SETTINGS:
        doShowSettings();
        break;
      case GA_HELP:
        doHelp();
        break;
      case GA_ABOUT:
        doAbout();
        break;
      case GA_APPLYSETTINGS:
        doApplySettings();
        break;
      case GA_APPLYOPTIONS:
        doApplyOptions();
        break;
      case GA_NEWHIGHSCORE:
        doSetNewHighScore();
        break;
      default:
        break;
    }
  }

}
