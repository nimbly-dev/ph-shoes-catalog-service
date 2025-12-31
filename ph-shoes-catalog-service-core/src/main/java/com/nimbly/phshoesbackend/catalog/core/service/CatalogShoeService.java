package com.nimbly.phshoesbackend.catalog.core.service;

import com.nimbly.phshoesbackend.catalog.core.model.CatalogShoe;
import com.nimbly.phshoesbackend.catalog.core.repository.jpa.CatalogShoeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;


public interface CatalogShoeService {
    Page<CatalogShoe> fetchBySpec(Specification<CatalogShoe> spec, Pageable pageable);
    List<CatalogShoeRepository.LatestData> getLatestDataByBrand();
}

