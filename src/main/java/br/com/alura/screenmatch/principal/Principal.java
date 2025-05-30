package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Principal {
  private Scanner scanner = new Scanner(System.in);
  private ConsumoAPI consumoAPI = new ConsumoAPI();
  private ConverteDados conversor = new ConverteDados();

  private final String ENDERECO = "https://omdbapi.com/?t=";
  private final String API_KEY = "&apikey=ffa3e7c8";

  public void exibeMenu() {
    System.out.print("Digite o nome da série para a busca: ");
    var nomeSerie = scanner.nextLine();

    consultaSerie(nomeSerie);
  }

  public void consultaSerie(String nomeSerie) {
    var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
    DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

    System.out.println(dados);

    consultaTemporadas(dados);

  }

  public void consultaTemporadas(DadosSerie dados) {
    List<DadosTemporada> temporadas = new ArrayList<>();
    String json;

    for(int i = 1; i <= dados.totalTemporadas(); i++) {
      var URL = ENDERECO + dados.titulo().replace(" ", "+") + "&season=" + i + API_KEY;
      json = consumoAPI.obterDados(URL);
      DadosTemporada temporada = conversor.obterDados(json, DadosTemporada.class);
      temporadas.add(temporada);
    }

    temporadas.forEach(System.out::println);

// Forma tradicional de iteração
//    for (int i = 0; i < dados.totalTemporadas(); i++) {
//      List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//
//      for ( int j = 0; j < episodiosTemporada.size(); j ++) {
//        System.out.println(episodiosTemporada.get(j).titulo());
//      }
//    }

    // Forma moderna de iteracao, utilizando lambda (paradigma funcional, pois o forEach recebe uma função)
    temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

    // Streams (fluxo de dados, trabalha com operacoes intermediarias - que geram novos streams, e finaliza com uma operacao fim)
    // Lambda: n -> expressao (funcao anonima)
    // method reference :: (quando a função recebe um parametro e opera apenas nesse parametro)
    List<String> nomes = Arrays.asList("Ivan", "Elaine", "Daniel", "Pamela", "Rodrigo");
    nomes.stream()
            .sorted()
            .limit(4)
            .filter(n -> n.startsWith("I"))
            .map(n -> n.toUpperCase())
            .forEach(System.out::println);
  }
}
