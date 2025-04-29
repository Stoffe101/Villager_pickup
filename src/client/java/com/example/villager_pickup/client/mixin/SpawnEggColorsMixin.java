package com.example.villager_pickup.client.mixin;
package com.example.villager_pickup.client.mixin;

import com.example.villager_pickup.Villager_pickup;
import com.example.villager_pickup.item.VillagerItem;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemColors.class)
public class SpawnEggColorsMixin {
    // The primary and secondary colors for our villager item
    private static final int PRIMARY_COLOR = 0x563C33;   // Brown for villager robe
    private static final int SECONDARY_COLOR = 0xBCAFA5; // Light gray for villager apron
    
    @Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
    private void getVillagerItemColor(ItemStack stack, int tintIndex, CallbackInfoReturnable<Integer> cir) {
        if (stack.getItem() instanceof VillagerItem) {
            // Return different colors based on tint index (like spawn eggs)
            if (tintIndex == 0) {
                cir.setReturnValue(PRIMARY_COLOR);
            } else if (tintIndex == 1) {
                cir.setReturnValue(SECONDARY_COLOR);
            }
        }
    }
}
import com.example.villager_pickup.Villager_pickup;
import com.example.villager_pickup.item.VillagerItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to handle color registration for villager item egg colors
 */
@Environment(EnvType.CLIENT)
@Mixin(ItemColors.class)
public class SpawnEggColorsMixin {
    // Villager egg colors
    private static final int PRIMARY_COLOR = 0x563C33;   // Brown
    private static final int SECONDARY_COLOR = 0x32B176; // Green
    
    @Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
    private void onGetColor(ItemStack stack, int tintIndex, CallbackInfoReturnable<Integer> cir) {
        if (stack.getItem() instanceof VillagerItem) {
            if (tintIndex == 0) {
                cir.setReturnValue(PRIMARY_COLOR);
            } else if (tintIndex == 1) {
                cir.setReturnValue(SECONDARY_COLOR);
            }
        }
    }
}
