package com.example.villager_pickup.component;

import com.example.villager_pickup.Villager_pickup;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the VillagerData component.
 * Handles storing and applying villager data.
 */
public class VillagerDataImpl implements VillagerData {
    private static final Logger LOGGER = LoggerFactory.getLogger(Villager_pickup.MOD_ID);
    private static final String ENTITY_DATA_KEY = "EntityData";
    
    private final ItemStack stack;
    
    public VillagerDataImpl(ItemStack stack) {
        this.stack = stack;
    }
    
    @Override
    public boolean storeFrom(VillagerEntity villager) {
        try {
            // Create NBT for the villager entity
            NbtCompound entityNbt = new NbtCompound();
            villager.writeNbt(entityNbt);
            
            // Store the NBT in the item using DataComponentTypes.CUSTOM_DATA
            NbtCompound customData = stack.getOrCreate(DataComponentTypes.CUSTOM_DATA);
            customData.put(ENTITY_DATA_KEY, entityNbt);
            stack.set(DataComponentTypes.CUSTOM_DATA, customData);
            
            LOGGER.debug("Stored villager data: {}", villager.getVillagerData().getProfession());
            return true;
        } catch (Exception e) {
            LOGGER.error("Error storing villager data: " + e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean applyTo(VillagerEntity villager) {
        if (!hasData()) {
            return false;
        }
        
        try {
            // Get the stored entity data using DataComponentTypes.CUSTOM_DATA
            NbtCompound customData = stack.get(DataComponentTypes.CUSTOM_DATA);
            if (customData != null && customData.contains(ENTITY_DATA_KEY)) {
                NbtCompound entityData = customData.getCompound(ENTITY_DATA_KEY);
                
                // Apply the data to the villager
                villager.readNbt(entityData);
                
                LOGGER.debug("Applied villager data successfully");
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Error applying villager data: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public boolean hasData() {
        // Check if the item has stored entity data using DataComponentTypes.CUSTOM_DATA
        NbtCompound customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        return customData != null && customData.contains(ENTITY_DATA_KEY);
    }
}
