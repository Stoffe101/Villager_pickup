package com.example.villager_pickup.component;

import com.example.villager_pickup.Villager_pickup;
import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Registry for all component types used in the mod.
 */
public class VillagerComponents {
    
    // Use vanilla's custom data component type
    public static final String VILLAGER_DATA_KEY = "villager_data";
    
    /**
     * Register all components.
     */
    public static void register() {
        Villager_pickup.LOGGER.info("Registering Villager Data Components");
    }
    
    /**
     * Get a VillagerData handler for the given item stack
     */
    public static VillagerData getVillagerData(net.minecraft.item.ItemStack stack) {
        return new VillagerDataImpl(stack);
    }
}
