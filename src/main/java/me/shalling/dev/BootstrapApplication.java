package me.shalling.dev;

import lombok.extern.slf4j.Slf4j;
import me.shalling.dev.init.StartApplicationServer;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/8 17:26
 */
@Slf4j
public class BootstrapApplication {
  public static void main(String... args) {
    StartApplicationServer.start(args);
  }
}
