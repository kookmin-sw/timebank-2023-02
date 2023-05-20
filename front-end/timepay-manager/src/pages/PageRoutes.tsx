import React from 'react';
import { Route, Routes } from 'react-router-dom';
import { BaseLayout } from '../components/BaseLayout';
import { UserPage } from './UserPage';
import { HomePage } from './HomePage';
import { PATH } from '../constants/path';
import { TransferPage } from './TransferPage';
import { InquiryPage, InquiryDetail } from './InquiryPage';
import { BankAccountPage } from './BankAccountPage';
import { AuthRoute } from './AuthRoute';
import { LoginPage } from './LoginPage';

export function PageRoutes() {
  return (
    <Routes>
      <Route path={PATH.LOGIN_PAGE} element={<LoginPage />} />
      <Route
        element={
          <AuthRoute>
            <BaseLayout />
          </AuthRoute>
        }
      >
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
