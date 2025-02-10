/*
 * Copyright (C) 2025.1.28 Liuzixun
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.tpa.tpamod;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraftforge.event.TickEvent;
import net.minecraft.commands.CommandSourceStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("tpamod")
public class tpamod {
	private static final Logger log = LogManager.getLogger("tpamod");
    public tpamod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        log.info("Tpa Mod is initializing...");
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
