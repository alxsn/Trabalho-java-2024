package classes;
import java.util.ArrayList;
import java.time.LocalDate;

public class Docente {
    private long codigo;
    private String nome;
    private LocalDate dataNascimento;
    private LocalDate dataIngresso;
    private ArrayList<Ocorrencia> ocorrencias = new ArrayList<>();

    public Docente(long codigo, String nome, LocalDate dataNascimento, LocalDate dataIngresso){
        this.codigo=codigo;
        this.nome=nome;
        this.dataNascimento=dataNascimento;
        this.dataIngresso=dataIngresso;
    }

    public void setOcorrencias(Ocorrencia ocorrencia){
        ocorrencias.add(ocorrencia);
    }

    public String getNome(){
        return nome;
    }
}
