package kookmin.software.capstone2023.timebank.application.service.bank.transfer;

import kookmin.software.capstone2023.timebank.application.exception.BadRequestException;
import kookmin.software.capstone2023.timebank.application.exception.NotFoundException;
import kookmin.software.capstone2023.timebank.application.exception.UnauthorizedException;
import kookmin.software.capstone2023.timebank.application.service.account.AccountFinder;
import kookmin.software.capstone2023.timebank.domain.model.*;
import kookmin.software.capstone2023.timebank.domain.repository.BankAccountJpaRepository;
import kookmin.software.capstone2023.timebank.domain.repository.BankAccountTransactionJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final BankAccountJpaRepository bankAccountJpaRepository;
    private final BankAccountTransactionJpaRepository bankAccountTransactionJpaRepository;
    private final AccountFinder accountFinder;

    // 계좌 이체를 수행하는 메소드
    @Transactional(
            isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class
    )
    @Override
    public BankAccountTransaction transfer(TransferRequest request) {
        System.out.println("test here++++++++++++++++++++++++++++++++++++++\n" +
                "||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println(request.getSenderAccountNumber());
        // 송금 계좌 조회
        BankAccount sender = bankAccountJpaRepository.findByAccountNumber(request.getSenderAccountNumber())
                .orElseThrow(() -> new NotFoundException("출금하려는 계좌 정보가 존재하지 않습니다"));

        System.out.println("test 2 here++++++++++++++++++++++++++++++++++++++\n" +
                "||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        // 수신 계좌 조회
        BankAccount receiver = bankAccountJpaRepository.findByAccountNumber(request.getReceiverAccountNumber())
                .orElseThrow(() -> new NotFoundException("입금하려는 계좌 정보가 존재하지 않습니다"));
        System.out.println("test 3 here++++++++++++++++++++++++++++++++++++++\n" +
                "||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println(request.toString());
        System.out.println(request.getAccountId());
        // 계정 조회
        Account account = accountFinder.findById(request.getAccountId());
        if (account == null) {
            throw new NotFoundException("계정 정보가 존재하지 않습니다.");
        }
        System.out.println("test 4 here++++++++++++++++++++++++++++++++++++++\n" +
                "||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        if (account.getType() == AccountType.INDIVIDUAL) {
            // 송금 계좌 비밀번호 일치 여부 확인
            if (!sender.getPassword().equals(request.getPassword())) {
                throw new UnauthorizedException("계좌 비밀번호가 일치하지 않습니다.");
            }
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("송금 금액은 0원 이상이어야 합니다.");
        }

        // request.getAmount에 소숫점 이하 값이 0이 아닌 어떠한 값이라도 있으면 BadRequestException 발생
        if (request.getAmount().stripTrailingZeros().scale() > 0) {
            throw new BadRequestException("송금 금액은 소숫점 이하 값이 없어야 합니다.");
        }

        if (sender.getOwnerType() == OwnerType.USER) {
            // 송금 계좌 잔액 부족 여부 확인
            if (sender.getBalance().compareTo(request.getAmount()) < 0) {
                throw new BadRequestException("계좌 잔액이 불충분합니다.");
            }
        }

        // 송금 계좌에서 출금할 트랜잭션 생성
        BankAccountTransaction senderTransaction = new BankAccountTransaction(
                sender.getId(),
                TransactionCode.WITHDRAW,
                request.getAmount(),
                TransactionStatus.REQUESTED,
                receiver,
                sender,
                sender.getBalance(),
                LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        );

        // 수신 계좌에 입금할 트랜잭션 생성
        BankAccountTransaction receiverTransaction = new BankAccountTransaction(
                receiver.getId(),
                TransactionCode.DEPOSIT,
                request.getAmount(),
                TransactionStatus.REQUESTED,
                receiver,
                sender,
                receiver.getBalance(),
                LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        );

        // 송금 계좌에서 출금하고, 수신 계좌에 입금
        performTransfer(sender, receiver, senderTransaction, receiverTransaction);

        if (senderTransaction.getStatus() == TransactionStatus.REQUESTED) {
            senderTransaction.setStatus(TransactionStatus.FAILURE);
        }
        if (receiverTransaction.getStatus() == TransactionStatus.REQUESTED) {
            receiverTransaction.setStatus(TransactionStatus.FAILURE);
        }

        bankAccountTransactionJpaRepository.save(senderTransaction);
        bankAccountTransactionJpaRepository.save(receiverTransaction);

        // 송금 계좌에서 출금한 트랜잭션 반환
        if (account.getType() != AccountType.INDIVIDUAL) { // 지점에서 요청한 거래라면
            if (sender.getOwnerType() == OwnerType.BRANCH) { // 지점 계좌에서 지급한 거래라면
                return senderTransaction;
            } else { // 지점 계좌에서 회수한 거래라면
                return receiverTransaction;
            }
        } else { // 개인 계좌 간 거래라면 요청자가 송금 계좌의 주인이므로 송금 계좌의 거래 내역 반환
            return senderTransaction;
        }
    }

    // 송금 계좌에서 출금하고, 수신 계좌에 입금하는 메소드
    @Transactional(
            isolation = Isolation.READ_COMMITTED, // READ_COMMITTED 레벨로 설정
            propagation = Propagation.REQUIRES_NEW // 새로운 트랜잭션을 생성
    )
    public void performTransfer(
            BankAccount sender,
            BankAccount receiver,
            BankAccountTransaction senderTransaction,
            BankAccountTransaction receiverTransaction
    ) {
        // 송금 계좌에서 출금
        sender.setBalance(sender.getBalance().subtract(senderTransaction.getAmount()));

        // 수신 계좌에 입금
        receiver.setBalance(receiver.getBalance().add(receiverTransaction.getAmount()));

        // 송금 계좌에서 출금한 트랜잭션 저장
        bankAccountTransactionJpaRepository.save(senderTransaction);

        // 수신 계좌에 입금한 트랜잭션 저장
        bankAccountTransactionJpaRepository.save(receiverTransaction);

        // 송금 계좌에서 출금한 트랜잭션 상태를 성공으로 변경
        senderTransaction.setStatus(TransactionStatus.SUCCESS);

        // 수신 계좌에 입금한 트랜잭션 상태를 성공으로 변경
        receiverTransaction.setStatus(TransactionStatus.SUCCESS);

        // 계좌 정보 저장
        bankAccountJpaRepository.saveAll(List.of(sender, receiver));
        bankAccountTransactionJpaRepository.saveAll(List.of(senderTransaction, receiverTransaction));
    }
}
