package com.revfileconverter.dtos;

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
}
