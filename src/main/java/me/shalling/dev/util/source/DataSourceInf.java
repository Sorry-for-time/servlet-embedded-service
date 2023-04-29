package me.shalling.dev.util.source;

import java.io.Serializable;
import java.sql.Connection;

/**
 * 数据池基本定义
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/2/21 17:13
 */
public interface DataSourceInf extends Serializable {
  /**
   * 获取数据库连接对象
   *
   * @return 数据库连接对象
   */
  Connection getConnection();

  /**
   * 回收数据库连接对象
   *
   * @param connection 数据库连接对象
   */
  void close(Connection connection);
}
