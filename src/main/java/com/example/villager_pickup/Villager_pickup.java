package com.example.villager_pickup;

import com.example.villager_pickup.component.VillagerComponents;
import com.example.villager_pickup.component.VillagerData;
import com.example.villager_pickup.item.VillagerItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Villager_pickup implements ModInitializer {
    public static final String MOD_ID = "villager_pickup";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    // Create our villager item with spawn egg appearance
    public static final VillagerItem VILLAGER_ITEM = new VillagerItem(new Item.Settings().maxCount(1));
    
    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Villager Pickup Mod");
        
        // Register components
        VillagerComponents.register();
        
        // Register the villager item using a new Identifier
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "captured_villager"), VILLAGER_ITEM);
        
        // Add to the Spawn Eggs group instead of Tools
        try {
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(content -> content.add(VILLAGER_ITEM));
            LOGGER.info("Successfully added Villager item to Spawn Eggs group");
        } catch (Exception e) {
            // Use constant strings for logging
            LOGGER.warn("Could not add item to Spawn Eggs group");
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
                        // Create a villager item
                        ItemStack villagerItem = VILLAGER_ITEM.getDefaultStack();
                        
                        // Get the component and store the villager data
                        VillagerData data = VillagerComponents.getVillagerData(villagerItem);
                        data.storeFrom(villager);
                        
                        // Set the item name based on the villager profession
                        String profession = villager.getVillagerData().getProfession().toString();
                        String displayName = formatProfessionName(profession);
                        // Create custom name for display
                        villagerItem.set(DataComponentTypes.CUSTOM_NAME, Text.literal(displayName));
                        
                        // Give the item to the player and remove the villager
                        player.setStackInHand(hand, villagerItem);
                        
                        // Play a sound effect
                        world.playSound(
                            null, 
                            player.getBlockPos(), 
                            SoundEvents.ENTITY_VILLAGER_HURT, 
                            SoundCategory.NEUTRAL, 
                            1.0F, 
                            1.0F
                        );
                        
                        // Remove the villager
                        entity.discard();
                        
                        // Send success message with formatting
                        if (player instanceof ServerPlayerEntity serverPlayer) {
                            serverPlayer.sendMessage(
                                Text.translatable("message.villager_pickup.picked_up", displayName)
                                    .formatted(Formatting.GREEN), 
                                true
                            );
                        }
                        
                        return ActionResult.SUCCESS;
                    } else {
                        // Let the player know their hand needs to be empty
                        if (player instanceof ServerPlayerEntity serverPlayer) {
                            serverPlayer.sendMessage(
                                Text.translatable("message.villager_pickup.hand_must_be_empty")
                                    .formatted(Formatting.RED), 
                                true
                            );
                        }
                    }
                }
                
                return ActionResult.PASS;
            });
            
            LOGGER.info("Successfully registered entity interaction handler");
        } catch (Exception e) {
            LOGGER.error("Failed to register entity interaction: " + e.getMessage());
            LOGGER.error("Mod will not function correctly");
            e.printStackTrace();
        }
    }
    
    /**
     * Formats a profession identifier into a readable display name.
     * 
     * @param profession The profession identifier (e.g. "minecraft:farmer")
     * @return A formatted display name (e.g. "Farmer Villager")
     */
    private static String formatProfessionName(String profession) {
        if (profession.equals("minecraft:none")) {
            return "Villager";
        }
        
        String cleanProfession = profession.replace("minecraft:", "").replace("_", " ");
        
        // Capitalize first letter of each word
        String[] words = cleanProfession.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1))
                  .append(" ");
            }
        }
        
        return sb.toString().trim() + " Villager";
    }
}
