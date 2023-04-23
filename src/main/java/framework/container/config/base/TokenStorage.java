package framework.container.config.base;

import me.shalling.dev.interceptor.meta.TokenMeta;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
