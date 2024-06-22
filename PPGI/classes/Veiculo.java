package classes;

import java.util.ArrayList;
import java.util.Comparator;

public abstract class Veiculo {
    public static final char[] TIPOS_VEICULO = {'C', 'P'};
    protected String sigla, nome;
    protected char tipo;
    protected double fatorImpacto;
    protected ArrayList<Publicacao> publicacoes = new ArrayList<>();
    protected ArrayList<Qualificacao> qualificacoes = new ArrayList<>();

    protected Veiculo(String sigla, String nome, char tipo, double fatorImpacto){
        this.sigla=sigla;
        this.nome=nome;
        this.tipo=tipo;
        this.fatorImpacto=fatorImpacto;
    }

    public void setPublicacao(Publicacao publicacao){
        publicacoes.add(publicacao);
    }

    public void setQualificacao(Qualificacao quali){
        qualificacoes.add(quali);
    }

    public ArrayList<Qualificacao> getQualificacoes(){
        return qualificacoes;
    }

    public char getTipo(){
        return tipo;
    }

    public String getNome(){
        return nome;
    }

    public double getFatorImpacto(){
        return fatorImpacto;
    }

    public Qualificacao getQualificacao(int ano){
        qualificacoes.sort(Comparator.comparingInt(Qualificacao::getAno));
        Qualificacao quali=null;

        //pega a quali correspondente ao ano da regra
        for(Qualificacao qualificacao : qualificacoes){
            if(ano >= qualificacao.getAno()){
                quali = qualificacao;
            }
        }

        return quali;
    }
}
