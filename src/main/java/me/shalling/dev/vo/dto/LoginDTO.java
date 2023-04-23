package me.shalling.dev.vo.dto;

import lombok.Data;

/**
 * 用户登录信息
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/22 15:45
 */
@Data
public class LoginDTO {
  /**
   * 用户名
   */
  private String username;

  /**
   * 登录密码
   */
  private String password;

  /**
   * 验证码
   */
  private String verifyCode;
}
