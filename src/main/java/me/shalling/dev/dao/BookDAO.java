package me.shalling.dev.dao;

import me.shalling.dev.util.orm.ORMUtil;
import me.shalling.dev.util.pagination.PaginationParamRegulateTool;
import me.shalling.dev.util.source.ThreadLocalDataSource;
import me.shalling.dev.vo.dto.BookDTO;
import me.shalling.dev.vo.dto.BookListDTO;
import me.shalling.dev.vo.dto.PaginationDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
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
  public Integer getRows() throws SQLException {
    Connection connection = ThreadLocalDataSource.getConnection();
    // 获取所有记录数
    String sql = """
      SELECT COUNT(*) FROM tb_book
      """;
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    ResultSet resultSet = preparedStatement.executeQuery();
    resultSet.next();
    return resultSet.getInt(1);
  }

  /**
   * 获取分页数据
   *
   * @param dto 分页参数
   * @return 包装参数
   */
  public BookListDTO getBookList(PaginationDTO dto) throws Exception {
    Integer rows = getRows();
    System.out.println("rows: " + rows);
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

  /**
   * 更新书籍信息
   *
   * @param bookDTO 新的书籍信息
   * @return 影响行数
   * @throws Exception 执行异常
   */
  public Integer updateBook(BookDTO bookDTO) throws SQLException {
    Connection connection = null;
    try {
      connection = ThreadLocalDataSource.getConnection();
      int effectedRows;
      String sql = """
        UPDATE tb_book SET typeId=?, name=?, price=?, description=?, pic=?, publish=?, author=?, stock=?, address=?
         WHERE id = ?
        """;
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setObject(1, bookDTO.getTypeId());
      preparedStatement.setObject(2, bookDTO.getName());
      preparedStatement.setObject(3, bookDTO.getPrice());
      preparedStatement.setObject(4, bookDTO.getDescription());
      preparedStatement.setObject(5, bookDTO.getPic());
      preparedStatement.setObject(6, bookDTO.getPublish());
      preparedStatement.setObject(7, bookDTO.getAddress());
      preparedStatement.setObject(8, bookDTO.getStock());
      preparedStatement.setObject(9, bookDTO.getAddress());
      preparedStatement.setObject(10, bookDTO.getId());
      effectedRows = preparedStatement.executeUpdate();
      connection.commit();
      return effectedRows;
    } catch (Exception e) {
      if (connection != null) {
        connection.rollback();
      }
      throw new RuntimeException(e);
    }
  }

  public Integer deleteBook(Integer[] idList) throws SQLException {
    Connection connection = null;
    try {
      connection = ThreadLocalDataSource.getConnection();
      String sql = """
        DELETE FROM tb_book WHERE id=?
        """;
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      for (Integer integer : idList) {
        preparedStatement.setObject(1, integer);
        preparedStatement.addBatch();
      }
      int[] ints = preparedStatement.executeBatch();
      return Arrays.stream(ints).reduce(0, Integer::sum);
    } catch (Exception e) {
      if (connection != null) {
        connection.rollback();
      }
      throw new RuntimeException(e);
    }
  }
}
