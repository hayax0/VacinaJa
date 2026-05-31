package vacinaja.model;

public class Posto {

    private int id;
    private String nome;
    private String bairro;
    private String endereco;
    private Estoque[] estoques;
    private int qtdEstoques;

    public Posto(int id, String nome, String bairro, String endereco) {
        this.id = id;
        this.nome = nome;
        this.bairro = bairro;
        this.endereco = endereco;
        this.estoques = new Estoque[10];
        this.qtdEstoques = 0;
    }

    public void adicionarEstoque(Vacina vacina, int doses) {
        if (qtdEstoques < estoques.length) {
            estoques[qtdEstoques] = new Estoque(vacina, doses);
            qtdEstoques++;
        }
    }

    public Estoque getEstoque(Vacina vacina) {
        for (int i = 0; i < qtdEstoques; i++) {
            if (estoques[i].getVacina().getId() == vacina.getId()) {
                return estoques[i];
            }
        }
        return null;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getBairro() { return bairro; }
    public String getEndereco() { return endereco; }
}
