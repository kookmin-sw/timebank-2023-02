import { css } from '@emotion/react';
import { COMMON_COLOR } from '../../styles/constants/colors';

export const cssMainHeaderStyle = css`
  position: fixed;
  top: 0;

  background: ${COMMON_COLOR.WHITE};
  width: 100%;
  height: 70px;

  padding: 25px 0 25px 20px;

  color: ${COMMON_COLOR.MAIN1};
  font-weight: 700;
  font-size: 20px;
  line-height: 15px;
  border: 0;
  border-bottom: 1px solid #cdcdcd;

  z-index: 999;

  display: flex;
  flex-direction: row;
  gap: 10px;
  justify-content: space-between;
  align-items: center;

  .header-title {
    margin-top: 1px;
  }
  .pad {
    width : 22px;
    height : 24px;
    color : transparent;
  }
  :root {
    --sat: env(safe-area-inset-top);
    --sar: env(safe-area-inset-right);
    --sab: env(safe-area-inset-bottom);
    --sal: env(safe-area-inset-left);
  }
`