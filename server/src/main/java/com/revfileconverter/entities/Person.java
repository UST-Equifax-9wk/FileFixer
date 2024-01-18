package com.revfileconverter.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({
        "firstname",
        "lastname",
        "dob",
        "email",
        "phone",
        "isUsCitizen"
})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Integer personid;
    @NotNull
    @Column(name = "first_name")
    private String firstname;
    @NotNull
    @Column(name = "last_name")
    private String lastname;
    @NotNull
    private String dob;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String phone;
    @NotNull
    private Boolean isUsCitizen;
    @JsonIgnore
    public Integer getPersonid() {
        return personid;
    }
}
