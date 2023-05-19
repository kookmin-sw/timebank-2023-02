import { Link, useNavigate } from "react-router-dom";
import { useState, useEffect, useCallback, useRef } from 'react';
import { useSetRecoilState } from 'recoil';
import { headerTitleState } from '../../states/uiState';
import { PATH } from '../../utils/paths';
import { Select, Card } from 'antd';
import axios from "axios";

import searchIcon from '../../assets/images/searchButton.png';
import "../../styles/css/Qna/qna_logmain.css";

type QNA = {
    "inquiryid": string,
    "title": string,
    "content": string,
    "inquiryDate": string,
    "replyStatus": string,
    "userId": string
}



function QnaLogMain() {

    const terms = [
        {id:0, value: '전체',}, {id:1, value: '지난 1개월간',}, {id:2, value: '지난 3개월간',}, {id:3, value: '지난 6개월간',}, 
    ];


    const navigate = useNavigate();

    const [qnaResponse, setQnaResponse] = useState<QNA[]>([]);
    const [filteredQnaResponse, setFilteredQnaResponse] = useState<QNA[]>([]);


    const [selectedTerm, setSelectedTerm] = useState("전체");
    const [searchVal, setSearchVal] = useState("");

    const [selectedQna, setSelectedQna] = useState("");
    const [selectedContent, setSelectedContent] = useState("");
    const [selectedTitle, setSelectedTitle] = useState("");
    const prevValue = useRef({selectedQna, selectedContent});


    const [userInf, setUserInf] = useState("");
    const accessToken = window.localStorage.getItem("access_token");


    function getCurrentDate(): string {
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0');
        const day = String(now.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    };

    // 문의 내역 받아오기
    const getQnas = async () => {
        let userID = "";
        await axios.get(PATH.SERVER + `/api/v1/users/me`, {
            headers:{
                'Authorization' : `Bearer ${accessToken}`
            }
        }
        ).then(response => {
            const data = response.data;
            userID = data.id;
            setUserInf(userID);
        }).catch(function(error){
            console.log(error);
        });
    
      await axios.get<QNA[]>(PATH.SERVER + `/api/v1/inquiries/users/${userID}`, {
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

    const handleCard = useCallback((selectedQna:string) =>{
        //console.log(selectedContent);
        navigate(`/qna/detail/${selectedQna}`, {state:{Qna : selectedQna, Content : selectedContent, Title : selectedTitle, userID : userInf}});
    },[navigate, selectedQna, selectedContent]
    );

    const setHeaderTitle = useSetRecoilState(headerTitleState);
    useEffect(() => {
        setHeaderTitle("문의 내역");     
        getQnas();
        if(selectedQna!==""&&(prevValue.current.selectedQna!==selectedQna && prevValue.current.selectedContent!==selectedContent)){
            handleCard(selectedQna);
        }
      }, [handleCard, selectedQna, selectedContent]);

    const filterQnas = (searchTerm: string, searchText: string) => {

        
        let filteredData = qnaResponse;
        let period = 0;

        if(searchTerm === "지난 1개월간") period = 1;
        else if (searchTerm === "지난 3개월간") period = 3;
        else if (searchTerm === "지난 6개월간") period = 6;
        
        // 검색어가 입력된 경우
        if (searchText !== '') {
            filteredData = filteredData.filter(qna =>
                qna.title.toLowerCase().includes(searchText.toLowerCase()) ||
                qna.content.toLowerCase().includes(searchText.toLowerCase())
                
            );
        }

        if (period > 0) {
            const currentDate = getCurrentDate();
            const startDate = new Date();
            startDate.setMonth(startDate.getMonth() - period);
            const formattedStartDate = startDate.toISOString().slice(0, 10);
            filteredData = filteredData.filter(qna =>
              qna.inquiryDate >= formattedStartDate && qna.inquiryDate <= currentDate
            );
        }

        setFilteredQnaResponse(filteredData);
 
    }

    const handleSearch = () =>{
        filterQnas(selectedTerm, searchVal);
        //console.log(selectedTerm+ " " +searchVal)
    };  

    return(
        
            <>
                <div className="mainGrid">
                    <div className="filterGrid">
                        <Select options = {terms} size = 'small' dropdownStyle={{textAlign : 'center' }} onChange={e => setSelectedTerm(e)} defaultValue={"전체"} className="selectTerm"></Select>
                        <input onChange={e => setSearchVal(e.target.value)} placeholder="문의 제목 입력" value={searchVal} className="searchBox"></input>
                        <img src={searchIcon} alt="" onClick={handleSearch} className="searchButton"></img>
                    </div>
                    
                    <Card className="mainBox">
                        {filteredQnaResponse.map((qna) => (
                            <Card key={qna.inquiryid} onClick={e=>{setSelectedTitle(qna.title);setSelectedQna(qna.inquiryid);setSelectedContent(qna.content);}} className="clickableDetailBox">
                                <span className="qnaStatus" style={qna.replyStatus === 'PENDING' ?{color : '#C7C7C7'} : {color : '#F1AF23'}}>{qna.replyStatus === 'PENDING' ? "등록완료" : "답변완료"}</span>
                                <span className="qnaTitle">Title : {qna.title}</span>
                            </Card>
                        ))}
                    </Card>
                </div>

                <div>
                </div>
            </>
        
    );


}

export default QnaLogMain;