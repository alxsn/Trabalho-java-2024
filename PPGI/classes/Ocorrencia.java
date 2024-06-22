package classes;

import java.time.LocalDate;

public class Ocorrencia {
    public static final String[] TIPOS_OCORRENCIA = {"Coordenador", "Licença Maternidade", "Bolsista CNPq"};
    private long codigo;
    private String evento;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Docente docente;

    public Ocorrencia(long codigo, String evento, LocalDate dataInicio, LocalDate dataFim){
        this.codigo=codigo;
        this.evento=evento;
        this.dataInicio=dataInicio;
        this.dataFim=dataFim;
    }

    public void setDocente(Docente docente){
        this.docente=docente;
    }

    public LocalDate getDataInicio(){
        return dataInicio;
    }

    public String getEvento(){
        return evento;
    }

    public LocalDate getDataFim(){
        return dataFim;
    }
}
