package com.daqem.coldcase.model.action;

import com.daqem.coldcase.model.Operation;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public interface IAction {

    String name();
    int getId();
    Operation getOperation();

    default Component getPrefix() {
        return switch (getOperation()) {
            case ADD -> com.daqem.coldcase.ColdCase.translate("action.prefix.add").withStyle(ChatFormatting.GREEN);
            case REMOVE -> com.daqem.coldcase.ColdCase.translate("action.prefix.remove").withStyle(ChatFormatting.RED);
            case NEUTRAL -> com.daqem.coldcase.ColdCase.translate("action.prefix.neutral");
        };
    }

    default Component getPastTense() {
        return com.daqem.coldcase.ColdCase.translate("action." + this.toString().toLowerCase() + ".past");
    }
}
