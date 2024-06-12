import java.io.FileNotFoundException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import classes.Docente;
import classes.Publicacao;
import classes.RegraPontuacao;
import classes.Veiculo;
import io.Escrita;
import io.Leitura;

public class App {
    // public enum qualis{A1, A2, A3, A4, B1, B2, B3, B4, B5, C};
    // public static final String[] q = {"A1", "A2", "A3", "A4", "B1", "B2", "B3", "B4", "B5", "C"};
        
    public static void main(String[] args) throws Exception {
        Map<Long, Docente> docentes = new TreeMap<Long, Docente>();
        Map<String, Veiculo> veiculos = new TreeMap<>();
        Map<Integer, RegraPontuacao> regrasPontuacao = new TreeMap<>();
        ArrayList<Publicacao> publicacoes = new ArrayList<>();

        
        try{
            Leitura.leDocentes(args[0], docentes);
            Leitura.leOcorrenciasDocentes(args[0], docentes);
            Leitura.leVeiculos(args[0], veiculos);
            Leitura.lePublicacoes(args[0], docentes, veiculos, publicacoes);
            Leitura.leQualificacoes(args[0], veiculos);
            Leitura.leRegrasPontuacao(args[0], regrasPontuacao);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Digite um ano: ");
            int ano = scanner.nextInt();

            Escrita.escreveRecredenciamento(args[0], ano, docentes, veiculos, regrasPontuacao);
            Escrita.escrevePublicacoes(args[0], publicacoes);
            Escrita.escreveEstatisticas(args[0]);

            scanner.close();

            // for(Map.Entry<Long, Docente> pair : docentes.entrySet()){
            //     System.out.println(pair.getValue().getNome());
            // }
        }catch(FileNotFoundException f){
            System.out.println("fnfe");
        }catch(NumberFormatException e){
            System.out.println(e);
        }catch(DateTimeParseException data){

        }
    }
}
