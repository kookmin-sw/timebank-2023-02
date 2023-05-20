import React from 'react';
import { Button, Checkbox, Form, Input } from 'antd';

export interface LoginFormData {
  username: string;
  password: string;
  remember: boolean;
}

interface Props {
  initialValues?: LoginFormData;
  onSubmit?: (values: LoginFormData) => void;
}

export function LoginForm({ initialValues, onSubmit }: Props) {
  return (
    <Form
      name="loginForm"
      labelCol={{ span: 4 }}
      wrapperCol={{ span: 20 }}
      initialValues={{ remember: true, ...initialValues }}
      autoComplete="off"
      style={{ width: '400px' }}
      onFinish={onSubmit}
    >
      <Form.Item
        label="아이디"
        name="username"
        rules={[
          {
            required: true,
            message: '아이디를 입력해주세요.',
          },
        ]}
      >
        <Input />
      </Form.Item>
      <Form.Item
        label="비밀번호"
        name="password"
        rules={[
          {
            required: true,
            message: '비밀번호를 입력해주세요.',
          },
        ]}
      >
        <Input.Password />
      </Form.Item>
      <Form.Item
        name="remember"
        valuePropName="checked"
        wrapperCol={{ offset: 4, span: 20 }}
      >
        <Checkbox>아이디 저장</Checkbox>
      </Form.Item>
      <Form.Item wrapperCol={{ offset: 0, span: 24 }}>
        <Button
          size="large"
          style={{
            width: '100%',
          }}
          type="primary"
          htmlType="submit"
        >
          로그인
        </Button>
      </Form.Item>
    </Form>
  );
}
