package me.shalling.dev.service;

import me.shalling.dev.vo.dto.BookListDTO;
import me.shalling.dev.vo.dto.PaginationDTO;

import java.lang.reflect.InvocationTargetException;
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
  BookListDTO getBookListByPaginationParams(PaginationDTO paginationDTO) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException;
}
