package me.shalling.dev.service.impl;

import me.shalling.dev.dao.BookDAO;
import me.shalling.dev.service.BookService;
import me.shalling.dev.vo.dto.BookDTO;
import me.shalling.dev.vo.dto.BookListDTO;
import me.shalling.dev.vo.dto.PaginationDTO;

import java.sql.SQLException;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/23 15:03
 */
public class BookServiceImpl implements BookService {
  private final BookDAO bookDAO = new BookDAO();

  @Override
  public BookListDTO getBookListByPaginationParams(PaginationDTO paginationDTO)
    throws Exception {
    return bookDAO.getBookList(paginationDTO);
  }

  @Override
  public Integer updateBook(BookDTO dto) throws Exception {
    return bookDAO.updateBook(dto);
  }

  @Override
  public Integer deleteBookByIdList(Integer[] idList) throws SQLException {
    return bookDAO.deleteBook(idList);
  }
}
