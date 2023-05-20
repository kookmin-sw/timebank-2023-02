import React, { PropsWithChildren } from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import { PATH } from '../constants/path';

export function AuthRoute({ children }: PropsWithChildren) {
  const auth = useAuth();
  if (!auth.isAuthenticated) {
    return <Navigate to={PATH.LOGIN_PAGE} replace />;
  }

  return <>{children}</>;
}
