package me.shalling.dev.vo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户基信息
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/22 16:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
  /**
   * 用户名
   */
  private String username;

  /**
   * 登录密码
   */
  private String password;
}
