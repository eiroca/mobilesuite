/** GPL >= 3.0
 * Based upon scriptris - a free j2me tetris (R) clone with bluetooth multiplayer support
 * 
 * Copyright (C) 2006-2010 eIrOcA (eNrIcO Croce & sImOnA Burzio)
 * Copyright (C) 2005-2006 Michael "ScriptKiller" Arndt <scriptkiller@gmx.de> http://scriptkiller.de/
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
import net.eiroca.j2me.app.Application;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.game.GameApp;
import net.eiroca.j2me.game.GameScreen;
import net.eiroca.j2me.mebis.MebisScreen;

/**
 * The Class Mebis.
 */
public class Mebis extends GameApp {

  /** The Constant MSG_NAME. */
  public static final int MSG_NAME = GameApp.MSG_USERDEF + 0;

  /** The Constant MSG_LOOSE. */
  public static final int MSG_LOOSE = GameApp.MSG_USERDEF + 1;

  /** The Constant MSG_WIN. */
  public static final int MSG_WIN = GameApp.MSG_USERDEF + 2;

  /** The Constant MSG_SCORE. */
  public static final int MSG_SCORE = GameApp.MSG_USERDEF + 3;

  /** The Constant MSG_LINES. */
  public static final int MSG_LINES = GameApp.MSG_USERDEF + 4;

  /**
   * Instantiates a new mebis.
   */
  public Mebis() {
    super();
    BaseApp.resPrefix = "me";
    Application.menu = new short[][] {
        {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_CONTINUE, GameApp.GA_CONTINUE, 0
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_NEWGAME, GameApp.GA_NEWGAME, 1
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_HIGHSCORE, GameApp.GA_HIGHSCORE, 2
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_HELP, GameApp.GA_HELP, 5
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_ABOUT, GameApp.GA_ABOUT, 6
        }
    };
    GameApp.hsName = "Mebis";
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.GameApp#getGameScreen()
   */
  public GameScreen getGameScreen() {
    return new MebisScreen(this);
  }

  /* (non-Javadoc)
   * @see net.eiroca.j2me.game.GameApp#processGameAction(int)
   */
  public void processGameAction(final int action) {
    switch (action) {
      case GA_STARTUP: // Continue
        Application.show(null, gameMenu, true);
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
      case GA_HELP:
        doHelp();
        break;
      case GA_ABOUT:
        doAbout();
        break;
      case GA_NEWHIGHSCORE:
        doSetNewHighScore();
        break;
      default:
        break;
    }
  }

}
