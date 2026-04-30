package com.daqem.coldcase.command.filter;

import com.daqem.coldcase.ColdCase;
import com.daqem.coldcase.model.action.Actions;
import com.daqem.coldcase.model.action.IAction;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record ActionFilter(List<IAction> actions) implements IFilter {

    public ActionFilter() {
        this(new ArrayList<>());
    }

    @Override
    public String getName() {
        return ColdCase.translate("filter.action").getString();
    }

    @Override
    public List<String> getOptions() {
        return Arrays.asList(Actions.ACTIONS.stream()
                .map(IAction::name)
                .map(String::toLowerCase)
                .toArray(String[]::new));
    }

    @Override
    public IFilter parse(StringReader reader, String suffix) throws CommandSyntaxException {
        String[] split = suffix.split(",");
        List<IAction> actions = new ArrayList<>(Actions.getActions(split));
        if (split.length != actions.size()) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument()
                    .createWithContext(reader);
        }
        return new ActionFilter(actions);
    }

    @Override
    public String toString() {
        return "ActionFilter{" +
                "actions=" + actions +
                '}';
    }
}
