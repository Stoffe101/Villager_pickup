package com.example.villager_pickup.component;
package com.example.villager_pickup.component;

import com.example.villager_pickup.Villager_pickup;
import dev.onyxstudios.cardinal_components_api.component.Component;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the VillagerData component interface
 */
public class VillagerDataImpl implements VillagerData {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Villager_pickup.MOD_ID);
    private final ItemStack stack;
    private boolean hasData = false;
    
    public VillagerDataImpl(ItemStack stack) {
        this.stack = stack;
    }
    
    @Override
    public void storeFrom(VillagerEntity villager) {
        try {
            // Create NBT data from the villager
            NbtCompound nbt = new NbtCompound();
            villager.writeNbt(nbt);
            
            // Store it in the item's NBT
            stack.getOrCreateNbt().put("EntityData", nbt);
            hasData = true;
            
            LOGGER.debug("Stored villager data: {}", villager.getVillagerData().getProfession());
        } catch (Exception e) {
            LOGGER.error("Failed to store villager data", e);
        }
    }
    
    @Override
    public boolean applyTo(VillagerEntity villager) {
        try {
            if (!hasData()) {
                return false;
            }
            
            // Get the stored data from the item
            NbtCompound nbt = stack.getOrCreateNbt().getCompound("EntityData");
            
            // Apply it to the villager
            villager.readNbt(nbt);
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to apply villager data", e);
            return false;
        }
    }
    
    package com.example.villager_pickup.component;
    
    import com.example.villager_pickup.Villager_pickup;
    import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
    import net.minecraft.entity.passive.VillagerEntity;
    import net.minecraft.item.ItemStack;
    import net.minecraft.nbt.NbtCompound;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    
    /**
     * Implementation of the VillagerData component for storing villager data on items.
     */
    public class VillagerDataImpl implements VillagerData, AutoSyncedComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(Villager_pickup.MOD_ID);
    private final ItemStack stack;
    
    public VillagerDataImpl(ItemStack stack) {
        this.stack = stack;
    }
    
    @Override
    public boolean storeFrom(VillagerEntity villager) {
        try {
            // Create NBT for the villager entity
            NbtCompound entityNbt = new NbtCompound();
            boolean success = villager.saveSelfNbt(entityNbt);
            
            if (success) {
                // Store the NBT in the item
                NbtCompound itemNbt = stack.getOrCreateNbt();
                itemNbt.put("EntityData", entityNbt);
                
                LOGGER.debug("Stored villager data successfully");
                return true;
            } else {
                LOGGER.error("Failed to save villager NBT data");
            }
        } catch (Exception e) {
            LOGGER.error("Error storing villager data: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public boolean applyTo(VillagerEntity villager) {
        if (!hasData()) {
            return false;
        }
        
        try {
            // Get the stored entity data
            NbtCompound itemNbt = stack.getNbt();
            if (itemNbt != null && itemNbt.contains("EntityData")) {
                NbtCompound entityData = itemNbt.getCompound("EntityData");
                
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
        // Check if the item has stored entity data
        return stack.hasNbt() && stack.getOrCreateNbt().contains("EntityData");
    }
    
    @Override
    public void readFromNbt(NbtCompound tag) {
        // Read component data from NBT
        if (tag.contains("EntityData")) {
            stack.getOrCreateNbt().put("EntityData", tag.getCompound("EntityData"));
        }
    }
    
    @Override
    public void writeToNbt(NbtCompound tag) {
        // Write component data to NBT
        if (hasData()) {
            tag.put("EntityData", stack.getOrCreateNbt().getCompound("EntityData"));
        }
    }
    }
    
    @Override
    public void readFromNbt(NbtCompound tag) {
        // Not needed for our implementation as we store directly in the item's NBT
    }
    
    @Override
    public void writeToNbt(NbtCompound tag) {
        // Not needed for our implementation as we read directly from the item's NBT
    }
}
import com.example.villager_pickup.Villager_pickup;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.item.ItemComponentCallback;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

package com.example.villager_pickup.component;

import com.example.villager_pickup.Villager_pickup;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

package com.example.villager_pickup.component;

import com.example.villager_pickup.Villager_pickup;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the VillagerData component.
 */
public class VillagerDataImpl implements VillagerData {
    private static final Logger LOGGER = LoggerFactory.getLogger(Villager_pickup.MOD_ID);
    private static final String VILLAGER_DATA_KEY = "villager_entity_data";
    
    private final ItemStack stack;
    private NbtCompound villagerData;
    private boolean hasData = false;
    
    public VillagerDataImpl(ItemStack stack) {
        this.stack = stack;
        
        // Load existing data if present
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && nbt.contains(VILLAGER_DATA_KEY)) {
            this.villagerData = nbt.getCompound(VILLAGER_DATA_KEY);
            this.hasData = true;
        } else {
            this.villagerData = new NbtCompound();
        }
    }
    
    @Override
    public void storeFrom(VillagerEntity villager) {
        try {
            // Create a new NBT compound for the villager
            NbtCompound nbt = new NbtCompound();
            
            // Let the villager write its data to NBT
            villager.writeNbt(nbt);
            
            // Store the data in our component
            this.villagerData = nbt;
            this.hasData = true;
            
            // Save the data to the item
            saveToItem();
        } catch (Exception e) {
            LOGGER.error("Failed to store villager data", e);
        }
    }
    
    @Override
    public boolean applyTo(VillagerEntity villager) {
        if (!hasData) {
            return false;
        }
        
        try {
            // Apply the stored NBT to the villager entity
            villager.readNbt(villagerData);
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to apply villager data", e);
            return false;
        }
    }
    
    @Override
    public boolean hasData() {
        return hasData;
    }
    
    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.contains(VILLAGER_DATA_KEY)) {
            this.villagerData = tag.getCompound(VILLAGER_DATA_KEY);
            this.hasData = true;
        } else {
            this.villagerData = new NbtCompound();
            this.hasData = false;
        }
    }
    
    @Override
    public void writeToNbt(NbtCompound tag) {
        if (hasData) {
            tag.put(VILLAGER_DATA_KEY, villagerData);
        }
    }
    
    /**
     * Save the component data to the item stack
     */
    private void saveToItem() {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.put(VILLAGER_DATA_KEY, villagerData);
    }
}
