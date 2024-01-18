package com.revfileconverter.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//delete once completed Ranges separetly
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonRanges {
    private JSONRange firstname;
    private JSONRange lastname;
    private JSONRange dob;
    private JSONRange email;
    private JSONRange phone;
    private JSONRange isUsCitizen;
}
