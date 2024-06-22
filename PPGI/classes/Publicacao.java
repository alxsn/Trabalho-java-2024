package classes;

import java.util.TreeMap;

public abstract class Publicacao {
    protected int ano;
    protected String siglaVeiculo;
    protected String titulo;
    protected TreeMap<Long, Docente> autores;
    protected int numero;
    protected int paginaInicial;
    protected int paginaFinal;
    protected Veiculo veiculo;

    protected Publicacao(int ano,
                         String siglaVeiculo,
                         String titulo,
                         TreeMap<Long, Docente> autores,
                         int numero,
                         int paginaInicial,
                         int paginaFinal,
                         Veiculo veiculo){
        this.ano=ano;
        this.siglaVeiculo=siglaVeiculo;
        this.titulo=titulo;
        this.autores=autores;
        this.numero=numero;
        this.paginaInicial=paginaInicial;
        this.paginaFinal=paginaFinal;
        this.veiculo=veiculo;
    }

    public int getAno(){
        return ano;
    }

    public String getSiglaVeiculo(){
        return siglaVeiculo;
    }

    public String getTitulo(){
        return titulo;
    }

    public TreeMap<Long, Docente> getAutores(){
        return autores;
    }

    public Veiculo getVeiculo(){
        return veiculo;
    }
}
