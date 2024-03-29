package io.github.matwein.xmc.be.services.cashaccount.controller;

import io.github.matwein.xmc.be.entities.PersistentObject;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccountTransaction;
import io.github.matwein.xmc.be.entities.cashaccount.Category;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CategoryDetectionController {
    private final CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;

    @Autowired
    public CategoryDetectionController(CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository) {
        this.cashAccountTransactionJpaRepository = cashAccountTransactionJpaRepository;
    }

    public Optional<Long> autoDetectCategory(CashAccount cashAccount, String usage) {
        LocalDate startDate = LocalDate.now().minusYears(1);
        List<CashAccountTransaction> transactions = cashAccountTransactionJpaRepository.findTransactionsOnOrAfterDateHavingCategory(cashAccount, startDate);

        Optional<Entry<Category, Long>> exactMatchingCategory = transactions.stream()
                .filter(transaction -> StringUtils.equalsIgnoreCase(transaction.getUsage(), usage))
                .map(CashAccountTransaction::getCategory)
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 0)
                .max(Entry.comparingByValue());

        if (exactMatchingCategory.isPresent()) {
            return Optional.of(exactMatchingCategory.get().getKey().getId());
        }

        return calculateWordMatches(usage, transactions)
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 0)
                .max(Entry.comparingByValue())
                .map(entry -> entry.getKey().getCategory())
                .map(PersistentObject::getId);
    }

    private Map<CashAccountTransaction, Integer> calculateWordMatches(String usage, List<CashAccountTransaction> transactions) {
        Map<CashAccountTransaction, Integer> wordMatches = new HashMap<>();

        String[] words = StringUtils.split(usage);
        for (String word : words) {
            if (word.length() < 3) {
                continue;
            }

            for (CashAccountTransaction transaction : transactions) {
                if (StringUtils.containsIgnoreCase(transaction.getUsage(), word)) {
                    addMatch(wordMatches, transaction);
                }
            }
        }

        return wordMatches;
    }

    private void addMatch(Map<CashAccountTransaction, Integer> wordMatches, CashAccountTransaction transaction) {
        Integer currentMatches = wordMatches.get(transaction);
        if (currentMatches == null) {
            currentMatches = 0;
        }

        wordMatches.put(transaction, currentMatches + 1);
    }
}
