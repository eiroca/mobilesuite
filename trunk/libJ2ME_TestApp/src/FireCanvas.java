import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;

public final class FireCanvas extends Canvas implements CommandListener, Runnable {

  private final FireDemo midlet;
  private final Display display;

  private boolean paused;
  private Thread paintThread;
  private boolean painting;
  private boolean computing;
  private boolean erase;

  private int pixels[][];

  private long random;

  private final Form infoForm;
  private final Command infoCommand;
  private final Command exitCommand;
  private final Command okCommand;

  private static int width;
  private static int height;
  private static int blockSize;
  private static int xPixelNb;
  private static int yPixelNb;
  private static int xScreenPos;
  private static int yScreenPos;

  private static boolean isColor;

  private final static String INFO_TEXT = "[UP/DOWN] changes screen size\n[LEFT/RIGHT] changes resolution\n\nv1.0\nmaxence@javatwork.com";
  private final static int DEFAULT_BLOCK_SIZE = 4;
  private final static int DEFAULT_MAX_SCREEN_DIM = 64;
  private final static int MIN_SCREEN_DIM = 16;

  public FireCanvas(final FireDemo midlet, final Display display) {
    this.midlet = midlet;
    this.display = display;
    FireCanvas.isColor = display.isColor();
    // fire width and height are set to a multiple of 8
    FireCanvas.width = getWidth();
    FireCanvas.width = FireCanvas.width - FireCanvas.width % 8;
    FireCanvas.height = getHeight();
    FireCanvas.height = FireCanvas.height - FireCanvas.height % 8;
    while (((FireCanvas.width > FireCanvas.DEFAULT_MAX_SCREEN_DIM) || (FireCanvas.height > FireCanvas.DEFAULT_MAX_SCREEN_DIM)) && ((FireCanvas.width >= FireCanvas.MIN_SCREEN_DIM + 8) && (FireCanvas.height >= FireCanvas.MIN_SCREEN_DIM + 8))) {
      FireCanvas.width -= 8;
      FireCanvas.height -= 8;
    }
    FireCanvas.blockSize = FireCanvas.DEFAULT_BLOCK_SIZE;
    computeDimensions();
    infoCommand = new Command("Info", Command.SCREEN, 2);
    exitCommand = new Command("Exit", Command.EXIT, 2);
    addCommand(infoCommand);
    addCommand(exitCommand);
    infoForm = new Form(null);
    infoForm.append(FireCanvas.INFO_TEXT);
    okCommand = new Command("Ok", Command.OK, 2);
    infoForm.addCommand(okCommand);
    infoForm.setCommandListener(this);
    setCommandListener(this);
  }

  /**
   * Starts painting thread
   */
  public void start() {
    paintThread = new Thread(this);
    paintThread.start();
  }

  public void paint(final Graphics g) {
    if (paused) { return; }
    painting = true;
    if (erase) {
      if (FireCanvas.isColor) {
        g.setColor(255, 255, 255);
      }
      else {
        g.setGrayScale(255);
      }
      g.fillRect(0, 0, getWidth(), getHeight());
      erase = false;
    }
    int xPos;
    int yPos = FireCanvas.yScreenPos;
    if (FireCanvas.isColor) {
      for (int y = 0; y < FireCanvas.yPixelNb; y++) {
        xPos = FireCanvas.xScreenPos;
        for (int x = 0; x < FireCanvas.xPixelNb; x++) {
          g.setColor(255, pixels[x][y], 0);
          g.fillRect(xPos, yPos, FireCanvas.blockSize, FireCanvas.blockSize);
          xPos += FireCanvas.blockSize;
        }
        yPos += FireCanvas.blockSize;
      }
    }
    else {
      for (int y = 0; y < FireCanvas.yPixelNb; y++) {
        xPos = FireCanvas.xScreenPos;
        for (int x = 0; x < FireCanvas.xPixelNb; x++) {
          g.setGrayScale(pixels[x][y]);
          g.fillRect(xPos, yPos, FireCanvas.blockSize, FireCanvas.blockSize);
          xPos += FireCanvas.blockSize;
        }
        yPos += FireCanvas.blockSize;
      }
    }
    painting = false;
  }

  private void computeDimensions() {
    FireCanvas.xPixelNb = FireCanvas.width / FireCanvas.blockSize;
    FireCanvas.yPixelNb = FireCanvas.height / FireCanvas.blockSize;
    FireCanvas.xScreenPos = (getWidth() - FireCanvas.width) / 2;
    FireCanvas.yScreenPos = (getHeight() - FireCanvas.height) / 2;
    pixels = new int[FireCanvas.xPixelNb][FireCanvas.yPixelNb + 1];
  }

  /**
   * Computes a new frame
   */
  public void computePixels() {
    if (paused) {
      return;
    }
    computing = true;
    int temp1;
    int temp2;
    int temp3;
    for (int y = 0; y < FireCanvas.yPixelNb; y++) {
      temp2 = pixels[0][y + 1];
      temp3 = pixels[1][y + 1];
      pixels[0][y] = (pixels[0][y] + temp2 + temp3) / 3;
      for (int x = 1; x < FireCanvas.xPixelNb - 1; x++) {
        temp1 = temp2;
        temp2 = temp3;
        temp3 = pixels[x + 1][y + 1];
        // New pixel is an average of 4 surrounding pixels
        // Bit shifting is cheaper than integer division
        pixels[x][y] = (pixels[x][y] + temp1 + temp2 + temp3) >> 2;
      }
      pixels[FireCanvas.xPixelNb - 1][y] = (pixels[FireCanvas.xPixelNb - 1][y] + temp2 + temp3) / 3;
    }
    // Randomize last line (this line is not displayed)
    for (int x = 0; x < FireCanvas.xPixelNb; x++) {
      // Cheap random
      random = random + System.currentTimeMillis();
      pixels[x][FireCanvas.yPixelNb] = (int) (random % 255);
    }
    computing = false;
  }

  public void commandAction(final Command c, final Displayable s) {
    if (c == infoCommand) {
      // Waits for current painting and computing jobs to finish
      paused = true;
      while (painting || computing) {
        try {
          Thread.sleep(5);
        }
        catch (final Exception e) {
          //
        }
      }
      display.setCurrent(infoForm);
    }
    else if (c == okCommand) {
      // Let's get back to work
      paused = false;
      display.setCurrent(this);
    }
    else if (c == exitCommand) {
      midlet.destroyApp(true);
      midlet.notifyDestroyed();
    }
  }

  public void keyPressed(final int keyCode) {
    while (painting) {
      try {
        Thread.sleep(5);
      }
      catch (final Exception e) {
        //
      }
    }
    paused = true;
    switch (getGameAction(keyCode)) {
      case UP:
        if ((FireCanvas.width < getWidth() - 7) && (FireCanvas.height < getHeight() - 7)) {
          FireCanvas.width += 8;
          FireCanvas.height += 8;
        }
        break;
      case DOWN:
        if ((FireCanvas.width >= FireCanvas.MIN_SCREEN_DIM + 8) && (FireCanvas.height >= FireCanvas.MIN_SCREEN_DIM + 8)) {
          FireCanvas.width -= 8;
          FireCanvas.height -= 8;
          erase = true;
        }
        break;
      case LEFT:
        if (FireCanvas.blockSize > 1) {
          FireCanvas.blockSize /= 2;
        }
        break;
      case RIGHT:
        if (FireCanvas.blockSize < 8) {
          FireCanvas.blockSize *= 2;
        }
        break;
      default:
        paused = false;
        return;
    }
    computeDimensions();
    paused = false;
  }

  public void run() {
    while (true) {
      while (paused) {
        // Displays up to 33 fps
        try {
          Thread.sleep(30);
        }
        catch (final Exception e) {
          //
        }
      }
      repaint();
      // Forces paint requests to be served
      serviceRepaints();
    }
  }

}
