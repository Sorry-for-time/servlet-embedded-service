package me.shalling.dev.entity;

import lombok.Data;

/**
 * 书籍实体类信息
 *
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/22 20:08
 */
@Data
public class Book {
  /**
   * 自增主键
   */
  private Integer id;

  /**
   * 分类 id
   */
  private Integer typeId;

  private String name;

  private Double price;

  private String description;

  private String pic;

  private String publish;

  private String author;

  private String stock;

  private String address;
}
