package cn.Laba.Distance.api.events.World;

import cn.Laba.Distance.api.Event;
import net.minecraft.entity.Entity;

public class EventEntityMovement extends Event {
    Entity entity;
    public EventEntityMovement(Entity entityIn) {
        entity = entityIn;
    }

    public Entity getMovedEntity() {
        return entity;
    }
}
