package classes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RegraPontuacao {
    private LocalDate inicioVigencia;
    private LocalDate fimVigencia;
    private String[] qualis1;
    private double[] pontosQualis1;
    private int qtdAnosPontos;
    private String[] qualis2;
    private int[] qtdMinimaArtigos;
    private int qtdAnosArtigo;
    private double qtdMinimaPontos;

    public RegraPontuacao(LocalDate inicioVigencia,
                          LocalDate fimVigencia,
                          String[] qualis1,
                          double[] pontosQualis1,
                          int qtdAnosPontos,
                          String[] qualis2,
                          int[] qtdMinimaArtigos,
                          int qtdAnosArtigo,
                          double qtdMinimaPontos){
        this.inicioVigencia=inicioVigencia;
        this.fimVigencia=fimVigencia;
        this.qualis1=qualis1;
        this.pontosQualis1=pontosQualis1;
        this.qtdAnosPontos=qtdAnosPontos;
        this.qualis2=qualis2;
        this.qtdMinimaArtigos=qtdMinimaArtigos;
        this.qtdAnosArtigo=qtdAnosArtigo;
        this.qtdMinimaPontos=qtdMinimaPontos;
    }
}