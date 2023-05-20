import React from 'react';
import axios from 'axios';
import moment from 'moment';

import { useState, useEffect} from 'react';
import { Select, Table, Card } from 'antd';
import { ColumnsType } from 'antd/es/table';
import { useNavigate } from 'react-router-dom';

import { PATH } from '../../constants/path';

type Transaction = {
  "id": string,
  "bankAccountId": string,
  "code": string,
  "amount": string,
  "status": string,
  "receiverBankAccountNumber": string,
  "senderBankAccountNumber": string,
  "balanceSnapshot": string,
  "transactionAt": string,
  "createdAt":  string,
  "updatedAt":string
}

export function TransferPage() {
  const navigate = useNavigate();
    
  const [qnaResponse, setQnaResponse] = useState<Transaction[]>([]);
  const [filteredQnaResponse, setFilteredQnaResponse] = useState<Transaction[]>([]);

  const [filteringStatus, setFilteringStatus] = useState("")
  const [filteringTitle, setFilteringTitle] = useState("");
  const branchId = "1";
  const accessToken = "1";

  const columns: ColumnsType<Transaction> = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: 'Bank Account ID',
      dataIndex: 'bankAccountId',
      key: 'bankAccountId',
    },
    {
      title: 'Code',
      dataIndex: 'code',
      key: 'code',
    },
    {
      title: 'Amount',
      dataIndex: 'amount',
      key: 'amount',
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
    },
    {
      title: 'Receiver Account Number',
      dataIndex: 'receiverBankAccountNumber',
      key: 'receiverAccountNumber',
    },
    {
      title: 'Sender Account Number',
      dataIndex: 'senderBankAccountNumber',
      key: 'senderAccountNumber',
    },
    {
      title: 'Balance Snapshot',
      dataIndex: 'balanceSnapshot',
      key: 'balanceSnapshot',
    },
    {
      title: 'Transaction At',
      dataIndex: 'transactionAt',
      key: 'transactionAt',
      render: (date:string) => moment(date).format('YY-MM-DD HH:mm:ss')
      
    },

  ];
  const getQnas = () => {
  axios.get(PATH.SERVER + `/api/v1/managers/${branchId}/transactions`, {        
      headers:{
      'Authorization':`Bearer ${accessToken}`
      }
  }).
  then(response => {
      console.log(response.data.content);
      setQnaResponse(response.data.content);
      setFilteredQnaResponse(response.data.content);
  }).
  catch(function(error){
      console.log(error)})
  };


  useEffect(() => {
      getQnas();
  }, []);

  const filterQnas = (searchText: string, searchStatus: string) => {

  }

  return (
    <div className='background'>
      <Card size = 'small' className='searchBox'>
          <span>검색어</span>
          <input onChange={(e) => setFilteringTitle(e.target.value)} className="inputbox" placeholder='제목, 내용, 혹은 작성자 입력'></input>
          <button onClick={() => filterQnas(filteringTitle, filteringStatus)} className="searchButton">검색</button>
      </Card>
      
      <Table columns={columns} dataSource={filteredQnaResponse} rowKey="id" size="middle" className='table' />
    </div>
  );
}
