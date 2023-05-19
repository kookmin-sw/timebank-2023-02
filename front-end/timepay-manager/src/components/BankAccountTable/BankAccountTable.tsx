import React from 'react';
import { Table } from 'antd';
import { BankAccountResponseData } from '../../api/api';
import { Pagination } from '../../hooks/usePagination';
import { DateTime } from 'luxon';

interface Props {
  isLoading: boolean;
  data: BankAccountResponseData[];
  total: number;
  pagination: Pagination;
  selectedRowKeys?: React.Key[];
  onRowSelectionChange?: (selectedRowKeys: React.Key[]) => void;
}

export function BankAccountTable({
  isLoading,
  data,
  total,
  pagination,
  selectedRowKeys,
  onRowSelectionChange,
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
                {balance}TP ({Math.round(balance / 60)}시간)
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
      ]}
      rowKey={(record) => record.id}
      rowSelection={{
        type: 'checkbox',
        selectedRowKeys: selectedRowKeys ?? [],
        onChange: onRowSelectionChange,
      }}
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
