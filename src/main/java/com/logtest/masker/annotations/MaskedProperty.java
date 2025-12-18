package com.logtest.masker.annotations;

import com.logtest.masker.patterns.MaskPatternType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaskedProperty {
    MaskPatternType type();
}
