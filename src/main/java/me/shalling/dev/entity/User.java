package me.shalling.dev.entity;

import lombok.Data;

/**
 * 用户信息实体类
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/22 20:11
 */
@Data
public class User {
  private Integer id;

  private String username;

  private String password;
}
