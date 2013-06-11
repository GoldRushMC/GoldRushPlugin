package com.goldrushmc.bukkit.db;

import javax.persistence.*;

@Entity
@Table(name = "item_list_tbl")
public class ItemForeignKeyTbl {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "ITEM_ID")
    @ManyToOne
    private ItemTbl item;
    @Column(name = "CART_ID")
    @ManyToOne
    private CartListTbl cart;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the item
     */
    public ItemTbl getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(ItemTbl item) {
        this.item = item;
    }

    /**
     * @return the cart
     */
    public CartListTbl getCart() {
        return cart;
    }

    /**
     * @param cart the cart to set
     */
    public void setCart(CartListTbl cart) {
        this.cart = cart;
    }

}
