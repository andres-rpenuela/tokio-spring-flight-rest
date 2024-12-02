package org.tokio.spring.flight.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder @Entity @Table(name="resources")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="resource_id", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID resourceId;

    @Column(name="size_resource", nullable = false)
    private int size;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "file_name", nullable = false)
    private String fileName;

}
