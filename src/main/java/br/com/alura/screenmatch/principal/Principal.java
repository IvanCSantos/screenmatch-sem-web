package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Principal {
  private Scanner scanner = new Scanner(System.in);
  private ConsumoAPI consumoAPI = new ConsumoAPI();
  private ConverteDados conversor = new ConverteDados();

  private final String ENDERECO = "https://omdbapi.com/?t=";
  private final String API_KEY = "&apikey=ffa3e7c8";

  public void inicia() {
    var nomeSerie = exibeMenu();

    DadosSerie serie = consultaSerie(nomeSerie);
    List<DadosTemporada> temporadas = consultaTemporadas(serie);
    List<DadosEpisodio> dadosEpisodios = consultaEpisodios(temporadas);
    List<Episodio> episodios = consultaEpisodios2(temporadas);

    List<Episodio> melhoresEpisodios = melhoresEpisodios(episodios, 5);

    imprimeTemporadas("\nTodas as temporadas", temporadas);
    imprimeEpisodios("\nTodos os episódios", episodios);
    imprimeEpisodios("\nMelhores episódios", melhoresEpisodios);

    var nomeEpisodio = exibeMenuBusca();
    var episodio = buscaEpisodio(episodios, nomeEpisodio);
    if (episodio.isPresent()) {
      imprimeEpisodios("\nUm único episódio", episodio.get());
    } else {
      System.out.println("Episódio não encontrado");
    }

  }

  public Optional<Episodio> buscaEpisodio(List<Episodio> dados, String nomeEpisodio) {
    Optional<Episodio> episodio = dados.stream()
            .filter(e -> e.getTitulo().toLowerCase().contains(nomeEpisodio.toLowerCase()))
            .findFirst();

    return episodio;
  }

  public String exibeMenu() {
    System.out.print("Digite o nome da série para a busca: ");
    var nomeSerie = scanner.nextLine();

    return nomeSerie;
  }

  public String exibeMenuBusca() {
    System.out.print("\nInforme o nome do episódio que deseja buscar: ");
    String nomeEpisodio = scanner.nextLine();

    return nomeEpisodio;
  }

  public DadosSerie consultaSerie(String nomeSerie) {
    var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
    DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

    return dados;
  }

  public List<DadosTemporada> consultaTemporadas(DadosSerie dados) {
    List<DadosTemporada> temporadas = new ArrayList<>();
    String json;

    for(int i = 1; i <= dados.totalTemporadas(); i++) {
      var URL = ENDERECO + dados.titulo().replace(" ", "+") + "&season=" + i + API_KEY;
      json = consumoAPI.obterDados(URL);

      DadosTemporada temporada = conversor.obterDados(json, DadosTemporada.class);
      temporadas.add(temporada);
    }


    return temporadas;
  }

  public void imprimeTemporadas(String titulo, List<DadosTemporada> temporadas) {
    System.out.println(titulo);
    temporadas.forEach(System.out::println);
  }

  public List<DadosEpisodio> consultaEpisodios(List<DadosTemporada> dados) {

    List<DadosEpisodio> dadosEpisodios = dados.stream()
            .flatMap(t -> t.episodios().stream())
            .collect(Collectors.toList());
    return dadosEpisodios;
  }

  public List<Episodio> consultaEpisodios2(List<DadosTemporada> dados) {
    List<Episodio> episodios = dados.stream()
            .flatMap(t -> t.episodios().stream()
                    .map(e -> new Episodio(t.temporada(), e))
            ).collect(Collectors.toList());
    return episodios;
  }

  public void imprimeEpisodios(String titulo, List<Episodio> episodios) {
    System.out.println(titulo);
    episodios.stream().forEach(System.out::println);
  }

  public void imprimeEpisodios(String titulo, Episodio episodio) {
    System.out.println(titulo);
    System.out.println(episodio);
  }

  public List<Episodio> melhoresEpisodios(List<Episodio> dadosEpisodios, int limit) {
    List<Episodio> melhores = dadosEpisodios.stream()
            .sorted(Comparator.comparing(Episodio::getAvaliacao).reversed())
            .limit(limit)
            .collect(Collectors.toList());
    return melhores;
  }
}
