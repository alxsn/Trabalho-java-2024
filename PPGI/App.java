import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import classes.Docente;
import classes.Publicacao;
import classes.RegraPontuacao;
import classes.Veiculo;
import exceptions.InconsistenciaException;
import io.Escrita;
import io.Leitura;

public class App {
        
    public static void main(String[] args) throws Exception {
        Map<Long, Docente> docentes = new TreeMap<>();
        Map<String, Veiculo> veiculos = new TreeMap<>();
        ArrayList<RegraPontuacao> regrasPontuacao = new ArrayList<>();
        ArrayList<Publicacao> publicacoes = new ArrayList<>();

        
        try{
            Escrita.criaArquivos(args[0]);
            
            Leitura.leDocentes(args[0], docentes);
            Leitura.leOcorrenciasDocentes(args[0], docentes);
            Leitura.leVeiculos(args[0], veiculos);
            Leitura.lePublicacoes(args[0], docentes, veiculos, publicacoes);
            Leitura.leQualificacoes(args[0], veiculos);
            Leitura.leRegrasPontuacao(args[0], regrasPontuacao);

            Scanner scanner = new Scanner(System.in);
            //System.out.println("Digite um ano: ");
            int ano = scanner.nextInt();

            Escrita.escreveRecredenciamento(args[0], ano, docentes, veiculos, regrasPontuacao);
            Escrita.escrevePublicacoes(args[0], publicacoes, ano);
            Escrita.escreveEstatisticas(args[0], publicacoes, ano);

            scanner.close();

        }catch(IOException e){
            System.out.println("Erro de I/O");
        }catch(ParseException e){
            System.out.println("Erro de formatação");
        }catch(InconsistenciaException e){
            System.out.println(e.getMessage());
        }
    }
}
