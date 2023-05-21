import { Link, useLocation, useNavigate } from "react-router-dom";
import { useState, useEffect } from 'react';
import { useSetRecoilState } from 'recoil';
import { headerTitleState } from '../../states/uiState';
import { PATH } from '../../utils/paths';
import axios from "axios";


import "../../styles/css/Transfer/transfer_account.css";


function TransferPassword() {
    const navigate = useNavigate();
    const location = useLocation();

    const account = location.state.account;
    const amount = location.state.amount;
    const name = location.state.name;
    const accessToken = window.localStorage.getItem("access_token");
    const userAccount = window.localStorage.getItem("accountNumber");
    
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const handleTransfer = async () => {

        try{
        await axios.post(PATH.SERVER + `/api/v1/bank/account/transfer`, {
            senderBankAccountNumber: userAccount,
            receiverBankAccountNumber: account,
            amount: amount,
            password: password
        },
        {  
            headers:{
                'Content-Type': 'application/json',
                'Authorization':`Bearer ${accessToken}`,           
            },  
        })
        .then(function(response){
            setError("");
            navigate(PATH.TRANSFERLOG, {state : {account : account, amount : amount, name : name}});
        })
        .catch(function(error){
            //console.log(error);
            setError("비밀번호 오류. 다시 입력해주세요.");
        })}
        catch{
        }
    };

    const setHeaderTitle = useSetRecoilState(headerTitleState);
    useEffect(() => {
      setHeaderTitle('거래비밀번호 입력');

    });


    return(
        <>
            <div>
                
                <div>
                <span className="menuInfo">거래 비밀번호 입력</span>
                <input type='password' onChange={e => setPassword(e.target.value)} placeholder="비밀번호입력" value={password||""} className="inputBox"></input>
                <span className="errorMessage">{error}</span>
                </div>

                <div>
                <Link to={PATH.TRANSFERAMOUNT} state={{account : account}}><button className="beforeButton">이전</button></Link>
                <button onClick={handleTransfer} className="nextButton">이체</button>
                </div>
            </div>
        </>
    );

}

export default TransferPassword;