package me.shalling.dev.framework.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serial;
import java.io.Serializable;

/**
 * jackson 序列化简单配置
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/8 22:08
 */
public class JSONSerializableTool implements Serializable {
  @Serial
  private static final long serialVersionUID = 3566755931937058990L;

  private final static ObjectMapper MAPPER = new ObjectMapper();

  public static <T> String toJson(T object) {
    try {
      return MAPPER.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T transformToObject(String jsonStr, Class<T> tClass) {
    return MAPPER.convertValue(jsonStr, tClass);
  }
}
