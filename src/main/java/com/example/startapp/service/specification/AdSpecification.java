package com.example.startapp.service.specification;

import com.example.startapp.dto.request.common.AdCriteriaRequest;
import com.example.startapp.entity.common.Ad;
import com.example.startapp.entity.common.Brand;
import com.example.startapp.entity.common.Category;
import com.example.startapp.entity.common.Model;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AdSpecification implements Specification<Ad> {

    public static Specification<Ad> getAdByCriteriaRequest(AdCriteriaRequest criteriaRequest){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("status"), "APPROVED"));

            // category filter
//            if (criteriaRequest.getCategoryId() != null) {
//                Subquery<Long> brandSubquery = query.subquery(Long.class);
//                Root<Brand> brandRoot = brandSubquery.from(Brand.class);
//                Join<Brand, Category> brandCategoryJoin = brandRoot.join("categories");
//                brandSubquery.select(brandRoot.get("id"))
//                        .where(criteriaBuilder.equal(brandCategoryJoin.get("id"), criteriaRequest.getCategoryId()));
//
//                predicates.add(root.get("brand").get("id").in(brandSubquery));
//            }

//            // other filters (brand, model, price, etc.)
//            if (criteriaRequest.getBrandIds() != null && !criteriaRequest.getBrandIds().isEmpty()) {
//                predicates.add(root.get("brand").get("id").in(criteriaRequest.getBrandIds()));
//            }


            if (criteriaRequest.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), criteriaRequest.getCategoryId()));
            }


            if (criteriaRequest.getCategoryId() != null && criteriaRequest.getBrandIds() != null && !criteriaRequest.getBrandIds().isEmpty()) {
                Subquery<Long> brandSubquery = query.subquery(Long.class);
                Root<Brand> brandRoot = brandSubquery.from(Brand.class);
                Join<Brand, Category> brandCategoryJoin = brandRoot.join("categories");
                brandSubquery.select(brandRoot.get("id"))
                        .where(criteriaBuilder.equal(brandCategoryJoin.get("id"), criteriaRequest.getCategoryId()))
                        .where(brandRoot.get("id").in(criteriaRequest.getBrandIds()));

                predicates.add(root.get("brand").get("id").in(brandSubquery));
            } else if (criteriaRequest.getBrandIds() != null && !criteriaRequest.getBrandIds().isEmpty()) {

                predicates.add(root.get("brand").get("id").in(criteriaRequest.getBrandIds()));
            }


            if (criteriaRequest.getCategoryId() != null && criteriaRequest.getBrandIds() != null && !criteriaRequest.getBrandIds().isEmpty() && criteriaRequest.getModelIds() != null && !criteriaRequest.getModelIds().isEmpty()) {
                Subquery<Long> modelSubquery = query.subquery(Long.class);
                Root<Model> modelRoot = modelSubquery.from(Model.class);
                modelSubquery.select(modelRoot.get("id"))
                        .where(criteriaBuilder.equal(modelRoot.get("brand").get("id"), root.get("brand").get("id")))
                        .where(modelRoot.get("id").in(criteriaRequest.getModelIds()));

                predicates.add(root.get("model").get("id").in(modelSubquery));
            }


            if (criteriaRequest.getModelIds() != null && !criteriaRequest.getModelIds().isEmpty()) {
                predicates.add(root.get("model").get("id").in(criteriaRequest.getModelIds()));
            }

            if (criteriaRequest.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), criteriaRequest.getMinPrice()));
            }

            if (criteriaRequest.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), criteriaRequest.getMaxPrice()));
            }


            if (criteriaRequest.getNewSelected() != null && criteriaRequest.getNewSelected()) {
                predicates.add(criteriaBuilder.equal(root.get("isNew"), true));
            }

            if (criteriaRequest.getSecondhandSelected() != null && criteriaRequest.getSecondhandSelected()) {
                predicates.add(criteriaBuilder.equal(root.get("isNew"), false));
            }


            if (criteriaRequest.getSearchText() != null && !criteriaRequest.getSearchText().isEmpty()) {
                Predicate brandSearch = criteriaBuilder.like(criteriaBuilder.lower(root.get("brand").get("name")),
                        "%" + criteriaRequest.getSearchText().toLowerCase() + "%");
                Predicate modelSearch = criteriaBuilder.like(criteriaBuilder.lower(root.get("model").get("name")),
                        "%" + criteriaRequest.getSearchText().toLowerCase() + "%");
                predicates.add(criteriaBuilder.or(brandSearch, modelSearch));
            }

            // Default Sorting by Date (Newest First) if no other sorting criteria provided
//            if (criteriaRequest.getSortByPriceAsc() == null && criteriaRequest.getSortByPriceDesc() == null
//                    && criteriaRequest.getSortByDate() == null && criteriaRequest.getSecondhandSelected() == null
//                    && criteriaRequest.getNewSelected() == null && criteriaRequest.getCategoryId() == null
//                    && (criteriaRequest.getBrandIds() == null || criteriaRequest.getBrandIds().isEmpty())
//                    && (criteriaRequest.getModelIds() == null || criteriaRequest.getModelIds().isEmpty())
//                    && criteriaRequest.getMinPrice() == null && criteriaRequest.getMaxPrice() == null
//                    && criteriaRequest.getSearchText() == null) {
//                query.orderBy(criteriaBuilder.desc(root.get("createdAt"))); // Newest First
//            }


            if (criteriaRequest.getSortByPriceAsc() != null && criteriaRequest.getSortByPriceAsc()) {
                query.orderBy(criteriaBuilder.asc(root.get("price")));
            }

            if (criteriaRequest.getSortByPriceDesc() != null && criteriaRequest.getSortByPriceDesc()) {
                query.orderBy(criteriaBuilder.desc(root.get("price")));
            }


            query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

            return query.getRestriction();
        };
    }

    @Override
    public Predicate toPredicate(Root<Ad> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
