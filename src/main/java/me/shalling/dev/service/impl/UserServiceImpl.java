package me.shalling.dev.service.impl;

import me.shalling.dev.dao.UserDAO;
import me.shalling.dev.entity.User;
import me.shalling.dev.service.UserService;
import me.shalling.dev.service.status.UserVerifyStatus;
import me.shalling.dev.vo.dto.LoginDTO;
import me.shalling.dev.vo.dto.UserDTO;

import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/22 20:13
 */
public class UserServiceImpl implements UserService {
  private final UserDAO userDao = new UserDAO();

  @Override
  public User getUser(UserDTO dto) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    return userDao.getUser(dto.getUsername());
  }

  @Override
  public UserVerifyStatus verifyUser(@NotNull LoginDTO dto, User user) {
    boolean matched;
    String msg;
    if (user == null) {
      matched = false;
      msg = "不存在用户";
    } else if (!Objects.requireNonNull(user).getPassword().equals(dto.getPassword())) {
      matched = false;
      msg = "密码不正确";
    } else {
      matched = true;
      msg = "验证成功";
    }
    return new UserVerifyStatus(matched, msg);
  }
}
