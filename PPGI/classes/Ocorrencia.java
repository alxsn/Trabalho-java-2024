package classes;

import java.time.LocalDate;

public class Ocorrencia {
    private long codigo;
    private String evento;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public Ocorrencia(long codigo, String evento, LocalDate dataInicio, LocalDate dataFim){
        this.codigo=codigo;
        this.evento=evento;
        this.dataInicio=dataInicio;
        this.dataFim=dataFim;
    }
}
