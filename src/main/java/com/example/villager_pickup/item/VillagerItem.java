package com.example.villager_pickup.item;

import com.example.villager_pickup.Villager_pickup;
import com.example.villager_pickup.component.VillagerComponents;
import com.example.villager_pickup.component.VillagerData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.nbt.NbtCompound;

import java.util.List;

/**
 * Custom item for capturing and placing villagers.
 * Stores villager data and spawns them back with all their information intact.
 */
public class VillagerItem extends Item {

    private static final Logger LOGGER = LoggerFactory.getLogger(Villager_pickup.MOD_ID);

    public VillagerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        // Always return success on client to prevent desync
        if (context.getWorld().isClient()) {
            return ActionResult.SUCCESS;
        }

        ServerWorld world = (ServerWorld) context.getWorld();
        ItemStack stack = context.getStack();
        BlockPos pos = context.getBlockPos();
        Direction direction = context.getSide();
        ServerPlayerEntity player = context.getPlayer() instanceof ServerPlayerEntity ? 
                                   (ServerPlayerEntity) context.getPlayer() : null;

        // Determine where to place the villager (on top or adjacent to the block)
        BlockPos placePos;
        if (direction == Direction.UP) {
            placePos = pos.up();
        } else {
            placePos = pos.offset(direction);
        }

        try {
            // Check if the position is obstructed
            if (!world.getBlockState(placePos).isAir()) {
                if (player != null) {
                    player.sendMessage(
                        Text.translatable("message.villager_pickup.space_obstructed")
                            .formatted(Formatting.RED), 
                        true
                    );
                }
                return ActionResult.FAIL;
            }
            
            // Create a new villager entity
            VillagerEntity villager = new VillagerEntity(EntityType.VILLAGER, world);
            
            // Position the villager with a slight offset to prevent suffocation
            villager.refreshPositionAndAngles(
                placePos.getX() + 0.5, 
                placePos.getY(), 
                placePos.getZ() + 0.5, 
                world.getRandom().nextFloat() * 360.0F, 0
            );
            
            // Apply stored data
            VillagerData villagerData = VillagerComponents.VILLAGER_DATA.get(stack);
            boolean dataApplied = false;
            
            if (villagerData.hasData()) {
                dataApplied = villagerData.applyTo(villager);
                LOGGER.debug("Applied villager data: {}", dataApplied);
            }
            
            // Apply custom name if available and no other data was applied
            if (!dataApplied && stack.hasCustomName()) {
                villager.setCustomName(stack.getName());
                LOGGER.debug("Applied custom name: {}", stack.getName().getString());
            }
            
            // Spawn the villager
            boolean entitySpawned = world.spawnEntity(villager);
            
            if (!entitySpawned) {
                LOGGER.error("Failed to spawn villager entity");
                return ActionResult.FAIL;
            }
            
            // Play placement sound
            world.playSound(
                null, 
                placePos, 
                SoundEvents.ENTITY_VILLAGER_AMBIENT, 
                SoundCategory.NEUTRAL, 
                1.0F, 
                1.0F
            );
            
            // Inform player
            if (player != null) {
                player.sendMessage(
                    Text.translatable("message.villager_pickup.placed")
                        .formatted(Formatting.GREEN), 
                    true
                );
            }
            
            // Consume the item in non-creative mode
            if (player != null && !player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
            
            return ActionResult.CONSUME;
        } catch (Exception e) {
            LOGGER.error("Error placing villager: {}", e.getMessage(), e);
        }
        
        return ActionResult.PASS;
    }
    
    @Override
    public boolean hasGlint(ItemStack stack) {
        // Add enchantment glint for items with villager data
        VillagerData data = VillagerComponents.VILLAGER_DATA.get(stack);
        return data.hasData();
    }
    
    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        // Only add basic tooltip here - detailed tooltips are added by the client event
        if (!VillagerComponents.VILLAGER_DATA.get(stack).hasData()) {
            tooltip.add(Text.translatable("tooltip.villager_pickup.right_click_to_place")
                    .formatted(Formatting.ITALIC, Formatting.DARK_GRAY));
        }
    }
}

