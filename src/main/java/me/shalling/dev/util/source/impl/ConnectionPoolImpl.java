package me.shalling.dev.util.source.impl;


import me.shalling.dev.container.config.base.DataSource;
import me.shalling.dev.util.source.DataSourceInf;

import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 简单连接池实现, 基于双端队列
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/2/21 16:24
 */
public final class ConnectionPoolImpl implements DataSourceInf {
  @Serial
  private static final long serialVersionUID = -1113189080734062693L;

  /**
   * 数据库隔离级别, 默认为: TRANSACTION_READ_COMMITTED(2)
   */
  private final static int ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;
  public static final int MAX_WAIT = 5000;

  /**
   * 存储线程的双端队列
   */
  private final ConcurrentLinkedDeque<Connection> connectionDeque;

  public ConnectionPoolImpl(DataSource config) {
    this.connectionDeque = new ConcurrentLinkedDeque<>();
    // 既然小于 1, 那么没意义...
    if (config.getMax() < 1) {
      throw new RuntimeException("the connect count must greater than `1`");
    }
    // 注册驱动, 确保兼容低版本
    try {
      Class.forName(config.getDriver());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    // 初始化连接池, 填充连接对象
    try {
      for (int i = 0; i < config.getMax(); i++) {
        Connection connection = DriverManager
          .getConnection(config.getUrl(), config.getUser(), config.getPassword());

        // 设置隔离级别和关闭自动提交
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(ISOLATION_LEVEL);
        // 添加进队列
        this.connectionDeque.add(connection);
      }
    } catch (SQLException exception) {
      throw new RuntimeException(exception);
    }
  }

  /**
   * 尝试获取数据库连接对象 <br>
   * 如果再 5s 内通过自旋未能取得连接, 那么抛出超时提示异常
   *
   * @return 数据库连接对象
   */
  public Connection getConnection() {
    Connection connection;
    // 尝试获取, 直到取得对象
    long start = System.currentTimeMillis();
    for (; ; ) {
      // 如果起始时间 + 5s 后小于当前的时间, 那么代表已经超时, 直接提示获取超时
      if (start + MAX_WAIT >= System.currentTimeMillis()) {
        if ((connection = this.connectionDeque.poll()) != null) {
          return connection;
        }
      } else {
        throw new RuntimeException(
          "get connection timeout! -> time costed: "
            + (System.currentTimeMillis() - start)
            + "ms"
        );
      }
    }
  }

  /**
   * 回收连接, 并重置连接设置, 提供给下一个对象使用
   *
   * @param connection 数据库连接对象
   */
  public void close(final Connection connection) {
    try {
      // 重置连接, 方便下一个获取进行使用
      connection.commit(); // 设置提交, 防止漏提交
      connection.clearWarnings();
      connection.setAutoCommit(false);
      connection.setTransactionIsolation(ISOLATION_LEVEL);
      this.connectionDeque.add(connection);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
