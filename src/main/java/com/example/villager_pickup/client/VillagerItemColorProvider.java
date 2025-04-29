package com.example.villager_pickup.client;

import com.example.villager_pickup.Villager_pickup;
import com.example.villager_pickup.item.VillagerItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.ItemStack;

/**
 * Provides the color tinting for the villager item to make it look like a spawn egg.
 */
@Environment(EnvType.CLIENT)
public class VillagerItemColorProvider implements ItemColorProvider {
    
    // Villager spawn egg colors
    private static final int PRIMARY_COLOR = 0x563C33;
    private static final int SECONDARY_COLOR = 0xBCAC8F;
    
    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        return tintIndex == 0 ? PRIMARY_COLOR : SECONDARY_COLOR;
    }
    
    /**
     * Registers the color provider for the villager item.
     */
    public static void register() {
        ColorProviderRegistry.ITEM.register(
            new VillagerItemColorProvider(),
            Villager_pickup.VILLAGER_ITEM
        );
    }
}
