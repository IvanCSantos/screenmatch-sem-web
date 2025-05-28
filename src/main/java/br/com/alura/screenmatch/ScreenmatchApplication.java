package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoAPI = new ConsumoAPI();
		ConverteDados conversor = new ConverteDados();

		// Um teste de consumo de API que retorna uma foto de café aleatória
		//json = consumoAPI.obterDados("https://coffee.alexflipnote.dev/random.json");
		//System.out.println(json);

		// Consumo dados sobre série
		var json = consumoAPI.obterDados("https://omdbapi.com/?t=gilmore+girls&apikey=ffa3e7c8");
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(json);
		System.out.println(dados);


		// Consumo dados sobre episório
		json = consumoAPI.obterDados("https://omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=ffa3e7c8");
		DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
		System.out.println(json);
		System.out.println(dadosEpisodio);

		// Consumo dados sobre a temporada
		json = consumoAPI.obterDados("https://omdbapi.com/?t=gilmore+girls&season=1&apikey=ffa3e7c8");
		DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
		System.out.println(json);
		System.out.println(dadosTemporada);
		for(int i = 1; i < dadosTemporada.episodios().size(); i++) {
			System.out.println("Episódio: " + dadosTemporada.episodios().get(i).numero() + ", Título: " + dadosTemporada.episodios().get(i).titulo());
		}

		List<DadosTemporada> temporadas = new ArrayList<>();

		// Todas as temporadas
		for (int i = 1; i <= dados.totalTemporadas(); i++) {
			json = consumoAPI.obterDados("https://omdbapi.com/?t=gilmore+girls&season=" + i + "&apikey=ffa3e7c8");
			DadosTemporada temporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(temporada);
		}
		temporadas.forEach(System.out::println);
	}

}
