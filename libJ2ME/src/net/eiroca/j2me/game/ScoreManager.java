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
package net.eiroca.j2me.game;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.rms.RecordStore;
import net.eiroca.j2me.app.BaseApp;

public class ScoreManager {

  private final String recordName;
  private final String gameName;
  private final Vector[] scores;
  private final int listLength;

  public ScoreManager(final String recordName, final String gameName, final int dif, final int listLength, final boolean readIt) {
    this.recordName = recordName;
    this.listLength = listLength;
    this.gameName = gameName;
    scores = new Vector[dif];
    for (int i = 0; i < dif; i++) {
      scores[i] = new Vector(listLength);
    }
    if (readIt) {
      readHighScoreList();
    }
  }

  public Score getHighScore(final int dif) {
    final int size = scores[dif].size();
    if (size == 0) { return null; }
    return (Score) scores[dif].elementAt(0);
  }

  public boolean hasHighScore(final int dif, final Score score) {
    return scores[dif].size() > 0;
  }

  public boolean isHighScore(final int dif, final Score score) {
    final int size = scores[dif].size();
    if (size < listLength) { return true; }
    final Score last = (Score) scores[dif].elementAt(size - 1);
    return (score.score > last.score);
  }

  public Vector getList(final int dif) {
    return scores[dif];
  }

  private void sort(final int dif) {
    boolean flipped;
    Score a;
    Score b;
    for (int i = scores[dif].size(); --i >= 0;) {
      flipped = false;
      for (int j = 0; j < i; j++) {
        a = (Score) scores[dif].elementAt(j);
        b = (Score) scores[dif].elementAt(j + 1);
        if (a.score < b.score) {
          scores[dif].setElementAt(b, j);
          scores[dif].setElementAt(a, j + 1);
          flipped = true;
        }
      }
      if (!flipped) { return; }
    }
  }

  public synchronized void saveScoreList() {
    final RecordStore rs = BaseApp.getRecordStore(recordName, true, false);
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    final DataOutputStream dos = new DataOutputStream(baos);
    try {
      dos.writeUTF(gameName);
      dos.writeInt(scores.length);
      for (int l = 0; l < scores.length; l++) {
        dos.writeInt(scores[l].size());
        for (int i = 0; i < scores[l].size(); i++) {
          final Score se = (Score) scores[l].elementAt(i);
          dos.writeUTF(se.name);
          dos.writeInt(se.level);
          dos.writeInt(se.score);
        }
      }
    }
    catch (final IOException e) {
      //
    }
    BaseApp.writeData(rs, baos);
    BaseApp.close(rs, null, dos);
  }

  private void readHighScoreList() {
    final RecordStore rs = BaseApp.getRecordStore(recordName, false, false);
    final DataInputStream dis = BaseApp.readRecord(rs, 1);
    if (dis != null) {
      try {
        final String tmp = dis.readUTF();
        if (gameName.equals(tmp)) {
          final int t = dis.readInt();
          if (t == scores.length) {
            for (int l = 0; l < scores.length; l++) {
              final int n = dis.readInt();
              int ps = 0;
              for (int i = 0; i < n; i++) {
                final Score se = new Score(dis.readUTF(), dis.readInt(), dis.readInt());
                if (ps < listLength) {
                  scores[l].addElement(se);
                  ps++;
                }
              }
            }
          }
        }
      }
      catch (final IOException e) {
        //
      }
    }
    BaseApp.close(rs, dis, null);
  }

  public void addNewScore(final int dif, final Score score) {
    if (score == null) { return; }
    final Score s = new Score(score.name, score.level, score.score);
    scores[dif].addElement(s);
    sort(dif);
    if (scores[dif].size() > listLength) {
      scores[dif].removeElementAt(scores[dif].size() - 1);
    }
    saveScoreList();
    return;
  }

}
