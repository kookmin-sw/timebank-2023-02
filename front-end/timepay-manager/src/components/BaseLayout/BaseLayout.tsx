import { Layout, theme, Button } from 'antd';
import { useState } from 'react';
import { Outlet } from 'react-router-dom';
import { Sider } from '../Sider';
import { Logo } from '../Logo';
import { useAuth } from '../../hooks/useAuth';

export const BaseLayout = () => {
  const [collapsed, setCollapsed] = useState(false);
  const { token } = theme.useToken();
  const auth = useAuth();

  return (
    <Layout css={{ minHeight: '100vh' }}>
      <Layout.Header
        css={{
          background: token.colorBgContainer,
          padding: '0px',
          height: '64px',
          display: 'flex',
          flexDirection: 'row',
          alignItems: 'center',
          justifyContent: 'space-between',
          borderBottom: `1px solid ${token.colorBorderSecondary}`,
        }}
      >
        <Logo />
        <Button danger css={{marginRight : '8px'}} onClick={auth.logout}>로그아웃</Button>
      </Layout.Header>
      <Layout>
        <Layout.Sider
          theme="light"
          collapsible
          collapsed={collapsed}
          onCollapse={setCollapsed}
          trigger={null}
        >
          <Sider />
        </Layout.Sider>
        <Layout.Content>
          <Outlet />
        </Layout.Content>
      </Layout>
    </Layout>
  );
};
