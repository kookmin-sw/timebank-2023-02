package kookmin.software.capstone2023.timebank.domain.repository.spec

import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Predicate
import kookmin.software.capstone2023.timebank.domain.model.Account
import kookmin.software.capstone2023.timebank.domain.model.BankAccount
import kookmin.software.capstone2023.timebank.domain.model.BankAccountTransaction
import kookmin.software.capstone2023.timebank.domain.model.BankBranch
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object BankAccountTransactionSpecs {

    // 조인할 필요 없는 애들

    // 거래번호로 검색
    fun withTransactionId(
        transactionId: Long, // 트랜젝션 id
    ): Specification<BankAccountTransaction> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<Long>("id"), transactionId)
        }
    }

    // 은행계좌 id 로 검색
    fun withBankAccountId(
        bankAccountId: Long?, // 은행 계좌 id
    ): Specification<BankAccountTransaction> {
        if (bankAccountId == null) {
            return Specification { _, _, _ -> null }
        }

        return Specification { root, query, cb ->
            query.distinct(true)

            val predicates = mutableListOf<Predicate>()

            var bankAccountJoin = root.join<BankAccountTransaction, BankAccount>("receiverBankAccount", JoinType.INNER)
//
            val accountJoin = bankAccountJoin.join<BankAccount, Account>("account", JoinType.INNER)

            bankAccountId?.let { predicates.add(cb.equal(root.get<String>("bankAccountId"), it)) }
            cb.and(*predicates.toTypedArray())
        }
    }

    // 날짜로 검색
    fun withTransactionAtBetween(
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): Specification<BankAccountTransaction> {
        if (startDate == null || endDate == null) {
            return Specification { _, _, _ -> null }
        }
        return Specification { root, _, cb ->
            val startDateTime = LocalDateTime.of(startDate, LocalTime.MIN)
            val endDateTime = LocalDateTime.of(endDate, LocalTime.MAX)
            val transactionAt = root.get<LocalDateTime>("transactionAt")
            cb.between(transactionAt, startDateTime, endDateTime)
        }
    }

    // 조인이 필요한 애들

    // 은행 지점으로 검색
    fun withBranchId(
        branchId: Long?, // 은행 지점 id
    ): Specification<BankAccountTransaction> {
        if (branchId == null) {
            return Specification { _, _, _ -> null }
        }
        return Specification { root, query, cb ->
            query.distinct(true)

            val predicates = mutableListOf<Predicate>()

            val bankAccountJoin = root.join<BankAccountTransaction, BankAccount>("receiverBankAccount", JoinType.INNER)
            val branchJoin = bankAccountJoin.join<BankAccount, BankBranch>("branch", JoinType.INNER)
            branchId?.let { predicates.add(cb.equal(branchJoin.get<Long>("id"), it)) }

            cb.and(*predicates.toTypedArray())
        }
    }

    // 계정 소유자명으로 검색
    fun withAccountOwnerName(
        accountOwnerName: String?, // 계정 소유자명
    ): Specification<BankAccountTransaction> {
        if (accountOwnerName == null) {
            return Specification { _, _, _ -> null }
        }

        return Specification { root, query, criteriaBuilder ->
            query.distinct(true)

            val predicates = mutableListOf<Predicate>()

            var accountJoin = root.join<BankAccountTransaction, BankAccount>("receiverBankAccount", JoinType.INNER)
            val userJoin = accountJoin.join<BankAccount, Account>("account", JoinType.INNER)
            accountOwnerName?.let { predicates.add(criteriaBuilder.like(userJoin.get("name"), "%$it%")) }

            criteriaBuilder.and(*predicates.toTypedArray())
        }
    }
}
