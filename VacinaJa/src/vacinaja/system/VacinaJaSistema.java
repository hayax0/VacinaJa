package vacinaja.system;

import vacinaja.exception.EstoqueInsuficienteException;
import vacinaja.exception.PacienteNaoAptoException;
import vacinaja.model.*;

public class VacinaJaSistema {

    private Posto[]     postos     = new Posto[100];
    private Vacina[]    vacinas    = new Vacina[100];
    private Cidadao[]   cidadaos   = new Cidadao[500];
    private Vacinacao[] vacinacoes = new Vacinacao[2000];
    private int qtdPostos     = 0;
    private int qtdVacinas    = 0;
    private int qtdCidadaos   = 0;
    private int qtdVacinacoes = 0;

    // ----------------------------------------------------------------
    // CADASTROS
    // ----------------------------------------------------------------

    public void cadastrarPosto(Posto p) {
        postos[qtdPostos++] = p;
        System.out.println("Posto cadastrado: " + p.getNome() + " (" + p.getBairro() + ")");
    }

    public void cadastrarVacina(Vacina v) {
        vacinas[qtdVacinas++] = v;
        System.out.println("Vacina cadastrada: " + v.getNome());
    }

    public void cadastrarCidadao(Cidadao c) {
        cidadaos[qtdCidadaos++] = c;
        System.out.println("Cidadao cadastrado: " + c.getNome());
    }

    // ----------------------------------------------------------------
    // BUSCA POR NOME
    // ----------------------------------------------------------------

    public Posto getPostoPorNome(String nome) {
        for (int i = 0; i < qtdPostos; i++) {
            if (postos[i].getNome().equals(nome)) return postos[i];
        }
        return null;
    }

    public Vacina getVacinaPorNome(String nome) {
        for (int i = 0; i < qtdVacinas; i++) {
            if (vacinas[i].getNome().equals(nome)) return vacinas[i];
        }
        return null;
    }

    public Cidadao getCidadaoPorNome(String nome) {
        for (int i = 0; i < qtdCidadaos; i++) {
            if (cidadaos[i].getNome().equals(nome)) return cidadaos[i];
        }
        return null;
    }

    // ----------------------------------------------------------------
    // LISTAS DE NOMES
    // ----------------------------------------------------------------

    public String[] getNomesPosTo() {
        String[] nomes = new String[qtdPostos];
        for (int i = 0; i < qtdPostos; i++) nomes[i] = postos[i].getNome();
        return nomes;
    }

    public String[] getNomesVacinas() {
        String[] nomes = new String[qtdVacinas];
        for (int i = 0; i < qtdVacinas; i++) nomes[i] = vacinas[i].getNome();
        return nomes;
    }

    public String[] getNomesCidadaos() {
        String[] nomes = new String[qtdCidadaos];
        for (int i = 0; i < qtdCidadaos; i++) nomes[i] = cidadaos[i].getNome();
        return nomes;
    }

    // ----------------------------------------------------------------
    // REGISTRAR VACINACAO
    // ----------------------------------------------------------------

    public void registrarVacinacao(Cidadao cidadao, Vacina vacina, Posto posto, int dose, String data)
            throws EstoqueInsuficienteException, PacienteNaoAptoException {

        if (!vacina.podeTomar(cidadao.getIdade())) {
            throw new PacienteNaoAptoException(
                    cidadao.getNome() + " tem " + cidadao.getIdade() +
                    " anos, mas " + vacina.getNome() + " e para " +
                    vacina.getFaixaMin() + " a " + vacina.getFaixaMax() + " anos");
        }

        if (cidadao.dosesTomadas(vacina.getId()) >= vacina.getDosesNecessarias()) {
            throw new PacienteNaoAptoException(
                    cidadao.getNome() + " ja tomou todas as " +
                    vacina.getDosesNecessarias() + " doses de " + vacina.getNome());
        }

        Estoque est = posto.getEstoque(vacina);
        if (est == null || est.getDoses() <= 0) {
            throw new EstoqueInsuficienteException(
                    "Sem estoque de " + vacina.getNome() + " no posto " + posto.getNome());
        }

        est.retirarDose();
        Vacinacao v = new Vacinacao(qtdVacinacoes + 1, cidadao, vacina, posto, dose, data);
        vacinacoes[qtdVacinacoes++] = v;
        cidadao.addVacinacao(v);

        System.out.println("Vacinacao registrada: " + cidadao.getNome() +
                " tomou " + vacina.getNome() + " (dose " + dose + ")" +
                " no " + posto.getNome() +
                " | estoque: " + est.getDoses() + " restantes");
    }

    // ----------------------------------------------------------------
    // CARTAO DIGITAL
    // ----------------------------------------------------------------

    public void emitirCartaoDigital(Cidadao cidadao) {
        System.out.println("\n--- CARTAO DIGITAL DE VACINACAO ---");
        System.out.println("Nome: "       + cidadao.getNome());
        System.out.println("CPF: "        + cidadao.getCpf());
        System.out.println("Nascimento: " + cidadao.getDataNascimento());
        System.out.println("Idade: "      + cidadao.getIdade() + " anos");
        System.out.println("Bairro: "     + cidadao.getBairro());
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

    // ----------------------------------------------------------------
    // DOSES EM ATRASO
    // ----------------------------------------------------------------

    public String getDosesEmAtrasoTexto(Cidadao cidadao, String dataAtual) {
        StringBuilder sb = new StringBuilder();
        sb.append("Doses em atraso para ").append(cidadao.getNome()).append(":\n\n");

        boolean achou = false;
        for (int i = 0; i < qtdVacinas; i++) {
            Vacina vac = vacinas[i];
            int tomadas = cidadao.dosesTomadas(vac.getId());

            if (tomadas == 0 && vac.podeTomar(cidadao.getIdade())) {
                sb.append("  [PENDENTE] ").append(vac.getNome())
                  .append(" - nenhuma dose tomada, paciente e elegivel\n");
                achou = true;
            } else if (tomadas > 0 && tomadas < vac.getDosesNecessarias()) {
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
                    sb.append("  [ATRASO] ").append(vac.getNome())
                      .append(" | tomou ").append(tomadas).append("/").append(vac.getDosesNecessarias())
                      .append(" | atrasada ").append(diasAtraso).append(" dias\n");
                    achou = true;
                } else {
                    int falta = vac.getIntervaloDias() - diasDesde;
                    sb.append("  [OK] ").append(vac.getNome())
                      .append(" | tomou ").append(tomadas).append("/").append(vac.getDosesNecessarias())
                      .append(" | proxima dose em ").append(falta).append(" dias\n");
                }
            }
        }
        if (!achou) sb.append("  Nenhuma dose em atraso.\n");
        return sb.toString();
    }


    public void detectarDosesEmAtraso(Cidadao cidadao, String dataAtual) {
        System.out.println(getDosesEmAtrasoTexto(cidadao, dataAtual));
    }

    // ----------------------------------------------------------------
    // RELATORIO DE COBERTURA
    // ----------------------------------------------------------------

    public String getRelatorioCoberturaTexto() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATORIO DE COBERTURA VACINAL POR BAIRRO ===\n\n");

        if (qtdCidadaos == 0) {
            sb.append("Nenhum cidadao cadastrado.\n");
            return sb.toString();
        }

        String[] bairros = new String[100];
        int qtdBairros = 0;
        for (int i = 0; i < qtdCidadaos; i++) {
            boolean existe = false;
            for (int j = 0; j < qtdBairros; j++) {
                if (bairros[j].equals(cidadaos[i].getBairro())) { existe = true; break; }
            }
            if (!existe) bairros[qtdBairros++] = cidadaos[i].getBairro();
        }

        for (int b = 0; b < qtdBairros; b++) {
            String bairro = bairros[b];
            int total = 0;
            for (int i = 0; i < qtdCidadaos; i++) {
                if (cidadaos[i].getBairro().equals(bairro)) total++;
            }
            sb.append("Bairro: ").append(bairro).append(" (").append(total).append(" cidadaos)\n");
            sb.append(String.format("  %-20s %-10s %-10s %-10s%n", "Vacina", "Vacinados", "Total", "Cobertura"));
            sb.append("  ").append("-".repeat(52)).append("\n");

            for (int v = 0; v < qtdVacinas; v++) {
                Vacina vac = vacinas[v];
                int vacinados = 0;
                for (int i = 0; i < qtdCidadaos; i++) {
                    if (cidadaos[i].getBairro().equals(bairro)) {
                        if (cidadaos[i].dosesTomadas(vac.getId()) >= vac.getDosesNecessarias()) vacinados++;
                    }
                }
                double pct = total > 0 ? (vacinados * 100.0 / total) : 0;
                sb.append(String.format("  %-20s %-10d %-10d %.1f%%%n", vac.getNome(), vacinados, total, pct));
            }
            sb.append("\n");
        }

        sb.append("Resumo: ").append(qtdPostos).append(" postos, ")
          .append(qtdVacinas).append(" vacinas, ")
          .append(qtdCidadaos).append(" cidadaos, ")
          .append(qtdVacinacoes).append(" vacinacoes\n");
        return sb.toString();
    }

    public void gerarRelatorioCobertura() {
        System.out.println(getRelatorioCoberturaTexto());
    }

    public Posto buscarPosto(String nome) {
        for (int i = 0; i < qtdPostos; i++) {
            if (postos[i].getNome().equalsIgnoreCase(nome)) return postos[i];
        }
        return null;
    }

    private int diferencaDias(String d1, String d2) {
        String[] p1 = d1.split("/");
        String[] p2 = d2.split("/");
        int n1 = Integer.parseInt(p1[0]) + Integer.parseInt(p1[1]) * 30 + Integer.parseInt(p1[2]) * 365;
        int n2 = Integer.parseInt(p2[0]) + Integer.parseInt(p2[1]) * 30 + Integer.parseInt(p2[2]) * 365;
        return n2 - n1;
    }
}
