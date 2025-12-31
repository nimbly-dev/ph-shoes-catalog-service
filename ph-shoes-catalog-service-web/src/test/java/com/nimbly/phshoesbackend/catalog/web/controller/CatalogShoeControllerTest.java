package com.nimbly.phshoesbackend.catalog.web.controller;

import com.nimbly.phshoesbackend.catalog.core.model.CatalogShoe;
import com.nimbly.phshoesbackend.catalog.core.repository.jpa.CatalogShoeRepository;
import com.nimbly.phshoesbackend.catalog.core.service.CatalogShoeService;
import com.nimbly.phshoesbackend.catalog.web.api.model.CatalogShoePage;
import com.nimbly.phshoesbackend.catalog.web.api.model.LatestBrandData;
import com.nimbly.phshoesbackend.catalog.web.mappers.CatalogShoeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogShoeControllerTest {
    @Mock
    private CatalogShoeService service;

    @Mock
    private CatalogShoeMapper mapper;

    @InjectMocks
    private CatalogShoeController controller;

    @Test
    void listCatalogShoes_returnsMappedPage() {
        // Arrange
        CatalogShoePage mappedPage = new CatalogShoePage();
        Page<CatalogShoe> results = new PageImpl<>(List.of(), PageRequest.of(1, 15), 0);
        when(service.fetchBySpec(any(), any(Pageable.class))).thenReturn(results);
        when(mapper.toPage(results)).thenReturn(mappedPage);

        // Act
        ResponseEntity<CatalogShoePage> response = controller.listCatalogShoes(
                "Nike",
                "Men",
                "Adult",
                LocalDate.of(2024, 11, 19),
                null,
                null,
                "runner",
                List.of("9"),
                true,
                100.0,
                150.0,
                1,
                15,
                "brand,desc"
        );

        // Assert
        assertThat(response.getBody()).isSameAs(mappedPage);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(service).fetchBySpec(any(), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isEqualTo(1);
        assertThat(pageable.getPageSize()).isEqualTo(15);
        Sort.Order order = pageable.getSort().getOrderFor("brand");
        assertThat(order).isNotNull();
        assertThat(order.getDirection()).isEqualTo(Sort.Direction.DESC);
        verify(mapper).toPage(results);
    }

    @Test
    void listCatalogShoes_rejectsInvalidDateRange() {
        // Arrange
        LocalDate startDate = LocalDate.of(2024, 12, 2);
        LocalDate endDate = LocalDate.of(2024, 11, 30);

        // Act
        Throwable thrown = catchThrowable(() -> controller.listCatalogShoes(
                null,
                null,
                null,
                null,
                startDate,
                endDate,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        ));

        // Assert
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("startDate");
        verifyNoInteractions(service, mapper);
    }

    @Test
    void listCatalogShoes_rejectsInvalidPriceRange() {
        // Arrange
        Double minPrice = 200.0;
        Double maxPrice = 100.0;

        // Act
        Throwable thrown = catchThrowable(() -> controller.listCatalogShoes(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                minPrice,
                maxPrice,
                null,
                null,
                null
        ));

        // Assert
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("minPrice");
        verifyNoInteractions(service, mapper);
    }

    @Test
    void listCatalogShoes_rejectsUnsupportedSort() {
        // Arrange
        String sort = "unknown,desc";

        // Act
        Throwable thrown = catchThrowable(() -> controller.listCatalogShoes(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                sort
        ));

        // Assert
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported sort field");
        verifyNoInteractions(service, mapper);
    }

    @Test
    void getLatestCatalogShoes_returnsMappedResults() {
        // Arrange
        CatalogShoeRepository.LatestData latestData = org.mockito.Mockito.mock(CatalogShoeRepository.LatestData.class);
        LatestBrandData mapped = new LatestBrandData().brand("Nike").latestDwid("20240201");
        when(service.getLatestDataByBrand()).thenReturn(List.of(latestData));
        when(mapper.toLatestBrandData(latestData)).thenReturn(mapped);

        // Act
        ResponseEntity<List<LatestBrandData>> response = controller.getLatestCatalogShoes();

        // Assert
        assertThat(response.getBody()).containsExactly(mapped);
    }
}
