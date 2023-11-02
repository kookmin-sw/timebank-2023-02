import { useEffect } from 'react';
import { useSetRecoilState } from 'recoil';
import { headerTitleState } from '../../states/uiState';
import MainImg from '../../assets/images/intro_page.svg';
import Logo from '../../assets/images/timepay_logo.svg';
import KaKaoImg from '../../assets/images/kakao_login_large_wide.svg';
import axios from 'axios';
import { PATH } from '../../utils/paths';

async function checkIsSignUpedUser() {
  try {
    const kakaoAccessToken = window.localStorage.getItem('kakao_access_token');
    if (kakaoAccessToken == null) {
      return false;
    }

    const response = await axios({
      method: 'POST',
      url: PATH.SERVER + '/api/v1/users/login',
      data: {
        authenticationType: 'social',
        socialPlatformType: 'KAKAO',
        accessToken: kakaoAccessToken,
      },
    });

    const data = response.data;
    window.localStorage.setItem('access_token', data.accessToken);

    return response.status === 200;
  } catch (e) {
    return false;
  }
}

const kakaoLogin = () => {
  window.Kakao.Auth.login({
    scope: 'profile_nickname, profile_image',
    success: function (authObj) {
      console.log(authObj);
      window.Kakao.API.request({
        url: '/v2/user/me',
        success: async () => {
          window.localStorage.setItem(
            'kakao_access_token',
            Kakao.Auth.getAccessToken(),
          );

          if (await checkIsSignUpedUser()) {
            window.location.href = './user/main';
          } else {
            window.location.href = './user/SignUp';
          }
        },
      });
    },
    fail: function (error) {
      console.log(error);
    },
  });
};

const IntroPage = () => {
  const setHeaderTitle = useSetRecoilState(headerTitleState);
  useEffect(() => {
    setHeaderTitle(null);
  });

  return (
    <>
      <div className="intro-page">
        <div className="top">
          <img src={Logo} alt="" />
          마을시간은행
        </div>
        <div className="main-title">
          시간을
          <br />
          저축할 수 있다면
        </div>
        <img src={MainImg} alt="" className="main-img" />
        <div className="kakao-img" onClick={kakaoLogin}>
          {' '}
          {/* kakaologin */}
          <img src={KaKaoImg} alt="" />
        </div>
      </div>
    </>
  );
};

export default IntroPage;
