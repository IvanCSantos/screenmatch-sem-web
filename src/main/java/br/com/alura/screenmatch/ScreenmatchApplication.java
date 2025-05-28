package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

		// Dados sobre série
		var json = consumoAPI.obterDados("https://omdbapi.com/?t=gilmore+girls&apikey=ffa3e7c8");
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(json);
		System.out.println(dados);


		// Dados sobre episório
		json = consumoAPI.obterDados("https://omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=ffa3e7c8");
		DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
		System.out.println(json);
		System.out.println(dadosEpisodio);
	}



}
