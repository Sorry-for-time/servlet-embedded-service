package me.shalling.dev.vo.dto;

import lombok.Data;

/**
 * 分页请求基本参数
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/23 0:06
 */
@Data
public class PaginationDTO {
  /**
   * 当前页
   */
  private Integer currentPage;

  /**
   * 每页大小
   */
  private Integer size;
}
