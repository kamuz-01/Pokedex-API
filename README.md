# **Projeto Pokedex-API**

## **✦✦ Descrição ✦✦**

✔ O projeto Pokedex-API é uma aplicação simples desenvolvida em Java utilizando Servlets e JSPs. 
Ela consome a API (https://pokeapi.co) para buscar informações sobre Pokémon e exibi-las em uma interface web interativa. 
A aplicação permite ao usuário procurar os Pokémon por Categoria e visualizar seu nome, ID, imagem e outras informações.

## **Funcionalidades:**

✔ Buscar e listar todos os Pokémon por categoria.

✔ Exibir o nome, ID, imagem e outras informações dos Pokémon.

✔ Interface simples e intuitiva para interação com a aplicação.

## **Tecnologias Utilizadas**

✔ Java: Linguagem de programação utilizada para desenvolver a lógica da aplicação.

✔ Servlets: Controlador para gerenciar as requisições e respostas.

✔ JSP: Página dinâmica para exibir as informações dos Pokémons.

✔ CSS3: Utilizada para a estilização da aplicação.

✔ https://pokeapi.co: API externa utilizada para buscar dados sobre os Pokémon.


## **Como Rodar a Aplicação**
Para rodar a aplicação em seu ambiente local, siga os passos abaixo:

### **Pré-requisitos**
✔ Java 21 LTS ou superior instalado no seu sistema.

✔ Apache Tomcat 11 ou outro servidor de aplicações que implemente o padrão Jakarta.

✔ IDE de sua preferência (por exemplo, NetBeans, Eclipse, ou IntelliJ IDEA).

*Note que esse projeto foi desenvolvido com a IDE eclipse, já que no Netbeans, na hora de executar o programa, dá o erro **Unable to connect to SSL due to javax.net.ssl.SSLHandshakeException**.*


## Rode a aplicação:

✔ Inicie o servidor (Tomcat, por exemplo) através da IDE ou da linha de comando.

✔ Acesse a aplicação no navegador: http://localhost:8080/Pokedex-API.

✔ Ao acessar o endereço no navegador, você verá a tela principal da Pokedex.

✔ Utilize a lista desplegável para procurar os Pokémon por categoria, como "Fogo", "Elétrico", etc.

✔ A aplicação exibirá todos os Pokémon com seu nome, ID, imagem e outros dados do Pokémon escolhido.

## Estrutura do Projeto
A estrutura do projeto segue o padrão MVC (Model-View-Controller):


## Como Funciona?
✔ PokedexServlet (Controller)

O PokedexServlet recebe as requisições de busca do usuário, consome a PokeAPI, e envia as informações do Pokémon para a index.jsp. 
O Servlet utiliza as bibliotecas HttpURLConnection e json-20250107 para realizar as requisições HTTP à API e processar as respostas em JSON.

✔ index.jsp (View)

A página index.jsp exibe as informações do Pokémon (nome, ID e imagem) que foram passadas pelo Servlet. Também há um formulário simples que permite ao usuário procurar por outro Pokémon.

## Melhorias Futuras

✔ Design aprimorado: Utilizar frameworks de CSS como Bootstrap para melhorar a interface do usuário.

✔ Testes automatizados: Adicionar testes unitários para garantir o funcionamento correto da aplicação.

## Contribuição
Se você deseja contribuir para o projeto, fique à vontade para abrir uma issue ou enviar um pull request. Aqui estão algumas formas de ajudar:

✔ Melhorias no código ou na estrutura do projeto.

✔ Correções de bugs ou problemas encontrados.

✔ Sugestões para novas funcionalidades.

## Licença
Este projeto está licenciado sob a MIT License - veja o arquivo LICENSE para mais detalhes.
