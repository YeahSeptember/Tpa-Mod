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