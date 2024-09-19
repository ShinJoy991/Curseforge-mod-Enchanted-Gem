package com.github.shinjoy991.enchantedgem;

import com.github.shinjoy991.enchantedgem.client.RendererRegister;
import com.github.shinjoy991.enchantedgem.loot.ModLootModifiers;
import com.github.shinjoy991.enchantedgem.register.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EnchantedGem.MODID)
public class EnchantedGem {
    public static final String MODID = "enchantedgem";
    public static final Logger LOGGER = LogManager.getLogger();

    public EnchantedGem() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(bus);
        ModItems.CREATIVE_MODE_TAB.register(bus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ModLootModifiers.register(bus);
        MinecraftForge.EVENT_BUS.register(RendererRegister.class);
        MinecraftForge.EVENT_BUS.register(this);
        bus.addListener(this::onLoadConfig);
    }

    private void onLoadConfig(final ModConfigEvent.Loading event) {
        Config.onLoad(event);
//        RegisterEvent.registermodevents();
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("EnchantedGem common setting up...");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("EnchantedGem hello from server starting");
    }

}
