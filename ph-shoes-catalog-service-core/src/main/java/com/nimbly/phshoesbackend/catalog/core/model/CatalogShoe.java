package com.nimbly.phshoesbackend.catalog.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@IdClass(CatalogShoeId.class)
@Table(
        name = "FACT_PRODUCT_SHOES",
        schema = "PRODUCTION_MARTS",
        catalog = "PH_SHOES_DB"
)
@Data
public class CatalogShoe {

    //Composite key dwid + id
    @Id
    @Column(name = "ID", length = 16777216)
    private String id;

    @Id
    @Column(name = "DWID", length = 16777216)
    private String dwid;

    @Column(name = "BRAND", length = 16777216)
    private String brand;

    @Column(name = "YEAR")
    private Integer year;

    @Column(name = "MONTH")
    private Integer month;

    @Column(name = "DAY")
    private Integer day;

    @Column(name = "TITLE", length = 16777216)
    private String title;

    @Column(name = "SUBTITLE", length = 16777216)
    private String subtitle;

    @Column(name = "URL", length = 16777216)
    private String url;

    @Column(name = "IMAGE", length = 16777216)
    private String image;

    @Column(name = "PRICE_SALE")
    private Double priceSale;

    @Column(name = "PRICE_ORIGINAL")
    private Double priceOriginal;

    @Column(name = "GENDER", length = 16777216)
    private String gender;

    @Column(name = "AGE_GROUP", length = 16777216)
    private String ageGroup;

    @Column(name = "EXTRA", columnDefinition = "VARIANT")
    private String extra;
}

