package me.shalling.dev.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 统一返回视图结果
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/22 15:42
 */
@Data
@Accessors(chain = true)
public class Result<T> {
  /**
   * 请求状态码
   */
  private Integer statusCode;

  /**
   * 描述信息
   */
  private String msg;

  /**
   * 数据
   */
  private T data;
}
