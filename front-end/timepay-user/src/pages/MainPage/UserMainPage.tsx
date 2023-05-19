import { useEffect, useCallback, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { headerTitleState } from '../../states/uiState';
import IconGear from '../../assets/images/icon-gear.svg';
import { PATH } from '../../utils/paths';
import BaseMenu from '../../components/Menu/BaseMenu';
import { Tooltip } from 'antd';
import axios from 'axios';
import { BankAccountTransaction } from '../../data/BankAccountTransaction';

const formattedDate = (date: string) => {
  var dateObj = new Date(date);
  var year = dateObj.getFullYear();
  var month = ('0' + (dateObj.getMonth() + 1)).slice(-2);
  var day = ('0' + dateObj.getDate()).slice(-2);
  var hours = ('0' + dateObj.getHours()).slice(-2);
  var minutes = ('0' + dateObj.getMinutes()).slice(-2);
  return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes;
};

const UserMainPage = () => {
  const navigate = useNavigate();
  const [accountNumber, setAccountNumber] = useState<string>('');
  const [title, setTitle] = useState<string>('정릉지점');
  const [balance, setBalance] = useState<number>(0);
  const [recentRemittanceAccount, setRecentRemittanceAccount] = useState([]);

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
        console.log(
          `getUserAccount status code : ${res.status}\ndata : ${res.data}`,
        );

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
      console.log(`${accountNumber}`);
      await axios({
        method: 'GET',
        url: PATH.SERVER + `/api/v1/bank/account/transaction/${accountNumber}`,
        headers: {
          Authorization: 'Bearer ' + access_token,
        },
      }).then((res) => {
        console.log(
          `getRecentRemittanceAccount status code : ${res.status}\nresponse data: ${res.data}`,
        );
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
  }, []);

  const handleOnClickLinkBtn = useCallback(
    (accountNumber: string) => {
      if (accountNumber === '') navigate(PATH.PASSWORD);
      else
        navigate(PATH.TRANSFER, {
          state: { account: accountNumber, balance: balance },
        });
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
                  <span style={{ color: '#F1AF23', paddingLeft: '5px' }}>
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
            {accountNumber === '' ? <div>계좌 생성하기</div> : <div>이체</div>}
          </div>
        </div>

        <div className="recent-list">
          <span className="title">최근 송금한 계좌</span>
          <div style={{ paddingTop: '20px' }}>
            {recentRemittanceAccount.map((transaction: any) => {
              return (
                <>
                  <div className="list">
                    <div style={{ fontSize: '16px' }}>
                      <div style={{ display: 'flex' }}>
                        <span style={{ fontWeight: 'bold' }}>
                          {transaction.code === 'DEPOSIT'
                            ? transaction.senderAccountOwnerName
                            : transaction.receiverAccountOwnerName}
                        </span>
                        {'  '}
                        님 <br />
                      </div>
                      <span style={{ fontWeight: 'bold' }}>계좌번호</span>{' '}
                      <span style={{ color: '#F1AF23' }}>
                        {transaction.code === 'DEPOSIT'
                          ? transaction.senderBankAccountNumber
                          : transaction.receiverBankAccountNumber}
                      </span>
                    </div>
                    {transaction.code === 'DEPOSIT' ? (
                      <div className="balance">
                        <span style={{ fontWeight: 'bold', color: '#74B4FF' }}>
                          + {transaction.amount}
                        </span>
                        <span style={{ fontWeight: 'bold', color: '#F1AF23' }}>
                          TP
                        </span>
                      </div>
                    ) : (
                      <div className="balance">
                        <span style={{ fontWeight: 'bold', color: '#FF9574' }}>
                          - {transaction.amount}
                        </span>
                        <span style={{ fontWeight: 'bold', color: '#F1AF23' }}>
                          TP
                        </span>
                      </div>
                    )}

                    <div className="date">
                      {formattedDate(transaction.transactionAt)}
                    </div>
                  </div>
                </>
              );
            })}
          </div>
        </div>
      </div>
    </>
  );
};

export default UserMainPage;
