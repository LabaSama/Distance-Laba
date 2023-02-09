/*
 * Decompiled with CFR 0.150.
 */
package cn.Laba.Distance.api.events;

public class Cancellable {
    private boolean cancelled;

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setCancelled() {
        this.cancelled = true;
    }
}

