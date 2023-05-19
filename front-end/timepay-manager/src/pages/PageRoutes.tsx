import React from 'react';
import { Route, Routes } from 'react-router-dom';
import { BaseLayout } from '../components/BaseLayout';
import { UserPage } from './UserPage';
import { HomePage } from './HomePage';
import { PATH } from '../constants/path';
import { TransferPage } from './TransferPage';
import { InquiryPage, InquiryDetail } from './InquiryPage';
import { BankAccountPage } from './BankAccountPage';

export function PageRoutes() {
  return (
    <Routes>
      <Route element={<BaseLayout />}>
        <Route index element={<HomePage />} />
        <Route path={PATH.USER_PAGE} element={<UserPage />} />
        <Route path={PATH.BANK_ACCOUNT_PAGE} element={<BankAccountPage />} />
        <Route path={PATH.TRANSFER_PAGE} element={<TransferPage />} />
        <Route path={PATH.INQUIRY_PAGE} element={<InquiryPage />} />
        <Route path={PATH.INQUIRY_DETAIL} element={<InquiryDetail />} />
      </Route>
    </Routes>
  );
}
