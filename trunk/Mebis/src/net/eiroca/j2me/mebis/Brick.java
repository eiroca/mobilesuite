/** GPL >= 2.0
 * Based upon scriptris - a free j2me tetris (R) clone with bluetooth multiplayer support
 *
 * Copyright (C) 2005-2006 Michael "ScriptKiller" Arndt <scriptkiller@gmx.de> http://scriptkiller.de/
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
package net.eiroca.j2me.mebis;

public class Brick {

  public static int colors[] = {
      0x0000FF, 0xFF0000, 0xCCCC00, 0xCC33FF, 0xFF9933, 0x00FF00, 0x00CCCC
  };

  /* types[type][rotation][y][x] */

  private final int types[][][][] = {
      {
        {
            {
                0, 1, 1, 0
            }, {
                0, 1, 1, 0
            }, {
                0, 0, 0, 0
            }, {
                0, 0, 0, 0
            }
        }
      }, {
          {
              {
                  0, 0, 1, 0
              }, {
                  0, 0, 1, 0
              }, {
                  0, 0, 1, 0
              }, {
                  0, 0, 1, 0
              }
          }, {
              {
                  1, 1, 1, 1
              }, {
                  0, 0, 0, 0
              }, {
                  0, 0, 0, 0
              }, {
                  0, 0, 0, 0
              }
          }
      }, {
          {
              {
                  0, 0, 1, 0
              }, {
                  0, 1, 1, 1
              }, {
                  0, 0, 0, 0
              }, {
                  0, 0, 0, 0
              },
          }, {
              {
                  0, 0, 1, 0
              }, {
                  0, 1, 1, 0
              }, {
                  0, 0, 1, 0
              }, {
                  0, 0, 0, 0
              },
          }, {
              {
                  0, 1, 1, 1
              }, {
                  0, 0, 1, 0
              }, {
                  0, 0, 0, 0
              }, {
                  0, 0, 0, 0
              },
          }, {
              {
                  0, 0, 1, 0
              }, {
                  0, 0, 1, 1
              }, {
                  0, 0, 1, 0
              }, {
                  0, 0, 0, 0
              },
          },
      }, {
          {
              {
                  0, 1, 0, 0
              }, {
                  0, 1, 0, 0
              }, {
                  0, 1, 1, 0
              }, {
                  0, 0, 0, 0
              }
          }, {
              {
                  0, 0, 0, 1
              }, {
                  0, 1, 1, 1
              }, {
                  0, 0, 0, 0
              }, {
                  0, 0, 0, 0
              }
          }, {
              {
                  0, 1, 1, 0
              }, {
                  0, 0, 1, 0
              }, {
                  0, 0, 1, 0
              }, {
                  0, 0, 0, 0
              }
          }, {
              {
                  0, 1, 1, 1
              }, {
                  0, 1, 0, 0
              }, {
                  0, 0, 0, 0
              }, {
                  0, 0, 0, 0
              }
          },
      }, {
          {
              {
                  0, 0, 1, 0
              }, {
                  0, 0, 1, 0
              }, {
                  0, 1, 1, 0
              }, {
                  0, 0, 0, 0
              }
          }, {
              {
                  0, 1, 1, 1
              }, {
                  0, 0, 0, 1
              }, {
                  0, 0, 0, 0
              }, {
                  0, 0, 0, 0
              }
          }, {
              {
                  0, 1, 1, 0
              }, {
                  0, 1, 0, 0
              }, {
                  0, 1, 0, 0
              }, {
                  0, 0, 0, 0
              }
          }, {
              {
                  0, 1, 0, 0
              }, {
                  0, 1, 1, 1
              }, {
                  0, 0, 0, 0
              }, {
                  0, 0, 0, 0
              }
          },
      }, {
          {
              {
                  0, 0, 1, 0
              }, {
                  0, 1, 1, 0
              }, {
                  0, 1, 0, 0
              }, {
                  0, 0, 0, 0
              }
          }, {
              {
                  0, 1, 1, 0
              }, {
                  0, 0, 1, 1
              }, {
                  0, 0, 0, 0
              }, {
                  0, 0, 0, 0
              }
          },
      }, {
          {
              {
                  0, 1, 0, 0
              }, {
                  0, 1, 1, 0
              }, {
                  0, 0, 1, 0
              }, {
                  0, 0, 0, 0
              }
          }, {
              {
                  0, 0, 1, 1
              }, {
                  0, 1, 1, 0
              }, {
                  0, 0, 0, 0
              }, {
                  0, 0, 0, 0
              }
          },
      }
  };

  /* type of that brick */
  public int type;
  /* rotation of that brick */
  public int rotation;

  /* x and y offset of brick */
  public int xoffset;
  public int yoffset;

  public Block[] blocks;

  public Brick(final int type) {
    if ((type < 0) || (type > 6)) {
      System.err.println("Invalid Type: " + type);
    }
    this.type = type;
    rotation = 0; /* start with standard-rotation */
    xoffset = 0;
    yoffset = 0;
    blocks = new Block[4];
    for (int i = 0; i < 4; i++) {
      /*
       * create block with correct color, xpos and ypos are added by updateBlocks() later!
       */
      blocks[i] = new Block(Brick.colors[type], 0, 0);
    }
    updateBlocks();
  }

  /*
   * rotate piece, call with left=true to rotate counterclockwise, with left=false to rotate
   * clockwise
   */
  public void rotate(final boolean left) {
    /* no of possible rotations */
    final int rotations = types[type].length;
    if (left) {
      rotation++;
      if (rotation >= rotations) {
        /* start from beginning */
        rotation = 0;
      }
    }
    else {
      rotation--;
      if (rotation < 0) {
        /* go to end */
        rotation = rotations - 1;
      }
    }
    updateBlocks();
  }

  /* one step down */
  public void step() {
    yoffset++;
    updateBlocks();
  }

  /* one step left */
  public void left() {
    xoffset--;
    updateBlocks();
  }

  /* one step right */
  public void right() {
    xoffset++;
    updateBlocks();
  }

  /* set position */
  public void setPosition(final int x, final int y) {
    xoffset = x;
    yoffset = y;
    updateBlocks();
  }

  /*
   * update blocks according to xoffset, yoffset and rotation
   */
  private void updateBlocks() {
    int i = 0; /* block counter */
    for (int y = 0; y < 4; y++) {
      for (int x = 0; x < 4; x++) {
        if (types[type][rotation][y][x] == 1) {
          if (i == blocks.length) {
            /* shouldn't get here! */
            break;
          }
          /* update each blocks position */
          blocks[i].update(xoffset + x, yoffset + y);
          i++;
        }
      }
    }

  }

  /* no clone() in J2ME, this is our own */
  public Brick clone() {
    final Brick b = new Brick(type);
    b.xoffset = xoffset;
    b.yoffset = yoffset;
    b.rotation = rotation;
    for (int i = 0; i < blocks.length; i++) {
      b.blocks[i] = blocks[i].clone();
    }
    return b;
  }

}
