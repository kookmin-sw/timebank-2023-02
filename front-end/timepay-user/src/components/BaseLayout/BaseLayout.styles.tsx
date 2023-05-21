import { css } from '@emotion/react';
import { COMMON_COLOR } from '../../styles/constants/colors';
import '../../styles/fonts/pretendard/pretendard.css';

export const cssBaseLayoutStyle = css`
  font-family: unset !important;
  background: ${COMMON_COLOR.WHITE};
  overflow-x: hidden;
  width: 100wh;
  min-width: 375px;
  height: 100vh;
  min-height: 667px;
  main {
    font-family: unset !important;
    &.show-header {
      padding-top: 70px; // 헤더에 가려진만큼 패딩 추가
    }
  }
`;
