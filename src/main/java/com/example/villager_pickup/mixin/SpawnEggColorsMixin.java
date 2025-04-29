package com.example.villager_pickup.mixin;

import com.example.villager_pickup.Villager_pickup;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to register our villager item with the same color handler as spawn eggs.
 * This makes our item have the same two-tone coloring as the villager spawn egg.
 */
@Mixin(ItemColors.class)
public class SpawnEggColorsMixin {
    
    private static final int PRIMARY_COLOR = 0x563C33;   // Brown color for villager
    private static final int SECONDARY_COLOR = 0xBCAC8F; // Beige color for villager
    
    @Inject(method = "create", at = @At("RETURN"))
    private static void registerVillagerItemColors(CallbackInfoReturnable<ItemColors> cir) {
        ItemColors colors = cir.getReturnValue();
        // Register our villager item with the same coloring as a spawn egg
        colors.register((stack, tintIndex) -> 
            tintIndex == 0 ? PRIMARY_COLOR : tintIndex == 1 ? SECONDARY_COLOR : -1, 
            Villager_pickup.VILLAGER_ITEM
        );
    }
}
