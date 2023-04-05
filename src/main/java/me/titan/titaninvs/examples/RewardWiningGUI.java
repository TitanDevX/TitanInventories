package me.titan.titaninvs.examples;

import me.titan.titaninvs.content.ClickableItem;
import me.titan.titaninvs.content.ItemBuilder;
import me.titan.titaninvs.invs.UpdatingInv;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class RewardWiningGUI extends UpdatingInv {
    List<ItemStack> possibleRewards;
    public RewardWiningGUI(List<ItemStack> possibleRewards)   {
        super("Your reward:",27, 10);
        this.possibleRewards = possibleRewards;
    }

    @Override
    public void init(Player p) {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 3; y++) {

                int slot = x+(y*9);
                put(slot, ClickableItem.empty(ItemBuilder.create(Material.PINK_STAINED_GLASS_PANE).getItemStack()));
            }
        }
        for (int x = 5; x < 9; x++) {
            for (int y = 0; y < 3; y++) {
                int slot = x+(y*9);

                put(slot, ClickableItem.empty(ItemBuilder.create(Material.PINK_STAINED_GLASS_PANE).getItemStack()));
            }
        }
        int s = 4;
        for (ItemStack possibleReward : possibleRewards) {
            put(s, ClickableItem.empty(possibleReward));
            s+=9;
        }

    }


    Random random = new Random();
    int rewardSlot = 4;
    int rewardIndex = 0;

    int updates = 0;

    @Override
    public void updateItems(Player player) {
        if(rewardIndex > possibleRewards.size()-1){
            rewardIndex = 0;
            rewardSlot = 4;
        }
        DyeColor color = DyeColor.values()[random.nextInt(DyeColor.values().length)];
        Material mat = Material.getMaterial(color.toString() + "_STAINED_GLASS_PANE");
        while(mat == null || color == DyeColor.BLACK){
            color = DyeColor.values()[random.nextInt(DyeColor.values().length)];
            mat = Material.getMaterial(color.toString() + "_STAINED_GLASS_PANE");
        }
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 3; y++) {
                int slot = x+(y*9);
                put(slot, ClickableItem.empty(ItemBuilder.create(mat).getItemStack()));
            }
        }

        for (int x = 5; x < 9; x++) {
            for (int y = 0; y < 3; y++) {
                int slot = x+(y*9);
                put(slot, ClickableItem.empty(ItemBuilder.create(mat).getItemStack()));
            }
        }

        for(int i = 4;i<=22;i+=9){
            if(i != rewardSlot){
                put(i,ClickableItem.empty(ItemBuilder.create(mat).getItemStack()));
            }else{
                remove(i);
            }
        }
        put(rewardSlot,ClickableItem.empty(possibleRewards.get(rewardIndex)));


        if(random.nextInt(100) == 10){
            stop();
            System.out.println("WON");
            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 3; y++) {
                    int slot = x+(y*9);
                    put(slot, ClickableItem.empty(ItemBuilder.create(Material.BLACK_STAINED_GLASS_PANE).getItemStack()));
                }
            }
            for (int x = 5; x < 9; x++) {
                for (int y = 0; y < 3; y++) {
                    int slot = x+(y*9);
                    put(slot, ClickableItem.empty(ItemBuilder.create(Material.BLACK_STAINED_GLASS_PANE).getItemStack()));
                }
            }
            put(13,ClickableItem.empty(possibleRewards.get(rewardIndex)));

            setTitle("You won " + rewardIndex);
        }
        rewardIndex++;
        rewardSlot+=9;
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,1f,1f);
    }

    @Override
    public boolean isStaticDelay() {
        return false;
    }
}
