package com.magabyzr.ecommercemg.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "profiles")

public class Profile {
    @Id
    @Column(name="id")
    private Long id;

    @Column(name = "bio")
    private String bio;

    @Column(name = "phone_Number")
    private String phone_Number;

    @Column(name = "date_Of_Birth")
    private LocalDate date_Of_Birth;

    @Column(name = "loyalty_points")
    private Integer loyalty_points;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    @MapsId
    @ToString.Exclude
    private User user;
}
