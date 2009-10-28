/** GPL >= 2.0
 * Based upon jtReversi game written by Jataka Ltd.
 *
 * Copyright (C) 2002-2004 Salamon Andras
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
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.game.GameApp;
import net.eiroca.j2me.game.GameScreen;
import net.eiroca.j2me.game.tpg.GameMinMax;
import net.eiroca.j2me.reversi.ui.ReversiScreen;

public class Reversi extends GameApp {

  public static int MSG_NAME = GameApp.MSG_USERDEF + 0;
  public static int MSG_GAMEMODE = GameApp.MSG_USERDEF + 1;
  public static int MSG_GAMEMODE1 = GameApp.MSG_USERDEF + 2;
  public static int MSG_GAMEMODE2 = GameApp.MSG_USERDEF + 3;
  public static int MSG_AILEVEL = GameApp.MSG_USERDEF + 4;
  public static int MSG_AILEVEL1 = GameApp.MSG_USERDEF + 5;
  public static int MSG_AILEVEL2 = GameApp.MSG_USERDEF + 6;
  public static int MSG_AILEVEL3 = GameApp.MSG_USERDEF + 7;
  public static int MSG_AILEVEL4 = GameApp.MSG_USERDEF + 8;
  public static int MSG_NAMEPLAYER1 = GameApp.MSG_USERDEF + 9;
  public static int MSG_NAMEPLAYER2 = GameApp.MSG_USERDEF + 10;
  public static int MSG_GOODLUCK = GameApp.MSG_USERDEF + 11;
  public static int MSG_THINKING = GameApp.MSG_USERDEF + 12;
  public static int MSG_INVALIDMOVE = GameApp.MSG_USERDEF + 13;
  public static int MSG_WONCOMPUTER = GameApp.MSG_USERDEF + 14;
  public static int MSG_HUMANWON = GameApp.MSG_USERDEF + 15;
  public static int MSG_PLAYERWON = GameApp.MSG_USERDEF + 16;
  public static int MSG_DRAW = GameApp.MSG_USERDEF + 17;
  public static int MSG_HUMAN = GameApp.MSG_USERDEF + 18;
  public static int MSG_COMPUTER = GameApp.MSG_USERDEF + 19;
  public static int MSG_PASS = GameApp.MSG_USERDEF + 20;
  public static int MSG_LEVELPREFIX = GameApp.MSG_USERDEF + 21;

  public static String[] playerNames;

  protected ChoiceGroup opPlayers;
  protected ChoiceGroup opLevel;

  public static int gsPlayer = 1;
  public static int gsLevel = 3;

  public Reversi() {
    super();
    Application.menu = new short[][] {
        {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_CONTINUE, GameApp.GA_CONTINUE, 0
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_NEWGAME, GameApp.GA_NEWGAME, 1
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_OPTIONS, GameApp.GA_OPTIONS, 3
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_HELP, GameApp.GA_HELP, 5
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_ABOUT, GameApp.GA_ABOUT, 6
        }
    };
    GameApp.hsName = "Reversi";
  }

  public void init() {
    super.init();
    Reversi.playerNames = new String[] {
        Application.messages[Reversi.MSG_NAMEPLAYER1], Application.messages[Reversi.MSG_NAMEPLAYER2]
    };
  }

  public GameScreen getGameScreen() {
    return new ReversiScreen(this);
  }

  protected Displayable getOptions() {
    final Form form = new Form(Application.messages[GameApp.MSG_MENU_MAIN_OPTIONS]);
    opPlayers = new ChoiceGroup(Application.messages[Reversi.MSG_GAMEMODE], Choice.EXCLUSIVE);
    opPlayers.append(Application.messages[Reversi.MSG_GAMEMODE1], null);
    opPlayers.append(Application.messages[Reversi.MSG_GAMEMODE2], null);
    opLevel = new ChoiceGroup(Application.messages[Reversi.MSG_AILEVEL], Choice.EXCLUSIVE);
    opLevel.append(Application.messages[Reversi.MSG_AILEVEL1], null);
    opLevel.append(Application.messages[Reversi.MSG_AILEVEL2], null);
    opLevel.append(Application.messages[Reversi.MSG_AILEVEL3], null);
    opLevel.append(Application.messages[Reversi.MSG_AILEVEL4], null);
    form.append(opPlayers);
    form.append(opLevel);
    Application.setup(form, Application.cBACK, Application.cOK);
    return form;
  }

  public void doShowOptions() {
    super.doShowOptions();
    opPlayers.setSelectedIndex(Reversi.gsPlayer - 1, true);
    opLevel.setSelectedIndex(Reversi.gsLevel - 1, true);
  }

  public void doApplyOptions() {
    Reversi.gsPlayer = opPlayers.getSelectedIndex() + 1;
    Reversi.gsLevel = opLevel.getSelectedIndex() + 1;
    ((ReversiScreen) GameApp.game).updateSkillInfo();
    super.doApplyOptions();
  }

  public void doGameAbort() {
    super.doGameAbort();
    GameMinMax.cancel(false);
    GameMinMax.clearPrecalculatedMoves();
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
      case GA_OPTIONS:
        doShowOptions();
        break;
      case GA_HELP:
        doHelp();
        break;
      case GA_ABOUT:
        doAbout();
        break;
      case GA_APPLYOPTIONS:
        doApplyOptions();
        break;
      default:
        break;
    }
  }

}
