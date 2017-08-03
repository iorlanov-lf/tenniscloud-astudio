package com.logiforge.tenniscloud.model.util;

import com.logiforge.lavolta.android.model.DynamicEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by iorlanov on 7/15/17.
 */

public class EditableEntityList <T extends DynamicEntity> {
    Map<String, T> addedEntities;
    Map<String, T> updatedEntities;
    Set<String> deletedEntityIds;
    List<T> entities;

    public EditableEntityList(List<T> entities) {
        addedEntities = new HashMap<>();
        updatedEntities = new HashMap<>();
        deletedEntityIds = new TreeSet<>();
        this.entities = entities;
    }

    public int size() {
        return entities.size();
    }

    public T get(int i) {
        return entities.get(i);
    }

    public List<T> getEntities() {
        return entities;
    }

    public T find(String id) {
        for(T entity : entities) {
            if(entity.id.equals(id)) {
                return entity;
            }
        }

        return null;
    }

    public void addEntity(T entity) {
        addedEntities.put(entity.id, entity);
        entities.add(entity);
    }
    public Collection<T> getAddedEntities() {
        return addedEntities.values();
    }

    public void markAsUpdated(T entity) {
        if(!addedEntities.containsKey(entity.id)) {
            updatedEntities.put(entity.id, entity);
        }
    }
    public Collection<T> getUpdatedEntities() {
        return updatedEntities.values();
    }

    public void deleteEntity(String entityId) {
        for(int i=0; i<entities.size(); i++) {
            if(entities.get(i).id.equals(entityId)) {
                entities.remove(i);
                addedEntities.remove(entityId);
                updatedEntities.remove(entityId);
                deletedEntityIds.add(entityId);
                break;
            }
        }
    }
    public Collection<String> getDeletedEntityIds() {
        Iterator<String> iter = deletedEntityIds.iterator();
        Collection<String> list = new ArrayList<String>();
        while(iter.hasNext()) {
            list.add(iter.next());
        }
        return list;
    }
}
