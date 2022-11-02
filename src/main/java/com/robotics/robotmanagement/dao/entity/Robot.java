package com.robotics.robotmanagement.dao.entity;

import com.robotics.robotmanagement.web.constants.LifeCyclePhase;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "robot")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Robot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "robot_id",unique = true)
    String robotId;

    @Column(name = "name")
    String name;

    @Column(name = "year_of_manufacture")
    Long yearOfManufacture;

    @Column(name = "mass")
    Long mass;

    @Column(name = "color")
    String color;

    @Column(name = "functions")
    String functions;

    @Column(name = "phase")
    @Enumerated(EnumType.STRING)
    LifeCyclePhase robotPhase;

    @Column(name = "create_time")
    @CreationTimestamp
    LocalDateTime createTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    LocalDateTime updateTime;

}
