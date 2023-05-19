import React from 'react';
import axios from 'axios';
import moment from 'moment';

import { useState, useEffect} from 'react';
import { Select, Table, Card } from 'antd';
import { ColumnsType } from 'antd/es/table';
import { useNavigate } from 'react-router-dom';

import { PATH } from '../../constants/path';
import './inquiry_main.css'


type QNA = {
    "inquiryid": string,
    "title": string,
    "content": string,
    "inquiryDate": string,
    "replyStatus": string,
    "userId": string
}

export function InquiryPage() {
    const navigate = useNavigate();
    
    const [qnaResponse, setQnaResponse] = useState<QNA[]>([]);
    const [filteredQnaResponse, setFilteredQnaResponse] = useState<QNA[]>([]);

    const [filteringStatus, setFilteringStatus] = useState("")
    const [filteringTitle, setFilteringTitle] = useState("");

    const accessToken = "1";

    const columns: ColumnsType<QNA> = [
        {
            title: '답변 상태',
            dataIndex: 'replyStatus',
            key: 'replyStatus',
            align: 'center',
            render: (text:string) =>{
                let content = '';
                let color = '';

                if (text === 'PENDING') {
                    content = '답변대기';
                    color = '#F1AF23';
                }
                else {
                    content = '답변완료';
                    color = '#C7C7C7';
                }
                return <span style={{color}}>{content}</span>
            }
        },
        {
            title: '제목',
            dataIndex: 'title',
            key: 'title',
            align: 'center',
        },
        {
            title: '작성자',
            dataIndex: 'userId',
            key: 'userId',
            align: 'center',
        },
        {
            title: '작성일시',
            dataIndex: 'inquiryDate',
            key: 'inquiryDate',
            align: 'center',
            render: (date:string) => moment(date).format('YYYY-MM-DD HH:mm:ss')
        },
        {
            title: '문의번호',
            dataIndex: 'inquiryid',
            key: 'inquiryid',
            align: 'center',
        },
    ];

    const status = [
        {id:'', value: '전체'},
        {id:'PENDING', value: '답변 대기',}, 
        {id:'ANSWERED', value: '답변 완료',}, 
    ];

    const getQnas = () => {
    axios.get<QNA[]>(PATH.SERVER + `/api/v1/inquiries`, {        
        headers:{
        'Authorization':`Bearer ${accessToken}`
        }
    }).
    then(response => {
        //console.log(response.data);
        setQnaResponse(response.data);
        setFilteredQnaResponse(response.data);
    }).
    catch(function(error){
        console.log(error)})
    };


    useEffect(() => {
        getQnas();
    }, []);

    const filterQnas = (searchText: string, searchStatus: string) => {

        let status = '';
        if (searchStatus === '답변 대기') status = 'PENDING';
        else if (searchStatus === '답변 완료') status = 'ANSWERED';
        
        let filteredData = qnaResponse;

        // 상태가 입력된 경우
        if (status !== '') {
            filteredData = filteredData.filter(qna => qna.replyStatus === status);
        }
    
        // 검색어가 입력된 경우
        if (searchText !== '') {
            filteredData = filteredData.filter(qna =>
                qna.title.toLowerCase().includes(searchText.toLowerCase()) ||
                qna.content.toLowerCase().includes(searchText.toLowerCase())
                
            );
        }


        setFilteredQnaResponse(filteredData);
 
    }

    const handleClick = (record : QNA) =>{
        navigate(`/inquiries/${record.inquiryid}`, 
        {state:{
            inquiryId : record.inquiryid,
            inquiryTitle: record.title,
            inquiryContent : record.content,
            inquiryDate: record.inquiryDate,
            replyStatus: record.replyStatus,
        }});
    }

    return (
        <div className='background'>
            <Card size = 'small' className='searchBox'>
                <Select options = {status} size = 'small'  dropdownStyle={{textAlign : 'center'}} onChange={(e)=>setFilteringStatus(e)} defaultValue="상태" className="status"></Select>
                <span>검색어</span>
                <input onChange={(e) => setFilteringTitle(e.target.value)} className="inputbox" placeholder='제목, 내용, 혹은 작성자 입력'></input>
                <button onClick={() => filterQnas(filteringTitle, filteringStatus)} className="searchButton">검색</button>
            </Card>
            
            <Table columns={columns} dataSource={filteredQnaResponse} rowKey="inquiryid" size="middle" className='table' rowClassName={() => 'pointer'}
                onRow={(record, rowIndex) => {
                    return {
                      onDoubleClick: () => {handleClick(record)},
                    };
                  }}
            />
        </div>
    );
}
