package me.shalling.dev.controller;

import framework.stereotype.RestControllerLeft;
import framework.stereotype.method.PostMappingLeft;
import framework.stereotype.params.PostBody;
import me.shalling.dev.constant.StatusCode;
import me.shalling.dev.service.BookService;
import me.shalling.dev.service.impl.BookServiceImpl;
import me.shalling.dev.vo.Result;
import me.shalling.dev.vo.dto.BookDTO;
import me.shalling.dev.vo.dto.BookListDTO;
import me.shalling.dev.vo.dto.PaginationDTO;

import java.sql.SQLException;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/23 0:02
 */
@RestControllerLeft("/admin/book")
public class BookController {
  private final BookService bookService = new BookServiceImpl();

  /**
   * 根据前端传递的分页参数获取所有的
   *
   * @param dto 前端传递的分页餐宿
   * @return 查询结果
   * @throws Exception 执行发生的异常
   */
  @PostMappingLeft("/book-list")
  public Result<BookListDTO> getBookList(@PostBody PaginationDTO dto)
    throws Exception {
    return new Result<BookListDTO>()
      .setStatusCode(StatusCode.OK)
      .setMsg("请求成功")
      .setData(bookService.getBookListByPaginationParams(dto));
  }

  @PostMappingLeft("/edit")
  public Result<Integer> updateBook(@PostBody BookDTO bookDTO) throws Exception {
    Integer effectedRows = bookService.updateBook(bookDTO);
    return new Result<Integer>()
      .setStatusCode(StatusCode.OK)
      .setData(effectedRows)
      .setMsg("影响的记录数: " + effectedRows);
  }

  @PostMappingLeft("/delete")
  public Result<Integer> deleteBooks(@PostBody Integer[] idList) throws SQLException {
    Integer effectedRows = bookService.deleteBookByIdList(idList);
    return new Result<Integer>()
      .setMsg("影响的行数: " + effectedRows)
      .setData(effectedRows)
      .setStatusCode(StatusCode.OK);
  }
}
