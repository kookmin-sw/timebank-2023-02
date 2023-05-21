import React, { useEffect } from 'react';
import { Card, Typography } from 'antd';
import { LoginForm } from '../../components/LoginForm';
import { LoginFormData } from '../../components/LoginForm/LoginForm';
import { useAuth } from '../../hooks/useAuth';
import { useNavigate } from 'react-router-dom';

export function LoginPage() {
  const auth = useAuth();
  const navigate = useNavigate();

  const handleSubmit = (values: LoginFormData) => {
    auth.login(values.username, values.password);
  };

  useEffect(() => {
    if (auth.isAuthenticated) {
      navigate('/bank-accounts');
    }
  }, [auth.isAuthenticated]);

  return (
    <div
      style={{
        width: '100vw',
        height: '100vh',
        display: 'flex',
        justifyContent: 'center',
      }}
    >
      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          alignItems: 'center',
        }}
      >
        <div
          style={{
            marginBottom: '20px',
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Typography.Title
            style={{
              marginTop: '0px',
              marginBottom: '0px',
              userSelect: 'none',
            }}
          >
            시간은행
          </Typography.Title>
          <Typography.Title
            level={2}
            style={{
              marginTop: '0px',
              marginBottom: '0px',
              fontWeight: 'normal',
              userSelect: 'none',
            }}
          >
            국민대-정릉지점
          </Typography.Title>
        </div>
        <Card>
          <LoginForm onSubmit={handleSubmit} />
        </Card>
      </div>
    </div>
  );
}
