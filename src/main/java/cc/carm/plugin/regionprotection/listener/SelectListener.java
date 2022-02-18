package cc.carm.plugin.regionprotection.listener;

import cc.carm.plugin.regionprotection.RegionProtection;
import cc.carm.plugin.regionprotection.configuration.PluginMessages;
import cc.carm.plugin.regionprotection.manager.PlayerManager;
import cc.carm.plugin.regionprotection.model.DataBlockLocation;
import cc.carm.plugin.regionprotection.model.SelectingRegion;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SelectListener implements Listener {

    @EventHandler
    public void onClicking(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK || event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        System.out.println("s0");
        if (block == null) return;
        boolean isPos1 = event.getAction() == Action.LEFT_CLICK_BLOCK;
        System.out.println("s1");
        Player player = event.getPlayer();
        if (!player.hasPermission("RegionProtection.admin")) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.GOLDEN_HOE) return;
        System.out.println("s2");

        PlayerManager manager = RegionProtection.getPlayerManager();
        if (!manager.isSelecting(player)) return;
        System.out.println("s3");
        SelectingRegion region = manager.getSelectingRegion(player);
        if (!block.getWorld().getName().equals(region.getWorld())) {
            if ((isPos1 && region.getPos2() == null) || (!isPos1 && region.getPos1() == null)) {
                region.setWorld(block.getWorld().getName());
            } else {
                PluginMessages.SelectMode.DIFFERENT_WORLD.send(player);
                return;
            }
        }
        DataBlockLocation location = new DataBlockLocation(block.getLocation());
        if (isPos1) {
            region.setPos1(location);
        } else {
            region.setPos2(location);
        }

        PluginMessages.SelectMode.SELECTED.send(player, new Object[]{isPos1 ? 1 : 2, location});

    }


}
