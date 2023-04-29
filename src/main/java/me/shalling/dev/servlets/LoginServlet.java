package me.shalling.dev.servlets;

import me.shalling.dev.container.ConfigProvider;
import me.shalling.dev.container.config.base.TokenConfig;
import me.shalling.dev.container.config.base.TokenStorage;
import framework.util.GsonSerializableTool;
import framework.view.util.ServletRequestExtractTool;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import me.shalling.dev.constant.StatusCode;
import me.shalling.dev.entity.User;
import me.shalling.dev.interceptor.meta.TokenMeta;
import me.shalling.dev.service.UserService;
import me.shalling.dev.service.impl.UserServiceImpl;
import me.shalling.dev.service.status.UserVerifyStatus;
import me.shalling.dev.vo.Result;
import me.shalling.dev.vo.dto.LoginDTO;
import me.shalling.dev.vo.dto.UserDTO;

import java.io.PrintWriter;
import java.io.Serial;
import java.util.Map;
import java.util.UUID;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/22 15:39
 */
@WebServlet(urlPatterns = {"/login"}, loadOnStartup = 0)
public class LoginServlet extends HttpServlet {
  private final static UserService userService = new UserServiceImpl();

  private final static TokenConfig tokenConfig = ConfigProvider.getConfiguration().getTokenConfig();

  private final static Map<String, TokenMeta> tokenMapSingleton = TokenStorage.getTokenMapSingleton();

  /**
   * 响应结果设置
   */
  public static final String JSON_TYPE = "application/json";

  @Serial
  private static final long serialVersionUID = 1718319034556203010L;

  /**
   * session 里保存的 验证码计算结果
   */
  public static final String VERIFY_CODE_VALUE = "verify-code-value";

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    // 设置响应结果为 json
    resp.setContentType(JSON_TYPE);
    PrintWriter writer = null;
    Result<TokenMeta> respondResult = new Result<>();
    HttpSession session = req.getSession(true);
    // 获取验证码的计算结果
    String verifyCodeValueStr = (String) session.getAttribute(VERIFY_CODE_VALUE);

    try {
      writer = resp.getWriter();
      String frontRequestData = ServletRequestExtractTool.getRequestJSONData(req);
      LoginDTO loginDTO = GsonSerializableTool.JSONToObject(frontRequestData, LoginDTO.class);

      // 如果未设置验证码或者空结果
      if (verifyCodeValueStr == null || "".equals(verifyCodeValueStr.strip())) {
        respondResult.setStatusCode(StatusCode.REQUEST_FAILURE);
        respondResult.setMsg("请先获取验证码");
      }
      // 如果验证码不匹配
      else if (!verifyCodeValueStr.equals(loginDTO.getVerifyCode())) {
        respondResult.setStatusCode(StatusCode.REQUEST_FAILURE);
        respondResult.setMsg("验证码不匹配");
      }
      // 如果验证码匹配, 那么进行用户匹配
      else {
        // 获取用户信息
        User user = userService.getUser(new UserDTO(loginDTO.getUsername(), loginDTO.getPassword()));

        // 获取数据库用户匹配信息
        UserVerifyStatus verifyData = userService.verifyUser(loginDTO, user);

        // 用户不匹配(账号不存在, 密码错误)
        if (!verifyData.getMatched()) {
          respondResult.setStatusCode(StatusCode.REQUEST_FAILURE);
          respondResult.setMsg(verifyData.getMsg());
        }
        // 数据库校验通过
        else {
          respondResult.setStatusCode(StatusCode.OK);
          respondResult.setMsg(verifyData.getMsg());

          String uuidKey = UUID.randomUUID().toString();
          // 创建 token 信息
          TokenMeta tokenMeta = new TokenMeta(
            user.getUsername(),
            uuidKey,
            System.currentTimeMillis(),
            tokenConfig.getExpire()
          );
          // 存储到全局对象里
          tokenMapSingleton.put(uuidKey, tokenMeta);
          respondResult.setData(tokenMeta);
        }
      }

      writer.write(GsonSerializableTool.objectToJSON(respondResult));
    } catch (Exception e) {
      // 假设发生错误, 那么直接给一个错误视图
      if (writer != null) {
        writer.write(GsonSerializableTool.objectToJSON(respondResult
          .setStatusCode(StatusCode.UN_KNOW_REASON)
          .setMsg("未知错误, 请联系管理员")
        ));
      }
      e.printStackTrace();
    } finally {
      // 无论如何, 都清空前一次会话的验证码
      if (verifyCodeValueStr != null) {
        session.removeAttribute(verifyCodeValueStr);
      }
    }
  }
}
