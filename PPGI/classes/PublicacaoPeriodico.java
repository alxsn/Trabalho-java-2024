package classes;

import java.util.Map;

public class PublicacaoPeriodico extends Publicacao{
    int volumePeriodico;
    public PublicacaoPeriodico(int ano,
                         String siglaVeiculo,
                         String titulo,
                         Map<Long, Docente> autores,
                         int numero,
                         int volumePeriodico,
                         int paginaInicial,
                         int paginaFinal,
                         Veiculo veiculo){
        super(ano, siglaVeiculo, titulo, autores, numero, paginaInicial, paginaFinal, veiculo);
        this.volumePeriodico = volumePeriodico;
    }
}
