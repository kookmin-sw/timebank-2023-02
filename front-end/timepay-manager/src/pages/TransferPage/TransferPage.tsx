import React from 'react';
import axios from 'axios';
import moment from 'moment';
import dayjs from 'dayjs';

import { useState, useEffect} from 'react';
import { Select, Table, Card, DatePicker } from 'antd';
import { ColumnsType } from 'antd/es/table';
import { useNavigate } from 'react-router-dom';

import { PATH } from '../../constants/path';
import './transferpage.css'

type Transaction = {
  "id": string,
  "bankAccountId": string,
  "code": string,
  "amount": string,
  "status": string,
  "receiverBankAccountNumber": string,
  "receiverAccountOwnerName": string,
  "senderBankAccountNumber": string,
  "senderAccountOwnerName": string,
  "balanceSnapshot": string,
  "transactionAt": string,
}

type searchParams = {
  startDate?: string,
  endDate?: string,
  name?: string,
  size?: number,
  sort?: string,
}

export function TransferPage() {
  const navigate = useNavigate();
    
  const [transactionResponse, setTransactionResponse] = useState<Transaction[]>([]);
  const [filteredTransactionResponse, setFilteredTransactionResponse] = useState<Transaction[]>([]);

  const [filteringStatus, setFilteringStatus] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [name, setName] = useState("");
  const branchId = "1";
  
  const accessToken = window.localStorage.access_token;
  const {RangePicker} = DatePicker;

  const columns: ColumnsType<Transaction> = [
    {
      title: '거래 종류',
      dataIndex: 'code',
      key: 'code',
      render: (code:string) => {
        if(code === 'WITHDRAW') {
          return '출금'
        }
        else {
          return '입금'
        }
      },
    },
    {
      title: '거래액',
      dataIndex: 'amount',
      key: 'amount',
    },
    {
      title: '거래 상태',
      dataIndex: 'status',
      key: 'status',
    },
    {
      title: '받는 분 계좌 (이름)',
      dataIndex: 'receiverBankAccount',
      key: 'receiverAccount',
      render: (text:string, record: Transaction) => {
        return `${record.receiverBankAccountNumber} (${record.receiverAccountOwnerName})`
      }
    },
    {
      title: '보내는 분 계좌 (이름)',
      dataIndex: 'senderBankAccount',
      key: 'senderAccount',
      render: (text:string, record: Transaction) => {
        return `${record.senderBankAccountNumber} (${record.senderAccountOwnerName})`
      }
    },
    {
      title: '거래 전 잔액',
      dataIndex: 'balanceSnapshot',
      key: 'balanceSnapshot',
    },
    {
      title: '거래 일시',
      dataIndex: 'transactionAt',
      key: 'transactionAt',
      render: (date:string) => moment(date).format('YY-MM-DD HH:mm:ss')
      
    },

  ];

    

  const params: searchParams = {
  };
  if (startDate) params.startDate = startDate;
  if (endDate) params.endDate = endDate;
  if (name) params.name = name;
  params.size = 500;
  params.sort = "transactionAt,DESC"


  const getTransaction = () => {
  axios.get(PATH.SERVER + `/api/v1/managers/${branchId}/transactions`, {        
      headers:{
      'Authorization':`Bearer ${accessToken}`
      },
      params
  }).
  then(response => {
      console.log(response.data.content);
      setTransactionResponse(response.data.content);
      setFilteredTransactionResponse(response.data.content);
  }).
  catch(function(error){
      console.log(error)})
  };


  useEffect(() => {
      getTransaction();
  }, []);

  const filterTransactions = () => {
    getTransaction();
  }

  return (
    <div className='background'>
      <Card size = 'small' className='searchBoxs'>
          <span className='info'>기간</span>
          <RangePicker 
            defaultValue = {[dayjs('2023-01-01', 'YYYY-MM-DD'),dayjs(moment().format('YYYY-MM-DD'), 'YYYY-MM-DD')]}
            onChange={(_,dateStrings) => {setStartDate(dateStrings[0]);setEndDate(dateStrings[1])}}
            className='rangePicker' 
          />
          <span className='info'>검색어</span>
          <input onChange={(e) => setName(e.target.value)} className="inputbox" placeholder='이름 입력'></input>
          <button onClick={() => filterTransactions()} className="searchButton">검색</button>
      </Card>
      
      <Table columns={columns} dataSource={filteredTransactionResponse} rowKey="id" size="middle" className='table' />
    </div>
  );
}
