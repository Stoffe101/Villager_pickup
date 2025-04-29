package com.example.villager_pickup.component;
package com.example.villager_pickup.component;

import com.example.villager_pickup.Villager_pickup;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import net.minecraft.util.Identifier;

package com.example.villager_pickup.component;

import com.example.villager_pickup.Villager_pickup;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import net.minecraft.util.Identifier;

/**
 * Registry for all component types used in the mod.
 */
public class VillagerComponents implements ItemComponentInitializer {
    
    // The component key for storing villager data on items
    public static final ComponentKey<VillagerData> VILLAGER_DATA = 
        ComponentRegistry.getOrCreate(new Identifier(Villager_pickup.MOD_ID, "villager_data"), VillagerData.class);
    
    /**
     * Register all components.
     */
    public static void register() {
        Villager_pickup.LOGGER.info("Registering Cardinal Components");
    }

    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        // Register our component factory for villager items
        registry.registerFor(item -> item == Villager_pickup.VILLAGER_ITEM, VILLAGER_DATA, VillagerDataImpl::new);
    }
}
import com.example.villager_pickup.Villager_pickup;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import net.minecraft.util.Identifier;

package com.example.villager_pickup.component;

import com.example.villager_pickup.Villager_pickup;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import net.minecraft.util.Identifier;

package com.example.villager_pickup.component;

import com.example.villager_pickup.Villager_pickup;
import dev.onyxstudios.cardinal_components_api.ComponentRegistry;
import dev.onyxstudios.cardinal_components_api.component.ComponentKey;
import dev.onyxstudios.cardinal_components_api.event.ItemComponentCallbackV2;
import dev.onyxstudios.cardinal_components_api.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cardinal_components_api.item.ItemComponentInitializer;
import net.minecraft.util.Identifier;

/**
 * Registry for all component types used in the mod.
 */
public class VillagerComponents implements ItemComponentInitializer {
    
    // The component key for storing villager data on items
    public static final ComponentKey<VillagerData> VILLAGER_DATA = 
        ComponentRegistry.getOrCreate(new Identifier(Villager_pickup.MOD_ID, "villager_data"), VillagerData.class);
    
    /**
     * Register all components.
     */
    public static void register() {
        Villager_pickup.LOGGER.info("Registering Cardinal Components");
    }

    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        // Register our component factory for all items
        registry.registerFor(item -> item == Villager_pickup.VILLAGER_ITEM, VILLAGER_DATA, VillagerDataImpl::new);
    }
}
