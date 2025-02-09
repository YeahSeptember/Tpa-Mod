package com.tpa.tpamod;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraftforge.event.TickEvent;
import net.minecraft.commands.CommandSourceStack;
import org.apache.logging.log4j.Logger;

@Mod("tpamod")
public class tpamod {
	private Logger log;
    public tpamod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        log.info("Tpa Mod Loding");
    }

    @Mod.EventBusSubscriber(modid = "tpamod", bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EventHandler {
        // 注册命令事件处理方法
        @SubscribeEvent
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
            CommandRegister.registerCommands(dispatcher);
        }

        // 服务器刻事件处理方法
        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                TeleportRequestManager.checkTimeouts();
            }
        }
    }
}
