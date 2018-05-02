package com.insurance.backend.util;

import com.insurance.backend.entity.Client;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ClientSpecification implements Specification<Client> {

    private Client filter;

    public ClientSpecification(Client filter) {
        super();
        this.filter = filter;
    }

    public Predicate toPredicate(Root<Client> root, CriteriaQuery<?> cq,
                                 CriteriaBuilder cb) {

        Predicate p = cb.and();

        if (!StringUtils.isBlank(filter.getFirstName())) {
            p.getExpressions()
                    .add(cb.like(cb.upper(root.get("firstName")), "%" + filter.getFirstName().toUpperCase() + "%"));
        }

        if (!StringUtils.isBlank(filter.getPatronymic())) {
            p.getExpressions()
                    .add(cb.like(cb.upper(root.get("patronymic")), "%" + filter.getPatronymic().toUpperCase() + "%"));
        }

        if (!StringUtils.isBlank(filter.getLastName())) {
            p.getExpressions()
                    .add(cb.like(cb.upper(root.get("lastName")), "%" + filter.getLastName().toUpperCase() + "%"));
        }

        return p;

    }

}
