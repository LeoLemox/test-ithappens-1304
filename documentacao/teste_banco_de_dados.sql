-- TODOS OS PRODUTOS COM QUANTIDADE MAIOR OU IGUAL A 100

select it04.it04_cod_produto     as cod_produto,
       it04.it04_descricao       as produto,
       sum(it08.it08_quantidade) as quantidade_total
from it08_estoque it08
         inner join it04_produto it04
                    on it08.fkit08it04_cod_produto = it04.it04_cod_produto
group by it04.it04_cod_produto, it04.it04_descricao
having sum(it08.it08_quantidade) >= 100;
------------------------------------------------------------------------------------------------------------------------

-- TODOS OS PRODUTOS COM ESTOQUE PARA A FILIAL DE CÓDIGO 60

select it04.it04_cod_produto     as cod_produto,
       it04.it04_descricao       as produto,
       sum(it08.it08_quantidade) as quantidade_total
from it08_estoque it08
         inner join it04_produto it04
                    on it08.fkit08it04_cod_produto = it04.it04_cod_produto
where it08.fkit08it03_cod_filial = 60
group by it04.it04_cod_produto, it04.it04_descricao
having sum(it08.it08_quantidade) > 0;
------------------------------------------------------------------------------------------------------------------------

-- TODOS OS CAMPOS PARA O DOMINIO PEDIDO E ITEMPEDIDO FILTRANDO APENAS O PRODUTO DE CÓDIGO 7993

select *
from it09_pedido it09
         inner join it10_item_pedido it10
                    on it09.it09_cod_pedido = it10.fkit10it09_cod_pedido
where it10.fkit10it04_cod_produto = 7993;
------------------------------------------------------------------------------------------------------------------------

-- PEDIDOS E SUAS RESPECTIVAS FORMAS DE PAGAMENTO

select *
from it09_pedido it09
         inner join it05_forma_pagamento it05
                    on it09.fkit09it05_cod_forma_pagamento = it05.it05_cod_forma_pagamento;
------------------------------------------------------------------------------------------------------------------------

-- VALORES DO PEDIDO E DOS ITENS DO PEDIDO

select it09.it09_cod_pedido       as cod_pedido,
       it04.it04_descricao        as produto,
       sum(it04.it04_cod_produto) as quantidade,
       it04.it04_valor            as valor_unitario,
       sum(it04.it04_valor)       as valor_total
from it09_pedido it09
         inner join it10_item_pedido it10
                    on it09.it09_cod_pedido = it10.fkit10it09_cod_pedido
         inner join it04_produto it04
                    on it10.fkit10it04_cod_produto = it04.it04_cod_produto
group by it09.it09_cod_pedido, it04.it04_descricao, it04.it04_valor;

------------------------------------------------------------------------------------------------------------------------

-- TOTAL DE ITENS DO PEDIDO MAIOR QUE 10

select it09.it09_cod_pedido           as cod_pedido,
       sum(it10.it10_cod_item_pedido) as quantidade
from it09_pedido it09
         inner join it10_item_pedido it10
                    on it09.it09_cod_pedido = it10.fkit10it09_cod_pedido
group by it09.it09_cod_pedido
having sum(it10.it10_cod_item_pedido) > 10;

------------------------------------------------------------------------------------------------------------------------