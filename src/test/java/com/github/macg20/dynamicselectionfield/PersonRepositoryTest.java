package com.github.macg20.dynamicselectionfield;

import org.assertj.core.api.Assertions;
import org.hibernate.query.criteria.internal.path.SingularAttributePath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.Tuple;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.macg20.dynamicselectionfield.PersonFieldsTestData.*;
import static com.github.macg20.dynamicselectionfield.PersonTestData.createPerson;

@SpringBootTest
@ExtendWith({SpringExtension.class})
class PersonRepositoryTest {

    private static final int SIZE_ONE = 1;
    private static final int FIRST_INDEX = 0;
    private static final String FIRST_NAME_ATTRIBUTE = "firstName";

    @Autowired
    PersonQueryRepository personQueryRepository;

    @Autowired
    PersonRepository personRepository;

    @Test
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

    @TestTemplate
    @ExtendWith({PersonFieldsTemplate.class})
    void should_return_tuple_with_specific_fields(PersonFields fields) {
        //given
        Person person = createPerson();
        personRepository.save(person);
        int fieldsSize = fields.getFields().size();

        // when
        Tuple result = personQueryRepository.findPersonByField(fields.getFields());

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getElements()).hasSize(fieldsSize);

        Set<String> resultAttributes = result.getElements()
                .stream().map(e -> ((SingularAttributePath) e).getAttribute().getName())
                .collect(Collectors.toSet());

        Assertions.assertThat(resultAttributes).hasSize(fieldsSize)
                .isEqualTo(fields.getFields());

    }

    public static class PersonFields {

        private Set<String> fields;

        PersonFields(Set<String> fields) {
            this.fields = fields;
        }

        Set<String> getFields() {
            return fields;
        }

        String sizeDescription() {
            return "Fields: " + fields.size();
        }
    }

    public static class PersonFieldsTemplate implements TestTemplateInvocationContextProvider {

        @Override
        public boolean supportsTestTemplate(ExtensionContext extensionContext) {
            return true;
        }

        @Override
        public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext extensionContext) {
            return Stream.of(
                    invocationContext(new PersonFields(oneField())),
                    invocationContext(new PersonFields(twoFields())),
                    invocationContext(new PersonFields(threeFields())),
                    invocationContext(new PersonFields(fourFields()))
            );
        }

        private TestTemplateInvocationContext invocationContext(PersonFields fields) {
            return new TestTemplateInvocationContext() {
                @Override
                public String getDisplayName(int invocationIndex) {
                    return fields.sizeDescription();
                }

                @Override
                public List<Extension> getAdditionalExtensions() {
                    return Collections.singletonList(new ParameterResolver() {
                        @Override
                        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
                            return parameterContext.getParameter().getType().equals(PersonFields.class);
                        }

                        @Override
                        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
                            return fields;
                        }
                    });
                }
            };
        }
    }

}
