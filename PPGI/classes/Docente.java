package classes;
import java.util.ArrayList;
import java.time.LocalDate;

public class Docente {
    private long codigo;
    private String nome;
    private LocalDate dataNascimento;
    private LocalDate dataIngresso;
    private ArrayList<Ocorrencia> ocorrencias = new ArrayList<>();
    private ArrayList<Publicacao> publicacoes = new ArrayList<>();

    public Docente(long codigo, String nome, LocalDate dataNascimento, LocalDate dataIngresso){
        this.codigo=codigo;
        this.nome=nome;
        this.dataNascimento=dataNascimento;
        this.dataIngresso=dataIngresso;
    }

    public void setOcorrencias(Ocorrencia ocorrencia){
        ocorrencias.add(ocorrencia);
    }

    public ArrayList<Ocorrencia> getOcorrencias(){
        return ocorrencias;
    }

    public void setPublicacao(Publicacao publicacao){
        this.publicacoes.add(publicacao);
    }

    public String getNome(){
        return nome;
    }

    public double calculaPontuacao(RegraPontuacao regraPontuacao){


        return 0.0;
    }

    public String ehRecredenciado(){
        return "";
    }
}
