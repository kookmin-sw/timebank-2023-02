/// <reference types="@emotion/react/types/css-prop" />
import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter } from 'react-router-dom';

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement,
);
root.render(
  <BrowserRouter basename="/user">
    <App />
  </BrowserRouter>,
);

// Kakao SDK 초기화
//window.Kakao.init("a66d3f28c1e74a0287ef3c99e077e122");
window.Kakao.init("e4cddb50924dd3b6c7e747fbb029acd6");
Kakao.isInitialized();


// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals

reportWebVitals();


