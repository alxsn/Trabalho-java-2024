package classes;

import java.time.LocalDate;

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

    public LocalDate getInicioVigencia(){
        return inicioVigencia;
    }

    public LocalDate getFimVigencia(){
        return fimVigencia;
    }

    public String[] getQualis1(){
        return qualis1;
    }

    public double[] getPontosQualis1(){
        return pontosQualis1;
    }

    public int getQtdAnosPontos(){
        return qtdAnosPontos;
    }

    public String[] getQualis2(){
        return qualis2;
    }

    public int[] getQtdMinimaArtigos(){
        return qtdMinimaArtigos;
    }

    public int getQtdAnosArtigo(){
        return qtdAnosArtigo;
    }

    public double getQtdMinimaPontos(){
        return qtdMinimaPontos;
    }
}
