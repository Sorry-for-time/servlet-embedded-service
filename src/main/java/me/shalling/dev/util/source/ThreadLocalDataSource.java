package me.shalling.dev.util.source;

import me.shalling.dev.container.ConfigProvider;
import me.shalling.dev.container.config.base.DataSource;
import me.shalling.dev.util.source.impl.ConnectionPoolImpl;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 通过 ThreadLocal 关联数据连接对象
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/2/21 15:22
 */
public final class ThreadLocalDataSource implements Serializable {
  @Serial
  private static final long serialVersionUID = -6566092881099432854L;

  /**
   * 数据源
   */
  private static final DataSourceInf DATA_SOURCE;

  /**
   * 线程映射对象
   */
  private static final ThreadLocal<Connection> LOCAL;

  private ThreadLocalDataSource() {
  }

  // 初始化
  static {
    DataSource dataSource = ConfigProvider.getConfiguration().getDataSource();
    try {
      Class.forName(dataSource.getDriver());
      DATA_SOURCE = new ConnectionPoolImpl(dataSource);
      LOCAL = new ThreadLocal<>();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 获取数据库连接对象
   *
   * @return 数据库连接对象
   * @throws SQLException SQLException
   */
  public static Connection getConnection() throws SQLException {
    // 先尝试获取当前线程对象
    Connection connection = LOCAL.get();
    // 如果存在, 直接返回, 否则尝试从连接池对象获取
    if (connection != null) {
      return connection;
    } else {
      Connection newConnection = DATA_SOURCE.getConnection();
      // 关联到线程
      LOCAL.set(newConnection);
      return LOCAL.get();
    }
  }

  /**
   * 释放当前线程所占用的数据库连接对象, 回收进连接池
   *
   * @throws SQLException SQLException
   */
  public static void close() throws SQLException {
    Connection connection = LOCAL.get();
    if (connection != null) {
      LOCAL.remove();
      DATA_SOURCE.close(connection);
    }
  }
}
