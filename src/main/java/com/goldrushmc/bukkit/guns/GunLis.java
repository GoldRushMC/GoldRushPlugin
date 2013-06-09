package com.goldrushmc.bukkit.guns;

import com.bergerkiller.bukkit.common.events.EntityMoveEvent;
import com.goldrushmc.bukkit.defaults.DefaultListener;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;


public class GunLis extends DefaultListener {

    public GunLis(JavaPlugin plugin) {
        super(plugin);
        // TODO Auto-generated constructor stub
    }

    public HashMap<Player, Revolver> revolverHash = new HashMap<Player, Revolver>();
    public HashMap<Player, Rifle> rifleHash = new HashMap<Player, Rifle>();
    public HashMap<Player, Shotgun> shotgunHash = new HashMap<Player, Shotgun>();

    public HashMap<Integer, Integer> firedEntityHash = new HashMap<Integer, Integer>();

    public HashMap<Entity, Vector> velocityHash = new HashMap<Entity, Vector>();
    public GunTools gunTools = new GunTools();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRightClick(PlayerInteractEvent e) {
        Action eAction = e.getAction();
        Player p = e.getPlayer();
        if (eAction.equals(Action.RIGHT_CLICK_AIR) || eAction.equals(Action.RIGHT_CLICK_BLOCK)) {
            if (!p.isSneaking()) {
                //Revolver
                if (p.getItemInHand().getType().equals(Material.GOLD_AXE)) {
                    if (p.getItemInHand().getItemMeta().hasDisplayName()) {
                        if (p.getItemInHand().getItemMeta().getDisplayName().equals("Colt 1851")) {
                            if (!revolverHash.containsKey(p)) {
                                Revolver revolver = new Revolver(p, this.plugin);
                                revolverHash.put(p, revolver);

                                revolverHash.get(p).fire();
                                firedEntityHash.put(revolverHash.get(p).firedEntity, revolverHash.get(p).damage);
                            } else {
                                revolverHash.get(p).fire();
                                firedEntityHash.put(revolverHash.get(p).firedEntity, revolverHash.get(p).damage);
                            }
                        }
                    }
                }

                //Rifle
                if (p.getItemInHand().getType().equals(Material.GOLD_SPADE)) {
                    if (p.getItemInHand().getItemMeta().hasDisplayName()) {
                        if (p.getItemInHand().getItemMeta().getDisplayName().equals("Sharps Rifle")) {
                            if (!rifleHash.containsKey(p)) {
                                Rifle rifle = new Rifle(p, this.plugin);
                                rifleHash.put(p, rifle);

                                rifleHash.get(p).fire();
                                firedEntityHash.put(rifleHash.get(p).firedEntity, rifleHash.get(p).damage);
                            } else {
                                rifleHash.get(p).fire();
                                firedEntityHash.put(rifleHash.get(p).firedEntity, rifleHash.get(p).damage);
                            }
                        }
                    }
                }

                //Shotgun
                if (p.getItemInHand().getType().equals(Material.GOLD_HOE)) {
                    if (p.getItemInHand().getItemMeta().hasDisplayName()) {
                        if (p.getItemInHand().getItemMeta().getDisplayName().equals("Coach Gun")) {
                            if (!shotgunHash.containsKey(p)) {
                                Shotgun shotgun = new Shotgun(p, this.plugin);
                                shotgunHash.put(p, shotgun);

                                shotgunHash.get(p).fire();
                                for (int i = 0; i < shotgunHash.get(p).firedEntity.size(); i++) {
                                    firedEntityHash.put(shotgunHash.get(p).firedEntity.get(i), shotgunHash.get(p).damage);
                                }
                            } else {
                                shotgunHash.get(p).fire();
                                for (int i = 0; i < shotgunHash.get(p).firedEntity.size(); i++) {
                                    firedEntityHash.put(shotgunHash.get(p).firedEntity.get(i), shotgunHash.get(p).damage);
                                }
                            }
                        }
                    }
                }
            } else {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 20));
            }

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeftClick(PlayerInteractEvent e) {
        Action eAction = e.getAction();
        Player p = e.getPlayer();
        if (eAction.equals(Action.LEFT_CLICK_AIR) || eAction.equals(Action.LEFT_CLICK_BLOCK)) {
            if (p.isSneaking()) {
                //Revolver
                if (p.getItemInHand().getType().equals(Material.GOLD_AXE)) {
                    if (p.getItemInHand().getItemMeta().hasDisplayName()) {
                        if (p.getItemInHand().getItemMeta().getDisplayName().equals("Colt 1851")) {
                            if (!revolverHash.containsKey(p)) {
                                Revolver revolver = new Revolver(p, this.plugin);
                                revolverHash.put(p, revolver);
                                revolverHash.get(p).reload();
                            } else {
                                revolverHash.get(p).reload();
                            }
                        }
                    }
                }

                //Rifle
                if (p.getItemInHand().getType().equals(Material.GOLD_SPADE)) {
                    if (p.getItemInHand().getItemMeta().hasDisplayName()) {
                        if (p.getItemInHand().getItemMeta().getDisplayName().equals("Sharps Rifle")) {
                            if (!rifleHash.containsKey(p)) {
                                Rifle rifle = new Rifle(p, this.plugin);
                                rifleHash.put(p, rifle);
                                rifleHash.get(p).reload();
                            } else {
                                rifleHash.get(p).reload();
                            }
                        }
                    }
                }

                //Shotgun
                if (p.getItemInHand().getType().equals(Material.GOLD_HOE)) {
                    if (p.getItemInHand().getItemMeta().hasDisplayName()) {
                        if (p.getItemInHand().getItemMeta().getDisplayName().equals("Coach Gun")) {
                            if (!shotgunHash.containsKey(p)) {
                                Shotgun shotgun = new Shotgun(p, this.plugin);
                                shotgunHash.put(p, shotgun);
                                shotgunHash.get(p).reload();
                            } else {
                                shotgunHash.get(p).reload();
                            }
                        }
                    }
                }

            } else {
                //Revolver
                if (p.getItemInHand().getType().equals(Material.GOLD_AXE)) {
                    if (p.getItemInHand().getItemMeta().hasDisplayName()) {
                        if (p.getItemInHand().getItemMeta().getDisplayName().equals("Colt 1851")) {
                            if (!revolverHash.containsKey(p)) {
                                Revolver revolver = new Revolver(p, this.plugin);
                                revolverHash.put(p, revolver);
                                revolverHash.get(p).cock();
                            } else {
                                revolverHash.get(p).cock();
                            }
                        }
                    }
                }

                //Rifle
                if (p.getItemInHand().getType().equals(Material.GOLD_SPADE)) {
                    if (p.getItemInHand().getItemMeta().hasDisplayName()) {
                        if (p.getItemInHand().getItemMeta().getDisplayName().equals("Sharps Rifle")) {
                            if (!rifleHash.containsKey(p)) {
                                Rifle rifle = new Rifle(p, this.plugin);
                                rifleHash.put(p, rifle);
                                rifleHash.get(p).cock();
                            } else {
                                rifleHash.get(p).cock();
                            }
                        }
                    }
                }

                //Shotgun
                if (p.getItemInHand().getType().equals(Material.GOLD_HOE)) {
                    if (p.getItemInHand().getItemMeta().hasDisplayName()) {
                        if (p.getItemInHand().getItemMeta().getDisplayName().equals("Coach Gun")) {
                            if (!shotgunHash.containsKey(p)) {
                                Shotgun shotgun = new Shotgun(p, this.plugin);
                                shotgunHash.put(p, shotgun);
                                shotgunHash.get(p).cock();
                            } else {
                                shotgunHash.get(p).cock();
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void OnEntityMove(EntityMoveEvent e) {
        if (e.getEntityType().equals(EntityType.SNOWBALL)) {
            if (!velocityHash.containsKey(e.getEntity())) {
                velocityHash.put(e.getEntity(), e.getEntity().getVelocity());
            } else {
                e.getEntity().setVelocity(velocityHash.get(e.getEntity()));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager().getType().equals(EntityType.SNOWBALL)) {
            int damager = e.getDamager().getEntityId();
            e.setDamage(firedEntityHash.get(damager));
        }
    }
}
