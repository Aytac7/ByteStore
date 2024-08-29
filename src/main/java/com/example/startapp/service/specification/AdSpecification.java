package com.example.startapp.service.specification;

import com.example.startapp.dto.request.common.AdCriteriaRequest;
import com.example.startapp.entity.Ad;
import com.example.startapp.entity.Brand;
import com.example.startapp.entity.Model;
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

            predicates.add(criteriaBuilder.equal(root.get("status"), "APPROVED"));


            if (adCriteriaRequest.getPriceFrom() > 0 && adCriteriaRequest.getPriceTo() > 0) {
                predicates.add(criteriaBuilder.between(root.get("price"),
                        adCriteriaRequest.getPriceFrom(), adCriteriaRequest.getPriceTo()));
            }
//            else if (adCriteriaRequest.getPriceFrom() > 0) {
//                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), adCriteriaRequest.getPriceFrom()));
//            } else if (adCriteriaRequest.getPriceTo() > 0) {
//                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), adCriteriaRequest.getPriceTo()));
//            }


            if (adCriteriaRequest.getBrandIds() != null && !adCriteriaRequest.getBrandIds().isEmpty()) {
                predicates.add(root.get("brand").get("id").in(adCriteriaRequest.getBrandIds()));
            }

            if (adCriteriaRequest.getModelIds() != null && !adCriteriaRequest.getModelIds().isEmpty()) {
                predicates.add(root.get("model").get("id").in(adCriteriaRequest.getModelIds()));
            }



            if (adCriteriaRequest.getIsNew()!=null ) {
                if (adCriteriaRequest.getIsNew()) {
                    predicates.add(criteriaBuilder.isTrue(root.get("isNew")));

                } else {
                    predicates.add(criteriaBuilder.isFalse(root.get("isNew")));
                }
            }




            if (adCriteriaRequest.isSortByNewest()) {
                query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };

    }

//    public static Specification<Ad> searchByBrandOrModel(String searchQuery) {
//        return (Root<Ad> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
//            if (searchQuery == null || searchQuery.isEmpty()) {
//                return criteriaBuilder.conjunction();
//            }
//
//            String searchPattern = "%" + searchQuery.toLowerCase() + "%";
//
//            Predicate brandPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("brand").get("name")), searchPattern);
//            Predicate modelPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("model").get("name")), searchPattern);
//
//            return criteriaBuilder.or(brandPredicate, modelPredicate);
//        };
//    }
    @Override
    public Predicate toPredicate(Root<Ad> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
