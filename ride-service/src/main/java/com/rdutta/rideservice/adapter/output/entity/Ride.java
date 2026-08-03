package com.rdutta.rideservice.adapter.output.entity;

import com.rdutta.rideservice.domain.dto.Location;
import com.rdutta.rideservice.domain.dto.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rides")
public class Ride {

    @Id
    @Column("ride_id")
    private String rideId;

    @Column("rider_id")
    private String riderId;

    @Column("driver_id")
    private String driverId;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL, prefix = "pickup_")
    private Location pickupLocation;

    @Embedded(onEmpty = Embedded.OnEmpty.USE_NULL, prefix = "dropoff_")
    private Location dropoffLocation;

    @Column("status")
    private RideStatus status;

    @Column("fare_amount")
    private BigDecimal fareAmount;

    @Version
    @Column("version")
    private Long version;

    @Column("created_at")
    @ReadOnlyProperty
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}