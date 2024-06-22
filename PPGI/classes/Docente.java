package classes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    public ArrayList<Publicacao> getPublicacoes(){
        return publicacoes;
    }

    public double calculaPontuacao(int ano, RegraPontuacao regraPontuacao){
        //mapa com a pontuação de cada quali
        Map<String, Double> pontuacaoQualis = new TreeMap<>();
        //quais são os qualis
        String[] qualis = Qualificacao.QUALIS;

        String[] qualis1 = regraPontuacao.getQualis1();
        double[] pontosQualis1 = regraPontuacao.getPontosQualis1();

        //define a pontuação de cada quali
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
        ArrayList<Qualificacao> qualificacoes;
        String quali=null;
        double pontuacaoQuali=0;

        for(Publicacao publicacao : this.publicacoes){
            if(publicacao.getAno() >= (ano-regraPontuacao.getQtdAnosPontos()) &&
               publicacao.getAno() < ano){
                
                qualificacoes = publicacao.getVeiculo().getQualificacoes();

                //ordena as qualificaçoes por ano
                qualificacoes.sort(Comparator.comparingInt(Qualificacao::getAno));

                //pega a quali correspondente ao ano da regra
                for(Qualificacao qualificacao : qualificacoes){
                    if(anoRegra >= qualificacao.getAno()){
                        quali = qualificacao.getQuali();
                    }
                }

                if(quali != null){
                    pontuacaoQuali = pontuacaoQualis.get(quali);
                }

                pontuacao += pontuacaoQuali;
            }
        }        

        return pontuacao;
    }

    public String ehRecredenciado(int ano, RegraPontuacao regraPontuacao){
        String evento=null;
        String dataString = "01/01/".concat(Integer.toString(regraPontuacao.getFimVigencia().getYear()));
        DateTimeFormatter dataFormato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data = LocalDate.parse(dataString, dataFormato);

        //função lambda para ordenar as ocorrencias em ordem crescente de data
        ocorrencias.sort((d1, d2) -> d1.getDataInicio().compareTo(d2.getDataInicio()));

        LocalDate fimVigenciaRegra = regraPontuacao.getFimVigencia();
        LocalDate dataInicioOcorrencia, dataFimOcorrencia;
        double pontuacao;
        boolean quantidadeMinimaPublicacoes;

        for(Ocorrencia ocorrencia : ocorrencias){
            dataInicioOcorrencia = ocorrencia.getDataInicio();
            dataFimOcorrencia = ocorrencia.getDataFim();           

            if(! dataInicioOcorrencia.isAfter(fimVigenciaRegra)){
                if(ocorrencia.getEvento().equals("Licença Maternidade")){
                    if(dataFimOcorrencia.isAfter(fimVigenciaRegra.minusYears(1))){
                        evento = "Licença Maternidade";
                    }
                }else if(ocorrencia.getEvento().equals("Coordenador")){
                    if(dataFimOcorrencia.getYear() >= fimVigenciaRegra.getYear()){                
                        evento = "Coordenador";
                    }
                }else if(ocorrencia.getEvento().equals("Bolsista CNPq")){
                    if(dataFimOcorrencia.getYear() >= fimVigenciaRegra.getYear()){             
                        evento = "Bolsista CNPq";
                    }
                }
            }
        }

        if(evento == null){
            //verifica se o docente entrou no programa a menos de 2 anos
            if(dataIngresso.isAfter(data.minusYears(2)) ||
               dataIngresso.isEqual(data.minusYears(2))){
                evento = "PPJ";
            }
            //verifica se o docente possui 60 anos ou mais
            else if(ChronoUnit.YEARS.between(dataNascimento, data) >= 60){
                evento = "PPS";
            }else{
                pontuacao = calculaPontuacao(ano, regraPontuacao);
                quantidadeMinimaPublicacoes = verificaQuantidadeMinimaPublicacoes(ano, regraPontuacao);

                //verifica se o docente alcançou pontuação minima e quantidades minimas de publicações nos estratos
                if(pontuacao >= regraPontuacao.getQtdMinimaPontos() &&
                   quantidadeMinimaPublicacoes == true){
                    evento = "Sim";
                }else{
                    evento = "Não";
                }
            }
        }
        
        return evento;
    }

    public boolean verificaQuantidadeMinimaPublicacoes(int ano, RegraPontuacao regraPontuacao){        
        String[] qualis = Qualificacao.QUALIS;

        String[] qualis2 = regraPontuacao.getQualis2();
        int[] qtdMinimaArtigos = regraPontuacao.getQtdMinimaArtigos();

        int[] qtdArtigosPublicadosPorEstrato = new int[qtdMinimaArtigos.length];

        //inicializa as listas
        ArrayList<ArrayList<String>> estratos = new ArrayList<>();
        for(String q : qualis2){
            estratos.add(new ArrayList<>());
        }

        //define os estratos
        for(int i=0, j=0; i < qualis.length; i++){
            if(j < (qualis2.length - 1)){//não é o ultimo qualis2
                //compara com o proximo qualis2
                if(!qualis[i].equals(qualis2[j+1])){//são diferentes
                    estratos.get(j).add(qualis[i]);
                    //pontuacaoQualis.put(qualis[i], pontosQualis1[j]);
                }else{//são iguais
                    //pontuacaoQualis.put(qualis[i], pontosQualis1[j+1]);
                    estratos.get(j+1).add(qualis[i]);
                    j++;
                }
            }else{//é o ultimo qualis1
                //pontuacaoQualis.put(qualis[i], pontosQualis1[j]);
                estratos.get(j).add(qualis[i]);
            }
        }

        ArrayList<String> estrato;
        int anoRegra = regraPontuacao.getInicioVigencia().getYear();
        ArrayList<Qualificacao> qualificacoes;
        String quali=null;

        for(Publicacao publicacao : publicacoes){
            if((publicacao.getAno() >= (ano - regraPontuacao.getQtdAnosArtigo())) &&
               (publicacao.getAno() < ano) &&
                publicacao.getVeiculo().getTipo() == 'P'){
                    
                qualificacoes = publicacao.getVeiculo().getQualificacoes();

                //ordena as qualificaçoes por ano
                qualificacoes.sort(Comparator.comparingInt(Qualificacao::getAno));

                //pega a quali correspondente ao ano da regra
                for(Qualificacao qualificacao : qualificacoes){
                    if(anoRegra >= qualificacao.getAno()){
                        quali = qualificacao.getQuali();
                    }
                }

                //verifica a qual estrato pertence a quali e soma 1 a quantidade de artigos nesse estrato
                for(int i=0; i < estratos.size(); i++){
                    estrato = estratos.get(i);
                    for(int j=0; j < estrato.size(); j++){
                        if(quali.equals(estrato.get(j))){
                            qtdArtigosPublicadosPorEstrato[i] += 1;
                        }
                    }
                }
            }
        }

        //verifica se o docente alcançou a quantidade minima de publicações
        for(int i=0; i < qtdArtigosPublicadosPorEstrato.length; i++){
            if(qtdArtigosPublicadosPorEstrato[i] < qtdMinimaArtigos[i]){
                return false;
            }
        }
        return true;
    }
}
