package classes;

import java.util.Map;

public class PublicacaoConferencia extends Publicacao{
    private String local;

    public PublicacaoConferencia(int ano,
                         String siglaVeiculo,
                         String titulo,
                         Map<Long, Docente> autores,
                         int numero,
                         String local,
                         int paginaInicial,
                         int paginaFinal,
                         Veiculo veiculo){
        super(ano, siglaVeiculo, titulo, autores, numero, paginaInicial, paginaFinal, veiculo);
        this.local=local;
    }
}
