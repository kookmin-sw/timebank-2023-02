import { useState } from 'react';

export function useTransferModal() {
  const [open, setOpen] = useState(false);
  const [transferType, setTransferType] = useState<'deposit' | 'withdraw'>(
    'deposit',
  );
  const [bankAccountNumber, setBankAccountNumber] = useState('');

  function openModal(
    transferType: 'deposit' | 'withdraw',
    bankAccountNumber: string,
  ) {
    setOpen(true);
    setTransferType(transferType);
    setBankAccountNumber(bankAccountNumber);
  }

  function closeModal() {
    setOpen(false);
  }

  return {
    openModal,
    closeModal,
    open,
    transferType,
    bankAccountNumber,
  };
}
