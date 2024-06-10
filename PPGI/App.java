import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import classes.Docente;
import classes.Ocorrencia;
import classes.Veiculo;
import io.Leitura;

public class App {
    public static void main(String[] args) throws Exception {
        Map<Long, Docente> docentes = new TreeMap<Long, Docente>();
        Map<String, Veiculo> veiculos = new TreeMap<>();
        
        
        try{
            Leitura.leDocentes(args[0], docentes);
            Leitura.leOcorrenciasDocentes(args[0], docentes);
            Leitura.leVeiculos(args[0], veiculos);

            // for(Map.Entry<Long, Docente> pair : docentes.entrySet()){
            //     System.out.println(pair.getValue().getNome());
            // }
        }catch(FileNotFoundException f){
            System.out.println("fnfe");
        }catch(NumberFormatException e){
            System.out.println("nfe");
        }catch(DateTimeParseException data){}
        

    }
}
