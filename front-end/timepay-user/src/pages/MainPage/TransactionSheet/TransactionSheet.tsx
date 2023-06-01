import Sheet from 'react-modal-sheet';
import moment from "moment";

interface Props {
  open: boolean;
  onClose?: () => void;
  transaction: any;
}

export function TransactionSheet({ open, onClose, transaction }: Props) {
  const balanceAfterTransaction =
    transaction.balanceSnapshot +
    (transaction.code === 'DEPOSIT' ? transaction.amount : -transaction.amount);

  return (
    <Sheet
      isOpen={open}
      onClose={() => onClose?.()}
      snapPoints={[(window.innerHeight / 5) * 3, 0]}
      initialSnap={0}
    >
      <Sheet.Container>
        <Sheet.Header />
        <Sheet.Content style={{ fontSize: '1.2rem' }}>
          <div
            style={{ marginLeft: '16px', fontSize: '1.3rem', fontWeight: 600 }}
          >
            이체내역
          </div>
          <div
            style={{
              padding: '16px',
              display: 'flex',
              flexDirection: 'column',
            }}
          >
            <div
              style={{
                display: 'flex',
                flexDirection: 'row',
                justifyContent: 'space-between',
              }}
            >
              <div style={{ fontSize: '1.2rem' }}>거래 금액</div>
              <div>{transaction.amount}TP</div>
            </div>
            <div
              style={{
                display: 'flex',
                flexDirection: 'row',
                justifyContent: 'space-between',
                marginTop: '16px',
              }}
            >
              <div>거래 후 잔액</div>
              <div>{balanceAfterTransaction}TP</div>
            </div>
            <div
              style={{
                display: 'flex',
                flexDirection: 'row',
                justifyContent: 'space-between',
                marginTop: '16px',
              }}
            >
              <div>거래 구분</div>
              <div>{transaction.code === 'DEPOSIT' ? '입금' : '출금'}</div>
            </div>
            <div
              style={{
                display: 'flex',
                flexDirection: 'row',
                justifyContent: 'space-between',
                marginTop: '16px',
              }}
            >
              <div>거래 일시</div>
              <div>{moment(transaction.transactionAt).format('yyyy년 M월 d일 hh시 m분 ss초')}</div>
            </div>
          </div>
          <div style={{ padding: 16 }}>
            <button
              style={{
                width: '100%',
                height: '40px',
                borderRadius: 8,
                fontSize: '1rem',
                fontWeight: 600,
                color: 'gray',
              }}
              onClick={() => onClose?.()}
            >
              닫기
            </button>
          </div>
        </Sheet.Content>
      </Sheet.Container>
      <Sheet.Backdrop />
    </Sheet>
  );
}
