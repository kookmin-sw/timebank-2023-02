import { useEffect, useCallback, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { headerTitleState } from '../../states/uiState';
import IconGear from '../../assets/images/icon-gear.svg';
import { PATH } from '../../utils/paths';
import BaseMenu from '../../components/Menu/BaseMenu';
import moment from 'moment';
import { Tooltip } from 'antd';
import axios from 'axios';
import { BankAccountTransaction } from '../../data/BankAccountTransaction';
import { TransactionSheet } from './TransactionSheet';

const UserMainPage = () => {
  const navigate = useNavigate();
  const [accountNumber, setAccountNumber] = useState<string>('');
  const [title, setTitle] = useState<string>('정릉지점');
  const [balance, setBalance] = useState<number>(0);
  const [recentRemittanceAccount, setRecentRemittanceAccount] = useState([]);

  const [openTransaction, setOpenTransaction] = useState(null);
  const [openTransactionSheet, setOpenTransactionSheet] = useState(false);

  async function getUserAccount() {
    try {
      const accessToken = window.localStorage.getItem('access_token');

      axios({
        method: 'GET',
        url: PATH.SERVER + '/api/v1/bank/account',
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }).then((res) => {
        /*console.log(
                                  `getUserAccount status code : ${res.status}\ndata : ${res.data}`,
                                );*/

        if (res.data.length === 0) {
          setAccountNumber('');
          setBalance(0);
          return;
        }

        const account = res.data[0];
        setAccountNumber(account.bankAccountNumber);
        setBalance(account.balance);

        getRecentRemittanceAccount(account.bankAccountNumber);
      });
    } catch (e) {
      console.error(e);
      return false;
    }
  }

  async function getRecentRemittanceAccount(
    accountNumber: string,
  ): Promise<BankAccountTransaction[]> {
    try {
      const access_token = window.localStorage.getItem('access_token');
      //console.log(`${accountNumber}`);
      await axios({
        method: 'GET',
        url: PATH.SERVER + `/api/v1/bank/account/transaction/${accountNumber}`,
        headers: {
          Authorization: 'Bearer ' + access_token,
        },
      }).then((res) => {
        /*console.log(
                                  `getRecentRemittanceAccount status code : ${res.status}\nresponse data: ${res.data}`
                                );*/
        setRecentRemittanceAccount(res.data.content);
      });
    } catch (e) {
      console.error(e);
      return [];
    }
    return [];
  }

  const setHeaderTitle = useSetRecoilState(headerTitleState);

  useEffect(() => {
    setHeaderTitle(null);
    getUserAccount();
    window.localStorage.removeItem('accountNumber');
    window.localStorage.removeItem('balance');
  }, []);

  const handleOnClickLinkBtn = useCallback(
    (accountNumber: string) => {
      if (accountNumber === '') navigate(PATH.PASSWORD);
      else navigate(PATH.TRANSFER);
    },
    [navigate],
  );

  const handleOnClickLinkGearBtn = useCallback(
    (path: string) => {
      navigate(path);
    },
    [navigate],
  );

  return (
    <>
      {openTransaction && (
        <TransactionSheet
          open={openTransactionSheet}
          onClose={() => setOpenTransactionSheet(false)}
          transaction={openTransaction}
        />
      )}
      <div className="main-page">
        <div className="main-header">
          <div className="menu">
            <Tooltip placement="bottom">
              <BaseMenu />
            </Tooltip>
          </div>
          <img
            src={IconGear}
            alt=""
            onClick={() => handleOnClickLinkGearBtn(PATH.PROFILEEDIT)}
          />
        </div>

        <div className="user-account">
          <div className="user-info">
            <div className="title">{title}</div>
            <div className="account-num">계좌번호 {accountNumber}</div>
            <div className="main-amount">
              {accountNumber === '' ? (
                <span style={{ color: '#787878', paddingLeft: '5px' }}>
                  현재 계좌가 없어요
                </span>
              ) : (
                <span>
                  {balance}
                  <span style={{ fontSize: '2rem', marginLeft: '4px' }}>
                    TP
                  </span>
                </span>
              )}
            </div>
          </div>

          <div
            className="bottom-btn"
            onClick={() => handleOnClickLinkBtn(accountNumber)}
          >
            {accountNumber === '' ? (
              <div>계좌 생성하기</div>
            ) : (
              <div>보내기</div>
            )}
          </div>
        </div>

        <div className="recent-list">
          <span className="title">거래이력</span>
          <div style={{ paddingTop: '20px' }}>
            {recentRemittanceAccount.map((transaction: any) => {
              const balanceAfterTransaction =
                transaction.balanceSnapshot +
                (transaction.code === 'DEPOSIT'
                  ? transaction.amount
                  : -transaction.amount);

              return (
                <div
                  key={transaction.id}
                  onClick={() => {
                    setOpenTransaction(transaction);
                    setOpenTransactionSheet(true);
                  }}
                >
                  <div className="list">
                    <div style={{ padding: '8px' }}>
                      <div
                        style={{
                          display: 'flex',
                          flexDirection: 'row',
                          justifyContent: 'space-between',
                        }}
                      >
                        <div style={{ display: 'flex' }}>
                          <span
                            style={{ fontSize: '1rem', fontWeight: 'bold' }}
                          >
                            {transaction.code === 'DEPOSIT'
                              ? transaction.senderAccountOwnerName
                              : transaction.receiverAccountOwnerName}
                          </span>
                        </div>
                        {transaction.code === 'DEPOSIT' ? (
                          <div className="balance">
                            <span
                              style={{
                                fontSize: '1rem',
                                fontWeight: 'bold',
                                color: '#F1AF23',
                              }}
                            >
                              + {transaction.amount}TP
                            </span>
                          </div>
                        ) : (
                          <div className="balance">
                            <span style={{ fontWeight: 'bold' }}>
                              - {transaction.amount}
                            </span>
                            <span style={{ fontWeight: 'bold' }}>TP</span>
                          </div>
                        )}
                      </div>
                      <div
                        style={{
                          display: 'flex',
                          flexDirection: 'row',
                          justifyContent: 'space-between',
                        }}
                      >
                        <div className="date">
                          {moment(transaction.transactionAt).format('hh시 m분')}
                        </div>
                        <div className="left-balance">
                          {balanceAfterTransaction}TP
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </>
  );
};

export default UserMainPage;
