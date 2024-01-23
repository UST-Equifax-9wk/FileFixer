package com.revfileconverter.entities;

import com.revfileconverter.enums.DataTypes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;

import java.util.Arrays;

@NoArgsConstructor
public class CustomTokenizer extends FixedLengthTokenizer {
    private DataTypes[] dataTypes;
    public void setDataTypes(DataTypes[] dataTypes){
        this.dataTypes = dataTypes;
    }

    public DataTypes[] getDataTypes() {
        return dataTypes;
    }
}
