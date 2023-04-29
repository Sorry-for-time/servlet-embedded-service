package me.shalling.dev.vo.dto;

import lombok.Data;

/**
 * 用户信息数据库匹配结果
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/22 16:25
 */
@Data
public class VerifyDTO {
  /**
   * 用户信息数据库匹配是否通过
   */
  private boolean matched;

  /**
   * 附带描述信息
   */
  private String msg;
}
