package me.shalling.dev.service;

import me.shalling.dev.vo.dto.BookDTO;
import me.shalling.dev.vo.dto.BookListDTO;
import me.shalling.dev.vo.dto.PaginationDTO;

import java.sql.SQLException;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/23 0:06
 */
public interface BookService {
  /**
   * 根据分页参数获取图书列表信息, 和附带的总记录信息等
   *
   * @param paginationDTO 分页参数
   * @return 查询结果列表
   */
  BookListDTO getBookListByPaginationParams(PaginationDTO paginationDTO) throws Exception;

  /**
   * 更新书籍信息
   *
   * @param dto 新的书籍信息
   * @return 影响的行数
   * @throws Exception 执行过程中发生的异常
   */
  Integer updateBook(BookDTO dto) throws Exception;

  Integer deleteBookByIdList(Integer[] idList) throws SQLException;
}
