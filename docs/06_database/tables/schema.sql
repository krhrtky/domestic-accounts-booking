CREATE TABLE users
(
    user_id    VARCHAR(255) PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

COMMENT
ON TABLE users IS 'アプリケーションの利用者情報を格納するテーブル';
COMMENT
ON COLUMN users.user_id IS '利用者の一意なID';
COMMENT
ON COLUMN users.name IS '利用者の名前';
COMMENT
ON COLUMN users.created_at IS 'レコード作成日時';
COMMENT
ON COLUMN users.updated_at IS 'レコード更新日時';

CREATE TABLE expense_purposes
(
    expense_purpose_id SERIAL PRIMARY KEY,
    purpose_code       VARCHAR(50) NOT NULL UNIQUE,
    description        VARCHAR(255),
    created_at         TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

COMMENT
ON TABLE expense_purposes IS '支出目的のマスターデータを格納するテーブル';
COMMENT
ON COLUMN expense_purposes.expense_purpose_id IS '支出目的の一意なID (自動採番)';
COMMENT
ON COLUMN expense_purposes.purpose_code IS '支出目的のコード (例: PERSONAL, HOUSEHOLD)';
COMMENT
ON COLUMN expense_purposes.description IS '支出目的の説明';
COMMENT
ON COLUMN expense_purposes.created_at IS 'レコード作成日時';
COMMENT
ON COLUMN expense_purposes.updated_at IS 'レコード更新日時';

INSERT INTO expense_purposes (purpose_code, description)
VALUES ('PERSONAL', '個人のための支出'),
       ('HOUSEHOLD', '家計のための家族共通の支出');
CREATE TABLE raw_transactions
(
    raw_transaction_id VARCHAR(255) PRIMARY KEY,
    transaction_date   DATE           NOT NULL,
    store_name         VARCHAR(255),
    amount             DECIMAL(12, 2) NOT NULL, -- 金額。精度とスケールは適宜調整
    imported_at        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    -- csv_source_identifier VARCHAR(255), -- (任意) どのCSVから取り込まれたかの識別子
    created_at         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

COMMENT
ON TABLE raw_transactions IS 'インポートされた加工前の取引明細データを格納するテーブル';
COMMENT
ON COLUMN raw_transactions.raw_transaction_id IS '元の取引明細の一意なID';
COMMENT
ON COLUMN raw_transactions.transaction_date IS '利用日';
COMMENT
ON COLUMN raw_transactions.store_name IS '利用店名';
COMMENT
ON COLUMN raw_transactions.amount IS '利用金額';
COMMENT
ON COLUMN raw_transactions.imported_at IS 'アプリケーションへのインポート日時';
-- COMMENT ON COLUMN raw_transactions.csv_source_identifier IS '(任意) どのCSVから取り込まれたかの識別子';
COMMENT
ON COLUMN raw_transactions.created_at IS 'レコード作成日時';
COMMENT
ON COLUMN raw_transactions.updated_at IS 'レコード更新日時';
CREATE TABLE recorded_expenses
(
    recorded_expense_id VARCHAR(255) PRIMARY KEY,
    raw_transaction_id  VARCHAR(255) NOT NULL UNIQUE,
    user_id             VARCHAR(255) NOT NULL,
    expense_purpose_id  INT          NOT NULL,
    memo                TEXT,
    recorded_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP, -- この支出が分類・記録された日時
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (raw_transaction_id) REFERENCES raw_transactions (raw_transaction_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE RESTRICT,
    FOREIGN KEY (expense_purpose_id) REFERENCES expense_purposes (expense_purpose_id) ON DELETE RESTRICT
);

COMMENT
ON TABLE recorded_expenses IS '分類・記録済みの支出情報を格納するテーブル';
COMMENT
ON COLUMN recorded_expenses.recorded_expense_id IS '記録済み支出の一意なID';
COMMENT
ON COLUMN recorded_expenses.raw_transaction_id IS '元となった加工前取引明細のID (FK)';
COMMENT
ON COLUMN recorded_expenses.user_id IS '支出を行った利用者（支払者）のID (FK)';
COMMENT
ON COLUMN recorded_expenses.expense_purpose_id IS '支出目的のID (FK)';
COMMENT
ON COLUMN recorded_expenses.memo IS 'ユーザーが入力できるメモ';
COMMENT
ON COLUMN recorded_expenses.recorded_at IS '支出が分類・記録された日時';
COMMENT
ON COLUMN recorded_expenses.created_at IS 'レコード作成日時';
COMMENT
ON COLUMN recorded_expenses.updated_at IS 'レコード更新日時';

-- パフォーマンス向上のためのインデックス作成例
CREATE INDEX idx_recorded_expenses_user_id ON recorded_expenses (user_id);
CREATE INDEX idx_recorded_expenses_recorded_at ON recorded_expenses (recorded_at);
CREATE INDEX idx_recorded_expenses_raw_transaction_id ON recorded_expenses (raw_transaction_id);
