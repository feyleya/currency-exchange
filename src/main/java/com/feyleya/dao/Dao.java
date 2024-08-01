package com.feyleya.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T, E> {
    List<T> getAll();
    Optional<T> getByCode(E entity);
    //void getByCode();
    void create(E entity);
}
