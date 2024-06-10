package classes;

import java.util.Map;

public abstract class Publicacao {
    protected int ano;
    protected String siglaVeiculo;
    protected String titulo;
    protected Map<Long, Docente> autores;
    protected int numero;
    protected int paginaInicial;
    protected int paginaFinal;

    protected Publicacao(int ano,
                         String siglaVeiculo,
                         String titulo,
                         Map<Long, Docente> autores,
                         int numero,
                         int paginaInicial,
                         int paginaFinal){
        this.ano=ano;
        this.siglaVeiculo=siglaVeiculo;
        this.titulo=titulo;
        this.autores=autores;
        this.numero=numero;
        this.paginaInicial=paginaInicial;
        this.paginaFinal=paginaFinal;
    }
}
