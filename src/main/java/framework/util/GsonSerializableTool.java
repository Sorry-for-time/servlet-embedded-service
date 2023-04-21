package framework.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * a simple json config to parse data, it will use GSON dependency
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/2/22 22:57
 */
public final class GsonSerializableTool implements Serializable {
  @Serial
  private static final long serialVersionUID = -1481963913595591709L;

  /**
   * the gson instance, do not modify it!
   */
  private final static Gson GSON_INSTANCE = new GsonBuilder()
    .setLenient()
    .disableJdkUnsafe()
    .serializeNulls()
    .enableComplexMapKeySerialization()
    .serializeSpecialFloatingPointValues()
    .create();

  /**
   * @param source the waiting transform object
   * @param <E>    source object type
   * @return the transformed json string
   */
  public static <E> String objectToJSON(final E source) {
    return GSON_INSTANCE.toJson(source, source.getClass());
  }

  /**
   * get a java object from json string
   *
   * @param jsonStr the prepared json string
   * @param eClass  the transformed object's class
   * @param <E>     the transformed object type
   * @return the transformed object from json string
   */
  public static <E> E JSONToObject(final String jsonStr, final Class<E> eClass) {
    return GSON_INSTANCE.fromJson(jsonStr, eClass);
  }
}
