import { apiClient } from "./client";

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

export async function listBankAccount(args: ListBankAccountArgs): Promise<ListBankAccountResponseData> {
    const response = await apiClient.get<ListBankAccountResponseData>("/api/v1/managers/bank-accounts", {
        params: args,
    });
    return response.data;
}