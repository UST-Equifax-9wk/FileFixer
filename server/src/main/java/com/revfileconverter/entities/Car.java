package com.revfileconverter.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({
        "manufacturer",
        "model",
        "color",
        "state",
        "licenseplate",
        "year"
})
public class Car {
    @Id
    @Column(name = "car_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer carid;
    @NotNull
    private String manufacturer;
    @NotNull
    private String model;
    @NotNull
    private String color;
    @NotNull
    private String state;
    @NotNull
    private String licenseplate;
    @NotNull
    private String year;
}
