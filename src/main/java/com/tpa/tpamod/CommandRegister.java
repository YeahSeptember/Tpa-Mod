package com.tpa.tpamod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.Logger;
import com.mojang.brigadier.arguments.*;

public class CommandRegister {
	private static Logger log;
	
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

            try {
                String sTarget = StringArgumentType.getString(context, "playerName");
                ServerPlayer target = utill.PlayerNameToObjectConverter.getPlayerByName(server, sTarget);

                if (target != null) {
                    TeleportRequestManager.addRequest(sender, target);
                    utill.ChatMessageSender.sendMessageToPlayer(sender, "你已向 " + sTarget + " 发起传送请求，请求将在 60 秒后失效.");
                    utill.ChatMessageSender.sendMessageToPlayer(target, "§e" + sender.getName().getString() + " 想要传送到你那里去,60秒后取消传送");
                    utill.ChatMessageSender.sendMessageToPlayer(target, "    §e输入/tpaccept " + sender.getName().getString() + " 同意传送,输入/tpdeny " + sender.getName().getString() + " 拒绝传送");
                    return 1;
                } else {
                    utill.ChatMessageSender.sendMessageToPlayer(sender, "§c错误:对方不在线!");
                }
            } catch (IllegalArgumentException e) {
                utill.ChatMessageSender.sendMessageToPlayer(sender, "§e询问玩家是否可以传送到他们那里");
                utill.ChatMessageSender.sendMessageToPlayer(sender, "§e用法:/tpa [playerName]");
            }
            return 0;
        }

        public static int tpaccept(CommandContext<CommandSourceStack> context) {
            CommandSourceStack source = context.getSource();
            MinecraftServer server = source.getServer();
            ServerPlayer target = source.getPlayer();

            if (target == null) {
                return 0;
            }

            try {
                String senderName = StringArgumentType.getString(context, "playerName");
                ServerPlayer sender = utill.PlayerNameToObjectConverter.getPlayerByName(server, senderName);
                TeleportRequest tpRequest = TeleportRequestManager.getRequest(sender);

                if (tpRequest != null && tpRequest.getTarget() == target) {
                	utill.PlayerTeleporter.teleportPlayerToPlayer(sender, target);
                    utill.ChatMessageSender.sendMessageToPlayer(sender, "§a对方同意了你的请求");
                    utill.ChatMessageSender.sendMessageToPlayer(target, "§a你同意了对方的请求");
                    TeleportRequestManager.removeRequest(sender);
                    return 1;
                } else {
                    utill.ChatMessageSender.sendMessageToPlayer(target, "§c错误:没有" + senderName + "的传送请求!");
                }
            } catch (IllegalArgumentException e) {
                utill.ChatMessageSender.sendMessageToPlayer(target, "§e同意传送请求");
                utill.ChatMessageSender.sendMessageToPlayer(target, "§e用法:/tpaccept [playerName]");
            }
            return 0;
        }

        public static int tpdeny(CommandContext<CommandSourceStack> context) {
            CommandSourceStack source = context.getSource();
            MinecraftServer server = source.getServer();
            ServerPlayer target = source.getPlayer();

            if (target == null) {
                return 0;
            }

            try {
                String senderName = StringArgumentType.getString(context, "playerName");
                ServerPlayer sender = utill.PlayerNameToObjectConverter.getPlayerByName(server, senderName);
                TeleportRequest tpRequest = TeleportRequestManager.getRequest(sender);

                if (tpRequest != null && tpRequest.getTarget() == target) {
                    utill.ChatMessageSender.sendMessageToPlayer(sender, "§c对方拒绝了你的请求");
                    utill.ChatMessageSender.sendMessageToPlayer(target, "§e你拒绝了对方的请求");
                    TeleportRequestManager.removeRequest(sender);
                    return 1;
                } else {
                    utill.ChatMessageSender.sendMessageToPlayer(target, "§c错误:没有" + senderName + "的传送请求!");
                }
            } catch (IllegalArgumentException e) {
                utill.ChatMessageSender.sendMessageToPlayer(target, "§e拒绝传送请求");
                utill.ChatMessageSender.sendMessageToPlayer(target, "§e用法:/tpdeny [playerName]");
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
                utill.ChatMessageSender.sendMessageToPlayer(sender, "§a你取消了传送请求");
                return 1;
            } else {
                utill.ChatMessageSender.sendMessageToPlayer(sender, "§c错误:你没有发送任何传送请求!");
            }
            return 0;
        }
    }
}