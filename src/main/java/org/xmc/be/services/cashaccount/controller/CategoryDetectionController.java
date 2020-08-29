package org.xmc.be.services.cashaccount.controller;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.Category;
import org.xmc.be.entities.PersistentObject;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;
import org.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;
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
        List<CashAccountTransaction> transactions = cashAccountTransactionJpaRepository.findTransactionsAfterDate(cashAccount, startDate);

        Optional<Entry<Category, Long>> exactMatchingCategory = transactions.stream()
                .filter(transaction -> StringUtils.equalsIgnoreCase(transaction.getUsage(), usage))
                .map(CashAccountTransaction::getCategory)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 0)
                .max(Comparator.comparing(Entry::getValue));

        if (exactMatchingCategory.isPresent()) {
            return Optional.of(exactMatchingCategory.get().getKey().getId());
        }

        return calculateWordMatches(usage, transactions)
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 0)
                .max(Comparator.comparing(Entry::getValue))
                .map(entry -> entry.getKey().getCategory())
                .map(PersistentObject::getId);
    }

    private Map<CashAccountTransaction, Integer> calculateWordMatches(String usage, List<CashAccountTransaction> transactions) {
        Map<CashAccountTransaction, Integer> wordMatches = Maps.newHashMap();

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
