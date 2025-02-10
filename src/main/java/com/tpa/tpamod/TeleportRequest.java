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

public class TeleportRequest {
    private final ServerPlayer sender;
    private final ServerPlayer target;
    private final long startTime;
    private static final long TIMEOUT_DURATION = 60 * 1000; // 60 秒

    public TeleportRequest(ServerPlayer sender, ServerPlayer target) {
        this.sender = sender;
        this.target = target;
        this.startTime = System.currentTimeMillis();
    }

    public ServerPlayer getSender() {
        return sender;
    }

    public ServerPlayer getTarget() {
        return target;
    }

    public boolean isTimedOut() {
        return System.currentTimeMillis() - startTime > TIMEOUT_DURATION;
    }
}