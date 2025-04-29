package com.example.villager_pickup.mixin.client;

import com.example.villager_pickup.item.VillagerItem;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to give our villager item spawn egg colors
 */
@Mixin(ItemColors.class)
public class SpawnEggColorsMixin {
    
    /**
     * Inject into the getColor method to handle our villager item
     */
    @Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
    private void getVillagerItemColor(ItemStack stack, int tintIndex, CallbackInfoReturnable<Integer> cir) {
        if (stack.getItem() instanceof VillagerItem) {
            // Use villager spawn egg colors
            if (tintIndex == 0) {
                // Base layer - brown
                cir.setReturnValue(0x563C33);
            } else if (tintIndex == 1) {
                // Overlay layer - green
                cir.setReturnValue(0x32B176);
            }
        }
    }
}