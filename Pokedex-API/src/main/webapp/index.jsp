<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
	<title>Pokédex</title>
	<link rel="stylesheet" href="css/main.css">
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
	<link rel="shortcut icon" href="imagens/pokedex.png" type="image/x-icon">
</head>
<body>

	<header>
		<nav class="menu">
			<img class="logo-pokedex" src="imagens/logo.png" alt="Logo Pokédex">
		</nav>
	</header>

	<div style="text-align: center; margin-top: 15px;">
		<label for="typeSelect"></label> <select id="typeSelect"
			onchange="location.href='pokemons?type=' + this.value">
			<option value="option">Selecione um tipo de Pokémon</option>
			<option value="fire" ${pokemonTipe == 'fire' ? 'selected' : ''}>Fogo</option>
			<option value="water" ${pokemonType == 'water' ? 'selected' : ''}>Água</option>
			<option value="electric" ${pokemonType == 'electric' ? 'selected' : ''}>Elétrico</option>
			<option value="grass" ${pokemonType == 'grass' ? 'selected' : ''}>Planta</option>
			<option value="bug" ${pokemonType == 'bug' ? 'selected' : ''}>Inseto</option>
			<option value="normal" ${pokemonType == 'normal' ? 'selected' : ''}>Normal</option>
			<option value="psychic" ${pokemonType == 'psychic' ? 'selected' : ''}>Psíquico</option>
			<option value="fairy" ${pokemonType == 'fairy' ? 'selected' : ''}>Fada</option>
			<option value="ghost" ${pokemonType == 'ghost' ? 'selected' : ''}>Fantasma</option>
			<option value="dragon" ${pokemonType == 'dragon' ? 'selected' : ''}>Dragão</option>
			<option value="ice" ${pokemonType == 'ice' ? 'selected' : ''}>Gelo</option>
			<option value="dark" ${pokemonType == 'dark' ? 'selected' : ''}>Sombrio</option>
			<option value="ground" ${pokemonType == 'ground' ? 'selected' : ''}>Terra</option>
			<option value="rock" ${pokemonType == 'rock' ? 'selected' : ''}>Pedra</option>
			<option value="poison" ${pokemonType == 'poison' ? 'selected' : ''}>Veneno</option>
			<option value="steel" ${pokemonType == 'steel' ? 'selected' : ''}>Metal</option>
			<option value="flying" ${pokemonType == 'flying' ? 'selected' : ''}>Voador</option>
			<option value="fighting" ${pokemonType == 'fighting' ? 'selected' : ''}>Lutador</option>
		</select>
	</div>

	<!-- Lista de Pokémon -->
	<div id="pokemonList">
		<c:forEach var="pokemon" items="${pokemonList}">
			<div class="pokemonCard">
				<img class="pokemonImage" src="${pokemon.image}" alt="${pokemon.name}">
				<h2>Informações básicas</h2>
				<h4>${pokemon.name}</h4>
				<h4># ${pokemon.id}</h4>
				
				<!-- Exibir os tipos do Pokémon -->
				<div class="pokemonTypes">
					<strong>Tipos:</strong>
					<c:forEach var="type" items="${pokemon.types}">
						<span class="pokemonType ${type}">${type}</span>
					</c:forEach>
				</div>

				<!-- Exibir altura e peso -->
				<div class="pokemonInfo">
					<p>
						<strong>Altura:</strong> ${pokemon.height/10} m
					</p>
					<p>
						<strong>Peso:</strong> ${pokemon.weight/10} Kg
					</p>
				</div>

				<!-- Exibir as estatísticas -->
				<h2>Estatísticas</h2>
				<c:forEach var="stat" items="${pokemon.stats}">
					<div class="stat-container stat-${stat.stat}">
						<div class="stat-label">${stat.stat}</div>
						<div class="stat-bar">
							<div class="stat-progress" style="width: ${stat.base_stat}%"></div>
						</div>
						<div class="stat-value">${stat.base_stat}</div>
					</div>
				</c:forEach>

				<!-- Exibir habilidades -->
				<div class="pokemonInfo">
					<h2>Habilidades</h2>
					<ul>
						<c:forEach var="ability" items="${pokemon.abilities}">
							<li>${ability}</li>
						</c:forEach>
					</ul>
				</div>

				<!-- Exibir os 3 primeiros movimentos -->
				<div class="pokemonInfo">
					<h2>Movimentos</h2>
					<ul>
						<c:forEach var="move" items="${pokemon.moves}" varStatus="status">
							<!-- Exibe apenas os 3 primeiros movimentos (0, 1, 2) -->
							<c:if test="${status.index < 3}">
								<li>${move}</li>
							</c:if>
						</c:forEach>
					</ul>
				</div>

				<!-- Exibir a cadeia evolutiva -->
				<div class="pokemonEvolution">
					<h2>Cadeia Evolutiva</h2>
					<ul>
						<c:forEach var="evolution" items="${pokemon.evolutionChain}">
							<li>${evolution}</li>
						</c:forEach>
					</ul>
				</div>

			</div>
		</c:forEach>
	</div>

	<!-- Navegação de paginação -->
	<div class="pagination">
		<!-- Botão Anterior -->
		<c:if test="${currentPage == 1}">
			<a class="disabled" aria-disabled="true" href="javascript:void(0)">Anterior</a>
		</c:if>
		<c:if test="${currentPage > 1}">
			<a href="?type=${pokemonType}&page=${currentPage - 1}" class="enabled">Anterior</a>
		</c:if>

		<!-- Exibir a informação de páginas somente se houver Pokémon -->
		<c:if test="${totalPokemons > 0}">
			<div>${currentPage} de ${totalPages}</div>
		</c:if>

		<!-- Botão Próximo -->
		<c:if test="${currentPage < totalPages}">
			<a href="?type=${pokemonType}&page=${currentPage + 1}" class="enabled">Próximo</a>
		</c:if>
	</div>

	<!-- Rodapé da página -->
	<footer>
		<div class="container">
			<div class="container-fluid">
				Pokédex criado por Karli De Jesus Munoz Manzano para a disciplina
				Programação III do curso de graduação em Análise e Desenvolvimento
				de Sistemas do Instituto Federal Catarinense - Campus Fraiburgo.<br>
				Todos os direitos reservados &copy; 2025
			</div>
		</div>
	</footer>

	<script src="js/carregamento.js"></script>

</body>
</html>