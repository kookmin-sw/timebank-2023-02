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
