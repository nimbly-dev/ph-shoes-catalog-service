package com.nimbly.phshoesbackend.catalog.core.service;

import com.nimbly.phshoesbackend.catalog.core.model.FactProductShoes;
import com.nimbly.phshoesbackend.catalog.core.repository.jpa.FactProductShoesSpecRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;


public interface FactProductShoesService {
    Page<FactProductShoes> fetchBySpec(Specification<FactProductShoes> spec, Pageable pageable);
    List<FactProductShoesSpecRepository.LatestData> getLatestDataByBrand();
}
