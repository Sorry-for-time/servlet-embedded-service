package me.shalling.dev.util.verifyCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/3/5 16:39
 */
public abstract class BasicFormulaTool implements Serializable {
  @Serial
  private static final long serialVersionUID = -4132960683436380228L;

  /**
   * 运算符定义
   */
  private static final char[] BASIC_OPERATOR = {'+', '-', '*'};

  /**
   * 创建一个数学表达式
   *
   * @param numCount      生成的位数
   * @param numValueLimit 数字取值范围
   * @param separator     分割符
   * @return 创建好的数学表达式
   */
  public static String createFormulaText(int numCount, int numValueLimit, String separator) {
    if ("".equals(separator)) {
      throw new RuntimeException("the the separator should not an empty string, at least is \" \" ");
    }
    StringBuilder mathBuffer = new StringBuilder(numCount * 2 - 1 + (numCount * 2 - 2) * separator.length());
    final int length = BASIC_OPERATOR.length;
    Random random = new Random();
    for (int i = 0; i < numCount; i++) {
      mathBuffer.append(random.nextInt(numValueLimit) + 1);
      if (i != numCount - 1) {
        mathBuffer.append(separator);
        mathBuffer.append(BASIC_OPERATOR[random.nextInt(length)]);
        mathBuffer.append(separator);
      }
    }
    return mathBuffer.toString();
  }

  /**
   * 判断字符是否为运算符
   *
   * @param str 进行判断的字符
   * @return 字符是否为运算符的判断结果
   */
  public static boolean isOperator(String str) {
    return switch (str) {
      case "+", "-", "*", "/" -> true;
      default -> false;
    };
  }

  /**
   * 取得公式的后序序列表达式结果
   *
   * @param mathText  数学公式
   * @param separator 分割符
   * @return 后序序列表达式
   */
  public static List<String> getFormulaSuffixTokenList(String mathText, String separator) {
    var operatorStack = new Stack<String>();
    var parsedTokenList = new ArrayList<String>();
    Arrays.stream(mathText.split(separator))
      .map(String::strip)
      .filter(str -> !"".equals(str))
      .forEachOrdered(
        // 5, 8,
        // +, *
        token -> {
          switch (token) {
            case "+", "-" -> {
              if (operatorStack.isEmpty()) {
                operatorStack.add(token);
              } else {
                while (!operatorStack.isEmpty() && !"(".equals(operatorStack.peek())) {
                  parsedTokenList.add(operatorStack.pop());
                }
                operatorStack.push(token);
              }
            }
            case "*", "/" -> {
              if (operatorStack.isEmpty()) {
                operatorStack.push(token);
              } else {
                while (!operatorStack.isEmpty() &&
                  ("*".equals(operatorStack.peek()) || "/".equals(operatorStack.peek()))
                ) {
                  parsedTokenList.add(operatorStack.pop());
                }
                operatorStack.push(token);
              }
            }
            case "(" -> operatorStack.push(token);
            case ")" -> {
              while (!operatorStack.isEmpty() && !"(".equals(operatorStack.peek())) {
                parsedTokenList.add(operatorStack.pop());
              }
              operatorStack.pop();
            }
            default -> parsedTokenList.add(token);
          }
        }
      );
    while (!operatorStack.isEmpty()) {
      parsedTokenList.add(operatorStack.pop());
    }

    return parsedTokenList;
  }

  /**
   * 计算后序序列表达式的汇总结果
   *
   * @param tokens 后序序列表达式解析结果
   * @return 汇总结果
   */
  public static int reduceTokenList(List<String> tokens) {
    Stack<Integer> reduceStack = new Stack<>();
    tokens.forEach(s -> {
      if (!isOperator(s)) {
        reduceStack.push(Integer.parseInt(s));
      } else {
        Integer topFrame = reduceStack.peek();
        reduceStack.pop();
        Integer lowerFrame = reduceStack.pop();
        int newVal = switch (s) {
          case "+" -> lowerFrame + topFrame;
          case "-" -> lowerFrame - topFrame;
          case "*" -> lowerFrame * topFrame;
          case "/" -> lowerFrame / topFrame;
          default -> throw new RuntimeException("illegal operator: " + s);
        };
        reduceStack.push(newVal);
      }
    });
    return reduceStack.peek();
  }
}
