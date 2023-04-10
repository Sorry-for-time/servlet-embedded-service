package me.shalling.dev.servletExamples;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.util.UUID;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/9 16:22
 */
@WebServlet(
  urlPatterns = {"/download"},
  asyncSupported = true,
  loadOnStartup = 0
)
public class DownloadController extends HttpServlet {
  @Serial
  private static final long serialVersionUID = 398140752476262739L;
  private static final int BUFFER_SIZE = 1024;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    try (
      InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("static/test-img.jpg");
      ServletOutputStream outputStream = resp.getOutputStream();
      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, BUFFER_SIZE)
    ) {
      resp.setContentType("image/jpg");
      resp.setHeader(
        "Content-disposition",
        "attachment; filename=" + UUID.randomUUID().toString().replaceAll("-", "") + ".jpg");

      byte[] bytes = new byte[BUFFER_SIZE];
      int readLen;
      if (resourceAsStream != null) {
        while ((readLen = resourceAsStream.read(bytes)) != -1) {
          bufferedOutputStream.write(bytes, 0, readLen);
        }
        bufferedOutputStream.flush();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
