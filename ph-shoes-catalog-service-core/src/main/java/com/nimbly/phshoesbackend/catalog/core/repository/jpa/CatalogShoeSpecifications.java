package com.nimbly.phshoesbackend.catalog.core.repository.jpa;

import com.nimbly.phshoesbackend.catalog.core.model.CatalogShoe;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class CatalogShoeSpecifications {

    public static Specification<CatalogShoe> hasBrand(String brand) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("brand")), brand.toLowerCase());
    }

    public static Specification<CatalogShoe> hasGender(String gender) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("gender")), gender.toLowerCase());
    }

    public static Specification<CatalogShoe> hasAgeGroup(String ageGroup) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("ageGroup")), ageGroup.toLowerCase());
    }

    /**
     * Existing date-based specs — assumes your dwid is formatted YYYYMMDD
     */
    public static Specification<CatalogShoe> collectedOn(LocalDate collectedDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.and(
                criteriaBuilder.equal(root.get("year"), collectedDate.getYear()),
                criteriaBuilder.equal(root.get("month"), collectedDate.getMonthValue()),
                criteriaBuilder.equal(root.get("day"), collectedDate.getDayOfMonth())
        );
    }


    public static Specification<CatalogShoe> collectedBetween(LocalDate startDate, LocalDate endDateInclusive) {
        // Turn start/end into comparable date keys
        final int startDateKey = startDate.getYear() * 10000 + startDate.getMonthValue() * 100 + startDate.getDayOfMonth();
        final int endDateKey = endDateInclusive.getYear() * 10000 + endDateInclusive.getMonthValue() * 100 + endDateInclusive.getDayOfMonth();

        return (root, query, criteriaBuilder) -> {
            // Build YYYYMMDD from columns: year*10000 + month*100 + day
            Expression<Integer> yearExpression = root.get("year");
            Expression<Integer> monthExpression = root.get("month");
            Expression<Integer> dayExpression = root.get("day");

            Expression<Integer> yearComponent = criteriaBuilder.prod(yearExpression, 10000);
            Expression<Integer> monthComponent = criteriaBuilder.prod(monthExpression, 100);
            Expression<Integer> dateKey = criteriaBuilder.sum(criteriaBuilder.sum(yearComponent, monthComponent), dayExpression);

            return criteriaBuilder.between(dateKey, startDateKey, endDateKey); // inclusive on both ends
        };
    }

    public static Specification<CatalogShoe> hasAnySize(List<String> sizes) {
        return (root, query, criteriaBuilder) -> {
            if (sizes == null || sizes.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Expression<String> extraText = criteriaBuilder.function("TO_VARCHAR", String.class, root.get("extra"));

            Predicate[] anySizePredicates = sizes.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(sizeValue -> !sizeValue.isEmpty())
                    .map(sizeValue -> criteriaBuilder.or(
                            criteriaBuilder.like(extraText, "%\"sizes\"%[\"" + sizeValue + "\"%"),
                            criteriaBuilder.like(extraText, "%,\"" + sizeValue + "\"%"),
                            criteriaBuilder.like(extraText, "%\"" + sizeValue + "\"]%")
                    ))
                    .toArray(Predicate[]::new);

            return criteriaBuilder.or(anySizePredicates);
        };
    }


    public static Specification<CatalogShoe> finalPriceGte(Double min) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.and(
                        criteriaBuilder.lessThan(root.get("priceSale"), root.get("priceOriginal")),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("priceSale"), min)
                ),
                criteriaBuilder.and(
                        criteriaBuilder.not(criteriaBuilder.lessThan(root.get("priceSale"), root.get("priceOriginal"))),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("priceOriginal"), min)
                )
        );
    }

    public static Specification<CatalogShoe> finalPriceLte(Double max) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.and(
                        criteriaBuilder.lessThan(root.get("priceSale"), root.get("priceOriginal")),
                        criteriaBuilder.lessThanOrEqualTo(root.get("priceSale"), max)
                ),
                criteriaBuilder.and(
                        criteriaBuilder.not(criteriaBuilder.lessThan(root.get("priceSale"), root.get("priceOriginal"))),
                        criteriaBuilder.lessThanOrEqualTo(root.get("priceOriginal"), max)
                )
        );
    }

    public static Specification<CatalogShoe> finalPriceBetween(Double min, Double max) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.and(
                        criteriaBuilder.lessThan(root.get("priceSale"), root.get("priceOriginal")),
                        criteriaBuilder.between(root.get("priceSale"), min, max)
                ),
                criteriaBuilder.and(
                        criteriaBuilder.not(criteriaBuilder.lessThan(root.get("priceSale"), root.get("priceOriginal"))),
                        criteriaBuilder.between(root.get("priceOriginal"), min, max)
                )
        );
    }

    /**
     * Matches title OR subtitle containing the keyword (case‐insensitive).
     * If `keyword` is blank or null, this spec returns “true” (no constraint).
     */
    public static Specification<CatalogShoe> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            Predicate titleLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern);
            Predicate subtitleLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("subtitle")), pattern);
            return criteriaBuilder.or(titleLike, subtitleLike);
        };
    }

    /**
     * Matches items where priceSale < priceOriginal (i.e. “on sale”).
     * If onSale == false (or null), this spec returns “true” (no constraint).
     */
    public static Specification<CatalogShoe> isOnSale() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("priceSale"), root.get("priceOriginal"));
    }

}

