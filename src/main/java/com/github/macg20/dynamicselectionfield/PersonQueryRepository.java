package com.github.macg20.dynamicselectionfield;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
class PersonQueryRepository {

    @PersistenceContext
    private EntityManager entityManager;


    Tuple findPersonByField(Set<String> fields) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
        Root<Person> root = query.from(Person.class);

        List<Selection<?>> selections =fields.stream().map(e-> root.get(e)).collect(Collectors.toList());

        query = query.multiselect(selections);
      return entityManager.createQuery(query).getSingleResult();
    }
}
