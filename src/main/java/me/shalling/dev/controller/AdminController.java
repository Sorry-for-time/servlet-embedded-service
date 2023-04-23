package me.shalling.dev.controller;

import framework.container.config.base.TokenStorage;
import framework.stereotype.RestControllerLeft;
import framework.stereotype.method.PostMappingLeft;
import framework.stereotype.params.PostBody;
import lombok.Data;
import me.shalling.dev.constant.StatusCode;
import me.shalling.dev.interceptor.meta.TokenMeta;
import me.shalling.dev.vo.Result;

import java.util.Map;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/23 20:31
 */
@RestControllerLeft("/admin/user")
public class AdminController {
  private final Map<String, TokenMeta> tokenMapSingleton = TokenStorage.getTokenMapSingleton();

  @Data
  private final static class LogoutDto {
    private String token;
  }

  /**
   * 下线, 就随便写了, 不想写了.....
   *
   * @param dto token 信息
   * @return 随便返回一个信息...
   */
  @PostMappingLeft("/logout")
  public Result<String> logout(@PostBody LogoutDto dto) {
    tokenMapSingleton.remove(dto.token);
    return new Result<String>()
      .setMsg("删除成功")
      .setStatusCode(StatusCode.OK);
  }
}
