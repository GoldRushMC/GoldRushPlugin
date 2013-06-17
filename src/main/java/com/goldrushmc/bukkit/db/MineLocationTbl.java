package com.goldrushmc.bukkit.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created with IntelliJ IDEA.
 * User: Lucas
 * Date: 6/17/13
 * Time: 1:03 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class MineLocationTbl extends  DefaultLocationTbl{

    @ManyToOne
    private MinesTbl mine;
    @Column(name = "IS_ENTRANCE")
    private boolean entrance = false;

    public MinesTbl getMine() {
        return mine;
    }

    public void setMine(MinesTbl mine) {
        this.mine = mine;
    }

    public boolean isEntrance() {
        return entrance;
    }

    public void setEntrance(boolean entrance) {
        this.entrance = entrance;
    }
}
