package com.example.startapp.service.specification;

import com.example.startapp.dto.request.common.AdCriteriaRequest;
import com.example.startapp.entity.common.Ad;
import com.example.startapp.entity.common.Brand;
import com.example.startapp.entity.common.Category;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AdSpecification implements Specification<Ad> {

    public static Specification<Ad> getAdByCriteriaRequest(AdCriteriaRequest criteriaRequest){
        return(root, query, criteriaBuilder) -> {
            List<Predicate>predicates=new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("status"), "APPROVED"));

            //category
            if(criteriaRequest.getCategoryId()!=null){
                Subquery<Long> brandSubquery=query.subquery(Long.class);
                Root<Brand> brandRoot=brandSubquery.from(Brand.class);
                Join<Brand, Category> brandCategoryJoin= brandRoot.join("categories");
                brandSubquery.select(brandRoot.get("id"))
                        .where(criteriaBuilder.equal(brandCategoryJoin.get("id"), criteriaRequest.getCategoryId()));

                predicates.add(root.get("brand").get("id").in(brandSubquery));
            }

            //brand
            if(criteriaRequest.getBrandIds()!=null && !criteriaRequest.getBrandIds().isEmpty()){
                predicates.add(root.get("brand").get("id").in(criteriaRequest.getBrandIds()));
            }

            //model
            if(criteriaRequest.getModelIds()!=null && !criteriaRequest.getModelIds().isEmpty()){
                predicates.add(root.get("model").get("id").in(criteriaRequest.getModelIds()));
            }

            //min
            if(criteriaRequest.getMinPrice()!=0){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), criteriaRequest.getMinPrice()));
            }
            //max
            if (criteriaRequest.getMaxPrice() != 0) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), criteriaRequest.getMaxPrice()));
            }

            //new
            if (criteriaRequest.getNewSelected() != null && criteriaRequest.getNewSelected()) {
                predicates.add(criteriaBuilder.equal(root.get("isNew"), true));
            }

            //secondHand
            if (criteriaRequest.getSecondhandSelected() != null && criteriaRequest.getSecondhandSelected()) {
                predicates.add(criteriaBuilder.equal(root.get("isNew"), false));
            }

            // If neither newSelected nor secondhandSelected are true, return both new and secondhand products
            if (criteriaRequest.getNewSelected() == null && criteriaRequest.getSecondhandSelected() == null) {
                // Don't add any filter for "isNew", meaning both new and secondhand will be fetched
            }

            //search
            if (criteriaRequest.getSearchText() != null && !criteriaRequest.getSearchText().isEmpty()) {
                Predicate brandSearch = criteriaBuilder.like(criteriaBuilder.lower(root.get("brand").get("name")),
                        "%" + criteriaRequest.getSearchText().toLowerCase() + "%");
                Predicate modelSearch = criteriaBuilder.like(criteriaBuilder.lower(root.get("model").get("name")),
                        "%" + criteriaRequest.getSearchText().toLowerCase() + "%");
                predicates.add(criteriaBuilder.or(brandSearch, modelSearch));
            }

            // Default Sorting by Date (Newest First)
            if (criteriaRequest.getSortByPriceAsc() == null && criteriaRequest.getSortByPriceDesc() == null
                    && criteriaRequest.getSortByDate() == null && criteriaRequest.getSecondhandSelected()==null
                    && criteriaRequest.getNewSelected()==null && criteriaRequest.getCategoryId()==null
                    && criteriaRequest.getBrandIds()==null && criteriaRequest.getModelIds()==null
                    && criteriaRequest.getMinPrice()==0 && criteriaRequest.getMaxPrice()==0
                    && criteriaRequest.getSearchText()==null

            ) {
                //default to sorting by the newest date
                query.orderBy(criteriaBuilder.desc(root.get("createdDate")));
            }


            //  price sorting (ascending or descending)
            if (criteriaRequest.getSortByPriceAsc() != null && criteriaRequest.getSortByPriceAsc()) {
                query.orderBy(criteriaBuilder.asc(root.get("price")));
            }
            if (criteriaRequest.getSortByPriceDesc() != null && criteriaRequest.getSortByPriceDesc()) {
                query.orderBy(criteriaBuilder.desc(root.get("price")));
            }

            // sorting by date (newest first)
            if (criteriaRequest.getSortByDate() != null && criteriaRequest.getSortByDate()) {
                query.orderBy(criteriaBuilder.desc(root.get("createdDate")));
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
