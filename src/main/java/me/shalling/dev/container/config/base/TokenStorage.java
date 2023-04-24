package me.shalling.dev.container.config.base;

import me.shalling.dev.interceptor.meta.TokenMeta;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用于存储全局 token 设置
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/22 16:30
 */
public class TokenStorage {
  private volatile static Map<String, TokenMeta> TOKEN_MAP;

  private TokenStorage() {
  }

  /**
   * 定期清理时间
   */
  public static final int TIMES = 1000 * 60 * 6;

  /**
   * 最大尝试数
   */
  private static final int MAX_RETRY_COUNT = 10;

  /**
   * 初始记录
   */
  private final static AtomicInteger initialRecord = new AtomicInteger(0);

  private static void tryCleanup() {
    new Thread(() -> {
      try {
        for (; ; ) {
          // noinspection BusyWait
          Thread.sleep(TIMES);
          getTokenMapSingleton().forEach((k, v) -> {
            var current = System.currentTimeMillis();
            if (current - v.getCratedTime() - v.getExpiredTime() >= 0) {
              getTokenMapSingleton().remove(k);
            }
          });
        }
      } catch (InterruptedException e) {
        System.out.println("expired token clean up thread: " + Thread.currentThread().getName() + "; " + e.getCause().getMessage());
        if (initialRecord.get() < MAX_RETRY_COUNT) {
          initialRecord.incrementAndGet();
          // retry
          tryCleanup();
        }
      }
    }).start();
  }

  static {
    // 启动清理线程
    tryCleanup();
  }

  /**
   * 获取 tokenMap 单例对象
   *
   * @return tokenMap 单例对象
   */
  public static Map<String, TokenMeta> getTokenMapSingleton() {
    if (TOKEN_MAP == null) {
      synchronized (TokenStorage.class) {
        if (TOKEN_MAP == null) {
          TOKEN_MAP = new ConcurrentHashMap<>(32);
        }
      }
    }

    return TOKEN_MAP;
  }
}
