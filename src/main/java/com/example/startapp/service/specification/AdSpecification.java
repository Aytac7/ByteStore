package com.example.startapp.service.specification;

import com.example.startapp.dto.request.common.AdCriteriaRequest;
import com.example.startapp.entity.Ad;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AdSpecification implements Specification<Ad> {

    public static Specification<Ad> getAdByCriteriaRequest(AdCriteriaRequest adCriteriaRequest){
        return(root, query, criteriaBuilder) -> {
            List<Predicate>predicates=new ArrayList<>();

            if (adCriteriaRequest.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), adCriteriaRequest.getStatus()));
            }


            if (adCriteriaRequest.getHeader() != null && !adCriteriaRequest.getHeader().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("header")),
                        "%" + adCriteriaRequest.getHeader().toLowerCase() + "%"
                ));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
    @Override
    public Predicate toPredicate(Root<Ad> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
