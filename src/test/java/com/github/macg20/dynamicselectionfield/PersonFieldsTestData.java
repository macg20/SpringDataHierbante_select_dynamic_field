package com.github.macg20.dynamicselectionfield;

import java.util.Set;

public abstract class PersonFieldsTestData {

    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String AGE = "age";
    private static final String WEIGHT = "weight";

    static Set<String> oneField() {
        return Set.of(FIRST_NAME);
    }

    static Set<String> twoFields() {
        return Set.of(FIRST_NAME, LAST_NAME);
    }

    static Set<String> threeFields() {
        return Set.of(FIRST_NAME, LAST_NAME, AGE);
    }

    static Set<String> fourFields() {
        return Set.of(FIRST_NAME, LAST_NAME, AGE, WEIGHT);
    }
}
