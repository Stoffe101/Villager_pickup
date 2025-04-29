package com.example.villager_pickup.component;
package com.example.villager_pickup.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.passive.VillagerEntity;

/**
 * Interface for storing and retrieving villager data.
 */
public interface VillagerData extends Component {
    
    /**
     * Stores data from a villager entity into the component.
     * 
     * @param villager The villager entity to store data from
     * @return True if the data was successfully stored
     */
    boolean storeFrom(VillagerEntity villager);
    
    /**
     * Applies stored data to a villager entity.
     * 
     * @param villager The villager entity to apply data to
     * @return True if the data was successfully applied
     */
    boolean applyTo(VillagerEntity villager);
    
    /**
     * Checks if the component has stored villager data.
     * 
     * @return True if villager data is stored in this component
     */
    boolean hasData();
}
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.passive.VillagerEntity;

package com.example.villager_pickup.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.passive.VillagerEntity;

package com.example.villager_pickup.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.passive.VillagerEntity;

package com.example.villager_pickup.component;
package com.example.villager_pickup.component;

import dev.onyxstudios.cardinal_components_api.component.Component;
import net.minecraft.entity.passive.VillagerEntity;

/**
 * Component interface for storing and retrieving villager data
 */
public interface VillagerData extends Component {
    
    /**
     * Store data from a villager entity
     * 
     * @param villager The villager entity to extract data from
     */
    void storeFrom(VillagerEntity villager);
    
    /**
     * Apply stored data to a villager entity
     * 
     * @param villager The villager entity to apply data to
     * @return true if data was successfully applied
     */
    boolean applyTo(VillagerEntity villager);
    
    /**
     * Check if this component has any stored villager data
     * 
     * @return true if data is present
     */
    boolean hasData();
}
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.passive.VillagerEntity;

/**
 * Component interface for storing villager data.
 */
public interface VillagerData extends Component {
    
    /**
     * Store data from a villager entity.
     *
     * @param villager The villager entity to store data from
     */
    void storeFrom(VillagerEntity villager);
    
    /**
     * Apply stored data to a villager entity.
     *
     * @param villager The villager entity to apply data to
     * @return true if data was successfully applied, false otherwise
     */
    boolean applyTo(VillagerEntity villager);
    
    /**
     * Check if this component has villager data.
     *
     * @return true if data is present, false otherwise
     */
    boolean hasData();
}
