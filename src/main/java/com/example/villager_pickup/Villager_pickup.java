package com.example.villager_pickup;

import com.example.villager_pickup.item.VillagerItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Villager_pickup implements ModInitializer {
    public static final String MOD_ID = "villager_pickup";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    // Create an item with vanilla settings
    public static final VillagerItem VILLAGER_ITEM = new VillagerItem(new Item.Settings().maxCount(1));
    
    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Villager Pickup Mod");
        
        // Register the villager item using a new Identifier
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "captured_villager"), VILLAGER_ITEM);
        
        // Add to a Tools group using Fabric API
        try {
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> content.add(VILLAGER_ITEM));
            LOGGER.info("Successfully added Villager item to Tools group");
        } catch (Exception e) {
            // Use constant strings for logging
            LOGGER.warn("Could not add item to Tools group");
            LOGGER.info("Item will still be available by search in creative inventory");
        }
        
        // Register the use entity callback to handle villager pickup
        registerEntityInteraction();
    }
    
    private void registerEntityInteraction() {
        try {
            // Access the UseEntityCallback through the API
            UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
                if (world.isClient() || hand != Hand.MAIN_HAND || !player.isSneaking()) {
                    return ActionResult.PASS;
                }
                
                if (entity instanceof VillagerEntity villager) {
                    if (player.getStackInHand(hand).isEmpty()) {
                        // Create an item stack with the villager data
                        NbtCompound villagerNbt = new NbtCompound();
                        entity.saveSelfNbt(villagerNbt);
                        
                        ItemStack villagerItem = VILLAGER_ITEM.getDefaultStack();
                        
                        // Add villager profession as custom data
                        if (villager.getVillagerData().getProfession() != null) {
                            String profession = villager.getVillagerData().getProfession().toString();
                            
                            // Create or get NBT
                            NbtCompound itemNbt = new NbtCompound();
                            if (villagerItem.hasNbt()) {
                                itemNbt = villagerItem.getNbt().copy();
                            }
                            
                            // Add display name
                            NbtCompound displayNbt = new NbtCompound();
                            displayNbt.putString("Name", Text.Serialization.toJson(Text.of(profession + " Villager")));
                            itemNbt.put("display", displayNbt);
                            
                            // Store villager data
                            itemNbt.put("VillagerData", villagerNbt);
                            itemNbt.putString("CustomProfession", profession);
                            
                            // Set NBT
                            villagerItem.setNbt(itemNbt);
                        } else {
                            // Create or get NBT
                            NbtCompound itemNbt = new NbtCompound();
                            if (villagerItem.hasNbt()) {
                                itemNbt = villagerItem.getNbt().copy();
                            }
                            
                            // Add display name
                            NbtCompound displayNbt = new NbtCompound();
                            displayNbt.putString("Name", Text.Serialization.toJson(Text.of("Captured Villager")));
                            itemNbt.put("display", displayNbt);
                            
                            // Store villager data
                            itemNbt.put("VillagerData", villagerNbt);
                            
                            // Set NBT
                            villagerItem.setNbt(itemNbt);
                        }
                        
                        // Give the item to the player and remove the villager
                        player.setStackInHand(hand, villagerItem);
                        entity.discard();
                        
                        player.sendMessage(Text.of("Villager picked up!"), true);
                        return ActionResult.SUCCESS;
                    }
                }
                
                return ActionResult.PASS;
            });
            
            LOGGER.info("Successfully registered entity interaction handler");
        } catch (Exception e) {
            // Use constant strings for logging
            LOGGER.error("Failed to register entity interaction");
            LOGGER.error("Mod will not function correctly");
        }
    }
}
