package vacinaja.model;

public class Vacina {

    private int id;
    private String nome;
    private String fabricante;
    private int intervaloDias;
    private int faixaMin;
    private int faixaMax;
    private int dosesNecessarias;

    public Vacina(int id, String nome, String fabricante, int intervaloDias,
                  int faixaMin, int faixaMax, int dosesNecessarias) {
        this.id = id;
        this.nome = nome;
        this.fabricante = fabricante;
        this.intervaloDias = intervaloDias;
        this.faixaMin = faixaMin;
        this.faixaMax = faixaMax;
        this.dosesNecessarias = dosesNecessarias;
    }

    public boolean podeTomar(int idade) {
        return idade >= faixaMin && idade <= faixaMax;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getFabricante() { return fabricante; }
    public int getIntervaloDias() { return intervaloDias; }
    public int getFaixaMin() { return faixaMin; }
    public int getFaixaMax() { return faixaMax; }
    public int getDosesNecessarias() { return dosesNecessarias; }
}
