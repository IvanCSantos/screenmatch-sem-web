package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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

    System.out.println("Temporadas:");
    temporadas.forEach(System.out::println);
    consultaEpisodios(temporadas);
  }

  public void consultaEpisodios(List<DadosTemporada> dados) {
    System.out.println("Episódios:");

    // Forma tradicional de iteração
    //    for (int i = 0; i < dados.totalTemporadas(); i++) {
    //      List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
    //
    //      for ( int j = 0; j < episodiosTemporada.size(); j ++) {
    //        System.out.println(episodiosTemporada.get(j).titulo());
    //      }
    //    }

    // Forma moderna de iteracao, utilizando lambda (paradigma funcional, pois o forEach recebe uma função)
    dados.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

    List<DadosEpisodio> dadosEpisodios = dados.stream()
            .flatMap(t -> t.episodios().stream())
            .collect(Collectors.toList());

    List<Episodio> episodios = dados.stream()
            .flatMap(t -> t.episodios().stream()
                      .map(e -> new Episodio(t.temporada(), e))
            ).collect(Collectors.toList());

    melhoresEpisodios(episodios, 5);
  }

  public void melhoresEpisodios(List<Episodio> dadosEpisodios, int limit) {
    dadosEpisodios.stream()
            .sorted(Comparator.comparing(Episodio::getAvaliacao).reversed())
            .limit(limit)
            .forEach(System.out::println);
  }

  public void testes() {


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

   List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8,9);
   List<Integer> pares = numeros.stream()
           .filter(n -> n % 2 == 0)
           .collect(Collectors.toList());
    System.out.println(pares);

   // Declarando array da forma mais básica
   int[] numeros2 = new int[5];
   numeros2[0] = 1;
   numeros2[1] = 2;

   // Declarando array e já inicializando
   Integer[] numerosDeOutroJeito = {1, 2, 3, 4, 5, 6, 7, 8, 9};
   // numerosDeOutroJeito.forEach(System.out::println); // Não funciona
   //System.out.println(numerosDeOutroJeito);
    Arrays.stream(numerosDeOutroJeito).forEach(System.out::println); // Mas pode ser usado assim com Arrays.stream()

  }
}
