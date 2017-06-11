package com.logiforge.tenniscloud.model.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 6/10/17.
 */

public class ListDiff <T> {
    public List<T> added;
    public List<UpdatedEntity<T>> updated;
    public List<T> deleted;

    public ListDiff() {
        added = new ArrayList<T>();
        updated = new ArrayList<UpdatedEntity<T>>();
        deleted = new ArrayList<T>();
    }

    public static class UpdatedEntity<T> {
        public T original;
        public T updated;

        public UpdatedEntity(T original, T updated) {
            this.original = original;
            this.updated = updated;
        }
    }
}
