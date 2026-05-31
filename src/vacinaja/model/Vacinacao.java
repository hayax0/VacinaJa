package vacinaja.model;

public class Vacinacao {

    private int id;
    private Cidadao cidadao;
    private Vacina vacina;
    private Posto posto;
    private int dose;
    private String data;

    public Vacinacao(int id, Cidadao cidadao, Vacina vacina, Posto posto, int dose, String data) {
        this.id = id;
        this.cidadao = cidadao;
        this.vacina = vacina;
        this.posto = posto;
        this.dose = dose;
        this.data = data;
    }

    public int getId() { return id; }
    public Cidadao getCidadao() { return cidadao; }
    public Vacina getVacina() { return vacina; }
    public Posto getPosto() { return posto; }
    public int getDose() { return dose; }
    public String getData() { return data; }
}
