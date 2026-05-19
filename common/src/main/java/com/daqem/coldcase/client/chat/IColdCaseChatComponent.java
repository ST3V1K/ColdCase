package com.daqem.coldcase.client.chat;

import net.minecraft.network.chat.Component;

import java.util.UUID;

/**
 * Marker interface injected onto {@code net.minecraft.client.gui.components.ChatComponent}
 * by {@link com.daqem.coldcase.mixin.MixinChatComponent}.
 *
 * <p>Cast a live {@code ChatComponent} instance to this interface to call
 * {@link #coldcase$addOrReplaceMessage}:
 * <pre>{@code
 *   IColdCaseChatComponent chat =
 *       (IColdCaseChatComponent) Minecraft.getInstance().gui.getChat();
 *   chat.coldcase$addOrReplaceMessage(messageId, component);
 * }</pre>
 */
public interface IColdCaseChatComponent {

    /**
     * Adds the component as a new chat line, or replaces the previously added
     * line that was tagged with the same {@code messageId}.
     *
     * <p>The first call for a given {@code messageId} always appends a new line.
     * Every subsequent call with the same {@code messageId} replaces that line
     * in-place without scrolling or reordering the chat history.
     *
     * @param messageId stable identifier for this clue slot in chat
     * @param component the content to display (navigation footer included)
     */
    void coldcase$addOrReplaceMessage(UUID messageId, Component component);
}
