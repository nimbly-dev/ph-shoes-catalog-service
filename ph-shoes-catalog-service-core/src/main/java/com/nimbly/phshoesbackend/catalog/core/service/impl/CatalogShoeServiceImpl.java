package com.nimbly.phshoesbackend.catalog.core.service.impl;

import com.nimbly.phshoesbackend.catalog.core.model.CatalogShoe;
import com.nimbly.phshoesbackend.catalog.core.repository.jpa.CatalogShoeRepository;
import com.nimbly.phshoesbackend.catalog.core.service.CatalogShoeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogShoeServiceImpl implements CatalogShoeService {

    private final static int MAX_PAGE_SIZE = 2500;

    private final CatalogShoeRepository specRepo;

    public CatalogShoeServiceImpl(CatalogShoeRepository specRepo) {
        this.specRepo = specRepo;
    }

    @Override
    public Page<CatalogShoe> fetchBySpec(Specification<CatalogShoe> spec, Pageable pageable) {
        int    safeSize   = Math.min(pageable.getPageSize(), MAX_PAGE_SIZE);
        Pageable safePg   = PageRequest.of(pageable.getPageNumber(), safeSize, pageable.getSort());
        return specRepo.findAll(spec, safePg);
    }

    @Override
    public List<CatalogShoeRepository.LatestData> getLatestDataByBrand() {
        return specRepo.findLatestDatePerBrand();
    }
}

