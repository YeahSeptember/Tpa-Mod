package com.tpa.tpamod;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;

public class utill {
	public static class PlayerTeleporter {
	    public static void teleportPlayerToPlayer(ServerPlayer sender, ServerPlayer target) {
	        ServerLevel targetLevel = target.serverLevel();
	        Vec3 targetPos = target.position();
	        sender.teleportTo(targetLevel, targetPos.x, targetPos.y, targetPos.z, target.getYRot(), target.getXRot());
	    }
	}
    
    public static class PlayerNameToObjectConverter {
        /**
         * 根据玩家名获取服务器端的玩家对象
         * @param server 服务器对象
         * @param playerName 玩家名
         * @return 对应的玩家对象，如果未找到则返回 null
         */
        public static ServerPlayer getPlayerByName(MinecraftServer server, String playerName) {
            if (playerName == null)
                return null;
            return server.getPlayerList().getPlayerByName(playerName);
        }
    }
}
