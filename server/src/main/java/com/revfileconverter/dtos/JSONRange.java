package com.revfileconverter.dtos;

import com.revfileconverter.enums.DataTypes;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JSONRange {
    private int startpos;
    private int endpos;
    private String name;
    private DataTypes dataTypes;
}
