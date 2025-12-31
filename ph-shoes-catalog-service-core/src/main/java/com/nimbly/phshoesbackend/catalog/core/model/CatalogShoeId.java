package com.nimbly.phshoesbackend.catalog.core.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class CatalogShoeId implements Serializable {

    private String id;

    private String dwid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CatalogShoeId that)) return false;
        return Objects.equals(id, that.id) &&
                Objects.equals(dwid, that.dwid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dwid);
    }

}

