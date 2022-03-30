package com.github.macg20.dynamicselectionfield;

import org.assertj.core.api.Assertions;
import org.hibernate.query.criteria.internal.path.SingularAttributePath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Set;

@SpringBootTest
class PersonRepositoryTest {

    private static final int SIZE_ONE = 1;
    private static final int FIRST_INDEX = 0;
    private static final String FIRST_NAME_ATTRIBUTE = "firstName";

    @Autowired
    PersonQueryRepository personQueryRepository;

    @Autowired
    PersonRepository personRepository;

    @Test
    @Transactional
    void should_return_one_row_with_only_first_name() {
        //given
        Person person = createPerson();
        personRepository.save(person);

        //when
        Tuple result = personQueryRepository.findPersonByField(Set.of(FIRST_NAME_ATTRIBUTE));


        //then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getElements()).hasSize(SIZE_ONE);

        SingularAttributePath resultAttributePath = (SingularAttributePath) result.getElements().get(FIRST_INDEX);
        SingularAttribute resultAttribute = resultAttributePath.getAttribute();

        Assertions.assertThat(resultAttribute.getName())
                .isEqualTo(FIRST_NAME_ATTRIBUTE);
        Assertions.assertThat(result.get(FIRST_INDEX)).isEqualTo("Joe");
    }

    private Person createPerson() {
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
