create sequence public.it01_usuario_seq start 1 increment 1;
create table if not exists public.it01_usuario
(
    it01_cod_usuario bigint       not null default nextval('public.it01_usuario_seq'::regclass),
    it01_nome        varchar(255) not null,
    it01_email       varchar(255) not null,
    it01_senha       varchar(255) not null,

    constraint pkit01_cod_usuario primary key (it01_cod_usuario)
);

create sequence public.it02_cliente_seq start 1 increment 1;
create table if not exists public.it02_cliente
(
    it02_cod_cliente bigint       not null default nextval('public.it02_cliente_seq'::regclass),
    it02_nome        varchar(255) not null,
    it02_cpf         varchar(255) not null,

    constraint pkit02_cod_cliente primary key (it02_cod_cliente)
);

create sequence public.it03_filial_seq start 1 increment 1;
create table if not exists public.it03_filial
(
    it03_cod_filial bigint       not null default nextval('public.it03_filial_seq'::regclass),
    it03_nome       varchar(255) not null,

    constraint pkit03_cod_filial primary key (it03_cod_filial)
);

create sequence public.it04_produto_seq start 1 increment 1;
create table if not exists public.it04_produto
(
    it04_cod_produto   bigint       not null default nextval('public.it04_produto_seq'::regclass),
    it04_descricao     varchar(255) not null,
    it04_codigo_barras varchar(13)  not null,

    constraint pkit04_cod_produto primary key (it04_cod_produto)
);

create sequence public.it05_forma_pagamento_seq start 1 increment 1;
create table if not exists public.it05_forma_pagamento
(
    it05_cod_forma_pagamento bigint       not null default nextval('public.it05_forma_pagamento_seq'::regclass),
    it05_descricao           varchar(255) not null,

    constraint pkit05_cod_forma_pagamento primary key (it05_cod_forma_pagamento)
);

create sequence public.it06_tipo_pedido_seq start 1 increment 1;
create table if not exists public.it06_tipo_pedido
(
    it06_cod_tipo_pedido bigint       not null default nextval('public.it06_tipo_pedido_seq'::regclass),
    it06_descricao       varchar(255) not null,

    constraint pkit06_cod_tipo_pedido primary key (it06_cod_tipo_pedido)
);

create sequence public.it07_status_item_seq start 1 increment 1;
create table if not exists public.it07_status_item
(
    it07_cod_status_item bigint       not null default nextval('public.it07_status_item_seq'::regclass),
    it07_descricao       varchar(255) not null,

    constraint pkit07_cod_status_item primary key (it07_cod_status_item)
);

create sequence public.it08_estoque_seq start 1 increment 1;
create table if not exists public.it08_estoque
(
    it08_cod_estoque       bigint not null default nextval('public.it08_estoque_seq'::regclass),
    it08_quantidade        integer         default 0,
    fkit08it03_cod_filial  bigint not null,
    fkit08it04_cod_produto bigint not null,

    constraint pkit08_cod_estoque primary key (it08_cod_estoque),
    constraint fkit08it03_cod_filial foreign key (fkit08it03_cod_filial)
        references it03_filial (it03_cod_filial),
    constraint fkit08it04_cod_produto foreign key (fkit08it04_cod_produto)
        references it04_produto (it04_cod_produto)
);

create sequence public.it09_pedido_seq start 1 increment 1;
create table if not exists public.it09_pedido
(
    it09_cod_pedido                bigint not null default nextval('public.it09_pedido_seq'::regclass),
    fkit09it01_cod_usuario         bigint not null,
    fkit09it02_cod_cliente         bigint,
    fkit09it03_cod_filial          bigint not null,
    fkit09it05_cod_forma_pagamento bigint,
    fkit09it06_cod_tipo_pedido     bigint,

    constraint pkit09_cod_pedido primary key (it09_cod_pedido),
    constraint fkit09it01_cod_usuario foreign key (fkit09it01_cod_usuario)
        references it01_usuario (it01_cod_usuario),
    constraint fkit09it02_cod_cliente foreign key (fkit09it02_cod_cliente)
        references it02_cliente (it02_cod_cliente),
    constraint fkit09it03_cod_filial foreign key (fkit09it03_cod_filial)
        references it03_filial (it03_cod_filial),
    constraint fkit09it05_cod_forma_pagamento foreign key (fkit09it05_cod_forma_pagamento)
        references it05_forma_pagamento (it05_cod_forma_pagamento),
    constraint fkit09it06_cod_tipo_pedido foreign key (fkit09it06_cod_tipo_pedido)
        references it06_tipo_pedido (it06_cod_tipo_pedido)
);

create sequence public.it10_item_pedido_seq start 1 increment 1;
create table if not exists public.it10_item_pedido
(
    it10_cod_item_pedido       bigint not null default nextval('public.it10_item_pedido_seq'::regclass),
    it10_quantidade            integer         default 0,
    it10_valor_unitario        numeric         default 0.0,
    fkit10it04_cod_produto     bigint not null,
    fkit10it07_cod_status_item bigint not null,

    constraint pkit10_cod_item_pedido primary key (it10_cod_item_pedido),
    constraint fkit10it04_cod_produto foreign key (fkit10it04_cod_produto)
        references it04_produto (it04_cod_produto),
    constraint fkit10it07_cod_status_item foreign key (fkit10it07_cod_status_item)
        references it07_status_item (it07_cod_status_item)
);
