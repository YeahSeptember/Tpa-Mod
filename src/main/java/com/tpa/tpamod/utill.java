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
        public static ServerPlayer getPlayerByName(MinecraftServer server, String playerName) {
            if (playerName == null)
                return null;
            return server.getPlayerList().getPlayerByName(playerName);
        }
    }
}
