package me.shalling.dev.util.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 简单分页实现, 默认会尝试进行边界纠错设置
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/2/25 12:40
 */
public final class PaginationParamRegulateTool implements Serializable {
  @Serial
  private static final long serialVersionUID = -4286361694739957293L;

  @Data
  @Accessors(chain = true)
  @NoArgsConstructor(force = true)
  @AllArgsConstructor
  public static class PaginationParamsRequire {
    /**
     * 当前页
     */
    private Integer currentPage;

    /**
     * 显示条数
     */
    private Integer size;

    /**
     * 总记录数
     */
    @NonNull
    private Integer rows;
  }

  /**
   * 根据已有条件规范合理化前端传递的分页参数
   *
   * @param paramsRequire  基本分页参数
   * @param allowedMaxSize 允许的最大每页显示条数
   * @return 经过边界合法处理的分页参数
   */
  public static PaginationParamsRequire getLegalPaginationParam(
    PaginationParamsRequire paramsRequire,
    Integer allowedMaxSize
  ) {
    // 获取读取到的参数项
    Integer size = paramsRequire.size;
    Integer currentPage = paramsRequire.currentPage;
    Integer rows = paramsRequire.rows;

    // 如果每页显示条数为 0, 就设置一个初始值
    if (size == null || size < 1) {
      size = 10;
      currentPage = 1;
    }

    // 如果当前页非法, 那么就设置一个初始值
    if (currentPage == null || currentPage < 1) {
      currentPage = 1;
    }

    // 如果每页显示条数超过了允许的最大数, 那么就设置为最大数
    if (size > allowedMaxSize) {
      size = allowedMaxSize;
    }

    // 如果 每页显示大小*页数 超过了实际记录数, 那么进行范围处理
    if ((currentPage - 1) * size >= rows) {
      // 如果存在记录数
      if (rows > 0) {
        // 获取尾号页数
        int tmp = rows / size;

        // 获取尾页剩余条数
        long leavedValue = rows % size;

        // 查询条件方式: [ (currentPage - 1) * size, size ]
        if (tmp > 1) {
          // 如果存在记录数不满足 size 的尾页, 那么将 tmp + 1
          if (leavedValue != 0) {
            currentPage = tmp + 1;
          } else {
            // 如果刚好完整一页, 那么直接替换
            currentPage = tmp;
          }
        } else {
          currentPage = 1;
        }
      }
      // 如果 记录数*页数 超过实际记录数, 那么直接设置成第一页
      else {
        currentPage = 1;
      }
    }

    // 返回一个合理化的分页参数对象
    return new PaginationParamsRequire(currentPage, size, rows);
  }
}
