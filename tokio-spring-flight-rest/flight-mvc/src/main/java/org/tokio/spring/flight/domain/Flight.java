package org.tokio.spring.flight.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name="flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @CreationTimestamp
    private LocalDateTime created;

    @Column(nullable = false)
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private STATUS_FLIGHT statusFlight;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int occupancy;

    @Column(name = "departure_time",nullable = false)
    private LocalDateTime departureTime;

    @OneToOne
    @JoinColumn(name="airport_departure_id",nullable = false)
    private Airport airportDeparture;

    @OneToOne
    @JoinColumn(name="airport_arrival_id",nullable = false)
    private Airport airportArrival;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="resource_id",referencedColumnName = "id")
    private Resource flightImg;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "flight",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<FlightBooking> flightBookings;

    // locking optimistic
    @Version
    private int version;

}
