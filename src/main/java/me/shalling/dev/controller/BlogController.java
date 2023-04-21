package me.shalling.dev.controller;

import framework.stereotype.RestControllerLeft;
import framework.stereotype.method.GetMappingLeft;
import framework.stereotype.method.PostMappingLeft;
import framework.stereotype.params.PostBody;
import framework.stereotype.permission.PermissionRequire;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/18 15:34
 */
@RestControllerLeft("/blog")
public class BlogController {
  private record RespondDTO(int id, String content) {
  }

  private record ReplySimpleDTO(long timestamp, String msg) {
  }

  private record SimpleReceiveDTO(String msg) {
  }

  @GetMappingLeft("/list")
  public List<RespondDTO> getVisitorList() {
    var respondDTOS = new ArrayList<RespondDTO>();
    respondDTOS.add(new RespondDTO(1, "高耦合"));
    respondDTOS.add(new RespondDTO(2, "低内聚"));
    respondDTOS.add(new RespondDTO(3, "不可维护"));
    respondDTOS.add(new RespondDTO(4, "杂乱无章"));
    respondDTOS.add(new RespondDTO(5, "练习时长不足两年半"));
    return respondDTOS;
  }

  @PostMappingLeft("/salute")
  @PermissionRequire(value = {"admin", "root"})
  public ReplySimpleDTO simpleReply(@PostBody SimpleReceiveDTO dto) {
    return new ReplySimpleDTO(System.currentTimeMillis(), "your msg is: " + dto.msg);
  }
}
