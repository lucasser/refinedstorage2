package com.refinedmods.refinedstorage.common.grid.workstations;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class WorkstationList<T extends AbstractMatrix> extends ArrayList<T> {

    public T getById(final String id) {
        return this.stream()
            .filter(matrix -> matrix.getWorkstationType().equals(id))
            .findFirst()
            .orElseThrow(() ->
                new NoSuchElementException("Workstation '" + id + "' not found"));
    }

    @Override
    public boolean add(final T t) {
        return super.add(t);
    }

    /*public T getByIdOrMakeNew(final String id) {
        return this.stream()
            .filter(matrix -> matrix.getWorkstationType().equals(id))
            .findFirst()
            .orElse(add());
    }*/
}
