import { atom } from 'recoil';

export interface AuthState {
  accessToken?: string;
}

export const authAtom = atom<AuthState>({
  key: 'auth',
  default: JSON.parse(localStorage.getItem('auth') || '{}'),
});
