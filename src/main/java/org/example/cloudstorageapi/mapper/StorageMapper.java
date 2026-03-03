package org.example.cloudstorageapi.mapper;

import java.util.List;

public interface StorageMapper <R, I>{

    R mapObject(String fullPath, long size, boolean hasRootFolder);
    List<R> mapObjects(List<I> items, boolean hasRootFolder);
}
