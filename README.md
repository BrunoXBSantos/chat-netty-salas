# chat-netty-salas

Servidor e Cliente assincrono usando netty e atomix.

O cliente pode entrar numa sala do servidor e apenas falar com os utilizadores presentes na sala. Servidor pode ter várias salas em paralelo.

## Comandos do utilizador no programa do cliente:

    $ /room <sala>            -> entra numa sala
    $ /leave                  ->  sai da sala
    $ <mensagem>              -> qualquer mensagem a enviar para os utilizadores da sala
