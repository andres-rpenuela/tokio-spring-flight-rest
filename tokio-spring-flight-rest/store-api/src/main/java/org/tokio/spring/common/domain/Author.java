package org.tokio.spring.common.domain;

import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Author {
    private Integer id;
    private String name;
}
