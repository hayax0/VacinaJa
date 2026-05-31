import vacinaja.exception.*;
import vacinaja.model.*;
import vacinaja.system.VacinaJaSistema;

public class Main {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("   VacinaJa - Controle de Vacinacao");
        System.out.println("========================================");

        VacinaJaSistema sistema = new VacinaJaSistema();

        // cadastrando vacinas
        // (id, nome, fabricante, intervalo dias, faixa min, faixa max, doses)
        System.out.println("\n--- Cadastrando vacinas ---");
        Vacina bcg = new Vacina(1, "BCG", "Fiocruz", 0, 0, 4, 1);
        Vacina hepatiteB = new Vacina(2, "Hepatite B", "Butantan", 30, 0, 59, 3);
        Vacina triplice = new Vacina(3, "Triplice Viral", "GSK", 90, 1, 59, 2);
        Vacina covid = new Vacina(4, "COVID-19", "Pfizer", 90, 18, 120, 3);
        Vacina febreA = new Vacina(5, "Febre Amarela", "Fiocruz", 0, 9, 59, 1);

        sistema.cadastrarVacina(bcg);
        sistema.cadastrarVacina(hepatiteB);
        sistema.cadastrarVacina(triplice);
        sistema.cadastrarVacina(covid);
        sistema.cadastrarVacina(febreA);

        // cadastrando postos
        System.out.println("\n--- Cadastrando postos ---");
        Posto ubsCentro = new Posto(1, "UBS Centro", "Centro", "Rua Principal, 100");
        Posto ubsVilaNova = new Posto(2, "UBS Vila Nova", "Vila Nova", "Av. Flores, 500");
        Posto ubsJardim = new Posto(3, "UBS Jardim", "Jardim America", "Rua Ipes, 200");

        sistema.cadastrarPosto(ubsCentro);
        sistema.cadastrarPosto(ubsVilaNova);
        sistema.cadastrarPosto(ubsJardim);

        // abastecendo estoque
        System.out.println("\n--- Abastecendo estoques ---");
        ubsCentro.adicionarEstoque(bcg, 50);
        ubsCentro.adicionarEstoque(hepatiteB, 100);
        ubsCentro.adicionarEstoque(triplice, 80);
        ubsCentro.adicionarEstoque(covid, 200);
        ubsCentro.adicionarEstoque(febreA, 60);

        ubsVilaNova.adicionarEstoque(bcg, 30);
        ubsVilaNova.adicionarEstoque(hepatiteB, 60);
        ubsVilaNova.adicionarEstoque(triplice, 50);
        ubsVilaNova.adicionarEstoque(covid, 150);

        ubsJardim.adicionarEstoque(hepatiteB, 80);
        ubsJardim.adicionarEstoque(covid, 100);
        ubsJardim.adicionarEstoque(febreA, 40);

        System.out.println("Estoques ok!");

        // cadastrando cidadaos
        System.out.println("\n--- Cadastrando cidadaos ---");
        Cidadao maria = new Cidadao(1, "Maria Silva", "111.111.111-11", "15/03/1990", "Centro");
        Cidadao joao = new Cidadao(2, "Joao Santos", "222.222.222-22", "20/06/1985", "Vila Nova");
        Cidadao ana = new Cidadao(3, "Ana Oliveira", "333.333.333-33", "10/01/2024", "Centro");
        Cidadao pedro = new Cidadao(4, "Pedro Lima", "444.444.444-44", "05/11/2010", "Jardim America");
        Cidadao lucia = new Cidadao(5, "Lucia Ferreira", "555.555.555-55", "28/09/1978", "Vila Nova");
        Cidadao carlos = new Cidadao(6, "Carlos Souza", "666.666.666-66", "12/04/2020", "Jardim America");

        sistema.cadastrarCidadao(maria);
        sistema.cadastrarCidadao(joao);
        sistema.cadastrarCidadao(ana);
        sistema.cadastrarCidadao(pedro);
        sistema.cadastrarCidadao(lucia);
        sistema.cadastrarCidadao(carlos);

        // registrando vacinacoes
        System.out.println("\n--- Registrando vacinacoes ---");
        try {
            // Maria (36): COVID doses 1 e 2, Hepatite B dose 1
            sistema.registrarVacinacao(maria, covid, ubsCentro, 1, "10/01/2026");
            sistema.registrarVacinacao(maria, covid, ubsCentro, 2, "15/03/2026");
            sistema.registrarVacinacao(maria, hepatiteB, ubsCentro, 1, "20/02/2026");

            // Joao (41): COVID 3 doses
            sistema.registrarVacinacao(joao, covid, ubsVilaNova, 1, "01/01/2026");
            sistema.registrarVacinacao(joao, covid, ubsVilaNova, 2, "05/03/2026");
            sistema.registrarVacinacao(joao, covid, ubsVilaNova, 3, "10/05/2026");

            // Ana (2): BCG, Hepatite B, Triplice
            sistema.registrarVacinacao(ana, bcg, ubsCentro, 1, "15/01/2026");
            sistema.registrarVacinacao(ana, hepatiteB, ubsCentro, 1, "15/01/2026");
            sistema.registrarVacinacao(ana, triplice, ubsCentro, 1, "15/03/2026");

            // Pedro (16): Triplice e Febre Amarela
            sistema.registrarVacinacao(pedro, triplice, ubsVilaNova, 1, "20/02/2026");
            sistema.registrarVacinacao(pedro, febreA, ubsJardim, 1, "20/02/2026");

            // Lucia (48): COVID e Triplice
            sistema.registrarVacinacao(lucia, covid, ubsVilaNova, 1, "01/03/2026");
            sistema.registrarVacinacao(lucia, triplice, ubsVilaNova, 1, "01/03/2026");

            // Carlos (6): Hepatite B ok, Febre Amarela vai dar erro (faixa 9-59)
            sistema.registrarVacinacao(carlos, hepatiteB, ubsJardim, 1, "10/04/2026");
            sistema.registrarVacinacao(carlos, febreA, ubsJardim, 1, "10/04/2026");

        } catch (EstoqueInsuficienteException e) {
            System.out.println("ERRO ESTOQUE: " + e.getMessage());
        } catch (PacienteNaoAptoException e) {
            System.out.println("ERRO PACIENTE: " + e.getMessage());
        }

        // testando excessoes
        System.out.println("\n--- Testando excessoes ---");

        // teste 1: idade invalida
        try {
            System.out.println("\nTentando dar COVID em Ana (2 anos):");
            sistema.registrarVacinacao(ana, covid, ubsCentro, 1, "26/05/2026");
        } catch (EstoqueInsuficienteException | PacienteNaoAptoException e) {
            System.out.println("ERRO: " + e.getMessage());
        }

        // teste 2: esquema vacinal completo
        try {
            System.out.println("\nTentando dar 4a dose de COVID em Joao (ja tomou 3):");
            sistema.registrarVacinacao(joao, covid, ubsVilaNova, 4, "26/05/2026");
        } catch (EstoqueInsuficienteException | PacienteNaoAptoException e) {
            System.out.println("ERRO: " + e.getMessage());
        }

        // teste 3: estoque insuficiente
        try {
            System.out.println("\nEsvaziando estoque de Hepatite B no Vila Nova...");
            Estoque est = ubsVilaNova.getEstoque(hepatiteB);
            System.out.println("Estoque antes: " + est.getDoses() + " doses");
            while (est.getDoses() > 0) est.retirarDose();
            System.out.println("Estoque depois: " + est.getDoses() + " doses");
            System.out.println("Tentando vacinar Maria:");
            sistema.registrarVacinacao(maria, hepatiteB, ubsVilaNova, 2, "26/05/2026");
        } catch (EstoqueInsuficienteException | PacienteNaoAptoException e) {
            System.out.println("ERRO: " + e.getMessage());
        }

        // cartao digital
        System.out.println("\n--- Cartoes digitais ---");
        sistema.emitirCartaoDigital(maria);
        sistema.emitirCartaoDigital(joao);
        sistema.emitirCartaoDigital(ana);

        // doses em atraso
        System.out.println("--- Doses em atraso ---");
        sistema.detectarDosesEmAtraso(maria, "26/05/2026");
        sistema.detectarDosesEmAtraso(joao, "26/05/2026");
        sistema.detectarDosesEmAtraso(ana, "26/05/2026");
        sistema.detectarDosesEmAtraso(pedro, "26/05/2026");

        // relatorio
        sistema.gerarRelatorioCobertura();

        System.out.println("\n========================================");
        System.out.println("   Sistema encerrado.");
        System.out.println("========================================");
    }
}
