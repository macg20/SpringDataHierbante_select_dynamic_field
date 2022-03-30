package com.github.macg20.dynamicselectionfield;

import java.util.Set;

public abstract class PersonFieldsTestData {

    static Set<String> oneField() {
        return Set.of("firstName");
    }

    static Set<String> twoFields() {
        return Set.of("firstName", "lastName");
    }

    static Set<String> threeFields() {
        return Set.of("firstName", "lastName", "age");
    }

    static Set<String> fourFields() {
        return Set.of("firstName", "lastName", "age", "weight");
    }
}
