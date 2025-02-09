package com.tpa.tpamod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.*;
import com.mojang.brigadier.arguments.*;

public class CommandRegister {
	public static final Logger log = LogManager.getLogger("tpamod");
	
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        // 注册 tpa 命令
        dispatcher.register(Commands.literal("tpa")
               .then(Commands.argument("playerName", StringArgumentType.word())
                       .executes(context -> executeTpaCommands.tpa(context))));
        
        log.info("Registering command tpa");
        
        // 注册 tpaccept 命令
        dispatcher.register(Commands.literal("tpaccept")
               .then(Commands.argument("playerName", StringArgumentType.word())
                       .executes(context -> executeTpaCommands.tpaccept(context))));
        log.info("Registering command tpaccept");
        
        // 注册 tpdeny 命令
        dispatcher.register(Commands.literal("tpdeny")
               .then(Commands.argument("playerName", StringArgumentType.word())
                       .executes(context -> executeTpaCommands.tpdeny(context))));
        log.info("Registering command tpdeny");

        // 注册 tpcancel 命令
        dispatcher.register(Commands.literal("tpcancel")
               .executes(context -> executeTpaCommands.tpcancel(context)));
        log.info("Registering command tpcancel");
    }

    public static class executeTpaCommands {

        public static int tpa(CommandContext<CommandSourceStack> context) {
            CommandSourceStack source = context.getSource();
            MinecraftServer server = source.getServer();
            ServerPlayer sender = source.getPlayer();

            if (sender == null) {
                return 0;
            }
            String sTarget = StringArgumentType.getString(context, "playerName");
            ServerPlayer target = utill.PlayerNameToObjectConverter.getPlayerByName(server, sTarget);

            if (target != null) {
                TeleportRequestManager.addRequest(sender, target);
                sender.sendSystemMessage(Component.translatable("commands.tpamod.request_sender", sTarget));
                target.sendSystemMessage(Component.translatable("commands.tpamod.request_target", sender.getName().getString()));
                return 1;
            } else {
                sender.sendSystemMessage(Component.translatable("commands.tpamod.offline"));
            }
            return 0;
        }

        public static int tpaccept(CommandContext<CommandSourceStack> context) {
            CommandSourceStack source = context.getSource();
            MinecraftServer server = source.getServer();
            ServerPlayer target = source.getPlayer();

            if (target == null)
                return 0;
            String senderName = StringArgumentType.getString(context, "playerName");
            ServerPlayer sender = utill.PlayerNameToObjectConverter.getPlayerByName(server, senderName);
            TeleportRequest tpRequest = TeleportRequestManager.getRequest(sender);
            if (sender == null) {
                target.sendSystemMessage(Component.translatable("commands.tpamod.offline"));
                return 0;
            }
            if (tpRequest != null && tpRequest.getTarget() == target) {
            	utill.PlayerTeleporter.teleportPlayerToPlayer(sender, target);
            	sender.sendSystemMessage(Component.translatable("commands.tpamod.accept_sender"));
            	target.sendSystemMessage(Component.translatable("commands.tpamod.accept_target"));
                TeleportRequestManager.removeRequest(sender);
                return 1;
            } else {
                target.sendSystemMessage(Component.translatable("commands.tpamod.no_request", sender.getName().getString()));
            }
            return 0;
        }

        public static int tpdeny(CommandContext<CommandSourceStack> context) {
            CommandSourceStack source = context.getSource();
            MinecraftServer server = source.getServer();
            ServerPlayer target = source.getPlayer();

            if (target == null)
                return 0;
	        String senderName = StringArgumentType.getString(context, "playerName");
	        ServerPlayer sender = utill.PlayerNameToObjectConverter.getPlayerByName(server, senderName);
	        TeleportRequest tpRequest = TeleportRequestManager.getRequest(sender);
	        if (sender == null) {
	            target.sendSystemMessage(Component.translatable("commands.tpamod.offline"));
	            return 0;
	        }
	        if (tpRequest != null && tpRequest.getTarget() == target) {
	        	sender.sendSystemMessage(Component.translatable("commands.tpamod.deny_sender"));
	        	target.sendSystemMessage(Component.translatable("commands.tpamod.deny_target"));
	            TeleportRequestManager.removeRequest(sender);
	            return 1;
	        } else {
	            sender.sendSystemMessage(Component.translatable("commands.tpamod.no_request", sender.getName().getString()));
	        }
            return 0;
        }

        public static int tpcancel(CommandContext<CommandSourceStack> context) {
            CommandSourceStack source = context.getSource();
            ServerPlayer sender = source.getPlayer();

            if (sender == null) {
                return 0;
            }

            TeleportRequest tpRequest = TeleportRequestManager.getRequest(sender);
            if (tpRequest != null) {
                TeleportRequestManager.removeRequest(sender);
                sender.sendSystemMessage(Component.translatable("commands.tpamod.cancel"));
                return 1;
            } else {
                sender.sendSystemMessage(Component.translatable("commands.tpamod.cancel_no_request"));
            }
            return 0;
        }
    }
}