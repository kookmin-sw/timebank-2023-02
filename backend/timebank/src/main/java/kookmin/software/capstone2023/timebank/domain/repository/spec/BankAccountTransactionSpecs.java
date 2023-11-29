package kookmin.software.capstone2023.timebank.domain.repository.spec;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import kookmin.software.capstone2023.timebank.domain.model.Account;
import kookmin.software.capstone2023.timebank.domain.model.BankAccount;
import kookmin.software.capstone2023.timebank.domain.model.BankAccountTransaction;
import kookmin.software.capstone2023.timebank.domain.model.BankBranch;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BankAccountTransactionSpecs {

    // 거래번호로 검색
    public static Specification<BankAccountTransaction> withTransactionId(long transactionId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), transactionId);
    }

    // 은행계좌 id 로 검색
    public static Specification<BankAccountTransaction> withBankAccountId(Long bankAccountId) {
        if (bankAccountId == null) {
            return (root, query, criteriaBuilder) -> null;
        }

        return (root, query, cb) -> {
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            Join<Object, Object> bankAccountJoin =
                    root.join("receiverBankAccount", JoinType.INNER);

            Join<Object, Object> accountJoin =
                    bankAccountJoin.join("account", JoinType.INNER);

            predicates.add(cb.equal(root.get("bankAccountId"), bankAccountId));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // 날짜로 검색
    public static Specification<BankAccountTransaction> withTransactionAtBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return (root, query, cb) -> null;
        }
        return (root, query, cb) -> {
            LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
            LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
            return cb.between(root.get("transactionAt"), startDateTime, endDateTime);
        };
    }

    // 은행 지점으로 검색
    public static Specification<BankAccountTransaction> withBranchId(Long branchId) {
        if (branchId == null) {
            return (root, query, cb) -> null;
        }
        return (root, query, cb) -> {
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            Join<Object, Object> bankAccountJoin =
                    root.join("receiverBankAccount", JoinType.INNER);

            Join<Object, Object> branchJoin =
                    bankAccountJoin.join("branch", JoinType.INNER);

            predicates.add(cb.equal(branchJoin.get("id"), branchId));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // 계정 소유자명으로 검색
    public static Specification<BankAccountTransaction> withAccountOwnerName(String accountOwnerName) {
        if (accountOwnerName == null) {
            return (root, query, criteriaBuilder) -> null;
        }

        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            Join<Object, Object> accountJoin =
                    root.join("receiverBankAccount", JoinType.INNER);

            Join<Object, Object> userJoin =
                    accountJoin.join("account", JoinType.INNER);

            predicates.add(criteriaBuilder.like(userJoin.get("name"), "%" + accountOwnerName + "%"));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
