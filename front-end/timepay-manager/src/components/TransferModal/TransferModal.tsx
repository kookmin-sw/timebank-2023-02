import { Button, Form, Input, Modal, Radio, Select, message } from 'antd';
import React, { useEffect } from 'react';
import { useMutation, useQuery, useQueryClient } from 'react-query';
import { listOwnBankAccounts, transfer } from '../../api/api';
import { useAuth } from '../../hooks/useAuth';
import { useForm } from 'antd/es/form/Form';

interface Props {
  type: 'deposit' | 'withdraw';
  bankAccountNumber: string;
  open: boolean;
  onCancel?: () => void;
}

export function TransferModal({
  type,
  bankAccountNumber,
  open,
  onCancel,
}: Props) {
  const queryClient = useQueryClient();
  const [form] = useForm();

  const auth = useAuth();
  const accessToken = auth.accessToken;
  if (!accessToken) throw new Error('accessToken이 없습니다.');

  useQuery(
    ['GetOwnBankAccount'],
    () => {
      return listOwnBankAccounts(accessToken);
    },
    {
      onSuccess: (data) => {
        if (!data || data.length === 0) {
          message.error(
            '지점 계좌를 불러올 수 없습니다. 관리자에게 문의하세요.',
          );
          return;
        }

        const ownBankAccount = data[0];
        setBranchBankAccountNumber(ownBankAccount.bankAccountNumber);
      },
      onError: () => {
        message.error('계좌 목록을 불러오는 중 오류가 발생했습니다.');
      },
    },
  );

  const transferMutate = useMutation(transfer, {
    onSuccess: () => {
      message.success('이체가 완료되었습니다.');
      queryClient.invalidateQueries('ListBankAccount');

      if (onCancel) onCancel();
    },
    onError: () => {
      message.error('이체 중 오류가 발생했습니다.');
    },
  });

  const [branchBankAccountNumber, setBranchBankAccountNumber] =
    React.useState<string>('');
  const [amount, setAmount] = React.useState<number>(0);

  useEffect(() => {
    setAmount(0);
  }, [bankAccountNumber]);

  const handleOk = () => {
    if (amount <= 0) {
      message.error('금액을 0원 이상으로 입력해주세요.');
      return;
    }

    transferMutate.mutate({
      accessToken: accessToken,
      branchBankAccountNumber: branchBankAccountNumber,
      userBankAccountNumber: bankAccountNumber,
      amount: amount * (type === 'deposit' ? 1 : -1),
    });
  };

  return (
    <Modal
      open={open}
      onOk={handleOk}
      onCancel={onCancel}
      title="이체"
      okText="이체하기"
      cancelText="취소"
      confirmLoading={transferMutate.isLoading}
    >
      <Form form={form}>
        <Form.Item label="이체 구분">
          <Radio.Group value={type}>
            <Radio value="deposit">지급</Radio>
            <Radio value="withdraw">회수</Radio>
          </Radio.Group>
        </Form.Item>
        <Form.Item label="지점 계좌번호">
          <Input disabled value={branchBankAccountNumber} />
        </Form.Item>
        <Form.Item label="대상 계좌번호">
          <Input disabled value={bankAccountNumber} />
        </Form.Item>
        <Form.Item label="금액 (TP)">
          <Input
            value={amount}
            onChange={(e) => {
              const amount = parseInt(e.target.value);
              if (isNaN(amount)) {
                setAmount(0);
                return;
              }

              setAmount(amount);
            }}
          />
        </Form.Item>
      </Form>
    </Modal>
  );
}
