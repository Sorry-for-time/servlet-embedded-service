package me.shalling.dev.dao;

import me.shalling.dev.util.orm.ORMUtil;
import me.shalling.dev.util.pagination.PaginationParamRegulateTool;
import me.shalling.dev.util.source.ThreadLocalDataSource;
import me.shalling.dev.vo.dto.BookDTO;
import me.shalling.dev.vo.dto.BookListDTO;
import me.shalling.dev.vo.dto.PaginationDTO;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/23 0:03
 */
public class BookDAO {
  /**
   * 获取统计数
   *
   * @return 条目数
   */
  public Long getRows() throws SQLException {
    Connection connection = ThreadLocalDataSource.getConnection();
    // 获取所有记录数
    String sql = """
      SELECT COUNT(*)  FROM tb_book
      """;
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    ResultSet resultSet = preparedStatement.executeQuery();
    resultSet.next();
    return resultSet.getLong(1);
  }

  /**
   * 获取分页数据
   *
   * @param dto 分页参数
   * @return 包装参数
   */
  public BookListDTO getBookList(PaginationDTO dto)
    throws SQLException,
    InvocationTargetException,
    InstantiationException,
    IllegalAccessException,
    NoSuchMethodException {
    Long rows = getRows();
    PaginationParamRegulateTool
      .PaginationParamsRequire
      legalPaginationParam = PaginationParamRegulateTool
      .getLegalPaginationParam(
        new PaginationParamRegulateTool.PaginationParamsRequire(
          (dto.getCurrentPage()),
          dto.getSize(),
          rows
        ), 100
      );
    Connection connection = ThreadLocalDataSource.getConnection();

    String sql = """
      SELECT id, typeId, name, price, description, pic, publish, author, stock, address FROM  tb_book LIMIT ?, ?
      """;
    PreparedStatement preparedStatement = connection.prepareStatement(sql);

    // limit (currentPage - 1) * size, size
    preparedStatement.setObject(1, legalPaginationParam.getCurrentPage() - 1);
    preparedStatement.setObject(2, legalPaginationParam.getSize());
    ResultSet resultSet = preparedStatement.executeQuery();
    List<BookDTO> bookDTOS = ORMUtil.selectForList(resultSet, BookDTO.class);
    return new BookListDTO(legalPaginationParam.getCurrentPage(), legalPaginationParam.getRows(), rows, bookDTOS);
  }
}
