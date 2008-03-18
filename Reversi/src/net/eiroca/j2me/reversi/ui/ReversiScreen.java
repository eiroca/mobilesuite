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
package net.eiroca.j2me.reversi.ui;

import Reversi;
import java.util.Timer;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import net.eiroca.j2me.app.BaseApp;
import net.eiroca.j2me.game.GameApp;
import net.eiroca.j2me.game.GameScreen;
import net.eiroca.j2me.game.tpg.GameMinMax;
import net.eiroca.j2me.game.tpg.GameTable;
import net.eiroca.j2me.game.tpg.TwoPlayerGame;
import net.eiroca.j2me.reversi.ReversiGame;
import net.eiroca.j2me.reversi.ReversiMove;
import net.eiroca.j2me.reversi.ReversiTable;

public final class ReversiScreen extends GameScreen {

  private static final char NL = '\n';
  private static final String SEP = ": ";
  private static final int COLOR_TEXT_BG = 0xEEEEEE;
  private static final int COLOR_TEXT_FG = 0x000000;
  private static final int COLOR_BG = 0xFFFFD0;
  private static final int COLOR_FG = 0x000000;
  private static final int COLOR_P1 = 0xFF0000;
  private static final int COLOR_P2 = 0x0000FF;
  private static final int COLOR_DARKBOX = 0x000000;
  private static final int ASPECT_LIMIT_A = 400; // 1.5
  private static final int ASPECT_LIMIT_B = 300; // 1.5
  private static final int[][] heurMatrix = {
      {
          500, -240, 85, 69, 69, 85, -240, 500
      }, {
          -240, -130, 49, 23, 23, 49, -130, -240
      }, {
          85, 49, 1, 9, 9, 1, 49, 85
      }, {
          69, 23, 9, 32, 32, 9, 23, 69
      }, {
          69, 23, 9, 32, 32, 9, 23, 69
      }, {
          85, 49, 1, 9, 9, 1, 49, 85
      }, {
          -240, -130, 49, 23, 23, 49, -130, -240
      }, {
          500, -240, 85, 69, 69, 85, -240, 500
      }
  };
  private final String infoLines[] = new String[3];
  private String message;
  private final int pnums[] = new int[2];
  private ReversiMove[] possibleMoves;
  private int sizex;
  private int sizey;
  private final int vertWidth;
  private int width;
  private int height;
  public int selx;
  public int sely;

  public long messageEnd;

  private MinimaxTimerTask mtt;
  private final Timer timer = new Timer();

  public static byte actPlayer;
  public boolean gameEnded = true;
  public boolean[] isHuman = new boolean[2];
  public GameTable[] tables;
  public static int turnNum;
  public static ReversiTable table;
  public static ReversiGame rgame;
  public static boolean twoplayer;

  private final int fontHeight;
  private final int off_y;
  private final int off_x;
  private final int pieceWidth;
  private final int pieceHeight;
  private final int piece_offx;
  private final int piece_offy;

  public ReversiScreen(final GameApp midlet) {
    super(midlet, false, true);
    ReversiScreen.rgame = new ReversiGame(ReversiScreen.heurMatrix, 10, 18, true);
    name = BaseApp.messages[Reversi.MSG_NAME];
    width = screenWidth * 8 / 10;
    vertWidth = screenWidth - width;
    height = screenHeight;
    sizex = (width - 1) / 8;
    sizey = (height - 1) / 8;
    if (ReversiScreen.ASPECT_LIMIT_B * sizex > ReversiScreen.ASPECT_LIMIT_A * sizey) {
      sizex = sizey * ReversiScreen.ASPECT_LIMIT_A / ReversiScreen.ASPECT_LIMIT_B;
    }
    if (ReversiScreen.ASPECT_LIMIT_B * sizey > ReversiScreen.ASPECT_LIMIT_A * sizex) {
      sizey = sizex * ReversiScreen.ASPECT_LIMIT_A / ReversiScreen.ASPECT_LIMIT_B;
    }
    width = sizex * 8;
    height = sizey * 8;
    fontHeight = screen.getFont().getHeight();
    pieceWidth = 20 * sizex / 30;
    pieceHeight = 20 * sizey / 30;
    piece_offx = (sizex - pieceWidth) / 2;
    piece_offy = (sizey - pieceHeight) / 2;
    selx = 0;
    sely = 0;
    off_y = (screenHeight - height) / 2;
    off_x = 2;
    updateSkillInfo();
  }

  public void init() {
    super.init();
    BaseApp.background = 0x00FFFFFF;
    BaseApp.foreground = 0x00000000;
    score.beginGame(1, 0, 0);
    if (Reversi.gsPlayer == 1) {
      isHuman[0] = true;
      isHuman[1] = false;
      ReversiScreen.twoplayer = false;
    }
    else {
      isHuman[0] = true;
      isHuman[1] = true;
      ReversiScreen.twoplayer = true;
    }
    updateSkillInfo();
    setMessage(BaseApp.messages[Reversi.MSG_GOODLUCK]);
    gameEnded = false;
    ReversiScreen.actPlayer = 0;
    ReversiScreen.turnNum = 1;
    ReversiScreen.table = new ReversiTable();
    updatePossibleMoves();
  }

  public boolean tick() {
    screen.setColor(BaseApp.background);
    screen.fillRect(0, 0, screenWidth, screenHeight);
    drawBoard();
    drawTable();
    drawSelectionBox();
    drawPossibleMoves();
    drawVertInfo();
    drawMessage();
    return true;
  }

  protected void drawMessage() {
    if ((message == null) || ((messageEnd != 0) && (messageEnd > System.currentTimeMillis()))) { return; }
    int startIndex;
    int endIndex = -1;
    final int breaks = BaseApp.lineBreaks(message);
    final int maxWidth = BaseApp.maxSubWidth(screen.getFont(), message) + 10;
    int cornerX = (width - maxWidth) / 2;
    if (cornerX < 0) {
      cornerX = (screenWidth - maxWidth) / 2;
    }
    else {
      cornerX += off_x;
    }
    int cornerY = off_y + (height - (breaks + 1) * fontHeight - 6) / 2;
    screen.setColor(ReversiScreen.COLOR_TEXT_BG);
    screen.fillRect(cornerX - 1, cornerY - 1, maxWidth, (breaks + 1) * fontHeight + 6);
    screen.setColor(ReversiScreen.COLOR_TEXT_FG);
    screen.drawRect(cornerX - 1, cornerY - 1, maxWidth, (breaks + 1) * fontHeight + 6);
    screen.drawRect(cornerX, cornerY, maxWidth - 2, (breaks + 1) * fontHeight + 4);
    while (endIndex < message.length()) {
      startIndex = endIndex + 1;
      endIndex = message.indexOf(NL, startIndex);
      if (endIndex == -1) {
        endIndex = message.length();
      }
      final String submessage = message.substring(startIndex, endIndex);
      screen.drawString(submessage, cornerX + 5, cornerY + 2, Graphics.TOP | Graphics.LEFT);
      cornerY += fontHeight;
    }
  }

  protected void drawBoard() {
    screen.setColor(ReversiScreen.COLOR_BG);
    screen.fillRect(off_x, off_y, width, height);
    screen.setColor(ReversiScreen.COLOR_FG);
    for (int i = 0; i <= 8; ++i) {
      screen.drawLine(off_x, off_y + i * sizey, off_x + width, off_y + i * sizey);
      screen.drawLine(off_x + i * sizex, off_y, off_x + i * sizex, off_y + height);
    }
  }

  protected void drawPiece(final int row, final int col, final int player) {
    final int x = off_x + row * sizex + piece_offx;
    final int y = off_y + col * sizey + piece_offy;
    if (player == 1) {
      screen.setColor(ReversiScreen.COLOR_P1);
    }
    else {
      screen.setColor(ReversiScreen.COLOR_P2);
    }
    screen.fillArc(x, y, pieceWidth, pieceHeight, 0, 360);
  }

  protected void drawPossibleMoves() {
    if (possibleMoves == null) {
      // end of the game
      return;
    }
    int x;
    int y;
    screen.setColor(ReversiScreen.COLOR_DARKBOX);
    for (int i = 0; i < possibleMoves.length; ++i) {
      x = off_x + possibleMoves[i].row * sizex + sizex / 2;
      y = off_y + possibleMoves[i].col * sizey + sizey / 2;
      screen.fillRect(x, y, 2, 2);
    }
  }

  protected void drawSelectionBox() {
    if (ReversiScreen.getActPlayer() == 0) {
      screen.setColor(ReversiScreen.COLOR_P1);
    }
    else {
      screen.setColor(ReversiScreen.COLOR_P2);
    }
    screen.drawRect(off_x + selx * sizex, off_y + sely * sizey, sizex, sizey);
    screen.drawRect(off_x + selx * sizex + 1, off_y + sely * sizey + 1, sizex - 2, sizey - 2);
  }

  protected void drawTable() {
    pnums[0] = 0;
    pnums[1] = 0;
    int item;
    for (int i = 0; i < 8; ++i) {
      for (int j = 0; j < 8; ++j) {
        item = ReversiScreen.table.getItem(i, j);
        if (item != 0) {
          drawPiece(i, j, item);
          pnums[item - 1]++;
        }
      }
    }
    infoLines[0] = Integer.toString(pnums[0]);
    infoLines[1] = Integer.toString(pnums[1]);
  }

  public void drawVertInfo() {
    // two pieces
    drawPiece(9, 0, 1);
    drawPiece(9, 7, 0);
    // numbers
    screen.setColor(BaseApp.foreground);
    screen.drawString(infoLines[0], width + vertWidth, off_y + sizey + 2, Graphics.TOP | Graphics.RIGHT);
    screen.drawString(infoLines[1], width + vertWidth, off_y + 7 * sizey, Graphics.BOTTOM | Graphics.RIGHT);
    // active player
    screen.fillRect(9 * sizex - sizex / 2, off_y + sizey / 2 + ReversiScreen.getActPlayer() * 7 * sizey, 2, 2);
    // skill
    if (infoLines[2] != null) {
      screen.drawString(infoLines[2], width + vertWidth, screenHeight / 2, Graphics.BASELINE | Graphics.RIGHT);
    }
  }

  public void keyPressed(final int keyCode) {
    if (gameEnded) {
      midlet.doGameStop();
    }
    else {
      switch (getGameAction(keyCode)) {
        case Canvas.UP:
          sely = (sely + 8 - 1) % 8;
          message = null;
          break;
        case Canvas.DOWN:
          sely = (sely + 1) % 8;
          message = null;
          break;
        case Canvas.LEFT:
          selx = (selx + 8 - 1) % 8;
          message = null;
          break;
        case Canvas.RIGHT:
          selx = (selx + 1) % 8;
          message = null;
          break;
        case Canvas.FIRE:
          if (message != null) {
            message = null;
          }
          else {
            nextTurn(selx, sely);
          }
          break;
        default:
          midlet.doGamePause();
          break;
      }
    }
  }

  public void setMessage(final String message) {
    this.message = message;
    messageEnd = 0;
  }

  public void setMessage(final String message, final int delay) {
    this.message = message;
    messageEnd = System.currentTimeMillis() + delay * 1000;
  }

  public void updatePossibleMoves() {
    possibleMoves = (ReversiMove[]) ReversiScreen.rgame.possibleMoves(ReversiScreen.table, ReversiScreen.actPlayer);
  }

  public void updateSkillInfo() {
    if (!ReversiScreen.twoplayer) {
      infoLines[2] = BaseApp.messages[Reversi.MSG_LEVELPREFIX] + Reversi.gsLevel;
    }
    else {
      infoLines[2] = null;
    }
  }

  protected ReversiMove computerTurn(final ReversiMove prevMove) {
    ReversiMove move = (ReversiMove) GameMinMax.precalculatedBestMove(prevMove);
    if (move == null) {
      setMessage(BaseApp.messages[Reversi.MSG_THINKING]);
      GameMinMax.cancel(false);
      move = (ReversiMove) GameMinMax.minimax(ReversiScreen.getActSkill(), ReversiScreen.table, ReversiScreen.actPlayer, ReversiScreen.rgame, true, 0, true, true, null);
    }
    message = null;
    ReversiScreen.rgame.resetEvalNum();
    return move;
  }

  public static byte getActPlayer() {
    return ReversiScreen.actPlayer;
  }

  public static int getActSkill() {
    int actSkill = Reversi.gsLevel;
    if (ReversiScreen.turnNum > 50) {
      actSkill++;
    }
    if (ReversiScreen.turnNum > 55) {
      actSkill++;
    }
    return actSkill;
  }

  public void nextTurn(final int row, final int col) {
    if (mtt != null) {
      mtt.cancel();
      while (mtt.ended == false) {
        synchronized (this) {
          try {
            wait(50);
          }
          catch (final Exception e) {
            //
          }
        }
      }
    }
    if (gameEnded) { return; }
    final ReversiMove move = new ReversiMove(row, col);
    processMove(move, false);
    updatePossibleMoves();
    while (!gameEnded && !isHuman[ReversiScreen.actPlayer]) {
      mtt = new MinimaxTimerTask();
      final ReversiMove computerMove = computerTurn(move);
      selx = computerMove.row;
      sely = computerMove.col;
      processMove(computerMove, true);
      updatePossibleMoves();
      GameMinMax.clearPrecalculatedMoves();
    }
  }

  protected void processMove(final ReversiMove move, final boolean startForeThinking) {
    final ReversiTable newTable = new ReversiTable();
    tables = ReversiScreen.rgame.animatedTurn(ReversiScreen.table, ReversiScreen.actPlayer, move, newTable);
    boolean goodMove = (tables != null);
    if (!goodMove) {
      setMessage(BaseApp.messages[Reversi.MSG_INVALIDMOVE], 2000);
    }
    else {
      if (startForeThinking) {
        mtt.setStartTable(tables[tables.length - 1]);
        timer.schedule(mtt, 0);
      }
      synchronized (this) {
        for (int i = 0; i < tables.length; ++i) {
          ReversiScreen.table = (ReversiTable) tables[i];
          if (i < tables.length - 1) {
            try {
              wait(300);
            }
            catch (final InterruptedException e) {
              // do something
            }
          }
        }
      }
      boolean nonPass = false;
      ReversiScreen.table = newTable;
      while (!nonPass && !gameEnded) {
        ReversiScreen.rgame.process(newTable, ReversiScreen.actPlayer);
        if (ReversiScreen.rgame.isGameEnded()) {
          final int result = ReversiScreen.rgame.getGameResult();
          String endMessage;
          final boolean firstWin = ((result == TwoPlayerGame.LOSS) && (ReversiScreen.actPlayer == 0)) || ((result == TwoPlayerGame.WIN) && (ReversiScreen.actPlayer == 1));
          final int winner = firstWin ? 1 : 0;
          if (!ReversiScreen.twoplayer && firstWin) {
            endMessage = BaseApp.messages[Reversi.MSG_WONCOMPUTER];
          }
          else if (result == TwoPlayerGame.DRAW) {
            endMessage = BaseApp.messages[Reversi.MSG_DRAW];
          }
          else {
            if (ReversiScreen.twoplayer) {
              endMessage = Reversi.playerNames[winner] + BaseApp.messages[Reversi.MSG_PLAYERWON];
            }
            else {
              endMessage = BaseApp.messages[Reversi.MSG_HUMANWON];
            }
          }
          final int firstNum = ReversiScreen.rgame.numFirstPlayer;
          final int secondNum = ReversiScreen.rgame.numSecondPlayer;
          endMessage += ReversiScreen.NL + Reversi.playerNames[0] + ReversiScreen.SEP + firstNum + ReversiScreen.NL + Reversi.playerNames[1] + ReversiScreen.SEP + secondNum;
          setMessage(endMessage);
          gameEnded = true;
        }
        else {
          ReversiScreen.actPlayer = (byte) (1 - ReversiScreen.actPlayer);
          ReversiScreen.turnNum++;
          if (!ReversiScreen.rgame.hasPossibleMove(ReversiScreen.table, ReversiScreen.actPlayer)) {
            String message;
            if (isHuman[ReversiScreen.actPlayer]) {
              if (ReversiScreen.twoplayer) {
                message = Reversi.playerNames[ReversiScreen.actPlayer];
              }
              else {
                message = BaseApp.messages[Reversi.MSG_HUMAN];
              }
            }
            else {
              message = BaseApp.messages[Reversi.MSG_COMPUTER];
            }
            setMessage(message + Reversi.MSG_PASS, 3000);
            ReversiScreen.table.setPassNum(ReversiScreen.table.getPassNum() + 1);
            // just to be sure
            GameMinMax.clearPrecalculatedMoves();
          }
          else {
            nonPass = true;
          }
        }
      }
    }
  }

  public void saveGameParameters(final byte[] b, final int offset) {
    int index = offset;
    // isHuman
    b[index] = 0;
    if (isHuman[0]) {
      b[index] |= 1;
    }
    if (isHuman[1]) {
      b[index] |= 2;
    }
    index++;
    // actPlayer
    b[index++] = ReversiScreen.actPlayer;
    // turnNum
    b[index++] = (byte) ReversiScreen.turnNum;
  }

  /**
   * Saves data into byte[]
   */
  public byte[] saveRecordStore() {
    final byte[] result = new byte[70];
    result[0] = (byte) Reversi.gsLevel;
    result[1] = (byte) (gameEnded ? 0 : 1);
    saveGameParameters(result, 2);
    ReversiScreen.table.toByteArray(result, 5);
    return result;
  }

  public void loadGameParameters(final byte[] b, final int offset) {
    int index = offset;
    isHuman[0] = false;
    isHuman[1] = false;
    if ((b[index] & 1) > 0) {
      isHuman[0] = true;
    }
    if ((b[index] & 2) > 0) {
      isHuman[1] = true;
    }
    ReversiScreen.twoplayer = isHuman[0] && isHuman[1];
    index++;
    ReversiScreen.actPlayer = b[index++];
    ReversiScreen.turnNum = b[index++];
  }

  /**
   * Loads data from byte[]
   */
  public boolean loadRecordStore(final byte[] b) {
    if (b.length != 70) { return false; }
    Reversi.gsLevel = b[0];
    gameEnded = (b[1] == 1) ? true : false;
    loadGameParameters(b, 2);
    ReversiScreen.table = new ReversiTable(b, 5);
    return true;
  }

}
