package me.shalling.dev.vo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.shalling.dev.entity.Book;

/**
 * @author Shalling
 * @version v0.01
 * @see <a href="https://github.com/Sorry-for-time">follow me on github</a>
 * @since 2023/4/23 0:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class BookDTO extends Book {
}
