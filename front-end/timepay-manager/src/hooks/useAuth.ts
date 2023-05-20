import { useRecoilState, useRecoilValue } from 'recoil';
import { authAtom } from '../states/authState';
import { useMutation } from 'react-query';
import { login } from '../api/api';
import { message } from 'antd';
import { AxiosError } from 'axios';

export function useAuth() {
  const [auth, setAuth] = useRecoilState(authAtom);
  const loginMutation = useMutation(login);

  return {
    isAuthenticated: !!auth.accessToken,
    accessToken: auth.accessToken,
    login: (username: string, password: string) => {
      loginMutation.mutate(
        {
          authenticationType: 'password',
          username,
          password,
        },
        {
          onSuccess: (data) => {
            console.info('로그인 성공', data);
            setAuth({
              accessToken: data.accessToken,
            });
          },
          onError: (error) => {
            if (error instanceof AxiosError) {
              const { response } = error;
              if (response?.status === 401) {
                message.error('아이디 또는 비밀번호가 일치하지 않습니다.');
              }
            }
          },
        },
      );
    },
    logout: () => {
      setAuth({});
    },
  };
}
