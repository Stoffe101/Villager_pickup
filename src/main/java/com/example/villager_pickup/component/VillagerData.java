package com.example.villager_pickup.component;

import net.minecraft.entity.passive.VillagerEntity;

/**
 * Interface for storing villager data.
 * This allows us to attach villager information to items.
 */
public interface VillagerData {
    
    /**
     * Store all data from a villager entity into this component
     * 
     * @param villager The villager entity to store data from
     * @return true if data was successfully stored
     */
    boolean storeFrom(VillagerEntity villager);
    
    /**
     * Apply stored data to a villager entity
     * 
     * @param villager The villager entity to apply data to
     * @return true if data was successfully applied
     */
    boolean applyTo(VillagerEntity villager);
    
    /**
     * Check if this component has any villager data stored
     * 
     * @return true if data is stored
     */
    boolean hasData();
}
