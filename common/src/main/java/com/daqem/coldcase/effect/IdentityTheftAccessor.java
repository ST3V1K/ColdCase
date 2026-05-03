package com.daqem.coldcase.effect;

import net.minecraft.network.chat.Component;

import java.util.Optional;

public interface IdentityTheftAccessor {

    void coldcase$setStolenIdentity(Component name);

    Optional<Component> coldcase$getStolenIdentity();

    default void coldcase$clearStolenIdentity() {
        coldcase$setStolenIdentity(null);
    }
}
