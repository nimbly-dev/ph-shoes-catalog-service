package com.nimbly.phshoesbackend.catalog.core.service.impl;

import com.nimbly.phshoesbackend.catalog.core.model.FactProductShoes;
import com.nimbly.phshoesbackend.catalog.core.repository.jpa.FactProductShoesSpecRepository;
import com.nimbly.phshoesbackend.catalog.core.service.FactProductShoesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FactProductShoesServiceImpl implements FactProductShoesService {

    private final static int MAX_PAGE_SIZE = 2500;

    private final FactProductShoesSpecRepository specRepo;

    public FactProductShoesServiceImpl(FactProductShoesSpecRepository specRepo) {
        this.specRepo = specRepo;
    }

    @Override
    public Page<FactProductShoes> fetchBySpec(Specification<FactProductShoes> spec, Pageable pageable) {
        int    safeSize   = Math.min(pageable.getPageSize(), MAX_PAGE_SIZE);
        Pageable safePg   = PageRequest.of(pageable.getPageNumber(), safeSize, pageable.getSort());
        return specRepo.findAll(spec, safePg);
    }

    @Override
    public List<FactProductShoesSpecRepository.LatestData> getLatestDataByBrand() {
        return specRepo.findLatestDatePerBrand();
    }
}
