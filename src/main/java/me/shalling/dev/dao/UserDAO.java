package me.shalling.dev.dao;

import me.shalling.dev.entity.User;
import me.shalling.dev.util.orm.ORMUtil;
import me.shalling.dev.util.source.ThreadLocalDataSource;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/22 21:13
 */
public class UserDAO {
  public User getUser(String username) throws
    SQLException,
    InvocationTargetException,
    NoSuchMethodException,
    InstantiationException,
    IllegalAccessException {
    // connection 不要手动释放
    Connection connection = ThreadLocalDataSource.getConnection();
    String sql = """
      SELECT id, username, password FROM tb_user WHERE username = ?
      """;
    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    // AutoCloseable 对象不需要自己关闭
    preparedStatement.setObject(1, username);
    ResultSet resultSet = preparedStatement.executeQuery();
    return ORMUtil.selectForOne(resultSet, User.class);
  }
}
