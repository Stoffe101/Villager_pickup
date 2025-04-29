package com.example.villager_pickup.item;

import com.example.villager_pickup.Villager_pickup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class VillagerItem extends Item {

    public VillagerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient()) {
            return ActionResult.SUCCESS;
        }

        ServerWorld world = (ServerWorld) context.getWorld();
        ItemStack stack = context.getStack();
        BlockPos pos = context.getBlockPos();
        Direction direction = context.getSide();

        // Adjust position to be on top of the block if clicked on the top
        BlockPos placePos;
        if (direction == Direction.UP) {
            placePos = pos.up();
        } else {
            placePos = pos.offset(direction);
        }

        try {
            // Check if the item has NBT data
            if (stack.hasNbt()) {
                NbtCompound itemNbt = stack.getNbt();
                
                // Check for our custom villager data
                if (itemNbt != null && itemNbt.contains(Villager_pickup.VILLAGER_DATA_KEY)) {
                    // Get the saved villager data
                    NbtCompound villagerNbt = itemNbt.getCompound(Villager_pickup.VILLAGER_DATA_KEY);
                    
                    // Create a new villager entity
                    VillagerEntity villager = new VillagerEntity(EntityType.VILLAGER, world);
                    
                    // Apply the saved villager data
                    villager.readNbt(villagerNbt);
                    
                    // Position the villager
                    villager.refreshPositionAndAngles(
                        placePos.getX() + 0.5, 
                        placePos.getY(), 
                        placePos.getZ() + 0.5, 
                        0, 0
                    );
                    
                    // Spawn the villager in the world
                    world.spawnEntity(villager);
                    
                    // Inform player
                    if (context.getPlayer() != null) {
                        context.getPlayer().sendMessage(Text.of("Villager placed!"), true);
                    }
                    
                    // Remove the item if not in creative mode
                    if (context.getPlayer() != null && !context.getPlayer().getAbilities().creativeMode) {
                        stack.decrement(1);
                    }
                    
                    return ActionResult.CONSUME;
                }
            }
        } catch (Exception e) {
            // Log error but don't crash
            System.err.println("Error placing villager: " + e.getMessage());
            e.printStackTrace();
        }
        
        return ActionResult.PASS;
    }
}
