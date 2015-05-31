# Multi-user-Chat

O sistema de chat é constituido por um servidor e um ou mais clientes. O servidor e cada cliente possuem uma tabela com os usuários connectados ao sistema. Para isso, para cada nó é atribuido um ID associado com um nome do usuário. Por padrão o servidor possui ID 1 e nome "server". Cada cliente é atribuido um ID único após connectar ao servidor. Caso nenhum nome seja passado para o cliente no momento de sua inicialização seu nome será "unknown" seguido por um número random. Para os clientes se comunicarem é utilizado este nome com exceção do servidor que pode enviar mensagens de broadcast para todos os clientes pela linha de comando.

Exemplo:

1) Para adicionar nome à um cliente este deve ser iniciado na linha de comando da seguinte forma:

java Client client_name

2) Suponha que haja dois usuários connectados, Miles e Matheus. Para que Miles envie uma mensagem para matheus este deve digitar na linha de comando:

.Miles digita
send Matheus any_message_here

.Matheus terá como resposta
Miles sent: any_message_here

Essa mensagem é enviada ao servidor que se encarregar de direcionar para o cliente adequado.

3) Mensagens podem ser enviadas exclusivamente para o servidor fazendo:

send server any_message_here


