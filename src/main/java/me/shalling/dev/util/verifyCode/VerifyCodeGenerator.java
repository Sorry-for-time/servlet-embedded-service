package me.shalling.dev.util.verifyCode;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

import static me.shalling.dev.util.verifyCode.BasicFormulaTool.*;

/**
 * a formula verify code generator
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/3/2 11:17
 */
public abstract class VerifyCodeGenerator implements Serializable {
  @Serial
  private static final long serialVersionUID = 5476294642449893222L;

  /**
   * the default preset image width
   */
  private static final int WIDTH = 80;

  /**
   * the default preset height
   */
  private static final int HEIGHT = 30;

  /**
   * the default character length
   */
  private static final int CHAR_LEN = 4;

  private static final int PRESET_FONT_SIZE = 26;

  /**
   * the static alphabet char
   */
  private final static char[] ALPHABETS = {
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
    'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
    'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
    'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
    'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
    'y', 'z'
  };

  /**
   * the mixed char code, includes number and alphabets
   */
  private final static char[] MIX_CHARACTERS = {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
    'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
    'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
    'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
    'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
    'y', 'z'
  };

  @Data
  @AllArgsConstructor
  public static class VerifyCodeResult {
    private String pattern;
    private BufferedImage bufferedImage;
  }

  private static BufferedImage createVerifyCodeImage(String text, int width, int height, int fontSize, int fontStyleCode) {
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics graphics = bufferedImage.getGraphics();

    // fill background
    Color backgroundColor = getBasicRandom();
    graphics.setColor(backgroundColor);
    graphics.fillRect(0, 0, width, height);

    // set font style
    Color oppositeColor = getOppositeRandomColor(backgroundColor);
    graphics.setColor(oppositeColor);
    graphics.setFont(new Font("Consolas", fontStyleCode, fontSize));

    Random random = new Random();

    /*
     * fill disorder graph
     *  */
    // draw line
    for (int i = 0; i < 6; ++i) {
      graphics.setColor(getBasicRandom());
      graphics.drawRect(random.nextInt(width), random.nextInt(height), 2, 2);
    }
    // draw irregular block
    for (int i = 0; i < 30; ++i) {
      graphics.setColor(getBasicRandom());
      graphics.drawRect(
        random.nextInt(width),
        random.nextInt(height),
        random.nextInt(width),
        random.nextInt(height));
    }

    // fill text
    graphics.drawString(
      text,
      random.nextInt(width * 2 / (text.length())),
      random.nextInt(height / 2) + height / 2
    );

    return bufferedImage;
  }

  public static VerifyCodeResult createPrettyAlphabetVerifyCode() {
    return createPrettyAlphabetVerifyCode(CHAR_LEN, WIDTH, HEIGHT, PRESET_FONT_SIZE, Font.BOLD);
  }

  public static VerifyCodeResult createPrettyAlphabetVerifyCode(int charLen, int width, int height, int fontSize, int styleCode) {
    StringBuilder strBuilder = new StringBuilder(charLen);
    for (int i = 0; i < charLen; ++i) {
      strBuilder.append(ALPHABETS[(int) (Math.random() * ALPHABETS.length)]);
    }
    String text = strBuilder.toString();
    return new VerifyCodeResult(
      text,
      createVerifyCodeImage(text, width, height, fontSize, styleCode)
    );
  }

  public static VerifyCodeResult createMixCharacterVerifyCode() {
    return createMixCharacterVerifyCode(CHAR_LEN, WIDTH, HEIGHT, PRESET_FONT_SIZE, Font.BOLD);
  }

  public static VerifyCodeResult createMixCharacterVerifyCode(int charLen, int width, int height, int fontSize, int styleCode) {
    StringBuilder strBuilder = new StringBuilder(charLen);
    for (int i = 0; i < charLen; ++i) {
      strBuilder.append(MIX_CHARACTERS[(int) (Math.random() * MIX_CHARACTERS.length)]);
    }
    String text = strBuilder.toString();
    return new VerifyCodeResult(
      text,
      createVerifyCodeImage(text, width, height, fontSize, styleCode)
    );
  }

  public static VerifyCodeResult createFormulaVerifyCode() {
    return createFormulaVerifyCode(3, 10, 150, 50, 27, Font.BOLD);
  }

  public static VerifyCodeResult createFormulaVerifyCode(
    int digitCount,
    int digitMaxValue,
    int width,
    int height,
    int fontSize,
    int styleCode
  ) {
    String formulaText = createFormulaText(digitCount, digitMaxValue, " ");
    List<String> suffixTokens = getFormulaSuffixTokenList(formulaText, " ");
    int reducedValue = reduceTokenList(suffixTokens);
    String writeText = formulaText + " =?";
    return new VerifyCodeResult(
      String.valueOf(reducedValue),
      createVerifyCodeImage(writeText.replace(" ", ""), width, height, fontSize, styleCode)
    );
  }

  /**
   * get a random rgb color object
   *
   * @return the random color object
   */
  private static Color getBasicRandom() {
    Random random = new Random();
    int red = random.nextInt(255);
    int green = random.nextInt(255);
    int blue = random.nextInt(255);
    return new Color(red, green, blue);
  }

  /**
   * get opposite color object
   *
   * @param color the source color object
   * @return the opposite color value object
   */
  private static Color getOppositeRandomColor(Color color) {
    Random random = new Random();
    int red = 255 - color.getRed();
    int green = 255 - color.getGreen();
    int blue = 255 - color.getBlue();
    return new Color(random.nextInt(red), random.nextInt(green), random.nextInt(blue));
  }
}
