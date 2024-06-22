package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import classes.Docente;
import classes.Publicacao;
import classes.Qualificacao;
import classes.RegraPontuacao;
import classes.Veiculo;
import utils.MapValueComparatorNomeDocente;

public interface Escrita {
    public static void criaArquivos(String caminho)throws IOException{
        File arquivo;

        String recredenciamentoCaminho = caminho.concat("/saida/1-recredenciamento.csv");
        arquivo = new File(recredenciamentoCaminho);
        arquivo.createNewFile();

        String publicacoesCaminho = caminho.concat("/saida/2-publicacoes.csv");
        arquivo = new File(publicacoesCaminho);
        arquivo.createNewFile();

        String estatisticasCaminho = caminho.concat("/saida/3-estatisticas.csv");
        arquivo = new File(estatisticasCaminho);
        arquivo.createNewFile();
    }

    public static void escreveRecredenciamento(String caminho,
                                               int ano,
                                               Map<Long, Docente> docentes,
                                               Map<String, Veiculo> veiculos,
                                               ArrayList<RegraPontuacao> regrasPontuacao)
    throws FileNotFoundException{
        String recredenciamentoCaminho = caminho.concat("/saida/1-recredenciamento.csv");
        PrintWriter writer = new PrintWriter(recredenciamentoCaminho);

        RegraPontuacao regraPontuacao=null;

        //ordena as datas de forma crescente
        regrasPontuacao.sort((d1, d2) -> d1.getInicioVigencia().compareTo(d2.getInicioVigencia()));

        //define o ano começando em primeiro de janeiro
        String dataString = "01/01/".concat(Integer.toString(ano));
        DateTimeFormatter dataFormato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate data = LocalDate.parse(dataString, dataFormato);

        //formata o numero para o padrão brasileiro com uma casa decimal
        DecimalFormatSymbols s = new DecimalFormatSymbols(Locale.of("pt", "BR"));
        DecimalFormat df = new DecimalFormat("0.0", s);

        //escolhe a regra com base no ano
        for(RegraPontuacao regra : regrasPontuacao){
            if((data.isAfter(regra.getInicioVigencia()) || data.isEqual(regra.getInicioVigencia()))&&
                (data.isBefore(regra.getFimVigencia()) || data.isEqual(regra.getFimVigencia()))){
                regraPontuacao = regra;
            }
        }

        //ordena docentes
        MapValueComparatorNomeDocente ordenaNomeDocente = new MapValueComparatorNomeDocente(docentes);
        TreeMap<Long, Docente> d = new TreeMap<Long, Docente>(ordenaNomeDocente);
        d.putAll(docentes);

        writer.println("Docente;Pontuação;Recredenciado?");

        Docente docente;
        for(Map.Entry<Long, Docente> pair : d.entrySet()){
            docente = pair.getValue();
            writer.println(docente.getNome()+
                           ";"+
                           df.format(docente.calculaPontuacao(ano, regraPontuacao))+
                           ";"+
                           docente.ehRecredenciado(ano, regraPontuacao));
        }

        writer.close();
    }

    public static void escrevePublicacoes(String caminho,
                                          ArrayList<Publicacao> publicacoes,
                                          int ano)
    throws FileNotFoundException{
        String publicacoesCaminho = caminho.concat("/saida/2-publicacoes.csv");
        PrintWriter writer = new PrintWriter(publicacoesCaminho);

        //formata o numero para o padrão brasileiro com 3 casas decimais
        DecimalFormatSymbols s = new DecimalFormatSymbols(Locale.of("pt", "BR"));
        DecimalFormat df = new DecimalFormat("0.000", s);

        ArrayList<Docente> autores;

        //ordena as publicacoes
        Collections.sort(publicacoes, new Comparator<Publicacao>() {
            @Override
            public int compare(Publicacao p1, Publicacao p2){
                if(p1.getVeiculo().getQualificacao(ano).getQuali().compareTo(p2.getVeiculo().
                                                                             getQualificacao(ano).
                                                                             getQuali()) != 0){
                    return p1.getVeiculo().getQualificacao(ano).getQuali().compareTo(p2.getVeiculo().
                                                                                     getQualificacao(ano).
                                                                                     getQuali());
                }
                else{
                    if(p1.getAno() != p2.getAno()){
                        if(p1.getAno() < p2.getAno()){
                            return 1;
                        }
                        else{
                            return -1;
                        }
                    }
                    else{
                        if(p1.getSiglaVeiculo().compareTo(p2.getSiglaVeiculo()) != 0){
                            return p1.getSiglaVeiculo().compareTo(p2.getSiglaVeiculo());
                        }
                        else{
                            return p1.getTitulo().compareTo(p2.getTitulo());
                        }
                    }
                }
            }
        });

        writer.println("Ano;Sigla Veículo;Veículo;Qualis;Fator de Impacto;Título;Docentes");

        for(Publicacao publicacao : publicacoes){
            writer.print(publicacao.getAno()+
                           ";"+
                           publicacao.getSiglaVeiculo()+
                           ";"+
                           publicacao.getVeiculo().getNome()+
                           ";"+
                           publicacao.getVeiculo().getQualificacoes().
                           get(publicacao.getVeiculo().getQualificacoes().size()-1).getQuali()+
                           ";"+
                           df.format(publicacao.getVeiculo().getFatorImpacto())+
                           ";"+
                           publicacao.getTitulo()+
                           ";");

            autores = new ArrayList<>(publicacao.getAutores().values());

            for(int i=0; i < autores.size(); i++){
                writer.print(autores.get(i).getNome());
                if(i < autores.size()-1){
                    writer.print(",");
                }
            }

            writer.println();
        }

        writer.close();
    }

    public static void escreveEstatisticas(String caminho, ArrayList<Publicacao> publicacoes, int ano)
    throws FileNotFoundException{
        String estatisticasCaminho = caminho.concat("/saida/3-estatisticas.csv");
        PrintWriter writer = new PrintWriter(estatisticasCaminho);

        String quali;
        int[] qtdArtigos = new int[Qualificacao.QUALIS.length];
        double[] mediaArtigosDocente = new double[Qualificacao.QUALIS.length];;

        writer.println("Qualis;Qtd. Artigos;Média Artigos / Docente");

        for(Publicacao publicacao : publicacoes){
            quali = publicacao.getVeiculo().getQualificacao(ano).getQuali();

            for(int i=0; i < Qualificacao.QUALIS.length; i++){
                if(quali.equals(Qualificacao.QUALIS[i])){
                    qtdArtigos[i] += 1;
                    mediaArtigosDocente[i] += 1.0/publicacao.getAutores().size();
                }
            }
        }

        //formata o numero para o padrão brasileiro com 2 casas decimais
        DecimalFormatSymbols s = new DecimalFormatSymbols(Locale.of("pt", "BR"));
        DecimalFormat df = new DecimalFormat("0.00", s);

        for(int i=0; i < Qualificacao.QUALIS.length; i++){
            writer.print(Qualificacao.QUALIS[i]+
                               ";"+
                               qtdArtigos[i]+
                               ";");
            
            if(qtdArtigos[i] == 0){
                writer.println(df.format(0));
            }
            else{
                writer.println(df.format(mediaArtigosDocente[i]));
            }
        }

        writer.close();
    }
}
