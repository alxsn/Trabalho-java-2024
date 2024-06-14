package io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import classes.Conferencia;
import classes.Docente;
import classes.Ocorrencia;
import classes.Periodico;
import classes.Publicacao;
import classes.PublicacaoConferencia;
import classes.PublicacaoPeriodico;
import classes.Qualificacao;
import classes.RegraPontuacao;
import classes.Veiculo;

public interface Leitura {
    public static void leDocentes(String caminho, Map<Long, Docente> docentes)
    throws FileNotFoundException, ParseException{
        Scanner scanner;
        String docentesCaminho = caminho.concat("/docentes.csv");
        scanner = new Scanner(new FileReader(docentesCaminho));

        //Consome o cabeçalho
        scanner.nextLine();

        String linha;
        String[] valores;        
        
        Docente docente;
        long codigo;
        String nome;
        LocalDate dataNascimento;
        LocalDate dataIngresso;

        DateTimeFormatter dataFormato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        while(scanner.hasNext()){
            linha = scanner.nextLine();
            valores = linha.split(";");
            //remove os espaços
            for(int i=0; i < valores.length; i++){
                valores[i]=valores[i].trim();
            }
            codigo = Long.parseLong(valores[0]);
            nome = valores[1];
            dataNascimento = LocalDate.parse(valores[2], dataFormato);
            dataIngresso = LocalDate.parse(valores[3], dataFormato);
            docente = new Docente(codigo, nome, dataNascimento, dataIngresso);
            docentes.put(codigo, docente);
        }
        scanner.close();
    }

    public static void leOcorrenciasDocentes(String caminho, Map<Long, Docente> docentes)
    throws FileNotFoundException, ParseException{
        Scanner scanner;
        String ocorrenciasCaminho = caminho.concat("/ocorrencias.csv");
        scanner = new Scanner(new FileReader(ocorrenciasCaminho));

        //Consome o cabeçalho
        scanner.nextLine();

        String linha;
        String[] valores;

        long codigo;
        Ocorrencia ocorrencia;
        String evento;
        LocalDate dataInicio, dataFim;

        DateTimeFormatter dataFormato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Docente docente;

        while(scanner.hasNext()){
            linha = scanner.nextLine();
            valores = linha.split(";");
            //remove os espaços
            for(int i=0; i < valores.length; i++){
                valores[i]=valores[i].trim();
            }
            codigo = Long.parseLong(valores[0]);
            evento = valores[1];
            dataInicio = LocalDate.parse(valores[2], dataFormato);
            dataFim = LocalDate.parse(valores[3], dataFormato);
            ocorrencia = new Ocorrencia(codigo, evento, dataInicio, dataFim);
            docente = docentes.get(codigo);
            docente.setOcorrencias(ocorrencia);
            ocorrencia.setDocente(docente);
        }
        scanner.close();
    }

    public static void leVeiculos(String caminho, Map<String, Veiculo> veiculos)
    throws FileNotFoundException, ParseException{
        Scanner scanner;
        String veiculosCaminho = caminho.concat("/veiculos.csv");
        scanner = new Scanner(new FileReader(veiculosCaminho));

        //Consome o cabeçalho
        scanner.nextLine();

        String linha;
        String[] valores;

        String sigla, nome, issn;
        char tipo;
        double fatorImpacto;
        Veiculo veiculo;

        NumberFormat numberFormat = NumberFormat.getInstance(Locale.of("pt", "BR"));

        while(scanner.hasNext()){
            linha = scanner.nextLine();
            valores = linha.split(";");
            //remove os espaços
            for(int i=0; i < valores.length; i++){
                valores[i]=valores[i].trim();
            }
            sigla = valores[0];
            nome = valores[1];
            tipo = valores[2].charAt(0);
            Number numero = numberFormat.parse(valores[3]);
            fatorImpacto = numero.doubleValue();
            if(tipo=='P'){
                issn = valores[4];
                veiculo = new Periodico(sigla, nome, tipo, fatorImpacto, issn);
            }
            else{
                veiculo = new Conferencia(sigla, nome, tipo, fatorImpacto);
            }
            veiculos.put(sigla, veiculo);
        }
        scanner.close();
    }

    public static void lePublicacoes(String caminho,
                                     Map<Long, Docente> docentes,
                                     Map<String, Veiculo> veiculos,
                                     ArrayList<Publicacao> publicacoes)
    throws FileNotFoundException, NumberFormatException{
        Scanner scanner;
        String publicacoesCaminho = caminho.concat("/publicacoes.csv");
        scanner = new Scanner(new FileReader(publicacoesCaminho));

        //Consome o cabeçalho
        scanner.nextLine();

        String linha;
        String[] valores;

        int ano;
        String siglaVeiculo;
        String titulo;
        Map<Long, Docente> autores;
        int numero;
        int volume;
        String local;
        int paginaInicial;
        int paginaFinal;

        String[] vetorAutores;
        Docente docente;
        long codigoAutor;
        Veiculo veiculo;
        Publicacao publicacao;

        while(scanner.hasNext()){
            linha = scanner.nextLine();
            valores = linha.split(";");
            //remove os espaços
            for(int i=0; i < valores.length; i++){
                valores[i]=valores[i].trim();
            }
            ano = Integer.parseInt(valores[0]);
            siglaVeiculo = valores[1];
            veiculo = veiculos.get(siglaVeiculo);
            titulo = valores[2];
            vetorAutores = valores[3].split(",");
            //remove os espaços
            for(int i=0; i < vetorAutores.length; i++){
                vetorAutores[i]=vetorAutores[i].trim();
            }
            //map dos autores
            autores = new TreeMap<>();
            for(String valor : vetorAutores){
                codigoAutor = Long.parseLong(valor);
                docente = docentes.get(codigoAutor);
                autores.put(codigoAutor, docente);
            }
            numero = Integer.parseInt(valores[4]);
            paginaInicial = Integer.parseInt(valores[7]);
            paginaFinal = Integer.parseInt(valores[8]);
            if(veiculo.getTipo()=='P'){//é periodico
                volume = Integer.parseInt(valores[5]);
                publicacao = new PublicacaoPeriodico(ano,
                                                     siglaVeiculo,
                                                     titulo,
                                                     autores,
                                                     numero,
                                                     volume,
                                                     paginaInicial,
                                                     paginaFinal,
                                                     veiculo);
            }            
            else{//é conferencia
                local = valores[6];
                publicacao = new PublicacaoConferencia(ano,
                                                       siglaVeiculo,
                                                       titulo,
                                                       autores,
                                                       numero,
                                                       local,
                                                       paginaInicial,
                                                       paginaFinal,
                                                       veiculo);
            }
            for(Map.Entry<Long, Docente> pair : autores.entrySet()){
                pair.getValue().setPublicacao(publicacao);
            }
            veiculo.setPublicacao(publicacao);
            publicacoes.add(publicacao);
        }
        scanner.close();
    }

    public static void leQualificacoes(String caminho, Map<String, Veiculo> veiculos)
    throws FileNotFoundException, NumberFormatException{
        Scanner scanner;
        String qualificacoesCaminho = caminho.concat("/qualis.csv");
        scanner = new Scanner(new FileReader(qualificacoesCaminho));

        //Consome o cabeçalho
        scanner.nextLine();

        String linha;
        String[] valores;

        int ano;
        String siglaVeiculo, quali;

        Veiculo veiculo;
        Qualificacao qualificacao;

        while(scanner.hasNext()){
            linha = scanner.nextLine();
            valores = linha.split(";");
            //remove os espaços
            for(int i=0; i < valores.length; i++){
                valores[i]=valores[i].trim();
            }
            ano = Integer.parseInt(valores[0]);
            siglaVeiculo = valores[1];
            veiculo = veiculos.get(siglaVeiculo);
            quali = valores[2];
            qualificacao = new Qualificacao(ano, siglaVeiculo, quali, veiculo);
            veiculo.setQualificacao(qualificacao);
        }
        scanner.close();
    }

    public static void leRegrasPontuacao(String caminho, ArrayList<RegraPontuacao> regrasPontuacao)
    throws FileNotFoundException, ParseException{
        Scanner scanner;
        String regrasPontuacaoCaminho = caminho.concat("/regras.csv");
        scanner = new Scanner(new FileReader(regrasPontuacaoCaminho));

        //Consome o cabeçalho
        scanner.nextLine();

        String linha;
        String[] valores;

        LocalDate inicioVigencia;
        LocalDate fimVigencia;
        DateTimeFormatter dataFormato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String[] qualis1;
        double[] pontosQualis1;
        int qtdAnosPontos;
        String[] qualis2;
        int[] qtdMinimaArtigos;
        int qtdAnosArtigo;
        double qtdMinimaPontos;

        RegraPontuacao regraPontuacao;
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.of("pt", "BR"));
        String[] pontosQualis1Aux;
        
        while(scanner.hasNext()){
            linha = scanner.nextLine();
            valores = linha.split(";");
            //remove os espaços
            for(int i=0; i < valores.length; i++){
                valores[i]=valores[i].trim();
            }

            inicioVigencia = LocalDate.parse(valores[0], dataFormato);
            fimVigencia = LocalDate.parse(valores[1], dataFormato);
            qualis1 = valores[2].split("-");
            //converte o vetor de string em vetor de double
            // pontosQualis1 = Arrays.stream(valores[3].split("-"))
            //                       .mapToDouble(Double::parseDouble)
            //                       .toArray();
            pontosQualis1 = new double[valores[3].split("-").length];
            pontosQualis1Aux = valores[3].split("-");
            for(int i=0; i < pontosQualis1Aux.length; i++){
                pontosQualis1Aux[i]=pontosQualis1Aux[i].trim();
            }          
            for(int i=0; i < pontosQualis1Aux.length; i++){
                Number numero = numberFormat.parse(pontosQualis1Aux[i]);
                pontosQualis1[i] = numero.doubleValue();//System.out.println(pontosQualis1[i]);
            }
            qtdAnosPontos = Integer.parseInt(valores[4]);
            qualis2 = valores[5].split("-");
            //converte o vetor de string em vetor de inteiros
            qtdMinimaArtigos = Arrays.stream(valores[6].split("-"))
                                     .mapToInt(Integer::parseInt)
                                     .toArray();
            qtdAnosArtigo = Integer.parseInt(valores[7]);
            Number numero = numberFormat.parse(valores[8]);
            qtdMinimaPontos = numero.doubleValue();

            regraPontuacao = new RegraPontuacao(inicioVigencia,
                                                fimVigencia,
                                                qualis1,
                                                pontosQualis1,
                                                qtdAnosPontos,
                                                qualis2,
                                                qtdMinimaArtigos,
                                                qtdAnosArtigo,
                                                qtdMinimaPontos);
            regrasPontuacao.add(regraPontuacao);
        }
        scanner.close();
    }
}