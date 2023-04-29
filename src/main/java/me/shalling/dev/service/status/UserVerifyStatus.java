package me.shalling.dev.service.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户验证信息
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/22 21:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVerifyStatus {
  /**
   * 是否匹配
   */
  private Boolean matched;

  /**
   * 匹配结果描述
   */
  private String msg;
}
