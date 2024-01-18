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
    @Size(max = 15)
    @Column(name = "first_name")
    private String firstname;
    @NotNull
    @Size(max = 15)
    @Column(name = "last_name")
    private String lastname;
    @NotNull
    @Size(max = 10, min = 10)
    private String dob;
    @NotNull
    @Size(max = 30)
    @Email
    private String email;
    @NotNull
    @Size(max = 10, min = 10)
    private String phone;
    @NotNull
    private Boolean isUsCitizen;
    @JsonIgnore
    public Integer getPersonid() {
        return personid;
    }
}
