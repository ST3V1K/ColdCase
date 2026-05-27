package com.daqem.coldcase.model.history;

import com.daqem.coldcase.ColdCase;
import com.daqem.coldcase.config.ColdCaseCustomConfig;
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
            int amount;
            for (amount = 1; amount < addedItems.size(); amount++) {
                if (random.nextDouble() > chance * transactionFactor) {
                    break;
                }
            }

            for (int i = 0; i < amount; i++) {
                component.append(addedItems.get(i).getMaterialComponent());
                if (i < amount - 1) {
                    component.append(", ");
                }
            }
        }

        if (!removedItems.isEmpty()) {
            if (!component.getString().isEmpty()) component.append("\n");
            component.append(ColdCase.literal("§cRemoved: "));
            int amount;
            for (amount = 1; amount < removedItems.size(); amount++) {
                if (random.nextDouble() > chance * transactionFactor) {
                    break;
                }
            }

            for (int i = 0; i < amount; i++) {
                component.append(removedItems.get(i).getMaterialComponent());
                if (i < amount - 1) {
                    component.append(", ");
                }
            }
        }

        return component;
    }

    public record TransactionKey(long timestamp, UUID userUuid) {
        public long getSeed() {
            return Long.hashCode(timestamp) * 17L + userUuid.hashCode() << 32;
        }
    }
}