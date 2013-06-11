package com.goldrushmc.bukkit.db;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "item_tbl")
public class ItemTbl {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "ITEM_NAME")
    @NotEmpty
    private String name;
    @Column(name = "BASE_PRICE")
    @NotNull
    private int baseValue;
    @Column(name = "SD_INDEX")
    private float index;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWorth() {
        return index * baseValue;
    }

    public int getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(int baseValue) {
        this.baseValue = baseValue;
    }

    public float getIndex() {
        return index;
    }

    public void setIndex(float index) {
        this.index = index;
    }
}
