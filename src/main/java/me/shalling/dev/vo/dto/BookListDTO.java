package me.shalling.dev.vo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 书籍查询结果信息
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/23 0:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BookListDTO {
  /**
   * 当前页
   */
  private Integer currentPage;

  /**
   * 总记录数
   */
  private Integer rows;

  /**
   * 页显示条目数量
   */
  private Integer size;

  /**
   * 查询结果
   */
  private List<BookDTO> bookList;
}
