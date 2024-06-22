package classes;

public class Qualificacao {
    public static final String[] QUALIS = {"A1", "A2", "A3", "A4", "B1", "B2", "B3", "B4", "B5", "C"};
    private int ano;
    private String siglaVeiculo;
    private String quali;
    private Veiculo veiculo;

    public Qualificacao(int ano, String siglaVeiculo, String quali, Veiculo veiculo){
        this.ano=ano;
        this.siglaVeiculo=siglaVeiculo;
        this.quali=quali;
        this.veiculo=veiculo;
    }

    public int getAno(){
        return ano;
    }

    public String getQuali(){
        return quali;
    }
}
