package org.tokio.spring.flight.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="flight_bookings")
public class FlightBooking  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Id
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    //@Id
    @ManyToOne
    @JoinColumn(name="flight_id")
    private Flight flight;
    @Column
    public LocalDateTime created;

    @Column(nullable = false,unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID locator;

}
