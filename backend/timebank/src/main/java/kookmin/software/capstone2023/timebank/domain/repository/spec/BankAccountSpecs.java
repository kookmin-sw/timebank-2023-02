package kookmin.software.capstone2023.timebank.domain.repository.spec;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import kookmin.software.capstone2023.timebank.domain.model.Account;
import kookmin.software.capstone2023.timebank.domain.model.BankAccount;
import kookmin.software.capstone2023.timebank.domain.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class BankAccountSpecs {
    //    public static Specification<BankAccount> withAccountNumber(String accountNumber) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("accountNumber"), accountNumber);
//    }
    public static Specification<BankAccount> withAccountNumber(String accountNumber) {
        return (root, query, criteriaBuilder) -> {
            if (accountNumber != null && !accountNumber.isEmpty()) {
                return criteriaBuilder.equal(root.get("accountNumber"), accountNumber);
            } else {
                return null; // 계좌번호가 없으면 null을 반환하여 해당 조건은 무시합니다.
            }
        };
    }


    public static Specification<BankAccount> withUser(
            Long id,
            String name,
            String phoneNumber,
            LocalDate birthday
    ) {
//        if (id == null && name == null && phoneNumber == null && birthday == null) {
//            return (root, query, criteriaBuilder) -> null;
//        }

        if (id == null && name == null && phoneNumber == null && birthday == null) {
            return (root, query, criteriaBuilder) -> null;
        }
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Predicate[] predicates = new Predicate[4];

            Join<Account, User> accountJoin = root.join("account", JoinType.INNER);
            Join<User, Account> userJoin = accountJoin.join("users", JoinType.INNER);

            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("id : " + id + "name : " + name + "phone : " + phoneNumber + " birth : " + birthday);

            if (id != null) {
                predicates[0] = criteriaBuilder.equal(userJoin.get("id"), id);
            }
            if (name != null) {
                predicates[1] = criteriaBuilder.like(userJoin.get("name"), "%" + name + "%");
            }
            if (phoneNumber != null) {
                predicates[2] = criteriaBuilder.equal(userJoin.get("phoneNumber"), phoneNumber);
            }
            if (birthday != null) {
                predicates[3] = criteriaBuilder.equal(userJoin.get("birthday"), birthday);
            }

            for (int i = 0; i < predicates.length; i++) {
                if (predicates[i] == null) {
                    predicates[i] = criteriaBuilder.conjunction(); // 빈 Predicate를 생성합니다.

                    for (int j = 0; j < predicates.length; j++) {
                        if (predicates[j] != null && i != j) {
                            predicates[i] = criteriaBuilder.and(predicates[i], predicates[j]);
                        }
                    }
                }
            }

            return criteriaBuilder.and(predicates);
        };
    }
}
