import React from 'react';
import { Button, Space, Table } from 'antd';
import { BankAccountResponseData } from '../../api/api';
import { Pagination } from '../../hooks/usePagination';
import { DateTime, Duration } from 'luxon';

interface Props {
  isLoading: boolean;
  data: BankAccountResponseData[];
  total: number;
  pagination: Pagination;
  onDepositButtonClick?: (bankAccountNumber: string) => void;
  onWithdrawButtonClick?: (bankAccountNumber: string) => void;
}

export function BankAccountTable({
  isLoading,
  data,
  total,
  pagination,
  onDepositButtonClick,
  onWithdrawButtonClick,
}: Props) {
  return (
    <Table
      bordered
      columns={[
        {
          title: '계좌번호',
          dataIndex: 'accountNumber',
          key: 'accountNumber',
        },
        {
          title: '계정 (ID)',
          render: (record: BankAccountResponseData) => {
            return (
              <div>
                {record.accountName} ({record.accountId})
              </div>
            );
          },
        },
        {
          title: '잔액 (시간)',
          dataIndex: 'balanceAmount',
          key: 'balance',
          render: (balance: number) => {
            return (
              <div>
                {balance}TP (
                {Duration.fromDurationLike({
                  minute: balance,
                }).toFormat('h시간 m분')}
                )
              </div>
            );
          },
        },
        {
          title: '생성일',
          dataIndex: 'createdAt',
          key: 'createdAt',
          render: (createdAt: string) => {
            return DateTime.fromISO(createdAt).toLocaleString(
              DateTime.DATETIME_SHORT,
              { locale: 'ko' },
            );
          },
        },
        {
          title: '기능',
          render: (record: BankAccountResponseData) => {
            return (
              <Space size="small">
                <a
                  href="#"
                  onClick={() => onDepositButtonClick?.(record.accountNumber)}
                >
                  지급
                </a>
                <a
                  href="#"
                  onClick={() => onWithdrawButtonClick?.(record.accountNumber)}
                >
                  회수
                </a>
              </Space>
            );
          },
        },
      ]}
      rowKey={(record) => record.accountNumber}
      loading={isLoading}
      dataSource={data}
      pagination={{
        position: ['bottomCenter'],
        showSizeChanger: true,
        pageSizeOptions: ['20', '50', '100'],
        current: pagination.page,
        pageSize: pagination.size,
        total: total,
        onChange: (page, size) => {
          pagination.setPage(page);
          pagination.setSize(size);
        },
        onShowSizeChange: (_, size) => {
          pagination.setSize(size);
        },
      }}
    />
  );
}
