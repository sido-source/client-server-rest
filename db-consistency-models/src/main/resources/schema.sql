DROP TABLE IF EXISTS agent_bank CASCADE;
DROP TABLE IF EXISTS payment_method CASCADE;
DROP TABLE IF EXISTS ledger_entry CASCADE;
DROP TABLE IF EXISTS payment_transaction CASCADE;
TRUNCATE TABLE cinema_seat RESTART IDENTITY;


-----------------------------------------
-- PARENT: PAYMENT TRANSACTION
-----------------------------------------
CREATE TABLE payment_transaction (
    id              BIGSERIAL PRIMARY KEY,
    reference_no    VARCHAR(64) NOT NULL UNIQUE,
    amount          NUMERIC(12,2) NOT NULL CHECK (amount > 0),
    currency        VARCHAR(3) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

-----------------------------------------
-- CHILD: LEDGER ENTRY
-----------------------------------------
CREATE TABLE ledger_entry (
    id              BIGSERIAL PRIMARY KEY,
    transaction_id  BIGINT NOT NULL,
    entry_type      VARCHAR(32) NOT NULL CHECK (entry_type IN ('DEBIT', 'CREDIT')),
    amount          NUMERIC(12,2) NOT NULL CHECK (amount > 0),
    account_code    VARCHAR(24) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_ledger_transaction
        FOREIGN KEY (transaction_id)
            REFERENCES payment_transaction(id)
            ON DELETE CASCADE
);

-----------------------------------------
-- CHILD: PAYMENT METHOD
-----------------------------------------
CREATE TABLE payment_method (
    id              BIGSERIAL PRIMARY KEY,
    transaction_id  BIGINT NOT NULL,
    method_type     VARCHAR(32) NOT NULL CHECK (method_type IN ('CARD', 'CASH', 'TRANSFER')),
    details         TEXT NOT NULL,

    CONSTRAINT fk_payment_method_transaction
        FOREIGN KEY (transaction_id)
            REFERENCES payment_transaction(id)
            ON DELETE CASCADE
);

-----------------------------------------
-- CHILD: AGENT BANK
-----------------------------------------
CREATE TABLE agent_bank (
    id              BIGSERIAL PRIMARY KEY,
    transaction_id  BIGINT NOT NULL,
    bank_name       VARCHAR(128) NOT NULL,
    swift_code      VARCHAR(16) NOT NULL,
    fee_amount      NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (fee_amount >= 0),

    CONSTRAINT fk_agent_transaction
        FOREIGN KEY (transaction_id)
            REFERENCES payment_transaction(id)
            ON DELETE CASCADE
);




CREATE TABLE cinema_seat (
    id      BIGSERIAL PRIMARY KEY,
    taken   BOOLEAN NOT NULL DEFAULT FALSE
);
INSERT INTO cinema_seat (taken) VALUES (FALSE);

-----------------------------------------
-- INDEXES FOR PERFORMANCE
-----------------------------------------
CREATE INDEX idx_ledger_transaction_id ON ledger_entry(transaction_id);
CREATE INDEX idx_payment_method_transaction_id ON payment_method(transaction_id);
CREATE INDEX idx_agent_bank_transaction_id ON agent_bank(transaction_id);
