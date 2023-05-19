import { Link, useNavigate } from "react-router-dom";
import { useState, useEffect } from 'react';
import { useSetRecoilState } from 'recoil';
import { headerTitleState } from '../../states/uiState';
import { PATH } from '../../utils/paths';
import axios from "axios";

import "../../styles/css/Transfer/transfer_account.css";

function TransferAccount() {
    const [account, setAccount] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const accessToken = window.localStorage.getItem("access_token")

    const [accountExist, setAccountExist] = useState(false);

    const handleNext =  async () =>{
        let accountFormat = account;
        if (accountFormat.length === 6){
            accountFormat = `${accountFormat.slice(0,2)}-${accountFormat.slice(2,4)}-${accountFormat.slice(4,6)}`
        }
        await axios.get(PATH.SERVER + `/api/v1/bank/account/${accountFormat}`, {
            headers:{
            'Authorization':`Bearer ${accessToken}`
            }
        }).
        then(response => {
            if(response.status === 200) {
                setAccountExist(true);
                navigate("/transfer/amount", {state : {account: accountFormat, owner: response.data['ownerName']}});
            }
        }).
        catch(function(error){
            //console.log(error);
        })

        if(accountExist === false || account ===""){
            setError("계좌번호 오류. 다시 입력해주세요.")    
        }
    };

    const getAccountInfo = async () =>{
        await axios.get(PATH.SERVER + `/api/v1/bank/account`, {
            headers:{
                'Authorization' : `Bearer ${accessToken}`
            }
        }).then(response =>{
            const data = response.data[0];
            window.localStorage.setItem("balance", data.balance);
            window.localStorage.setItem("accountNumber", data.bankAccountNumber);
        })
    };

    const setHeaderTitle = useSetRecoilState(headerTitleState);
    useEffect(() => {
      setHeaderTitle('계좌번호 입력');
      getAccountInfo();
    });
    
    return(
        <div>
            <div>
                
                <span className="menuInfo">계좌번호 입력</span>
                <input onChange={e => setAccount(e.target.value)} placeholder="계좌 번호 입력" value={account||""} className="inputBox"></input>
                <span className="errorMessage">{error}</span>
            </div>

            <div>
            <button onClick={handleNext} className="nextButton">다음</button>
            <Link to="/main" ><button className="beforeButton">이전</button></Link>
            </div>
        </div>
    );


}

export default TransferAccount;