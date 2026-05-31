package vacinaja.system;

import vacinaja.exception.EstoqueInsuficienteException;
import vacinaja.exception.PacienteNaoAptoException;
import vacinaja.model.*;

public class VacinaJaSistema {

    private Posto[] postos = new Posto[10];
    private Vacina[] vacinas = new Vacina[10];
    private Cidadao[] cidadaos = new Cidadao[100];
    private Vacinacao[] vacinacoes = new Vacinacao[500];
    private int qtdPostos = 0;
    private int qtdVacinas = 0;
    private int qtdCidadaos = 0;
    private int qtdVacinacoes = 0;

    public void cadastrarPosto(Posto p) {
        postos[qtdPostos] = p;
        qtdPostos++;
        System.out.println("Posto cadastrado: " + p.getNome() + " (" + p.getBairro() + ")");
    }

    public void cadastrarVacina(Vacina v) {
        vacinas[qtdVacinas] = v;
        qtdVacinas++;
        System.out.println("Vacina cadastrada: " + v.getNome());
    }

    public void cadastrarCidadao(Cidadao c) {
        cidadaos[qtdCidadaos] = c;
        qtdCidadaos++;
        System.out.println("Cidadao cadastrado: " + c.getNome());
    }

    public void registrarVacinacao(Cidadao cidadao, Vacina vacina, Posto posto, int dose, String data)
            throws EstoqueInsuficienteException, PacienteNaoAptoException {

        // verifica faixa etaria
        if (!vacina.podeTomar(cidadao.getIdade())) {
            throw new PacienteNaoAptoException(
                    cidadao.getNome() + " tem " + cidadao.getIdade() +
                    " anos, mas " + vacina.getNome() + " e para " +
                    vacina.getFaixaMin() + " a " + vacina.getFaixaMax() + " anos");
        }

        // verifica se ja tomou tudo
        if (cidadao.dosesTomadas(vacina.getId()) >= vacina.getDosesNecessarias()) {
            throw new PacienteNaoAptoException(
                    cidadao.getNome() + " ja tomou todas as " +
                    vacina.getDosesNecessarias() + " doses de " + vacina.getNome());
        }

        // verifica estoque
        Estoque est = posto.getEstoque(vacina);
        if (est == null || est.getDoses() <= 0) {
            throw new EstoqueInsuficienteException(
                    "Sem estoque de " + vacina.getNome() + " no posto " + posto.getNome());
        }

        // registra
        est.retirarDose();
        Vacinacao v = new Vacinacao(qtdVacinacoes + 1, cidadao, vacina, posto, dose, data);
        vacinacoes[qtdVacinacoes] = v;
        qtdVacinacoes++;
        cidadao.addVacinacao(v);

        System.out.println("Vacinacao registrada: " + cidadao.getNome() +
                " tomou " + vacina.getNome() + " (dose " + dose + ")" +
                " no " + posto.getNome() +
                " | estoque: " + est.getDoses() + " restantes");
    }

    public void emitirCartaoDigital(Cidadao cidadao) {
        System.out.println("\n--- CARTAO DIGITAL DE VACINACAO ---");
        System.out.println("Nome: " + cidadao.getNome());
        System.out.println("CPF: " + cidadao.getCpf());
        System.out.println("Nascimento: " + cidadao.getDataNascimento());
        System.out.println("Idade: " + cidadao.getIdade() + " anos");
        System.out.println("Bairro: " + cidadao.getBairro());
        System.out.println();

        if (cidadao.getQtdVacinacoes() == 0) {
            System.out.println("Nenhuma vacina registrada.");
        } else {
            System.out.printf("%-5s %-18s %-8s %-22s %-10s%n", "Dose", "Vacina", "Cod", "Posto", "Data");
            System.out.println("-".repeat(65));
            for (int i = 0; i < cidadao.getQtdVacinacoes(); i++) {
                Vacinacao vx = cidadao.getCartao()[i];
                System.out.printf("%-5s %-18s %-8d %-22s %-10s%n",
                        vx.getDose() + "a",
                        vx.getVacina().getNome(),
                        vx.getVacina().getId(),
                        vx.getPosto().getNome(),
                        vx.getData());
            }
        }
        System.out.println("-----------------------------------\n");
    }

    public void detectarDosesEmAtraso(Cidadao cidadao, String dataAtual) {
        System.out.println("\n>> Doses em atraso para " + cidadao.getNome() + ":");

        boolean achou = false;
        for (int i = 0; i < qtdVacinas; i++) {
            Vacina vac = vacinas[i];
            int tomadas = cidadao.dosesTomadas(vac.getId());

            if (tomadas == 0 && vac.podeTomar(cidadao.getIdade())) {
                System.out.println("  [PENDENTE] " + vac.getNome() + " - nenhuma dose tomada, paciente e elegivel");
                achou = true;
            } else if (tomadas > 0 && tomadas < vac.getDosesNecessarias()) {
                // pega a ultima dose pra calcular o intervalo
                Vacinacao ultima = null;
                for (int j = cidadao.getQtdVacinacoes() - 1; j >= 0; j--) {
                    if (cidadao.getCartao()[j].getVacina().getId() == vac.getId()) {
                        ultima = cidadao.getCartao()[j];
                        break;
                    }
                }
                int diasDesde = diferencaDias(ultima.getData(), dataAtual);
                if (diasDesde > vac.getIntervaloDias()) {
                    int diasAtraso = diasDesde - vac.getIntervaloDias();
                    System.out.println("  [ATRASO] " + vac.getNome() +
                            " | tomou " + tomadas + "/" + vac.getDosesNecessarias() +
                            " | atrasada " + diasAtraso + " dias");
                    achou = true;
                } else {
                    int falta = vac.getIntervaloDias() - diasDesde;
                    System.out.println("  [OK] " + vac.getNome() +
                            " | tomou " + tomadas + "/" + vac.getDosesNecessarias() +
                            " | proxima dose em " + falta + " dias");
                }
            }
        }
        if (!achou) {
            System.out.println("  Nenhuma dose em atraso.");
        }
    }

    public void gerarRelatorioCobertura() {
        System.out.println("\n=== RELATORIO DE COBERTURA VACINAL POR BAIRRO ===\n");

        // acha bairros unicos
        String[] bairros = new String[50];
        int qtdBairros = 0;
        for (int i = 0; i < qtdCidadaos; i++) {
            boolean existe = false;
            for (int j = 0; j < qtdBairros; j++) {
                if (bairros[j].equals(cidadaos[i].getBairro())) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                bairros[qtdBairros] = cidadaos[i].getBairro();
                qtdBairros++;
            }
        }

        for (int b = 0; b < qtdBairros; b++) {
            String bairro = bairros[b];
            int total = 0;
            for (int i = 0; i < qtdCidadaos; i++) {
                if (cidadaos[i].getBairro().equals(bairro)) total++;
            }

            System.out.println("Bairro: " + bairro + " (" + total + " cidadaos)");
            System.out.printf("  %-18s %-10s %-10s %-10s%n", "Vacina", "Vacinados", "Total", "Cobertura");
            System.out.println("  " + "-".repeat(50));

            for (int v = 0; v < qtdVacinas; v++) {
                Vacina vac = vacinas[v];
                int vacinados = 0;
                for (int i = 0; i < qtdCidadaos; i++) {
                    if (cidadaos[i].getBairro().equals(bairro)) {
                        if (cidadaos[i].dosesTomadas(vac.getId()) >= vac.getDosesNecessarias()) {
                            vacinados++;
                        }
                    }
                }
                double pct = total > 0 ? (vacinados * 100.0 / total) : 0;
                System.out.printf("  %-18s %-10d %-10d %.1f%%%n", vac.getNome(), vacinados, total, pct);
            }
            System.out.println();
        }

        System.out.println("Resumo: " + qtdPostos + " postos, " + qtdVacinas +
                " vacinas, " + qtdCidadaos + " cidadaos, " + qtdVacinacoes + " vacinacoes");
    }

    public Posto buscarPosto(String nome) {
        for (int i = 0; i < qtdPostos; i++) {
            if (postos[i].getNome().equalsIgnoreCase(nome)) return postos[i];
        }
        return null;
    }

    // calcula diferença de dias (simplao: mes com 30 dias)
    private int diferencaDias(String d1, String d2) {
        String[] p1 = d1.split("/");
        String[] p2 = d2.split("/");
        int n1 = Integer.parseInt(p1[0]) + Integer.parseInt(p1[1]) * 30 + Integer.parseInt(p1[2]) * 365;
        int n2 = Integer.parseInt(p2[0]) + Integer.parseInt(p2[1]) * 30 + Integer.parseInt(p2[2]) * 365;
        return n2 - n1;
    }
}
