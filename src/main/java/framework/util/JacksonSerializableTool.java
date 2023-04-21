package framework.util;

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
public class JacksonSerializableTool implements Serializable {
  @Serial
  private static final long serialVersionUID = 3566755931937058990L;

  private final static ObjectMapper MAPPER = new ObjectMapper();

  /**
   * 将对象转化为 json
   *
   * @param object 待序列化对象
   * @param <T>    对象类型
   * @return 转换的 json 对象
   */
  public static <T> String objectToJSON(T object) {
    try {
      return MAPPER.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 将 json 字符串还原为对象
   *
   * @param jsonStr json 字符串
   * @param tClass  需要转换的目标对象的类属性
   * @param <T>     目标对象的类型
   * @return 转化为对象
   */
  public static <T> T JSONToObject(String jsonStr, Class<T> tClass) {
    return MAPPER.convertValue(jsonStr, tClass);
  }
}
