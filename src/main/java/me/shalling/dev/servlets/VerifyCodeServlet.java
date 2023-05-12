package me.shalling.dev.servlets;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import me.shalling.dev.util.verifyCode.VerifyCodeGenerator;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.Serial;

/**
 * 验证码处理器
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/22 15:00
 */
@WebServlet(urlPatterns = {"/verify-code"}, loadOnStartup = 1, asyncSupported = true)
public class VerifyCodeServlet extends HttpServlet {
  @Serial
  private static final long serialVersionUID = -6197588984346637907L;
  public static final String VERIFY_CODE_VALUE = "verify-code-value";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    try (
      ServletOutputStream outputStream = resp.getOutputStream()
    ) {
      // 创建验证码
      resp.setContentType("image/jpeg");
      VerifyCodeGenerator.VerifyCodeResult formulaVerifyCode = VerifyCodeGenerator.createFormulaVerifyCode();
      HttpSession session = req.getSession();
      System.out.println("id: " + session.getId());
      // 往 session 作用域中存储 验证码信息
      session.setAttribute(VERIFY_CODE_VALUE, formulaVerifyCode.getPattern());
      System.out.println(session.getAttribute(VERIFY_CODE_VALUE));
      ImageIO.write(formulaVerifyCode.getBufferedImage(), "jpg", outputStream);
    }
  }
}
