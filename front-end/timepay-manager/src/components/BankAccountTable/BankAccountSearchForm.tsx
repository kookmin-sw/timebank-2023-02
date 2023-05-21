import React from 'react';
import { Button, Col, Form, Input, Row, Space, theme } from 'antd';
import { useForm } from 'antd/es/form/Form';

export interface BankAccountSearchFormValues {
  bankAccountNumber?: string;
  userId?: number;
  userName?: string;
  userPhoneNumber?: string;
  userBirthday?: string;
}

interface Props {
  initialValues?: BankAccountSearchFormValues;
  onSubmit?: (values: BankAccountSearchFormValues) => void;
}

export function BankAccountSearchForm({ initialValues, onSubmit }: Props) {
  const [form] = useForm<BankAccountSearchFormValues>();
  const { token } = theme.useToken();

  const formStyle = {
    maxWidth: 'none',
    background: token.colorWhite,
    padding: '16px',
  };

  return (
    <Form
      initialValues={initialValues}
      form={form}
      onFinish={onSubmit}
      style={formStyle}
    >
      <Row gutter={24}>
        <Col>
          <Form.Item name="bankAccountNumber" label="계좌번호">
            <Input placeholder="00-00-00" />
          </Form.Item>
        </Col>
        <Col>
          <Form.Item name="userName" label="이름">
            <Input />
          </Form.Item>
        </Col>
        <Col>
          <Form.Item name="userPhoneNumber" label="휴대폰 번호">
            <Input placeholder="010-0000-0000" />
          </Form.Item>
        </Col>
        <Col>
          <Form.Item name="userBirthday" label="생년월일">
            <Input placeholder="2023-01-01" />
          </Form.Item>
        </Col>
      </Row>
      <div style={{ textAlign: 'end' }}>
        <Space size="small">
          <Button type="primary" htmlType="submit">
            검색
          </Button>
          <Button onClick={() => form.resetFields()}>초기화</Button>
        </Space>
      </div>
    </Form>
  );
}
