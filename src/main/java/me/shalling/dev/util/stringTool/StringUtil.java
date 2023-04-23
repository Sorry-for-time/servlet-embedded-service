package me.shalling.dev.util.stringTool;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/2/19 22:35
 */
public final class StringUtil implements Serializable {
  @Serial
  private static final long serialVersionUID = 7699271929708754286L;

  private StringUtil() {
  }

  /**
   * close all separators and use the camel style format sql column name
   *
   * @param originColumnName sql column name
   * @param separator        the separator
   * @return camel style column name
   */
  public static String transformAllSeparatorsToCamelCase(final String originColumnName, final String separator) {
    if (originColumnName.length() <= 1) {
      return originColumnName;
    }
    // a new value to back
    String formattedName = originColumnName.intern();

    int loc = formattedName.indexOf(separator);
    while (loc != -1) {
      String needToUppercaseChar = formattedName.substring(loc + 1, loc + 2).toUpperCase();
      String endString = formattedName.substring(loc + 2);
      formattedName = formattedName.substring(0, loc) + needToUppercaseChar + endString;
      // update new index for next loop operation
      loc = formattedName.indexOf(separator);
    }
    return formattedName;
  }

  /**
   * transform the first letter to uppercase
   *
   * @param fieldName the prepared transform name
   * @return first letter uppercase field name
   */
  public static String firstLetterUppercase(final String fieldName) {
    String upperCase = String.valueOf(fieldName.toCharArray()[0]).toUpperCase();
    if (fieldName.length() == 1) {
      return upperCase;
    } else {
      return upperCase + fieldName.substring(1);
    }
  }
}
