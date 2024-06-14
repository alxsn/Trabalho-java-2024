package classes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

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
        //define a pontuação de cada quali
        Map<String, Double> pontuacaoQualis = new TreeMap<>();
        String[] qualis = Qualificacao.qualis;
        String[] qualis1 = regraPontuacao.getQualis1();
        double[] pontosQualis1 = regraPontuacao.getPontosQualis1();

        for(int i=0, j=0; i < qualis.length; i++){
            if(j < (qualis1.length - 1)){//não é o ultimo qualis1
                if(!qualis[i].equals(qualis1[j+1])){//são diferentes
                    pontuacaoQualis.put(qualis[i], pontosQualis1[j]);
                }else{//são iguais
                    pontuacaoQualis.put(qualis[i], pontosQualis1[j+1]);
                    j++;
                }
            }else{//é o ultimo qualis1
                pontuacaoQualis.put(qualis[i], pontosQualis1[j]);
            }
        }

        //calcula pontuação
        int anoRegra = regraPontuacao.getInicioVigencia().getYear();
        double pontuacao=0;
        int anoPublicacao;
        ArrayList<Qualificacao> qualificacoes;
        String quali=null;
        double pontuacaoQuali=0;

        for(Publicacao publicacao : this.publicacoes){
            if(publicacao.getAno() >= (anoRegra-2) && publicacao.getAno() < anoRegra){
                anoPublicacao = publicacao.getAno();
                qualificacoes = publicacao.getVeiculo().getQualificacoes();
                qualificacoes.sort(Comparator.comparingInt(Qualificacao::getAno));

                for(Qualificacao qualificacao : qualificacoes){
                    if(anoRegra >= qualificacao.getAno()){
                        quali = qualificacao.getQuali();
                    }
                }

                if(quali != null){
                    pontuacaoQuali = pontuacaoQualis.get(quali);
                }

                // if(nome.equals("Andy Samberg")){
                //     System.out.println(pontuacaoQuali+
                //                        " "+
                //                        publicacao.getAno()+
                //                        " "+quali);
                // }
                pontuacao += pontuacaoQuali;
            }
        }        

        return pontuacao;
    }

    public String ehRecredenciado(RegraPontuacao regraPontuacao){
        String evento;

        //função lambda para ordenar as ocorrencias em ordem crescente de data
        ocorrencias.sort((d1, d2) -> d1.getDataInicio().compareTo(d2.getDataInicio()));

        //Ocorrencia ocorrencia = ocorrencias.getLast();
        LocalDate fimVigenciaRegra = regraPontuacao.getFimVigencia();
        LocalDate dataFimOcorrencia;
        for(Ocorrencia ocorrencia : ocorrencias){
            dataFimOcorrencia = ocorrencia.getDataFim();

            if(dataFimOcorrencia.getYear() == fimVigenciaRegra.getYear())
            if(ocorrencia.getEvento().equals("Licença Maternidade")){
                if(dataFimOcorrencia.isBefore(fimVigenciaRegra.minusYears(1))){
                    evento = "Licença Maternidade";
                }
            }
        }
        
        return "";
    }

    public ArrayList<Publicacao> getPublicacoes(){
        return publicacoes;
    }
}
