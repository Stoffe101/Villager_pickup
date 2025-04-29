package com.example.villager_pickup.client;

import com.example.villager_pickup.Villager_pickup;
import com.example.villager_pickup.component.VillagerComponents;
import com.example.villager_pickup.component.VillagerData;
import com.example.villager_pickup.item.VillagerItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

@Environment(EnvType.CLIENT)
public class Villager_pickupClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Villager_pickup.LOGGER.info("Initializing Villager Pickup Client");
        
        // Register custom tooltip renderer for villager items
        registerTooltipCallback();
    }
    
    /**
     * Register a tooltip callback to show villager information on the item
     */
    private void registerTooltipCallback() {
        ItemTooltipCallback.EVENT.register((stack, context, lines, tooltipType) -> {
            // Only process our villager item
            if (stack.getItem() instanceof VillagerItem) {
                // Get the component data
                VillagerData data = VillagerComponents.VILLAGER_DATA.get(stack);
                
                if (data.hasData()) {
                    // Add separation line
                    lines.add(Text.literal(""));
                    
                    // Extract villager information from the component
                    try {
                        // Try to get profession data
                        NbtCompound nbt = stack.getOrCreateNbt();
                        if (nbt.contains("EntityData") && nbt.getCompound("EntityData").contains("VillagerData")) {
                            NbtCompound entityData = nbt.getCompound("EntityData");
                            NbtCompound villagerData = entityData.getCompound("VillagerData");
                            
                            // Get profession and level
                            String professionId = villagerData.getString("profession");
                            int level = villagerData.getInt("level");
                            
                            // Format profession name
                            String profession = formatProfessionName(professionId);
                            
                            // Add profession and level information
                            lines.add(Text.translatable("tooltip.villager_pickup.profession", profession)
                                    .formatted(Formatting.GRAY));
                            
                            if (level > 0) {
                                lines.add(Text.translatable("tooltip.villager_pickup.level", level)
                                        .formatted(Formatting.GRAY));
                            }
                        }
                        
                        // Add instruction
                        lines.add(Text.translatable("tooltip.villager_pickup.right_click_to_place")
                                .formatted(Formatting.ITALIC, Formatting.DARK_GRAY));
                        
                    } catch (Exception e) {
                        // Fallback if there's an error reading NBT
                        lines.add(Text.translatable("tooltip.villager_pickup.contains_villager")
                                .formatted(Formatting.GRAY));
                    }
                }
            }
        });
    }
    
    /**
     * Formats a profession identifier into a readable name
     */
    private String formatProfessionName(String professionId) {
        if (professionId == null || professionId.isEmpty() || professionId.equals("minecraft:none")) {
            return "Unemployed";
        }
        
        // Remove minecraft namespace
        String rawName = professionId.replace("minecraft:", "");
        
        // Replace underscores with spaces and capitalize first letter of each word
        StringBuilder result = new StringBuilder();
        for (String word : rawName.split("_")) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1))
                      .append(" ");
            }
        }
        
        return result.toString().trim();
    }
}
