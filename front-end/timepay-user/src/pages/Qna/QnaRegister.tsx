
import { Link, useNavigate } from "react-router-dom";
import { useState, useEffect } from 'react';
import { useSetRecoilState } from 'recoil';
import { headerTitleState } from '../../states/uiState';
import { PATH } from '../../utils/paths';
import axios from "axios";

import "../../styles/css/Qna/qna_register.css";

function QnaRegister() {

    const navigate = useNavigate();

    const [qnaTitle, setQnaTitle] = useState("");
    const [qnaDetail, setQnaDetail] = useState("");
    const accessToken = window.localStorage.getItem("access_token");

    const handleRegister = (e:React.MouseEvent<HTMLButtonElement>) => {
        console.log(accessToken);
        axios.post(PATH.SERVER + `/api/v1/inquiries`, 
        {
            title: qnaTitle,
            content: qnaDetail,
        },
        {
            headers:{'Authorization':`Bearer ${accessToken}`},

        }).then(function(response){
            navigate(`/main`);
            console.log(response);
        }).catch(function(error){
            console.log(error);
        })
    }

    const setHeaderTitle = useSetRecoilState(headerTitleState);
    useEffect(() => {
      setHeaderTitle('문의 등록');
    });

    return(
        <>
            <div className="mainGrid">
                    <input onChange={e => setQnaTitle(e.target.value)} placeholder="문의 제목 입력" className="inputTitle"></input>
                    <textarea onChange={e => setQnaDetail(e.target.value)} placeholder="문의 내용 입력" className="inputContent"></textarea>

                    <button onClick={handleRegister} className="registerButton">문의 등록</button>
            </div>
        </>
    );


}

export default QnaRegister;

//inputTitle, inputDetail auto Resize방법 찾아서 적용