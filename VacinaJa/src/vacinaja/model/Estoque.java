package vacinaja.model;

public class Estoque {

    private Vacina vacina;
    private int doses;

    public Estoque(Vacina vacina, int doses) {
        this.vacina = vacina;
        this.doses = doses;
    }

    public void retirarDose() {
        doses--;
    }

    public Vacina getVacina() { return vacina; }
    public int getDoses() { return doses; }
}
