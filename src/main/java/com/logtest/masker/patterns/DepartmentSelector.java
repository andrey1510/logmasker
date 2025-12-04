package com.logtest.masker.patterns;

import com.logtest.masker.annotations.MaskedProperty;
import com.logtest.masker.patterns.dep1.DepOneProcessor;
import com.logtest.masker.patterns.dep2.DepTwoProcessor;

import java.lang.reflect.Field;

public class DepartmentSelector {

    private static final Department currentDepartment = Department.DEPARTMENT_ONE;

    public enum Department {
        DEPARTMENT_ONE,
        DEPARTMENT_TWO
    }

    public static String maskString(Field field, String value){
        return switch (currentDepartment) {
            case DEPARTMENT_ONE -> DepOneProcessor.processString(
                DepOneProcessor.PatternType.valueOf(field.getAnnotation(MaskedProperty.class).type()), value
            );
            case DEPARTMENT_TWO -> DepTwoProcessor.processString(
                DepTwoProcessor.PatternType.valueOf(field.getAnnotation(MaskedProperty.class).type()), value
            );
        };
    }

    public static Object maskTemporal(Field field, Object value) {
        MaskedProperty annotation = field.getAnnotation(MaskedProperty.class);
        return switch (currentDepartment) {
            case DEPARTMENT_ONE -> DepOneProcessor.processTemporalValue(
                DepOneProcessor.PatternType.valueOf(annotation.type()), value
            );
            case DEPARTMENT_TWO -> DepTwoProcessor.processTemporalValue(
                DepTwoProcessor.PatternType.valueOf(annotation.type()), value
            );
        };
    }

}