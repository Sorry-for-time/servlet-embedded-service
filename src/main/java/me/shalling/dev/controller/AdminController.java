package me.shalling.dev.controller;

import framework.stereotype.RestControllerLeft;
import framework.stereotype.method.PostMappingLeft;
import framework.stereotype.params.PostBody;
import me.shalling.dev.constant.StatusCode;
import me.shalling.dev.service.BookService;
import me.shalling.dev.service.impl.BookServiceImpl;
import me.shalling.dev.vo.Result;
import me.shalling.dev.vo.dto.BookListDTO;
import me.shalling.dev.vo.dto.PaginationDTO;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/23 0:02
 */
@RestControllerLeft("/admin")
public class AdminController {
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
}
