package framework.scanner;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/18 14:54
 */
public class PackageClassScanner {
  /**
   * .class 文件狗缀名
   */
  public static final String SUB_SUFFIX = ".class";

  /**
   * 截取后缀的长度
   */
  private static final int END_FLAG_LEN = SUB_SUFFIX.length();

  /**
   * 返回包路径下的所有类文件的完整类名列表(里面不会包含非类文件的扫描结果)
   *
   * @param packageName 包在项目内的路径, 例如: <code>me.shalling.dev.entity</code>
   *                    注意避免混用系统路径
   * @return 包路径下的完整类名文件列表
   */
  public static List<String> getClassFullNameList(String packageName) {
    String packageAbsolutePath = getPackageAbsolutePath(packageName);
    var pathList = new ArrayList<String>();
    resolveClassPathTree(packageAbsolutePath, pathList);
    return pathList;
  }

  /**
   * 获取 class 文件在项目内的完整全类名字符串<br>
   * 如果文件不是以 <code>.class</code>结尾的文件, 表明不是目标文件, 直接返回 null<br>
   * 否则将绝对路径类文件转化为类路径类名形式, 如: <code>me.shalling.dev.controller.BlogController</code> <br>
   * 用于提供给类似 Class.forName 之类的反射相关 API 使用
   *
   * @param file 待处理的文件对象
   * @return 文件的完整类名
   */
  private static String getClassName(File file) {
    // 只处理 .class 文件结尾的文件, 否则返回 null
    if (file.getName().endsWith(SUB_SUFFIX)) {
      // 标准化处理项目文件路径, 为了处理 windows 万恶的 '\' 路径分割符和 unix/linux 下的 '/' 的差异
      String projectStandardPath = URLDecoder.decode(getProjectAbsolutePath(), StandardCharsets.UTF_8);

      // 将类的文件夹路径转换为当前系统所呈现的文件系统路径
      // 在末尾添加一个路径分割符, 方便后续进行字符串截取, 如将 /xxx/project/target/classes 转换为: /xxx/project/target/classes/
      String classDirSystemPath = new File(projectStandardPath).getAbsolutePath() + File.separator;

      // 从传入的文件完整路径中截取类在项目中的包路径
      String classInPackagePath = file.getAbsolutePath().substring(classDirSystemPath.length());

      // 替换所有系统文路径分割符为
      String className = classInPackagePath.replace(File.separator, ".");

      // 去除掉尾部 .class , 只保留全类名字符串
      return className.substring(0, className.length() - END_FLAG_LEN);
    } else {
      return null;
    }
  }

  /**
   * 将包的绝对路径下的所有类的完整类名
   *
   * @param packageAbsoluteFileSystemPath 扫描的包的系统文件绝对路径
   * @param fillingList                   接收包路径下所有类的完整类名的列表
   */
  private static void resolveClassPathTree(String packageAbsoluteFileSystemPath, List<String> fillingList) {
    String decodedPath = URLDecoder.decode(packageAbsoluteFileSystemPath, StandardCharsets.UTF_8);
    // listFiles()获取路径下的所有文件或文件夹，返回的是一个File[]数组
    File[] fs = new File(decodedPath).listFiles();

    if (fs != null) {
      for (File file : fs) {
        // file.isDirectory()方法判断当前额的file对象是否是一个文件夹
        if (file.isDirectory()) {
          // 获取文件夹的绝对路径
          // 递归调用
          resolveClassPathTree(file.getAbsolutePath(), fillingList);
        } else {
          // 将文件名转换成完整类名并添加到传入的列表当中
          String path = getClassName(file);

          // 防止添加 null(非 .class 文件的解析结果)
          if (path != null)
            fillingList.add(path);
        }
      }
    }
  }

  /**
   * 将包路径转换成系统文件路径, 便于使用 File 类进行描述行解析
   *
   * @param packageName 包在项目内的相对路径, 例如: <code>me.shalling.dev.entity</code>
   * @return 获取完整的包完整路径
   */
  public static String getPackageAbsolutePath(String packageName) {
    // 将 . 替换成不同系统平台对应的文件路径分隔符
    packageName = packageName.replace(".", File.separator);
    return getProjectAbsolutePath() + packageName;
  }

  /**
   * 获取当前项目的绝对根路径
   *
   * @return 当前项目的绝对根路径
   */
  private static String getProjectAbsolutePath() {
    return Objects
      .requireNonNull(PackageClassScanner.class.getClassLoader().getResource(""))
      .getPath();
  }
}
