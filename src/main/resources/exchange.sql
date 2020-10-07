CREATE TABLE exchange_rate
(
    id      bigint not null,
    average double not null,
    base    varchar(255),
    date    date,
    rate    double not null,
    target  varchar(255),
    trend   integer,
    t   integer,
    primary key (id)
)
