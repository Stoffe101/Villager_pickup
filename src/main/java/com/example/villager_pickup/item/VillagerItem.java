package com.example.villager_pickup.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
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

        // Get NBT data if it exists
        NbtCompound nbt = stack.hasNbt() ? stack.getNbt() : null;
        
        // Check if item has our custom villager data
        if (nbt != null && nbt.contains("VillagerData")) {
            // Create a new villager entity using COMMAND spawn reason (replaced SPAWN_EGG)
            VillagerEntity villager = EntityType.VILLAGER.create(world, placePos, SpawnReason.COMMAND);
            
            if (villager != null) {
                // Load the saved data into the villager
                villager.readNbt(nbt.getCompound("VillagerData"));
                
                // Position the villager
                villager.refreshPositionAndAngles(
                    placePos.getX() + 0.5, 
                    placePos.getY(), 
                    placePos.getZ() + 0.5, 
                    0, 0
                );
                
                // Spawn the villager in the world
                world.spawnEntityAndPassengers(villager);
                
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
        
        return ActionResult.PASS;
    }
}
