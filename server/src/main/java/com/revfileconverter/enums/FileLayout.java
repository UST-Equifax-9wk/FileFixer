package com.revfileconverter.enums;

import lombok.Getter;

@Getter
public enum FileLayout {
    PERSON(0),
    PERSON2(1),
    CAR(2),
    CAR2(3);

    private final int value;
    FileLayout(int i) {
        this.value = i;
    }
}
