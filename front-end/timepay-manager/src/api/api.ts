import { apiClient } from './client';

export interface LoginResponseData {
  accessToken: string;
}

export interface LoginArgs {
  authenticationType: 'password';
  username: string;
  password: string;
}

export async function login(args: LoginArgs): Promise<LoginResponseData> {
  const response = await apiClient.post<LoginResponseData>(
    '/api/v1/managers/login',
    args,
  );

  return response.data;
}

export interface ListBankAccountResponseData {
  content: BankAccountResponseData[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface BankAccountResponseData {
  id: number;
  branchId: string;
  branchName: string;
  accountId: number;
  accountName: string;
  accountNumber: string;
  balanceAmount: number;
  createdAt: string;
}

export interface ListBankAccountArgs {
  bankAccountNumber?: string;
  userId?: number;
  userName?: string;
  userPhoneNumber?: string;
  userBirthday?: string;
  page?: number;
  size?: number;
}

export async function listBankAccount(
  accessToken: string,
  args: ListBankAccountArgs,
): Promise<ListBankAccountResponseData> {
  const response = await apiClient.get<ListBankAccountResponseData>(
    '/api/v1/managers/bank-accounts',
    {
      params: args,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    },
  );
  return response.data;
}

export interface OwnBankAccountResponseData {
  bankAccountId: number;
  branchId: number;
  balance: number;
  bankAccountNumber: string;
  ownerName: string;
  ownerType: string;
}

export async function listOwnBankAccounts(
  accessToken: string,
): Promise<ReadonlyArray<OwnBankAccountResponseData>> {
  const response = await apiClient.get<OwnBankAccountResponseData[]>(
    '/api/v1/bank/account',
    {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    },
  );

  return response.data;
}

export interface TransferArgs {
  accessToken: string;
  branchBankAccountNumber: string;
  userBankAccountNumber: string;
  amount: number;
}

export async function transfer(args: TransferArgs) {
  await apiClient.post(
    '/api/v1/managers/payments',
    {
      branchBankAccountNumber: args.branchBankAccountNumber,
      userBankAccountNumber: args.userBankAccountNumber,
      isDeposit: args.amount > 0,
      amount: Math.abs(args.amount),
    },
    {
      headers: {
        Authorization: `Bearer ${args.accessToken}`,
      },
    },
  );
}
