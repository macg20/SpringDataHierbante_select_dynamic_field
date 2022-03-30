package com.github.macg20.dynamicselectionfield;

public abstract class PersonTestData {

    static Person createPerson() {
        Person person = new Person();
        person.setId(1);
        person.setFirstName("Joe");
        person.setLastName("FooBaar");
        person.setHeight(180);
        person.setWeight(70);
        person.setAge(20);
        return person;
    }

}
