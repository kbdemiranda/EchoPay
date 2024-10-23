create schema if not exists echopay;

create table echopay.transactions
(
    uuid           uuid,
    amount         numeric(10, 2) not null,
    payment_method varchar(20)    not null,
    currency_code  varchar(3)     not null,
    description    varchar(255),
    status         varchar(10)    not null,
    created_at     timestamp without time zone default now(),

    constraint pk_transactions primary key (uuid)
);
