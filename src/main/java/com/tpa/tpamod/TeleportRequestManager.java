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

import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class TeleportRequestManager {
    private static final Map<ServerPlayer, TeleportRequest> requests = new HashMap<>();

    public static void addRequest(ServerPlayer sender, ServerPlayer target) {
        TeleportRequest request = new TeleportRequest(sender, target);
        requests.put(sender, request);
    }

    public static TeleportRequest getRequest(ServerPlayer sender) {
        return requests.get(sender);
    }

    public static void removeRequest(ServerPlayer sender) {
        requests.remove(sender);
    }

    public static void checkTimeouts() {
        requests.entrySet().removeIf(entry -> {
            TeleportRequest request = entry.getValue();
            if (request.isTimedOut()) {
                ServerPlayer sender = entry.getKey();
                ServerPlayer target = request.getTarget();
                sender.sendSystemMessage(net.minecraft.network.chat.Component.literal("你向 " + target.getName().getString() + " 的传送请求已超时。"));
                target.sendSystemMessage(net.minecraft.network.chat.Component.literal(sender.getName().getString() + " 向你的传送请求已超时。"));
                return true;
            }
            return false;
        });
    }
}