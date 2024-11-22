package org.tokio.spring.flight.api.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="users")
public class User {

    @Id
    @org.tokio.spring.flight.api.helper.generated.tsId.TSId
    private String id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;

    @Column
    @CurrentTimestamp
    private LocalDateTime created;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @Column
    private boolean active;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="users_with_roles",
    joinColumns =  {@JoinColumn(name="user_id")},
    inverseJoinColumns = { @JoinColumn(name="role_id")})
    private Set<Role> roles;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="resource_id",referencedColumnName = "id")
    private Resource userImage;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    private Set<FlightBooking> flightBookings;

}
