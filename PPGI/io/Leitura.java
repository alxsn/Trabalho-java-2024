package io;

import java.io.FileReader;
import java.io.IOException;
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
import exceptions.InconsistenciaException;

public interface Leitura {
    public static void leDocentes(String caminho, Map<Long, Docente> docentes)
    throws IOException, ParseException, InconsistenciaException{
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

            //se o docente já estiver cadastrado, lança uma exceção
            docente = docentes.get(codigo);
            if(docente != null){
                scanner.close();
                throw new InconsistenciaException("Código repetido para docente: "+
                                                    codigo+
                                                    ".");
            }

            nome = valores[1];
            dataNascimento = LocalDate.parse(valores[2], dataFormato);
            dataIngresso = LocalDate.parse(valores[3], dataFormato);
            docente = new Docente(codigo, nome, dataNascimento, dataIngresso);
            docentes.put(codigo, docente);
        }
        scanner.close();
    }

    public static void leOcorrenciasDocentes(String caminho, Map<Long, Docente> docentes)
    throws IOException, ParseException, InconsistenciaException{
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

            //se docente não estiver cadastrado, lança uma exceção
            docente = docentes.get(codigo);
            if(docente == null){
                scanner.close();
                throw new InconsistenciaException("Código de docente não definido usado na ocorrência: "+
                                                  codigo+
                                                  ":"+
                                                  evento+
                                                  ".");
            }

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
    throws IOException, ParseException, InconsistenciaException{
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
        boolean verificaTipo;

        while(scanner.hasNext()){
            linha = scanner.nextLine();
            valores = linha.split(";");

            //remove os espaços
            for(int i=0; i < valores.length; i++){
                valores[i]=valores[i].trim();
            }

            sigla = valores[0];

            //verifica se o veiculo ja estiver cadastrado, lança uma exceção
            veiculo = veiculos.get(sigla);
            if(veiculo != null){
                scanner.close();
                throw new InconsistenciaException("Código repetido para veiculo: "+
                                                    sigla+
                                                    ".");
            }

            nome = valores[1];
            tipo = valores[2].charAt(0);

            //verifica se o tipo do veiculo é um dos especificados, senão lança uma exceção
            verificaTipo = false;
            for(char c : Veiculo.TIPOS_VEICULO){
                if(tipo == c){
                    verificaTipo = true;
                }
            }
            if(verificaTipo == false){
                scanner.close();
                throw new InconsistenciaException("Tipo de veículo desconhecido para veículo "+
                                                  sigla+
                                                  ": "+
                                                  tipo+
                                                  ".");
            }

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
    throws IOException, ParseException, InconsistenciaException{
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
        TreeMap<Long, Docente> autores;
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

            //se o veiculo não estiver cadastrado, lança uma exceção
            if(veiculo == null){
                scanner.close();
                throw new InconsistenciaException("Sigla de veículo não definida usada na publicação \""+
                                                    titulo+
                                                    "\": "+
                                                    siglaVeiculo+
                                                    ".");
            }

            vetorAutores = valores[3].split(",");

            //remove os espaços
            for(int i=0; i < vetorAutores.length; i++){
                vetorAutores[i]=vetorAutores[i].trim();
            }

            //map dos autores
            autores = new TreeMap<>();
            for(String valor : vetorAutores){
                codigoAutor = Long.parseLong(valor);

                //se docente não estiver cadastrado, lança uma exceção
                docente = docentes.get(codigoAutor);
                if(docente == null){
                    scanner.close();
                    throw new InconsistenciaException("Código de docente não definido usado na publicação \""+
                                                      titulo+
                                                      "\": "+
                                                      codigoAutor+
                                                      ".");
                }
                
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
    throws IOException, ParseException, InconsistenciaException{
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

        boolean verificaQuali;

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

            //se o veiculo não estiver cadastrado, lança uma exceção
            if(veiculo == null){
                scanner.close();
                throw new InconsistenciaException("Sigla de veículo não definida usada na qualificação do ano \""+
                                                    ano+
                                                    "\": "+
                                                    siglaVeiculo+
                                                    ".");
            }

            quali = valores[2];

            //verifica se o quali é um dos especificados, senão lança, uma exceção
            verificaQuali = false;
            for(String q : Qualificacao.QUALIS){
                if(quali.equals(q)){
                    verificaQuali = true;
                }
            }
            if(verificaQuali == false){
                scanner.close();
                throw new InconsistenciaException("Qualis desconhecido para qualificação do veículo "+
                                                  siglaVeiculo+
                                                  " no ano "+
                                                  ano+
                                                  ": "+
                                                  quali+
                                                  ".");
            }

            qualificacao = new Qualificacao(ano, siglaVeiculo, quali, veiculo);
            veiculo.setQualificacao(qualificacao);
        }
        scanner.close();
    }

    public static void leRegrasPontuacao(String caminho, ArrayList<RegraPontuacao> regrasPontuacao)
    throws IOException, ParseException, InconsistenciaException{
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
        boolean verificaQuali;
        
        while(scanner.hasNext()){
            linha = scanner.nextLine();
            valores = linha.split(";");

            //remove os espaços
            for(int i=0; i < valores.length; i++){
                valores[i]=valores[i].trim();
            }

            inicioVigencia = LocalDate.parse(valores[0], dataFormato);
            fimVigencia = LocalDate.parse(valores[1], dataFormato);

            //verifica sobreposição de datas, se sobrepostas, lança uma exceção
            for(RegraPontuacao r : regrasPontuacao){
                if((
                    (inicioVigencia.isBefore(r.getInicioVigencia()) || inicioVigencia.isEqual(r.getInicioVigencia())) &&
                    (fimVigencia.isAfter(r.getFimVigencia()) || fimVigencia.isEqual(r.getFimVigencia()))
                   ) ||
                   (
                    (inicioVigencia.isAfter(r.getInicioVigencia()) || inicioVigencia.isEqual(r.getInicioVigencia())) &&
                    (fimVigencia.isBefore(r.getFimVigencia())) || fimVigencia.isEqual(r.getFimVigencia())
                   ) ||
                   (
                    (inicioVigencia.isBefore(r.getInicioVigencia()) || inicioVigencia.isEqual(r.getInicioVigencia())) &&
                    (fimVigencia.isAfter(r.getInicioVigencia()))
                   ) ||
                   (
                    (inicioVigencia.isBefore(r.getFimVigencia())) &&
                    (fimVigencia.isAfter(r.getFimVigencia()) || fimVigencia.isEqual(r.getFimVigencia()))
                   )
                  ){

                    scanner.close();
                    throw new InconsistenciaException("Múltiplas regras de pontuação para o mesmo período: "+
                                                      inicioVigencia.format(dataFormato)+
                                                      " : "+
                                                      fimVigencia.format(dataFormato)+
                                                      ".");
                }
            }

            qualis1 = valores[2].split("-");

            //remove os espaços
            for(int i=0; i< qualis1.length; i++){
                qualis1[i] = qualis1[i].trim();
            }

            for(String quali : qualis1){
                //verifica se o quali é um dos especificados, senão, lança uma exceção
                verificaQuali = false;
                for(String q : Qualificacao.QUALIS){
                    if(quali.equals(q)){
                        verificaQuali = true;
                    }
                }
                if(verificaQuali == false){
                    scanner.close();
                    throw new InconsistenciaException("Qualis desconhecido para regras de "+
                                                      inicioVigencia.format(dataFormato)+
                                                      ": "+
                                                      quali);
                }
            }

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
                pontosQualis1[i] = numero.doubleValue();
            }
            qtdAnosPontos = Integer.parseInt(valores[4]);
            qualis2 = valores[5].split("-");

            //remove os espaços
            for(int i=0; i< qualis2.length; i++){
                qualis2[i] = qualis2[i].trim();
            }

            for(String quali : qualis2){
                //verifica se o quali é um dos especificados, senão, lança uma exceção
                verificaQuali = false;
                for(String q : Qualificacao.QUALIS){
                    if(quali.equals(q)){
                        verificaQuali = true;
                    }
                }
                if(verificaQuali == false){
                    scanner.close();
                    throw new InconsistenciaException("Qualis desconhecido para regras de"+
                                                      inicioVigencia+
                                                      ":"+
                                                      quali);
                }
            }

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