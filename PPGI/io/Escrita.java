package io;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import classes.Docente;
import classes.Publicacao;
import classes.RegraPontuacao;
import classes.Veiculo;

public interface Escrita {
    public static void escreveRecredenciamento(String caminho,
                                               int ano,
                                               Map<Long, Docente> docentes,
                                               Map<String, Veiculo> veiculos,
                                               ArrayList<RegraPontuacao> regrasPontuacao)
    throws FileNotFoundException{
        String recredenciamentoCaminho = caminho.concat("/saida/1-recredenciamento.csv");
        PrintWriter writer = new PrintWriter(recredenciamentoCaminho);

        RegraPontuacao regraPontuacao=null;
        regrasPontuacao.sort((d1, d2) -> d1.getInicioVigencia().compareTo(d2.getInicioVigencia()));
        for(int i=0; i < regrasPontuacao.size(); i++){
            if(i <= (regrasPontuacao.size()-2)){
                if(regrasPontuacao.get(i).getInicioVigencia().getYear() == ano &&
                   regrasPontuacao.get(i).
                   getInicioVigencia().
                   isBefore(regrasPontuacao.get(i+1).getInicioVigencia()) &&
                   regrasPontuacao.get(i+1).getInicioVigencia().getYear() == ano){
                    regraPontuacao = regrasPontuacao.get(i);
                }else if(regrasPontuacao.get(i).getInicioVigencia().getYear() == ano &&
                regrasPontuacao.get(i+1).getInicioVigencia().getYear() != ano){
                    regraPontuacao = regrasPontuacao.get(i);
                }
            }else{
                if(regrasPontuacao.get(i).getInicioVigencia().getYear() == ano){
                    regraPontuacao = regrasPontuacao.get(i);
                }
            }
        }
        System.out.println("O ano é "+regraPontuacao.getInicioVigencia());
        writer.println("Docente;Pontuação;Recredenciado?");

        Docente docente;
        for(Map.Entry<Long, Docente> pair : docentes.entrySet()){
            docente = pair.getValue();
            writer.println(docente.getNome()+
                           ";"+
                           docente.calculaPontuacao(regraPontuacao)+
                           ";"+
                           docente.ehRecredenciado(regraPontuacao));
        }

        writer.close();
    }

    public static void escrevePublicacoes(String caminho, ArrayList<Publicacao> publicacoes)
    throws FileNotFoundException{
        String publicacoesCaminho = caminho.concat("/saida/2-publicacoes.csv");
        PrintWriter writer = new PrintWriter(publicacoesCaminho);

        writer.close();
    }

    public static void escreveEstatisticas(String caminho)
    throws FileNotFoundException{
        String estatisticasCaminho = caminho.concat("/saida/3-estatisticas.csv");
        PrintWriter writer = new PrintWriter(estatisticasCaminho);

        writer.close();
    }
}
