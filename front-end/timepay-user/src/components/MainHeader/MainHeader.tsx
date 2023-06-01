import { Layout } from 'antd';
import { useRecoilValue } from 'recoil';
import { headerTitleState } from '../../states/uiState';
import { cssMainHeaderStyle } from './MainHeader.styles';
import { ReactComponent as BackArrow } from '../../assets/images/icons/header-back-arrow.svg';
import { useNavigate } from 'react-router-dom';
import { useCallback } from 'react';
import { PATH } from '../../utils/paths';

const MainHeader = () => {
  const navigate = useNavigate();
  const headerTitle = useRecoilValue(headerTitleState);
  const handleClickBack = useCallback(() => {
    navigate(PATH.MAIN);
  }, [navigate]);
  return (
    <Layout.Header css={cssMainHeaderStyle}>
      <BackArrow onClick={handleClickBack} />
      <span className="header-title">{headerTitle}</span>
      <span className="pad"/>
    </Layout.Header>
  );
};

export default MainHeader;
