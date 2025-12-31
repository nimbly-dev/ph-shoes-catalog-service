package com.nimbly.phshoesbackend.catalog.core.repository.jpa;

import com.nimbly.phshoesbackend.catalog.core.model.CatalogShoe;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class CatalogShoeSpecificationsTest {
    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private CriteriaQuery<?> criteriaQuery;
    @Mock
    private Root<CatalogShoe> root;
    @Mock
    private Predicate predicate;
    @Mock
    private Expression<String> stringExpression;
    @Mock
    private Expression<Integer> intExpression;
    @Mock
    private Path<String> stringPath;
    @Mock
    private Path<Integer> intPath;
    @Mock
    private Path<Double> doublePath;

    @BeforeEach
    void setUp() {
        lenient().when(root.<String>get(anyString())).thenReturn(stringPath);
        lenient().when(root.<Integer>get(eq("year"))).thenReturn(intPath);
        lenient().when(root.<Integer>get(eq("month"))).thenReturn(intPath);
        lenient().when(root.<Integer>get(eq("day"))).thenReturn(intPath);
        lenient().when(root.<Double>get(eq("priceSale"))).thenReturn(doublePath);
        lenient().when(root.<Double>get(eq("priceOriginal"))).thenReturn(doublePath);

        lenient().when(criteriaBuilder.lower(any(Expression.class))).thenReturn(stringExpression);
        lenient().when(criteriaBuilder.equal(any(Expression.class), anyString())).thenReturn(predicate);
        lenient().when(criteriaBuilder.equal(any(Expression.class), anyInt())).thenReturn(predicate);
        lenient().when(criteriaBuilder.like(any(Expression.class), anyString())).thenReturn(predicate);
        lenient().when(criteriaBuilder.and(any(Predicate.class), any(Predicate.class))).thenReturn(predicate);
        lenient().when(criteriaBuilder.and(any(Predicate.class), any(Predicate.class), any(Predicate.class))).thenReturn(predicate);
        lenient().when(criteriaBuilder.or(any(Predicate.class), any(Predicate.class))).thenReturn(predicate);
        lenient().when(criteriaBuilder.or(any(Predicate.class), any(Predicate.class), any(Predicate.class))).thenReturn(predicate);
        lenient().when(criteriaBuilder.or(any(Predicate[].class))).thenReturn(predicate);
        lenient().when(criteriaBuilder.not(any(Predicate.class))).thenReturn(predicate);
        lenient().when(criteriaBuilder.lessThan(any(Expression.class), any(Expression.class))).thenReturn(predicate);
        lenient().when(criteriaBuilder.lessThan(any(Expression.class), anyDouble())).thenReturn(predicate);
        lenient().when(criteriaBuilder.lessThanOrEqualTo(any(Expression.class), anyDouble())).thenReturn(predicate);
        lenient().when(criteriaBuilder.greaterThanOrEqualTo(any(Expression.class), anyDouble())).thenReturn(predicate);
        lenient().when(criteriaBuilder.between(any(Expression.class), anyInt(), anyInt())).thenReturn(predicate);
        lenient().when(criteriaBuilder.between(any(Expression.class), anyDouble(), anyDouble())).thenReturn(predicate);
        lenient().when(criteriaBuilder.function(anyString(), eq(String.class), any(Expression.class))).thenReturn(stringExpression);
        lenient().when(criteriaBuilder.prod(any(Expression.class), anyInt())).thenReturn(intExpression);
        lenient().when(criteriaBuilder.sum(any(Expression.class), any(Expression.class))).thenReturn(intExpression);
        lenient().when(criteriaBuilder.conjunction()).thenReturn(predicate);
    }

    @Test
    void basicAttributeSpecificationsReturnPredicates() {
        // Arrange
        String brand = "Nike";
        String gender = "Men";
        String ageGroup = "Adult";

        // Act
        Predicate brandPredicate = CatalogShoeSpecifications.hasBrand(brand).toPredicate(root, criteriaQuery, criteriaBuilder);
        Predicate genderPredicate = CatalogShoeSpecifications.hasGender(gender).toPredicate(root, criteriaQuery, criteriaBuilder);
        Predicate agePredicate = CatalogShoeSpecifications.hasAgeGroup(ageGroup).toPredicate(root, criteriaQuery, criteriaBuilder);
        Predicate onSalePredicate = CatalogShoeSpecifications.isOnSale().toPredicate(root, criteriaQuery, criteriaBuilder);

        // Assert
        assertThat(brandPredicate).isSameAs(predicate);
        assertThat(genderPredicate).isSameAs(predicate);
        assertThat(agePredicate).isSameAs(predicate);
        assertThat(onSalePredicate).isSameAs(predicate);
    }

    @Test
    void collectedOnBuildsPredicate() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 11, 19);

        // Act
        Predicate result = CatalogShoeSpecifications.collectedOn(date).toPredicate(root, criteriaQuery, criteriaBuilder);

        // Assert
        assertThat(result).isSameAs(predicate);
    }

    @Test
    void collectedBetweenBuildsPredicate() {
        // Arrange
        LocalDate start = LocalDate.of(2024, 10, 1);
        LocalDate end = LocalDate.of(2024, 10, 31);

        // Act
        Predicate result = CatalogShoeSpecifications.collectedBetween(start, end).toPredicate(root, criteriaQuery, criteriaBuilder);

        // Assert
        assertThat(result).isSameAs(predicate);
    }

    @Test
    void hasAnySizeReturnsConjunctionWhenEmpty() {
        // Arrange
        List<String> sizes = List.of();

        // Act
        Predicate result = CatalogShoeSpecifications.hasAnySize(sizes).toPredicate(root, criteriaQuery, criteriaBuilder);

        // Assert
        assertThat(result).isSameAs(predicate);
    }

    @Test
    void hasAnySizeBuildsPredicateWhenSizesProvided() {
        // Arrange
        List<String> sizes = List.of("9", "10");

        // Act
        Predicate result = CatalogShoeSpecifications.hasAnySize(sizes).toPredicate(root, criteriaQuery, criteriaBuilder);

        // Assert
        assertThat(result).isSameAs(predicate);
    }

    @Test
    void hasKeywordReturnsConjunctionWhenBlank() {
        // Arrange
        String keyword = " ";

        // Act
        Predicate result = CatalogShoeSpecifications.hasKeyword(keyword).toPredicate(root, criteriaQuery, criteriaBuilder);

        // Assert
        assertThat(result).isSameAs(predicate);
    }

    @Test
    void hasKeywordBuildsPredicateWhenPresent() {
        // Arrange
        String keyword = "runner";

        // Act
        Predicate result = CatalogShoeSpecifications.hasKeyword(keyword).toPredicate(root, criteriaQuery, criteriaBuilder);

        // Assert
        assertThat(result).isSameAs(predicate);
    }

    @Test
    void finalPriceFiltersBuildPredicates() {
        // Arrange
        double min = 50.0;
        double max = 120.0;

        // Act
        Predicate gteResult = CatalogShoeSpecifications.finalPriceGte(min).toPredicate(root, criteriaQuery, criteriaBuilder);
        Predicate lteResult = CatalogShoeSpecifications.finalPriceLte(max).toPredicate(root, criteriaQuery, criteriaBuilder);
        Predicate betweenResult = CatalogShoeSpecifications.finalPriceBetween(min, max).toPredicate(root, criteriaQuery, criteriaBuilder);

        // Assert
        assertThat(gteResult).isSameAs(predicate);
        assertThat(lteResult).isSameAs(predicate);
        assertThat(betweenResult).isSameAs(predicate);
    }
}

