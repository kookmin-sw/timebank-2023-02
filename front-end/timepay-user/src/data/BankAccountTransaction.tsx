export interface BankAccountTransaction {
  amount: number;
  balanceSnapshot: number;
  code: string;
  id: number;
  receiverAccountId: number;
  receiverAccountOwnerName: string;
  receiverBankAccountNumber: string;
  senderAccountId: number;
  senderAccountOwnerName: string;
  senderBankAccountNumber: string;
  status: string;
  transactionAt: string;
}
