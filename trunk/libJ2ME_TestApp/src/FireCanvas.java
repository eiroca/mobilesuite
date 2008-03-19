import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;

public final class FireCanvas extends Canvas implements CommandListener, Runnable {

  private FireDemo midlet;
  private Display display;

  private boolean paused;
  private Thread paintThread;
  private boolean painting;
  private boolean computing;
  private boolean erase;

  private int pixels[][];

  private long random;

  private Form infoForm;
  private Command infoCommand;
  private Command exitCommand;
  private Command okCommand;

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

  public FireCanvas(FireDemo midlet, Display display) {
    this.midlet = midlet;
    this.display = display;
    isColor = display.isColor();
    // fire width and height are set to a multiple of 8
    width = getWidth();
    width = width - width % 8;
    height = getHeight();
    height = height - height % 8;
    while ((width > DEFAULT_MAX_SCREEN_DIM || height > DEFAULT_MAX_SCREEN_DIM) && (width >= MIN_SCREEN_DIM + 8 && height >= MIN_SCREEN_DIM + 8)) {
      width -= 8;
      height -= 8;
    }
    blockSize = DEFAULT_BLOCK_SIZE;
    computeDimensions();
    infoCommand = new Command("Info", Command.SCREEN, 2);
    exitCommand = new Command("Exit", Command.EXIT, 2);
    addCommand(infoCommand);
    addCommand(exitCommand);
    infoForm = new Form(null);
    infoForm.append(INFO_TEXT);
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

  public void paint(Graphics g) {
    if (paused) {
      return;
    }
    painting = true;
    if (erase) {
      if (isColor) {
        g.setColor(255, 255, 255);
      }
      else {
        g.setGrayScale(255);
      }
      g.fillRect(0, 0, getWidth(), getHeight());
      erase = false;
    }
    int xPos;
    int yPos = yScreenPos;
    if (isColor) {
      for (int y = 0; y < yPixelNb; y++) {
        xPos = xScreenPos;
        for (int x = 0; x < xPixelNb; x++) {
          g.setColor(255, pixels[x][y], 0);
          g.fillRect(xPos, yPos, blockSize, blockSize);
          xPos += blockSize;
        }
        yPos += blockSize;
      }
    }
    else {
      for (int y = 0; y < yPixelNb; y++) {
        xPos = xScreenPos;
        for (int x = 0; x < xPixelNb; x++) {
          g.setGrayScale(pixels[x][y]);
          g.fillRect(xPos, yPos, blockSize, blockSize);
          xPos += blockSize;
        }
        yPos += blockSize;
      }
    }
    painting = false;
  }

  private void computeDimensions() {
    xPixelNb = width / blockSize;
    yPixelNb = height / blockSize;
    xScreenPos = (getWidth() - width) / 2;
    yScreenPos = (getHeight() - height) / 2;
    pixels = new int[xPixelNb][yPixelNb + 1];
  }

  /**
   * Computes a new frame
   */
  public void computePixels() {
    if (paused) return;
    computing = true;
    int temp1;
    int temp2;
    int temp3;
    for (int y = 0; y < yPixelNb; y++) {
      temp2 = pixels[0][y + 1];
      temp3 = pixels[1][y + 1];
      pixels[0][y] = (pixels[0][y] + temp2 + temp3) / 3;
      for (int x = 1; x < xPixelNb - 1; x++) {
        temp1 = temp2;
        temp2 = temp3;
        temp3 = pixels[x + 1][y + 1];
        // New pixel is an average of 4 surrounding pixels
        // Bit shifting is cheaper than integer division
        pixels[x][y] = (pixels[x][y] + temp1 + temp2 + temp3) >> 2;
      }
      pixels[xPixelNb - 1][y] = (pixels[xPixelNb - 1][y] + temp2 + temp3) / 3;
    }
    // Randomize last line (this line is not displayed)
    for (int x = 0; x < xPixelNb; x++) {
      // Cheap random
      random = random + System.currentTimeMillis();
      pixels[x][yPixelNb] = (int) (random % 255);
    }
    computing = false;
  }

  public void commandAction(Command c, Displayable s) {
    if (c == infoCommand) {
      // Waits for current painting and computing jobs to finish
      paused = true;
      while (painting || computing) {
        try {
          Thread.sleep(5);
        }
        catch (Exception e) {
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

  public void keyPressed(int keyCode) {
    while (painting) {
      try {
        Thread.sleep(5);
      }
      catch (Exception e) {
        //
      }
    }
    paused = true;
    switch (getGameAction(keyCode)) {
      case UP:
        if (width < getWidth() - 7 && height < getHeight() - 7) {
          width += 8;
          height += 8;
        }
        break;
      case DOWN:
        if (width >= MIN_SCREEN_DIM + 8 && height >= MIN_SCREEN_DIM + 8) {
          width -= 8;
          height -= 8;
          erase = true;
        }
        break;
      case LEFT:
        if (blockSize > 1) {
          blockSize /= 2;
        }
        break;
      case RIGHT:
        if (blockSize < 8) {
          blockSize *= 2;
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
        catch (Exception e) {
          //
        }
      }
      repaint();
      // Forces paint requests to be served
      serviceRepaints();
    }
  }

}
