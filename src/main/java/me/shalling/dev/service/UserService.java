package me.shalling.dev.service;

import me.shalling.dev.entity.User;
import me.shalling.dev.service.status.UserVerifyStatus;
import me.shalling.dev.vo.dto.LoginDTO;
import me.shalling.dev.vo.dto.UserDTO;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/22 16:20
 */
public interface UserService {
  /**
   * 给定的用户是否存在
   *
   * @param dto 用户信息
   * @return 用户是否存在
   */
  User getUser(UserDTO dto) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

  /**
   * 给定的用户信息是否通过校验
   *
   * @param dto 用户信息
   * @return 用户信校验信息
   */
  UserVerifyStatus verifyUser(LoginDTO dto, User user);
}
