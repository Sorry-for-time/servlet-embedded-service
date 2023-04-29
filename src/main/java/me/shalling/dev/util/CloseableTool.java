package me.shalling.dev.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/22 21:30
 */
public class CloseableTool {
  public static void closeAll(Closeable... closeables) {
    for (Closeable closeable : closeables) {
      if (closeable != null) {
        try {
          closeable.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}
