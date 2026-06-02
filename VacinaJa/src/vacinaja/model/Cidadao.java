package vacinaja.model;

public class Cidadao {

    private int id;
    private String nome;
    private String cpf;
    private String dataNascimento;
    private String bairro;
    private Vacinacao[] cartao;
    private int qtdVacinacoes;

    public Cidadao(int id, String nome, String cpf, String dataNascimento, String bairro) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.bairro = bairro;
        this.cartao = new Vacinacao[50];
        this.qtdVacinacoes = 0;
    }

    public void addVacinacao(Vacinacao v) {
        if (qtdVacinacoes < cartao.length) {
            cartao[qtdVacinacoes] = v;
            qtdVacinacoes++;
        }
    }

    public int getIdade() {
        String[] p = dataNascimento.split("/");
        int ano = Integer.parseInt(p[2]);
        return 2026 - ano;
    }

    public int dosesTomadas(int vacinaId) {
        int total = 0;
        for (int i = 0; i < qtdVacinacoes; i++) {
            if (cartao[i].getVacina().getId() == vacinaId) {
                total++;
            }
        }
        return total;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getDataNascimento() { return dataNascimento; }
    public String getBairro() { return bairro; }
    public Vacinacao[] getCartao() { return cartao; }
    public int getQtdVacinacoes() { return qtdVacinacoes; }
}
