import { Layout } from 'antd';
import { Outlet } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { headerTitleState } from '../../states/uiState';
import MainHeader from '../MainHeader';
import { cssBaseLayoutStyle } from './BaseLayout.styles';
const BaseLayout = () => {
  const headerTitle = useRecoilValue(headerTitleState);

  return (
    <Layout css={cssBaseLayoutStyle}>
      {headerTitle && <MainHeader />}
      <Layout.Content
        className={`main-section-container ${
          headerTitle ? 'show-header' : 'no-header'
        }`}
      >
        <Outlet />
      </Layout.Content>
      {/* <MainFooter /> */}
    </Layout>
  );
};
export default BaseLayout;
