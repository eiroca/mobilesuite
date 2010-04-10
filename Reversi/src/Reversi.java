/** GPL >= 3.0
 * Based upon jtReversi game written by Jataka Ltd.
 * 
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 * Copyright (C) 2002-2004 Salamon Andras
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
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.game.GameApp;
import net.eiroca.j2me.game.GameScreen;
import net.eiroca.j2me.game.tpg.GameMinMax;
import net.eiroca.j2me.reversi.ui.ReversiScreen;

/**
 * The Class Reversi.
 */
public class Reversi extends GameApp {

  /** The MS g_ name. */
  public static int MSG_NAME = GameApp.MSG_USERDEF + 0;

  /** The MS g_ gamemode. */
  public static int MSG_GAMEMODE = GameApp.MSG_USERDEF + 1;

  /** The MS g_ gamemod e1. */
  public static int MSG_GAMEMODE1 = GameApp.MSG_USERDEF + 2;

  /** The MS g_ gamemod e2. */
  public static int MSG_GAMEMODE2 = GameApp.MSG_USERDEF + 3;

  /** The MS g_ ailevel. */
  public static int MSG_AILEVEL = GameApp.MSG_USERDEF + 4;

  /** The MS g_ aileve l1. */
  public static int MSG_AILEVEL1 = GameApp.MSG_USERDEF + 5;

  /** The MS g_ aileve l2. */
  public static int MSG_AILEVEL2 = GameApp.MSG_USERDEF + 6;

  /** The MS g_ aileve l3. */
  public static int MSG_AILEVEL3 = GameApp.MSG_USERDEF + 7;

  /** The MS g_ aileve l4. */
  public static int MSG_AILEVEL4 = GameApp.MSG_USERDEF + 8;

  /** The MS g_ nameplaye r1. */
  public static int MSG_NAMEPLAYER1 = GameApp.MSG_USERDEF + 9;

  /** The MS g_ nameplaye r2. */
  public static int MSG_NAMEPLAYER2 = GameApp.MSG_USERDEF + 10;

  /** The MS g_ goodluck. */
  public static int MSG_GOODLUCK = GameApp.MSG_USERDEF + 11;

  /** The MS g_ thinking. */
  public static int MSG_THINKING = GameApp.MSG_USERDEF + 12;

  /** The MS g_ invalidmove. */
  public static int MSG_INVALIDMOVE = GameApp.MSG_USERDEF + 13;

  /** The MS g_ woncomputer. */
  public static int MSG_WONCOMPUTER = GameApp.MSG_USERDEF + 14;

  /** The MS g_ humanwon. */
  public static int MSG_HUMANWON = GameApp.MSG_USERDEF + 15;

  /** The MS g_ playerwon. */
  public static int MSG_PLAYERWON = GameApp.MSG_USERDEF + 16;

  /** The MS g_ draw. */
  public static int MSG_DRAW = GameApp.MSG_USERDEF + 17;

  /** The MS g_ human. */
  public static int MSG_HUMAN = GameApp.MSG_USERDEF + 18;

  /** The MS g_ computer. */
  public static int MSG_COMPUTER = GameApp.MSG_USERDEF + 19;

  /** The MS g_ pass. */
  public static int MSG_PASS = GameApp.MSG_USERDEF + 20;

  /** The MS g_ levelprefix. */
  public static int MSG_LEVELPREFIX = GameApp.MSG_USERDEF + 21;

  /** The player names. */
  public static String[] playerNames;

  /** The op players. */
  protected ChoiceGroup opPlayers;

  /** The op level. */
  protected ChoiceGroup opLevel;

  /** The gs player. */
  public static int gsPlayer = 1;

  /** The gs level. */
  public static int gsLevel = 3;

  /**
   * Instantiates a new reversi.
   */
  public Reversi() {
    super();
    BaseApp.resPrefix = "re";
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

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.GameApp#init()
   */
  public void init() {
    super.init();
    Reversi.playerNames = new String[] {
        Application.messages[Reversi.MSG_NAMEPLAYER1], Application.messages[Reversi.MSG_NAMEPLAYER2]
    };
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.GameApp#getGameScreen()
   */
  public GameScreen getGameScreen() {
    return new ReversiScreen(this);
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.GameApp#getOptions()
   */
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

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.GameApp#doShowOptions()
   */
  public void doShowOptions() {
    super.doShowOptions();
    opPlayers.setSelectedIndex(Reversi.gsPlayer - 1, true);
    opLevel.setSelectedIndex(Reversi.gsLevel - 1, true);
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.GameApp#doApplyOptions()
   */
  public void doApplyOptions() {
    Reversi.gsPlayer = opPlayers.getSelectedIndex() + 1;
    Reversi.gsLevel = opLevel.getSelectedIndex() + 1;
    ((ReversiScreen) GameApp.game).updateSkillInfo();
    super.doApplyOptions();
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.GameApp#doGameAbort()
   */
  public void doGameAbort() {
    super.doGameAbort();
    GameMinMax.cancel(false);
    GameMinMax.clearPrecalculatedMoves();
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.GameApp#processGameAction(int)
   */
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
