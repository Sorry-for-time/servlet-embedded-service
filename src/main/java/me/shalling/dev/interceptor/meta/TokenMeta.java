package me.shalling.dev.interceptor.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * token 元数据信息
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/22 17:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenMeta {
  /**
   * 提供的用户对象名称
   */
  private String supportedUser;

  /**
   * uuid 标识
   */
  private String key;

  /**
   * 创建时间
   */
  private long cratedTime;

  /**
   * 持续时间(即多少 ms 后过期)
   */
  private long expiredTime;
}
