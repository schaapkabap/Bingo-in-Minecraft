package com.ja90n.bingo.runable;

import com.ja90n.bingo.Bingo;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class WrongClickRunable extends BukkitRunnable {

    private Inventory inventory;
    private int slot;
    private String name;

    public WrongClickRunable(Inventory inventory, int slot, String name, Bingo bingo){
        this.inventory = inventory;
        this.slot = slot;
        this.name = name;
        runTaskTimer(bingo,0,10);
    }

    int timesRun = 0;
    @Override
    public void run() {
        try {
            if (timesRun == 0){
                ItemStack itemStack = new ItemStack(Material.RED_CONCRETE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(name);
                itemStack.setItemMeta(itemMeta);

                inventory.setItem(slot,itemStack);
            } else {
                ItemStack itemStack = new ItemStack(Material.PAPER);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(name);
                itemStack.setItemMeta(itemMeta);

                inventory.setItem(slot,itemStack);
                cancel();
            }
            timesRun++;
        } catch (Exception e){
            cancel();
        }
    }
}
