package com.daqem.coldcase.model.history;

import com.daqem.coldcase.ColdCase;
import com.daqem.coldcase.model.action.ItemAction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class UnreliableTransactionHistory extends UnreliableHistory {

    private final List<UnreliableContainerHistory> transactions;
    private final double transactionFactor;
    private final Random random;
    private final double chance;

    public UnreliableTransactionHistory(TransactionKey key, List<UnreliableContainerHistory> transactions, double transactionFactor, double chance) {
        super(transactions.getFirst().getTime(), transactions.getFirst()
                .getUser(), transactions.getFirst().getPosition());
        this.transactions = transactions;
        this.transactionFactor = transactionFactor;
        this.random = new Random(key.getSeed());
        this.chance = chance;
    }

    @Override
    public Component getClueComponent() {
        MutableComponent component = Component.empty();

        transactions.getFirst().appendBaseClues(component, transactionFactor, random);

        List<UnreliableContainerHistory> addedItems = transactions.stream()
                .filter(t -> t.getAction() == ItemAction.ADD_ITEM)
                .toList();

        List<UnreliableContainerHistory> removedItems = transactions.stream()
                .filter(t -> t.getAction() == ItemAction.REMOVE_ITEM)
                .toList();

        if (!addedItems.isEmpty()) {
            if (!component.getString().isEmpty()) component.append("\n");
            component.append(ColdCase.literal("§aAdded: "));
            appendItems(component, addedItems);
        }

        if (!removedItems.isEmpty()) {
            if (!component.getString().isEmpty()) component.append("\n");
            component.append(ColdCase.literal("§cRemoved: "));
            appendItems(component, removedItems);
        }

        return component;
    }

    private void appendItems(MutableComponent component, List<UnreliableContainerHistory> items) {
        int amount;
        for (amount = 1; amount < items.size(); amount++) {
            if (random.nextDouble() > chance * transactionFactor) {
                break;
            }
        }

        for (int i = 0; i < amount; i++) {
            UnreliableContainerHistory item = items.get(i);
            MutableComponent materialComponent = item.getMaterialComponent();
            if (item.getItemStack().getCount() > 1) {
                materialComponent.append(Component.literal(" x" + item.getItemStack().getCount()));
            }
            component.append(materialComponent);
            if (i < amount - 1) {
                component.append(", ");
            }
        }
    }

    public record TransactionKey(long timestamp, UUID userUuid) {
        public long getSeed() {
            return Long.hashCode(timestamp) * 17L + userUuid.hashCode() << 32;
        }
    }
}