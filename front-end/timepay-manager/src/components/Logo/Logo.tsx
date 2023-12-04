import React from 'react';
import { ReactComponent as TimeBankIcon } from '../../assets/images/timebank.svg';

export function Logo() {
  return (
    <div
      css={{
        padding: '16px',
        height: '64px',
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'flex-start',
      }}
    >
      <TimeBankIcon css={{ width: '32px', height: '32px' }} />
      <div
        css={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'flex-start',
          justifyContent: 'center',
          marginLeft: '8px',
          lineHeight: 1,
        }}
      >
        <span
          css={{
            fontSize: '1.0rem',
            fontWeight: 'bold',
          }}
        >
          시간은행
        </span>
        <span>관리자 화면 test</span>
      </div>
    </div>
  );
}
