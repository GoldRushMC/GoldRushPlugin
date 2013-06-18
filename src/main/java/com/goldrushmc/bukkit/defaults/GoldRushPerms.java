package com.goldrushmc.bukkit.defaults;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class GoldRushPerms {
    public static final Permission DEFAULT = new Permission("goldrushmc.types", "The player can use any of the trainstation types commands.", PermissionDefault.OP),
            LIST = new Permission("goldrushmc.types.list", "", PermissionDefault.TRUE),
            CREATE = new Permission("goldrushmc.types.create", "The player can create things", PermissionDefault.OP),
            ADD_CART = new Permission("goldrushmc.types.addcart", "The player can buy carts", PermissionDefault.TRUE),
            REMOVE_CART = new Permission("goldrushmc.types.removecart", "The player can sell carts", PermissionDefault.TRUE),
            ADD_SIGN = new Permission("goldrushmc.types.addsign", "The player can add signs to stations", PermissionDefault.OP),
            REMOVE_SIGN = new Permission("goldrushmc.types.removesign", "The player can remove signs from stations", PermissionDefault.OP),
            CHANGE_BLOCK = new Permission("goldrushmc.types.changeblock", "The player can edit blocks within trainstation stations", PermissionDefault.OP),
            REMOVE = new Permission("goldrushmc.types.destroy", "The player can delete trainstation stations", PermissionDefault.OP),
            ADD = new Permission("goldrushmc.types.create", "The player can create trainstation stations", PermissionDefault.OP),
            SCHEDULE = new Permission("goldrushmc.types.schedule", "The player can schedule trainstation cycles", PermissionDefault.OP);


    final Permission perm;

    public GoldRushPerms(String perm, String description, PermissionDefault defaultP) {
        this.perm = new Permission(perm, description, defaultP);
    }
}
