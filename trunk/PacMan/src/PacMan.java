/** GPL >= 2.0
 * Based upon Nokia PacMan game written by Marius Rieder
 *
 * Copyright (C) Marius Rieder
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
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.game.GameApp;
import net.eiroca.j2me.game.GameScreen;
import net.eiroca.j2me.game.GameUISettings;
import net.eiroca.j2me.pacman.PacManScreen;

public class PacMan extends GameApp {

  public static final int MSG_PACMAN_LIVES = GameApp.MSG_USERDEF + 0;
  public static final int MSG_PACMAN_NAME = GameApp.MSG_USERDEF + 1;
  public static final int MSG_PACMAN_LEVEL = GameApp.MSG_USERDEF + 2;

  public static final String RES_FIELD = "field.png";
  public static final String RES_MAP = "level.map";
  public static final String RES_GHOST = "ghost.png";
  public static final String RES_PACMAN = "pacman.png";

  public PacMan() {
    super();

    BaseApp.background = 0x00000000;
    BaseApp.foreground = 0x00FFFF00;

    BaseApp.menu = new short[][] {
        {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_CONTINUE, GameApp.GA_CONTINUE, 0
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_NEWGAME, GameApp.GA_NEWGAME, 1
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_HIGHSCORE, GameApp.GA_HIGHSCORE, 2
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_SETTINGS, GameApp.GA_SETTINGS, 4
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_HELP, GameApp.GA_HELP, 5
        }, {
            GameApp.ME_MAINMENU, GameApp.MSG_MENU_MAIN_ABOUT, GameApp.GA_ABOUT, 6
        }
    };

    GameApp.hsName = "PacMan";

  }

  protected GameUISettings getSettings() {
    return new GameUISettings(this, GameApp.FT_VIBRATE + GameApp.FT_LIGHT);
  }

  public GameScreen getGameScreen() {
    return new PacManScreen(this, true, true);
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
      case GA_NEWHIGHSCORE:
        doSetNewHighScore();
        break;
      default:
        break;
    }
  }

}
