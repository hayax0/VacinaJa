import vacinaja.exception.*;
import vacinaja.model.*;
import vacinaja.system.VacinaJaSistema;

import javax.swing.*;
import java.awt.*;

public class Main {

    static VacinaJaSistema sistema = new VacinaJaSistema();

    // contadores de ID simples
    static int proximoIdVacina   = 1;
    static int proximoIdPosto    = 1;
    static int proximoIdCidadao  = 1;

    public static void main(String[] args) {

        System.setProperty("java.awt.headless", "false");

        boolean rodando = true;
        while (rodando) {
            String[] opcoes = {
                "1 - Cadastrar Vacina",
                "2 - Cadastrar Posto",
                "3 - Cadastrar Cidadao",
                "4 - Abastecer Estoque",
                "5 - Registrar Vacinacao",
                "6 - Emitir Cartao Digital",
                "7 - Verificar Doses em Atraso",
                "8 - Relatorio de Cobertura",
                "9 - Sair"
            };

            int escolha = JOptionPane.showOptionDialog(
                null,
                "Bem-vindo ao VacinaJa!\nO que deseja fazer?",
                "VacinaJa - Menu Principal",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                opcoes[0]
            );

            // fechou a janela
            if (escolha == JOptionPane.CLOSED_OPTION) {
                rodando = false;
                continue;
            }

            switch (escolha) {
                case 0 -> cadastrarVacina();
                case 1 -> cadastrarPosto();
                case 2 -> cadastrarCidadao();
                case 3 -> abastecerEstoque();
                case 4 -> registrarVacinacao();
                case 5 -> emitirCartaoDigital();
                case 6 -> verificarDosesEmAtraso();
                case 7 -> relatorioCobertura();
                case 8 -> rodando = false;
            }
        }

        JOptionPane.showMessageDialog(null,
            "Sistema encerrado. Ate logo!",
            "VacinaJa",
            JOptionPane.INFORMATION_MESSAGE);
    }

    // ----------------------------------------------------------------
    // 1) CADASTRAR VACINA
    // ----------------------------------------------------------------
    static void cadastrarVacina() {
        JPanel painel = new JPanel(new GridLayout(0, 2, 6, 6));

        JTextField fNome        = new JTextField();
        JTextField fFabricante  = new JTextField();
        JTextField fIntervalo   = new JTextField("0");
        JTextField fFaixaMin    = new JTextField("0");
        JTextField fFaixaMax    = new JTextField("120");
        JTextField fDoses       = new JTextField("1");

        painel.add(new JLabel("Nome da vacina:"));       painel.add(fNome);
        painel.add(new JLabel("Fabricante:"));           painel.add(fFabricante);
        painel.add(new JLabel("Intervalo entre doses (dias):")); painel.add(fIntervalo);
        painel.add(new JLabel("Faixa etaria minima:")); painel.add(fFaixaMin);
        painel.add(new JLabel("Faixa etaria maxima:")); painel.add(fFaixaMax);
        painel.add(new JLabel("Numero de doses necessarias:")); painel.add(fDoses);

        int ok = JOptionPane.showConfirmDialog(null, painel,
            "Cadastrar Vacina", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (ok != JOptionPane.OK_OPTION) return;

        try {
            String nome       = fNome.getText().trim();
            String fabricante = fFabricante.getText().trim();
            int intervalo     = Integer.parseInt(fIntervalo.getText().trim());
            int faixaMin      = Integer.parseInt(fFaixaMin.getText().trim());
            int faixaMax      = Integer.parseInt(fFaixaMax.getText().trim());
            int doses         = Integer.parseInt(fDoses.getText().trim());

            if (nome.isEmpty() || fabricante.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nome e fabricante sao obrigatorios.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Vacina v = new Vacina(proximoIdVacina++, nome, fabricante, intervalo, faixaMin, faixaMax, doses);
            sistema.cadastrarVacina(v);

            JOptionPane.showMessageDialog(null,
                "Vacina cadastrada com sucesso!\n\n" +
                "Nome: "       + v.getNome()            + "\n" +
                "Fabricante: " + v.getFabricante()       + "\n" +
                "Faixa: "      + v.getFaixaMin() + " a " + v.getFaixaMax() + " anos\n" +
                "Doses: "      + v.getDosesNecessarias() + "\n" +
                "Intervalo: "  + v.getIntervaloDias()    + " dias",
                "Vacina Cadastrada", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                "Valores numericos invalidos. Por favor, preencha corretamente.",
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ----------------------------------------------------------------
    // 2) CADASTRAR POSTO
    // ----------------------------------------------------------------
    static void cadastrarPosto() {
        JPanel painel = new JPanel(new GridLayout(0, 2, 6, 6));

        JTextField fNome      = new JTextField();
        JTextField fBairro    = new JTextField();
        JTextField fEndereco  = new JTextField();

        painel.add(new JLabel("Nome do posto:"));   painel.add(fNome);
        painel.add(new JLabel("Bairro:"));          painel.add(fBairro);
        painel.add(new JLabel("Endereco:"));        painel.add(fEndereco);

        int ok = JOptionPane.showConfirmDialog(null, painel,
            "Cadastrar Posto", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (ok != JOptionPane.OK_OPTION) return;

        String nome     = fNome.getText().trim();
        String bairro   = fBairro.getText().trim();
        String endereco = fEndereco.getText().trim();

        if (nome.isEmpty() || bairro.isEmpty() || endereco.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos os campos sao obrigatorios.",
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Posto p = new Posto(proximoIdPosto++, nome, bairro, endereco);
        sistema.cadastrarPosto(p);

        JOptionPane.showMessageDialog(null,
            "Posto cadastrado com sucesso!\n\n" +
            "Nome: "     + p.getNome()     + "\n" +
            "Bairro: "   + p.getBairro()   + "\n" +
            "Endereco: " + p.getEndereco(),
            "Posto Cadastrado", JOptionPane.INFORMATION_MESSAGE);
    }

    // ----------------------------------------------------------------
    // 3) CADASTRAR CIDADAO
    // ----------------------------------------------------------------
    static void cadastrarCidadao() {
        JPanel painel = new JPanel(new GridLayout(0, 2, 6, 6));

        JTextField fNome          = new JTextField();
        JTextField fCpf           = new JTextField();
        JTextField fNascimento    = new JTextField("dd/MM/aaaa");
        JTextField fBairro        = new JTextField();

        painel.add(new JLabel("Nome completo:"));         painel.add(fNome);
        painel.add(new JLabel("CPF (xxx.xxx.xxx-xx):")); painel.add(fCpf);
        painel.add(new JLabel("Data de nascimento:"));    painel.add(fNascimento);
        painel.add(new JLabel("Bairro:"));                painel.add(fBairro);

        int ok = JOptionPane.showConfirmDialog(null, painel,
            "Cadastrar Cidadao", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (ok != JOptionPane.OK_OPTION) return;

        String nome       = fNome.getText().trim();
        String cpf        = fCpf.getText().trim();
        String nascimento = fNascimento.getText().trim();
        String bairro     = fBairro.getText().trim();

        if (nome.isEmpty() || cpf.isEmpty() || nascimento.isEmpty() || bairro.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos os campos sao obrigatorios.",
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!nascimento.matches("\\d{2}/\\d{2}/\\d{4}")) {
            JOptionPane.showMessageDialog(null,
                "Data de nascimento deve estar no formato dd/MM/aaaa.",
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cidadao c = new Cidadao(proximoIdCidadao++, nome, cpf, nascimento, bairro);
        sistema.cadastrarCidadao(c);

        JOptionPane.showMessageDialog(null,
            "Cidadao cadastrado com sucesso!\n\n" +
            "Nome: "        + c.getNome()            + "\n" +
            "CPF: "         + c.getCpf()             + "\n" +
            "Nascimento: "  + c.getDataNascimento()  + "\n" +
            "Idade: "       + c.getIdade()           + " anos\n" +
            "Bairro: "      + c.getBairro(),
            "Cidadao Cadastrado", JOptionPane.INFORMATION_MESSAGE);
    }

    // ----------------------------------------------------------------
    // 4) ABASTECER ESTOQUE
    // ----------------------------------------------------------------
    static void abastecerEstoque() {
        String[] listaPosToNomes  = sistema.getNomesPosTo();
        String[] listaVacinaNomes = sistema.getNomesVacinas();

        if (listaPosToNomes == null || listaPosToNomes.length == 0) {
            JOptionPane.showMessageDialog(null, "Nenhum posto cadastrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (listaVacinaNomes == null || listaVacinaNomes.length == 0) {
            JOptionPane.showMessageDialog(null, "Nenhuma vacina cadastrada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel painel = new JPanel(new GridLayout(0, 2, 6, 6));

        JComboBox<String> cbPosto  = new JComboBox<>(listaPosToNomes);
        JComboBox<String> cbVacina = new JComboBox<>(listaVacinaNomes);
        JTextField fDoses = new JTextField();

        painel.add(new JLabel("Posto:"));            painel.add(cbPosto);
        painel.add(new JLabel("Vacina:"));           painel.add(cbVacina);
        painel.add(new JLabel("Quantidade de doses:")); painel.add(fDoses);

        int ok = JOptionPane.showConfirmDialog(null, painel,
            "Abastecer Estoque", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (ok != JOptionPane.OK_OPTION) return;

        try {
            int doses = Integer.parseInt(fDoses.getText().trim());
            if (doses <= 0) throw new NumberFormatException();

            Posto  posto  = sistema.getPostoPorNome((String) cbPosto.getSelectedItem());
            Vacina vacina = sistema.getVacinaPorNome((String) cbVacina.getSelectedItem());

            posto.adicionarEstoque(vacina, doses);

            JOptionPane.showMessageDialog(null,
                "Estoque abastecido!\n\n" +
                "Posto: "  + posto.getNome()  + "\n" +
                "Vacina: " + vacina.getNome() + "\n" +
                "Doses adicionadas: " + doses,
                "Estoque Atualizado", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                "Informe uma quantidade valida (numero inteiro positivo).",
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ----------------------------------------------------------------
    // 5) REGISTRAR VACINACAO
    // ----------------------------------------------------------------
    static void registrarVacinacao() {
        String[] listaCidadaos = sistema.getNomesCidadaos();
        String[] listaVacinas  = sistema.getNomesVacinas();
        String[] listaPostos   = sistema.getNomesPosTo();

        if (listaCidadaos == null || listaCidadaos.length == 0) {
            JOptionPane.showMessageDialog(null, "Nenhum cidadao cadastrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (listaVacinas == null || listaVacinas.length == 0) {
            JOptionPane.showMessageDialog(null, "Nenhuma vacina cadastrada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (listaPostos == null || listaPostos.length == 0) {
            JOptionPane.showMessageDialog(null, "Nenhum posto cadastrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel painel = new JPanel(new GridLayout(0, 2, 6, 6));

        JComboBox<String> cbCidadao = new JComboBox<>(listaCidadaos);
        JComboBox<String> cbVacina  = new JComboBox<>(listaVacinas);
        JComboBox<String> cbPosto   = new JComboBox<>(listaPostos);
        JTextField fDose = new JTextField("1");
        JTextField fData = new JTextField("dd/MM/aaaa");

        painel.add(new JLabel("Cidadao:"));     painel.add(cbCidadao);
        painel.add(new JLabel("Vacina:"));      painel.add(cbVacina);
        painel.add(new JLabel("Posto:"));       painel.add(cbPosto);
        painel.add(new JLabel("Numero da dose:")); painel.add(fDose);
        painel.add(new JLabel("Data (dd/MM/aaaa):")); painel.add(fData);

        int ok = JOptionPane.showConfirmDialog(null, painel,
            "Registrar Vacinacao", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (ok != JOptionPane.OK_OPTION) return;

        try {
            int dose = Integer.parseInt(fDose.getText().trim());
            String data = fData.getText().trim();

            if (!data.matches("\\d{2}/\\d{2}/\\d{4}")) {
                JOptionPane.showMessageDialog(null, "Data invalida. Use o formato dd/MM/aaaa.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Cidadao cidadao = sistema.getCidadaoPorNome((String) cbCidadao.getSelectedItem());
            Vacina  vacina  = sistema.getVacinaPorNome((String) cbVacina.getSelectedItem());
            Posto   posto   = sistema.getPostoPorNome((String) cbPosto.getSelectedItem());

            sistema.registrarVacinacao(cidadao, vacina, posto, dose, data);

            JOptionPane.showMessageDialog(null,
                "Vacinacao registrada com sucesso!\n\n" +
                "Cidadao: " + cidadao.getNome() + "\n" +
                "Vacina: "  + vacina.getNome()  + " (dose " + dose + ")\n" +
                "Posto: "   + posto.getNome()   + "\n" +
                "Data: "    + data,
                "Vacinacao Registrada", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Numero da dose invalido.",
                "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (EstoqueInsuficienteException e) {
            JOptionPane.showMessageDialog(null, "Erro de estoque:\n" + e.getMessage(),
                "Estoque Insuficiente", JOptionPane.ERROR_MESSAGE);
        } catch (PacienteNaoAptoException e) {
            JOptionPane.showMessageDialog(null, "Paciente nao apto:\n" + e.getMessage(),
                "Paciente Nao Apto", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ----------------------------------------------------------------
    // 6) EMITIR CARTAO DIGITAL
    // ----------------------------------------------------------------
    static void emitirCartaoDigital() {
        String[] listaCidadaos = sistema.getNomesCidadaos();
        if (listaCidadaos == null || listaCidadaos.length == 0) {
            JOptionPane.showMessageDialog(null, "Nenhum cidadao cadastrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String escolhido = (String) JOptionPane.showInputDialog(
            null,
            "Selecione o cidadao:",
            "Emitir Cartao Digital",
            JOptionPane.PLAIN_MESSAGE,
            null,
            listaCidadaos,
            listaCidadaos[0]
        );

        if (escolhido == null) return;

        Cidadao c = sistema.getCidadaoPorNome(escolhido);

        StringBuilder sb = new StringBuilder();
        sb.append("======== CARTAO DIGITAL DE VACINACAO ========\n");
        sb.append("Nome:       ").append(c.getNome()).append("\n");
        sb.append("CPF:        ").append(c.getCpf()).append("\n");
        sb.append("Nascimento: ").append(c.getDataNascimento()).append("\n");
        sb.append("Idade:      ").append(c.getIdade()).append(" anos\n");
        sb.append("Bairro:     ").append(c.getBairro()).append("\n");
        sb.append("=============================================\n");

        if (c.getQtdVacinacoes() == 0) {
            sb.append("  Nenhuma vacina registrada.\n");
        } else {
            sb.append(String.format("%-5s %-20s %-22s %-10s%n", "Dose", "Vacina", "Posto", "Data"));
            sb.append("-".repeat(60)).append("\n");
            for (int i = 0; i < c.getQtdVacinacoes(); i++) {
                Vacinacao vx = c.getCartao()[i];
                sb.append(String.format("%-5s %-20s %-22s %-10s%n",
                    vx.getDose() + "a",
                    vx.getVacina().getNome(),
                    vx.getPosto().getNome(),
                    vx.getData()));
            }
        }
        sb.append("=============================================\n");

        JTextArea area = new JTextArea(sb.toString());
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(520, 260));

        JOptionPane.showMessageDialog(null, scroll,
            "Cartao Digital - " + c.getNome(),
            JOptionPane.PLAIN_MESSAGE);
    }

    // ----------------------------------------------------------------
    // 7) VERIFICAR DOSES EM ATRASO
    // ----------------------------------------------------------------
    static void verificarDosesEmAtraso() {
        String[] listaCidadaos = sistema.getNomesCidadaos();
        if (listaCidadaos == null || listaCidadaos.length == 0) {
            JOptionPane.showMessageDialog(null, "Nenhum cidadao cadastrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel painel = new JPanel(new GridLayout(0, 2, 6, 6));
        JComboBox<String> cbCidadao = new JComboBox<>(listaCidadaos);
        JTextField fDataAtual = new JTextField("02/06/2026");

        painel.add(new JLabel("Cidadao:"));              painel.add(cbCidadao);
        painel.add(new JLabel("Data atual (dd/MM/aaaa):")); painel.add(fDataAtual);

        int ok = JOptionPane.showConfirmDialog(null, painel,
            "Verificar Doses em Atraso", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (ok != JOptionPane.OK_OPTION) return;

        String data = fDataAtual.getText().trim();
        if (!data.matches("\\d{2}/\\d{2}/\\d{4}")) {
            JOptionPane.showMessageDialog(null, "Data invalida. Use dd/MM/aaaa.",
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cidadao c = sistema.getCidadaoPorNome((String) cbCidadao.getSelectedItem());
        String resultado = sistema.getDosesEmAtrasoTexto(c, data);

        JTextArea area = new JTextArea(resultado);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(480, 220));

        JOptionPane.showMessageDialog(null, scroll,
            "Doses em Atraso - " + c.getNome(),
            JOptionPane.PLAIN_MESSAGE);
    }

    // ----------------------------------------------------------------
    // 8) RELATORIO DE COBERTURA
    // ----------------------------------------------------------------
    static void relatorioCobertura() {
        String relatorio = sistema.getRelatorioCoberturaTexto();
        JTextArea area = new JTextArea(relatorio);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(null, scroll,
            "Relatorio de Cobertura Vacinal",
            JOptionPane.PLAIN_MESSAGE);
    }
}
