# Getting Started


### Insert Sample Accounts

You can create tables in 'bank' database (they can be auto created)

and 

You can populate the `accounts` table with some sample data using the following SQL:

```sql

create table public.accounts
(
    id        uuid           not null
        primary key,
    balance   numeric(38, 2) not null,
    client_id uuid           not null,
    version   bigint
);

alter table public.accounts
    owner to postgres;


create table public.ledger_entry
(
    id          uuid                        not null
        primary key,
    account_id  uuid                        not null,
    amount      numeric(38, 2)              not null,
    created_at  timestamp(6) with time zone not null,
    direction   varchar(255)                not null
        constraint ledger_entry_direction_check
            check ((direction)::text = ANY ((ARRAY ['DEBIT'::character varying, 'CREDIT'::character varying])::text[])),
    transfer_id uuid                        not null,
    constraint ux_ledger_unique
        unique (transfer_id, account_id, direction)
);

alter table public.ledger_entry
    owner to postgres;


create table public.outbox_events
(
    id             uuid         not null
        primary key,
    aggregate_id   uuid         not null,
    aggregate_type varchar(255) not null,
    created_at     timestamp(6) with time zone,
    event_type     varchar(255) not null,
    payload        text         not null,
    processed      boolean      not null
);

alter table public.outbox_events
    owner to postgres;

create index idx_outbox_status_created
    on public.outbox_events (processed, created_at);



create table public.transfers
(
    id              uuid not null
        primary key,
    amount          numeric(38, 2),
    created_at      timestamp(6) with time zone,
    idempotency_key varchar(255),
    receiver_id     uuid,
    sender_id       uuid,
    status          varchar(255)
        constraint transfers_status_check
            check ((status)::text = ANY
                   ((ARRAY ['PENDING'::character varying, 'COMPLETED'::character varying, 'FAILED'::character varying])::text[]))
);

alter table public.transfers
    owner to postgres;

create unique index idx_transfer_idempotency_key
    on public.transfers (idempotency_key);


INSERT INTO accounts (id, client_id, balance, version) VALUES
                                                           ('11111111-1111-1111-1111-111111111111', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 1000.00, 0),
                                                           ('22222222-2222-2222-2222-222222222222', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 500.00, 0),
                                                           ('33333333-3333-3333-3333-333333333333', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 2000.00, 0);

```

### Test the API

You can send a test transfer using **cURL**:

#### To make transfer between accounts

```bash
curl -X POST 'http://localhost:8080/api/v1/transfers/send' \
  --header 'Idempotency-Key: u63d3a99-2f43-4a7c-baf1-0bdbb2a67dk7' \
  --header 'Content-Type: application/json' \
  --data '{
    "senderId": "11111111-1111-1111-1111-111111111111",
    "receiverId": "22222222-2222-2222-2222-222222222222",
    "amount": 33
  }'

```

#### To get accounts by ClientId
```bash
curl -X GET 'http://localhost:8080/api/v1/accounts/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'

```
