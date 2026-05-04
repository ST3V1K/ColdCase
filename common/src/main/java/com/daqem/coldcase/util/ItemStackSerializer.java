package com.daqem.coldcase.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemStackSerializer {

    @Nullable
    public static String serialize(ItemStack itemStack, HolderLookup.Provider provider) {
        if (itemStack.isEmpty()) {
            return null;
        }
        try {
            JsonElement jsonElement = ItemStack.CODEC.encodeStart(provider.createSerializationContext(JsonOps.INSTANCE), itemStack).getOrThrow();
            return jsonElement.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static ItemStack deserialize(@Nullable String jsonString, HolderLookup.Provider provider) {
        if (jsonString == null || jsonString.isEmpty()) {
            return ItemStack.EMPTY;
        }
        try {
            JsonElement jsonElement = JsonParser.parseString(jsonString);
            return ItemStack.CODEC.parse(provider.createSerializationContext(JsonOps.INSTANCE), jsonElement).getOrThrow();
        } catch (Exception e) {
            return ItemStack.EMPTY;
        }
    }
}