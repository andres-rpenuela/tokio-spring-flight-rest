package org.tokio.spring.common.domain;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class Book {
    private Integer id;
    private String title;
    private String genre;
    private List<Author> authors;
}
