package classes;

public class Periodico extends Veiculo{
    private String issn;

    public Periodico(String sigla, String nome, char tipo, double fatorImpacto, String issn){
        super(sigla, nome, tipo, fatorImpacto);
        this.issn=issn;
    }
}
