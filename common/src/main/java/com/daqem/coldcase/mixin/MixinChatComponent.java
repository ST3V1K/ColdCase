package com.daqem.coldcase.mixin;

import com.daqem.coldcase.client.chat.IColdCaseChatComponent;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.UUID;

/**
 * Mixin on {@link ChatComponent} that implements {@link IColdCaseChatComponent}.
 *
 * <h3>Strategy</h3>
 * A UUID marker is embedded as the {@code insertion} string on the root
 * {@link Component} of every clue message (the insertion is invisible
 * during normal gameplay – it only appears when shift-clicking the text,
 * which players never do for automated clue messages).
 *
 * <p>On each call to {@link #coldcase$addOrReplaceMessage}:
 * <ol>
 *   <li>Wrap the incoming {@link Component} inside a root empty literal
 *       whose style carries {@code insertion = "coldcase:<uuid>"}.</li>
 *   <li>Scan {@code allMessages} for an existing entry whose root insertion
 *       matches the same string.</li>
 *   <li>If found – replace that {@link GuiMessage} in-place and call
 *       {@link #refreshTrimmedMessages()} to rebuild the visible chat lines.</li>
 *   <li>If not found – delegate to the vanilla {@link #addMessage(Component)}
 *       path so the message is treated exactly like a system message.</li>
 * </ol>
 */
@Mixin(ChatComponent.class)
public abstract class MixinChatComponent implements IColdCaseChatComponent {

    // -------------------------------------------------------------------------
    // Shadowed vanilla members
    // -------------------------------------------------------------------------

    /** Chronologically ordered store of all chat messages (index 0 = newest). */
    @Shadow
    private List<GuiMessage> allMessages;

    /** Rebuilds the line-wrapped {@code trimmedMessages} list from {@code allMessages}. */
    @Shadow
    abstract void refreshTrimmedMessages();

    /** Vanilla entry point for adding a plain system message (no signature / tag). */
    @Shadow
    public abstract void addMessage(Component component);

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    /** Prefix used in the insertion string so we never collide with real player text. */
    @Unique
    private static final String COLDCASE_MARKER_PREFIX = "coldcase:";

    // -------------------------------------------------------------------------
    // IColdCaseChatComponent implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     *
     * <p>Thread-safety: this method must only be called on the main client thread
     * (use {@code context.queue()} when dispatching from a network handler).
     */
    @Override
    @Unique
    public void coldcase$addOrReplaceMessage(UUID messageId, Component newComponent) {
        String marker = COLDCASE_MARKER_PREFIX + messageId;

        // Build a wrapper component that carries our invisible UUID marker as the
        // root `insertion` style property, then appends the real clue content.
        MutableComponent markedComponent = Component.empty()
                .withStyle(Style.EMPTY.withInsertion(marker));
        markedComponent.append(newComponent);

        // Search for an existing clue message with the same marker.
        for (int i = 0; i < allMessages.size(); i++) {
            Component existing = allMessages.get(i).content();
            String insertion = existing.getStyle().getInsertion();
            if (marker.equals(insertion)) {
                // Replace in-place: preserve the original game-tick timestamp and tag
                // so the message does not visually jump in the chat history.
                GuiMessage old = allMessages.get(i);
                allMessages.set(i, new GuiMessage(old.addedTime(), markedComponent, null, old.tag()));
                // Rebuild the line-wrapped view so the updated text is displayed.
                refreshTrimmedMessages();
                return;
            }
        }

        // First time we see this messageId – add it as a brand-new system message.
        addMessage(markedComponent);
    }
}
