package com.daqem.coldcase.model.history;

import com.daqem.coldcase.model.BlockPosition;
import com.daqem.coldcase.model.Time;
import com.daqem.coldcase.model.User;
import com.daqem.coldcase.model.action.BlockAction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.UUID;

public class BlockHistory extends History {

    private final BlockPosition position;
    private final BlockAction action;
    private final String material;
    private final String tool;
    private final String skinColor;
    private final byte cleanworkArmor;

    public BlockHistory(long time, String name, String uuid, int x, int y, int z, String material, int blockAction) {
        this(new Time(time), new User(name, UUID.fromString(uuid)), new BlockPosition(x, y, z), material, BlockAction.fromId(blockAction), "minecraft:air", "unknown", (byte) 0);
    }

    public BlockHistory(long time, String name, String uuid, int x, int y, int z, String material, int blockAction, String tool, String skinColor) {
        this(new Time(time), new User(name, UUID.fromString(uuid)), new BlockPosition(x, y, z), material, BlockAction.fromId(blockAction), tool, skinColor, (byte) 0);
    }

    public BlockHistory(long time, String name, String uuid, int x, int y, int z, String material, int blockAction, String tool, String skinColor, byte cleanworkArmor) {
        this(new Time(time), new User(name, UUID.fromString(uuid)), new BlockPosition(x, y, z), material, BlockAction.fromId(blockAction), tool, skinColor, cleanworkArmor);
    }

    public BlockHistory(Time time, User user, BlockPosition position, String material, BlockAction action, String tool, String skinColor, byte cleanworkArmor) {
        super(time, user, position, action);
        this.position = position;
        this.action = action;
        this.material = material;
        this.tool = tool;
        this.skinColor = skinColor;
        this.cleanworkArmor = cleanworkArmor;
    }

    public BlockPosition getPosition() {
        return position;
    }

    public BlockAction getAction() {
        return action;
    }

    public Component getMaterialComponent() {
        var item = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(material)).asItem();
        MutableComponent mutableComponent = com.daqem.coldcase.ColdCase.themedLiteral(this.material.replace("minecraft:", ""));
        if (item != Items.AIR) {
            return mutableComponent
                    .withStyle(mutableComponent
                            .getStyle()
                            .withHoverEvent(
                                    new HoverEvent(
                                            HoverEvent.Action.SHOW_ITEM,
                                            new HoverEvent.ItemStackInfo(
                                                    BuiltInRegistries.BLOCK.get(
                                                                    ResourceLocation.parse(material)
                                                            ).asItem()
                                                            .getDefaultInstance()))));
        } else {
            return mutableComponent
                    .withStyle(mutableComponent
                            .getStyle()
                            .withHoverEvent(new HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    Component.literal(this.material)
                            )));
        }
    }

    public String getMaterial() {
        return material;
    }

    public String getTool() {
        return tool;
    }

    public String getSkinColor() {
        return skinColor;
    }

    public byte getCleanworkArmor() {
        return cleanworkArmor;
    }
}