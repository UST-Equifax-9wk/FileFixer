package com.revfileconverter.enums;

import lombok.Getter;

@Getter
public enum FileLayout {
    PERSON(0),
    CAR(1);
    private final int value;
    FileLayout(int i) {
        this.value = i;
    }
}
