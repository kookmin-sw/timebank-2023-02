import React, { useState } from 'react';
import { BankAccountTable } from '../../components/BankAccountTable/BankAccountTable';
import {
  BankAccountSearchForm,
  BankAccountSearchFormValues,
} from '../../components/BankAccountTable/BankAccountSearchForm';
import { useQuery } from 'react-query';
import { ListBankAccountResponseData, listBankAccount } from '../../api/api';
import { AxiosError } from 'axios';
import { message } from 'antd';
import { usePagination } from '../../hooks/usePagination';
import { useAuth } from '../../hooks/useAuth';

export function BankAccountPage() {
  const auth = useAuth();

  const pagination = usePagination();
  const [searchFormData, setSearchFormData] =
    useState<BankAccountSearchFormValues>();

  const listBankAccountQuery = useQuery<
    ListBankAccountResponseData,
    AxiosError
  >({
    queryKey: ['ListBankAccount', pagination, searchFormData],
    queryFn: () => {
      const accessToken = auth.accessToken;
      if (!accessToken) throw new Error('accessToken이 없습니다.');

      return listBankAccount(accessToken, {
        bankAccountNumber: searchFormData?.bankAccountNumber,
        userId: searchFormData?.userId,
        userName: searchFormData?.userName,
        userPhoneNumber: searchFormData?.userPhoneNumber,
        userBirthday: searchFormData?.userBirthday,
        page: pagination.page - 1,
        size: pagination.size,
      });
    },
    onError: (_) => {
      message.error('계좌 목록을 불러오는 중 오류가 발생했습니다.');
    },
  });

  const handleSearchFormSubmit = (values: BankAccountSearchFormValues) => {
    setSearchFormData(values);
    pagination.resetPagination();
  };

  return (
    <div>
      <BankAccountSearchForm onSubmit={handleSearchFormSubmit} />
      <BankAccountTable
        isLoading={listBankAccountQuery.isLoading}
        data={listBankAccountQuery.data?.content ?? []}
        total={listBankAccountQuery.data?.totalElements ?? 0}
        pagination={pagination}
      />
    </div>
  );
}
