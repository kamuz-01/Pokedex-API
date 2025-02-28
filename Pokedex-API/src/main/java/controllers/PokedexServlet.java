package controllers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/pokemons")
public class PokedexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Set<String> POKEMON_FILTRADOS = new HashSet<>(Arrays.asList(
        "pikachu-alola", "pikachu-world-cap", "raichu-alola", "pikachu-starter", 
        "pikachu-partner-cap", "pikachu-kalos-cap", "pikachu-alola-cap", "pikachu-hoenn-cap", 
        "pikachu-unova-cap", "pikachu-libre", "pikachu-cosplay", "pikachu-original-cap", 
        "pikachu-belle", "pikachu-pop-star", "pikachu-phd", "pikachu-rock-star", 
        "pikachu-gmax", "pikachu-sinnoh-cap", "miraidon-low-power-mode", "miraidon-drive-mode", 
        "miraidon-aquatic-mode", "miraidon-glide-mode"));
    
    private static final int POKEMONS_POR_PAGINA = 6; // Quantidade de Pokémons por página

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obter o tipo de Pokémon selecionado pelo usuário
        String pokemonTipo = request.getParameter("type");
        if (pokemonTipo == null) {
        	pokemonTipo = "option";
        }

        // Obter o número da página
        int pagina = 1;
        try {
            String parametroPagina = request.getParameter("page");
            if (parametroPagina != null) {
                pagina = Integer.parseInt(parametroPagina);
            }
        } catch (NumberFormatException e) {
            pagina = 1; // Valor default em caso de erro
        }

        // Calcular o offset
        int deslocamento = (pagina - 1) * POKEMONS_POR_PAGINA;

        // URL da API
        String url = "https://pokeapi.co/api/v2/type/" + pokemonTipo + "/";

        // Buscar os dados da API
        JSONObject apiResponsta = buscarDadosPokemons(url);

        List<Map<String, Object>> listaPokemons = new ArrayList<>();
        int totalPokemons = 0;
        if (apiResponsta != null) {
            JSONArray pokemons = apiResponsta.getJSONArray("pokemon");
            totalPokemons = pokemons.length();

            // Paginar os resultados
            int inicioPagina = deslocamento;
            int fimPagina = Math.min(inicioPagina + POKEMONS_POR_PAGINA, totalPokemons);

            for (int i = inicioPagina; i < fimPagina; i++) {
                JSONObject pokemonData = pokemons.getJSONObject(i);
                String nomePokemon = pokemonData.getJSONObject("pokemon").getString("name");

                // Filtra Pokémon que não devem ser exibidos
                if (POKEMON_FILTRADOS.contains(nomePokemon)) {
                    continue;
                }

                // Buscar os detalhes do Pokémon, incluindo a imagem, altura, peso e estatísticas
                String pokemonDetalhesUrl = "https://pokeapi.co/api/v2/pokemon/" + nomePokemon;
                JSONObject detalhesPokemon = buscarDadosPokemons(pokemonDetalhesUrl);

                if (detalhesPokemon != null) {
                    Map<String, Object> pokemonMap = construirDetalhesPokemon(detalhesPokemon, nomePokemon);

                    // Adiciona os dados do Pokémon à lista
                    listaPokemons.add(pokemonMap);
                }
            }
        }

        // Calcular o número total de páginas
        int totalPaginas = (int) Math.ceil((double) totalPokemons / POKEMONS_POR_PAGINA);

        // Passando os dados para o JSP
        request.setAttribute("pokemonList", listaPokemons);
        request.setAttribute("pokemonType", pokemonTipo);
        request.setAttribute("totalPages", totalPaginas);
        request.setAttribute("currentPage", pagina);

        // Passar também a quantidade total de Pokémons
        request.setAttribute("totalPokemons", totalPokemons);

        // Encaminhar para o JSP
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    // Construir detalhes do Pokémon
    private Map<String, Object> construirDetalhesPokemon(JSONObject detalhesPokemon, String nomePokemon) {
        Map<String, Object> mapaPokemons = new HashMap<>();
        
        // Obter a URL da imagem
        String pokemonImagem = obterImagensPokemon(detalhesPokemon);

        // Obter os tipos
        List<String> listadoTipos = obterTiposPokemon(detalhesPokemon);

        // Obter as estatísticas
        List<Map<String, Object>> listadoEstatisticas = obterEstatisticasPokemon(detalhesPokemon);

        // Adicionar altura e peso
        int altura = detalhesPokemon.getInt("height");
        int peso = detalhesPokemon.getInt("weight");
        
        // Obter id (número da Pokédex)
        int pokemonId = detalhesPokemon.getInt("id");
        mapaPokemons.put("id", pokemonId);

        // Obter habilidades
        List<String> abilityList = obterHabilidadesPokemon(detalhesPokemon);

        // Obter movimentos
        List<String> moveList = obterMovimentosPokemon(detalhesPokemon);

        // Buscar cadeia evolutiva
        List<String> evolutionList = obterCadeiaEvolutiva(pokemonId, nomePokemon);

        // Preenchendo o mapa com todos os detalhes
        mapaPokemons.put("name", nomePokemon);
        mapaPokemons.put("image", pokemonImagem);
        mapaPokemons.put("types", listadoTipos);
        mapaPokemons.put("stats", listadoEstatisticas);
        mapaPokemons.put("height", altura);
        mapaPokemons.put("weight", peso);
        mapaPokemons.put("abilities", abilityList);
        mapaPokemons.put("moves", moveList);
        mapaPokemons.put("evolutionChain", evolutionList);

        return mapaPokemons;
    }

    // Métodos auxiliares
    private String obterImagensPokemon(JSONObject detalhesPokemon) {
        if (detalhesPokemon.getJSONObject("sprites").has("other")) {
            JSONObject otherSprites = detalhesPokemon.getJSONObject("sprites").getJSONObject("other");
            if (otherSprites.has("official-artwork")) {
                return otherSprites.getJSONObject("official-artwork").optString("front_default", "path/to/default/image.png");
            }
        }
        return "path/to/default/image.png";
    }

    private List<String> obterTiposPokemon(JSONObject detalhesPokemon) {
        List<String> listaTipos = new ArrayList<>();
        JSONArray tipos = detalhesPokemon.getJSONArray("types");
        for (int i = 0; i < tipos.length(); i++) {
            String tipo = tipos.getJSONObject(i).getJSONObject("type").getString("name");
            listaTipos.add(tipo);
        }
        return listaTipos;
    }

    private List<Map<String, Object>> obterEstatisticasPokemon(JSONObject detalhesPokemon) {
        List<Map<String, Object>> listaEstatisticas = new ArrayList<>();
        JSONArray estatisticas = detalhesPokemon.getJSONArray("stats");
        for (int i = 0; i < estatisticas.length(); i++) {
            JSONObject dadosEstatisticos = estatisticas.getJSONObject(i);
            Map<String, Object> mapaEstatisticas = new HashMap<>();
            mapaEstatisticas.put("stat", dadosEstatisticos.getJSONObject("stat").getString("name"));
            mapaEstatisticas.put("base_stat", dadosEstatisticos.getInt("base_stat"));
            listaEstatisticas.add(mapaEstatisticas);
        }
        return listaEstatisticas;
    }

    private List<String> obterHabilidadesPokemon(JSONObject detalhesPokemon) {
        List<String> listaHabilidades = new ArrayList<>();
        JSONArray habilidades = detalhesPokemon.getJSONArray("abilities");
        for (int i = 0; i < habilidades.length(); i++) {
            String nomeHabilidade = habilidades.getJSONObject(i).getJSONObject("ability").getString("name");
            listaHabilidades.add(nomeHabilidade);
        }
        return listaHabilidades;
    }

    private List<String> obterMovimentosPokemon(JSONObject detalhesPokemon) {
        List<String> listaMovimentos = new ArrayList<>();
        JSONArray movimentos = detalhesPokemon.getJSONArray("moves");
        for (int i = 0; i < movimentos.length(); i++) {
            String nomeMovimentos = movimentos.getJSONObject(i).getJSONObject("move").getString("name");
            listaMovimentos.add(nomeMovimentos);
        }
        return listaMovimentos;
    }

    private List<String> obterCadeiaEvolutiva(int pokemonId, String nomeAtualPokemon) {
        String speciesUrl = "https://pokeapi.co/api/v2/pokemon-species/" + pokemonId;
        JSONObject detalheSpecies = buscarDadosPokemons(speciesUrl);

        List<String> listaEvolucoes = new ArrayList<>();
        if (detalheSpecies != null) {

            // Agora, faça a requisição para obter a cadeia evolutiva real
            JSONObject cadeiaEvolutivaUrl = detalheSpecies.getJSONObject("evolution_chain");
            String cadeiaEvolutivaUrlStr = cadeiaEvolutivaUrl.getString("url");
            
            // Chama o método fetchPokemonData novamente para pegar a cadeia evolutiva completa
            JSONObject detalhesCadeiaEvolutiva = buscarDadosPokemons(cadeiaEvolutivaUrlStr);

            if (detalhesCadeiaEvolutiva != null) {
                // Agora, processa a cadeia evolutiva
                cadeiaProcessoEvolutivo(detalhesCadeiaEvolutiva.getJSONObject("chain"), listaEvolucoes, nomeAtualPokemon);
            } 
        }

        return listaEvolucoes;
    }


    private void cadeiaProcessoEvolutivo(JSONObject sequenciaEvolucao, List<String> listaEvolucoes, String nomeAtualPokemon) {
        if (sequenciaEvolucao.has("species")) {
            String nomeSpecies = sequenciaEvolucao.getJSONObject("species").getString("name");
            if (!nomeSpecies.equals(nomeAtualPokemon)) {
                listaEvolucoes.add(nomeSpecies);
            }
        }

        if (sequenciaEvolucao.has("evolves_to")) {
            JSONArray proximaEvolucao = sequenciaEvolucao.getJSONArray("evolves_to");
            for (int i = 0; i < proximaEvolucao.length(); i++) {
                cadeiaProcessoEvolutivo(proximaEvolucao.getJSONObject(i), listaEvolucoes, nomeAtualPokemon);
            }
        }
    }

    private JSONObject buscarDadosPokemons(String url) {
        try {
            java.net.URL apiUrl = new java.net.URL(url);
            HttpURLConnection conexao = (HttpURLConnection) apiUrl.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setConnectTimeout(10000);
            conexao.setReadTimeout(10000);
            conexao.connect();

            int codigoResposta = conexao.getResponseCode();
            if (codigoResposta != HttpURLConnection.HTTP_OK) {
                return null;
            }

            try (java.io.InputStreamReader in = new java.io.InputStreamReader(conexao.getInputStream());
                 java.io.BufferedReader leituraResposta = new java.io.BufferedReader(in)) {
                StringBuilder resposta = new StringBuilder();
                String serie;
                while ((serie = leituraResposta.readLine()) != null) {
                    resposta.append(serie);
                }
                return new JSONObject(resposta.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}