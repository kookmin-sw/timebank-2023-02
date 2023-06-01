import { useEffect, useCallback, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { headerTitleState } from '../../states/uiState';
import { PATH } from '../../utils/paths';
import axios from 'axios';

async function setUserPassWord(password: string) {
  try {
    const access_token = window.localStorage.getItem('access_token');
    await axios({
      method: 'POST',
      url: PATH.SERVER + '/api/v1/bank/account',
      headers: {
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + access_token,
      },
      data: {
        password: password,
      },
    }).then((res) => {
      console.log(`status code : ${res.status}\nresponse data: ${res.data}`);
      const data = res.data;
    });
  } catch (e) {
    console.error(e);
  }
}

const Password = () => {
  const navigate = useNavigate();
  let [password, setPassWord] = useState('');
  let [passwordCert, setPassWordCert] = useState('');
  let [isSamePassword, setIsSamePassword] = useState(false);

  const setHeaderTitle = useSetRecoilState(headerTitleState);
  useEffect(() => {
    setHeaderTitle(null);
  });
  const handleOnClickLinkBtn = useCallback(
    async (path: string, password: string, passwordCert: string) => {
      if (
        password === passwordCert &&
        password.length == 4 &&
        passwordCert.length == 4
      ) {
        await setUserPassWord(password);
        navigate(path);
      } else {
        setIsSamePassword(true);
      }
      console.log(isSamePassword);
    },
    [navigate],
  );

  return (
    <>
      <div className="sign-up">
        <span className="title">초기 비밀번호를 설정해주세요</span>
        <div className="info-box">
          <label>초기 비밀번호</label>
          <input
            type="password"
            value={password}
            maxLength={4}
            onChange={(e) => {
              setPassWord(e.target.value);
            }}
          />
          <label style={{ marginTop: '10px' }}>초기 비밀번호 확인</label>
          <input
            type="password"
            maxLength={4}
            value={passwordCert}
            onChange={(e) => {
              setPassWordCert(e.target.value);
            }}
          />
        </div>
        {isSamePassword && (
          <label
            style={{
              position: 'absolute',
              top: '65%',
              left: '18%',
              textAlign: 'center',
              color: '#FF2E00',
              fontSize: '18px',
            }}
          >
            비밀번호 오류. 다시 입력해주세요.
          </label>
        )}
        <div className="finish-btn">
          <button
            onClick={() =>
              handleOnClickLinkBtn(PATH.MAIN, password, passwordCert)
            }
          >
            계좌생성
          </button>
        </div>
      </div>
    </>
  );
};

export default Password;

<div
  style={{
    position: 'absolute',
    top: '28%',
    left: '10%',
    textAlign: 'center',
    color: '#FF2E00',
    fontSize: '18px',
  }}
>
  비밀번호 오류. 다시 입력해주세요.
</div>;
