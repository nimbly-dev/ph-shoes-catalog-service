package com.nimbly.phshoesbackend.catalog.core.service.impl;

import com.nimbly.phshoesbackend.catalog.core.model.CatalogShoe;
import com.nimbly.phshoesbackend.catalog.core.repository.jpa.CatalogShoeRepository;
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
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogShoeServiceImplTest {
    @Mock
    private CatalogShoeRepository specRepository;

    @InjectMocks
    private CatalogShoeServiceImpl service;

    @Test
    void fetchBySpec_capsPageSizeAtMax() {
        // Arrange
        Specification<CatalogShoe> spec = (root, query, builder) -> null;
        Pageable requestedPage = PageRequest.of(2, 5000, Sort.by("brand").ascending());
        Page<CatalogShoe> expected = new PageImpl<>(List.of(), PageRequest.of(2, 2500, Sort.by("brand").ascending()), 0);
        when(specRepository.findAll(eq(spec), any(Pageable.class))).thenReturn(expected);

        // Act
        Page<CatalogShoe> result = service.fetchBySpec(spec, requestedPage);

        // Assert
        assertThat(result).isSameAs(expected);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(specRepository).findAll(eq(spec), pageableCaptor.capture());
        Pageable safePage = pageableCaptor.getValue();
        assertThat(safePage.getPageNumber()).isEqualTo(2);
        assertThat(safePage.getPageSize()).isEqualTo(2500);
        assertThat(safePage.getSort()).isEqualTo(requestedPage.getSort());
    }

    @Test
    void getLatestDataByBrand_returnsRepositoryResults() {
        // Arrange
        CatalogShoeRepository.LatestData latestData = org.mockito.Mockito.mock(CatalogShoeRepository.LatestData.class);
        List<CatalogShoeRepository.LatestData> expected = List.of(latestData);
        when(specRepository.findLatestDatePerBrand()).thenReturn(expected);

        // Act
        List<CatalogShoeRepository.LatestData> result = service.getLatestDataByBrand();

        // Assert
        assertThat(result).isSameAs(expected);
    }
}

