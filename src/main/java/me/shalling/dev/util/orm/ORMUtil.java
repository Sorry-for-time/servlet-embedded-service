package me.shalling.dev.util.orm;


import me.shalling.dev.util.source.ThreadLocalDataSource;
import me.shalling.dev.util.stringTool.StringUtil;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * a simple orm mapping util
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/2/19 22:34
 */
public final class ORMUtil implements Serializable {
  private ORMUtil() {
  }

  @Serial
  private static final long serialVersionUID = -4904159590402336713L;

  /**
   * reflect data basic fusion
   *
   * @param <T> entity class type template
   */
  private record MetaDataDetail<T>(Method[] allVisibleBeanMethods,
                                   Constructor<T> nonArgConstructor,
                                   Map<String, Method> methodsMap,
                                   String[] allSqlColumnNames) implements Serializable {
    @Serial
    private static final long serialVersionUID = 8888011464749874627L;

    public static <T> MetaDataDetail<T>
    MetaDetailFactory(ResultSet resultSet, Class<T> tClass)
      throws NoSuchMethodException, SQLException {
      Method[] methods = tClass.getMethods();
      Constructor<T> constructor = tClass.getConstructor();
      String[] methodNames = new String[resultSet.getMetaData().getColumnCount()];
      HashMap<String, Method> methodHashMap = new HashMap<>();

      for (Method method : methods) {
        String name = method.getName();
        if (name.contains("get") || name.contains("set")) {
          methodHashMap.put(name, method);
        }
      }

      for (int i = 0; i < methodNames.length; i++) {
        String sqlColumnName = StringUtil
          .transformAllSeparatorsToCamelCase(resultSet
              .getMetaData()
              .getColumnName(i + 1),
            "_");
        // if the sql column like is_xxx, transform it to xxx, delete `is` prefix, resolve the next match conflict
        if (sqlColumnName.indexOf("is") == 0) {
          sqlColumnName = sqlColumnName.substring(2);
        }
        methodNames[i] = sqlColumnName;
      }

      return new MetaDataDetail<>(methods, constructor, methodHashMap, methodNames);
    }
  }

  /**
   * query all result-set row data and transfer to entity then add to a list and back the list
   *
   * @param executeResult query result
   * @param mappingClass  the definition entity class(pro`  vide reflect require metadata)
   * @param <T>           mapping entity class template
   * @return the mapping entity list
   * @throws InvocationTargetException InvocationTargetException
   * @throws InstantiationException    InstantiationException
   * @throws IllegalAccessException    IllegalAccessException
   * @throws NoSuchMethodException     NoSuchMethodException
   * @throws SQLException              SQLException
   */
  public static <T> List<T> selectForList(
    ResultSet executeResult,
    Class<T> mappingClass
  ) throws
    InvocationTargetException,
    InstantiationException,
    IllegalAccessException,
    NoSuchMethodException,
    SQLException {
    // reflect data
    MetaDataDetail<T> tMetaDataDetail = MetaDataDetail.MetaDetailFactory(executeResult, mappingClass);
    List<T> resultList = new ArrayList<>();
    boolean hasNext = executeResult.next();
    while (hasNext) {
      // generate a new entity instance, avoid duplicate usage;
      T instance = tMetaDataDetail.nonArgConstructor.newInstance();
      // each the all names and mapping value use entity setter method set data
      // noinspection DuplicatedCode
      for (int i = 0; i < tMetaDataDetail.allSqlColumnNames.length; ++i) {
        Method method = tMetaDataDetail
          .methodsMap
          .get(
            "set" + StringUtil.firstLetterUppercase(tMetaDataDetail.allSqlColumnNames[i]));
        if (method != null) {
          // set value
          method.invoke(instance, executeResult.getObject(i + 1));
        }
      }
      resultList.add(instance);
      // update cursor
      hasNext = executeResult.next();
    }
    return resultList;
  }

  /**
   * it will back the first row data mapping entity instance from result-set, so you should ensure the query result just contains one row, or you should consider {@see selectForList} instead.
   *
   * @param executeResult sql query result
   * @param mappingClass  mapping entity class
   * @param <T>           mapping class template
   * @return a result of mapping entity instance
   * @throws SQLException              SQLException
   * @throws NoSuchMethodException     NoSuchMethodException
   * @throws InvocationTargetException InvocationTargetException
   * @throws InstantiationException    InstantiationException
   * @throws IllegalAccessException    IllegalAccessException
   */
  public static <T> T selectForOne(
    ResultSet executeResult,
    Class<T> mappingClass
  ) throws
    SQLException,
    NoSuchMethodException,
    InvocationTargetException,
    InstantiationException,
    IllegalAccessException {
    MetaDataDetail<T> tMetaDataDetail = MetaDataDetail.MetaDetailFactory(executeResult, mappingClass);
    boolean isDone = executeResult.next();
    if (isDone) {
      @SuppressWarnings("DuplicatedCode") T willBack = tMetaDataDetail.nonArgConstructor.newInstance();
      // noinspection DuplicatedCode
      for (int i = 0; i < tMetaDataDetail.allSqlColumnNames.length; ++i) {
        Method method = tMetaDataDetail
          .methodsMap
          .get(
            "set" + StringUtil.firstLetterUppercase(tMetaDataDetail.allSqlColumnNames[i])
          );
        if (method != null) {
          // set value
          method.invoke(willBack, executeResult.getObject(i + 1));
        }
      }
      return willBack;
    }

    return null;
  }

  /**
   * execute update or delete sql
   *
   * @param sql    the prepared running sql
   * @param params sql params
   * @return the sql execute effect rows
   */
  public static int forExecuteUpdate(String sql, Object... params) {
    Connection connection = null;
    int effectRows = 0;

    try {
      connection = ThreadLocalDataSource.getConnection();
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(sql);
      for (int i = 0; i < params.length; i++) {
        statement.setObject(i + 1, params[i]);
      }
      effectRows = statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      if (connection != null) {
        try {
          connection.rollback();
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    } finally {
      try {
        ThreadLocalDataSource.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return effectRows;
  }
}
