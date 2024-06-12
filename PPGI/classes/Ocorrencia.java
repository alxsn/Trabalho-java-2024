package classes;

import java.time.LocalDate;

import javax.swing.plaf.metal.OceanTheme;

public class Ocorrencia {
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
}
