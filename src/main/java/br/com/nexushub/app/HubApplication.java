package br.com.nexushub.app;

import br.com.nexushub.dao.TarefaDAO;
import br.com.nexushub.model.Tarefa;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class HubApplication {

    private static final Scanner scanner = new Scanner(System.in);
    private static final TarefaDAO tarefaDAO = new TarefaDAO();

    static void main() {
        while (true) {
            exibirMenu();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    adicionarNovaTarefa();
                    break;
                case 2:
                    listarTarefas();
                    break;
                case 3:
                    atualizarTarefaExistente();
                    break;
                case 4:
                    excluirTarefa();
                    break;
                case 0:
                    System.out.println("Saindo da aplicação...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
            pressioneEnterParaContinuar();
        }
    }

    private static void exibirMenu() {
        System.out.println("\n---[ Menu de Gerenciamento de Tarefas ]---");
        System.out.println("1. Adicionar nova tarefa");
        System.out.println("2. Listar todas as tarefas");
        System.out.println("3. Atualizar uma tarefa");
        System.out.println("4. Excluir uma tarefa");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void adicionarNovaTarefa() {
        System.out.println("\n--- Adicionar Nova Tarefa ---");
        System.out.print("Digite a descrição: ");
        String descricao = scanner.nextLine();

        System.out.print("Digite a prioridade (alta, media, baixa): ");
        String prioridade = scanner.nextLine();

        int idUsuarioExemplo = 1;
        int idCategoriaExemplo = 1;

        Tarefa novaTarefa = new Tarefa(descricao, LocalDate.now(), prioridade, idUsuarioExemplo, idCategoriaExemplo);
        tarefaDAO.adicionarTarefa(novaTarefa);
    }

    private static void listarTarefas() {
        System.out.println("\n--- Lista de Tarefas ---");
        List<Tarefa> tarefas = tarefaDAO.listarTodasAsTarefas();
        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada.");
        } else {
            for (Tarefa t : tarefas) {
                System.out.printf("ID: %d | Descrição: %s | Prioridade: %s | Concluída: %s\n",
                        t.getIdTarefa(), t.getDescricao(), t.getPrioridade(), t.isConcluida());
            }
        }
    }

    private static void atualizarTarefaExistente() {
        System.out.println("\n--- Atualizar Tarefa ---");
        listarTarefas();
        System.out.print("Digite o ID da tarefa que deseja atualizar: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        Tarefa tarefaParaAtualizar = tarefaDAO.buscarTarefaPorId(id);
        if (tarefaParaAtualizar == null) {
            System.out.println("Tarefa não encontrada.");
            return;
        }

        System.out.print("Digite a nova descrição (deixe em branco para não alterar): ");
        String novaDescricao = scanner.nextLine();
        if (!novaDescricao.trim().isEmpty()) {
            tarefaParaAtualizar.setDescricao(novaDescricao);
        }

        System.out.print("Digite a nova prioridade (deixe em branco para não alterar): ");
        String novaPrioridade = scanner.nextLine();
        if (!novaPrioridade.trim().isEmpty()) {
            tarefaParaAtualizar.setPrioridade(novaPrioridade);
        }

        System.out.print("A tarefa foi concluída? (s/n, deixe em branco para não alterar): ");
        String concluidaStr = scanner.nextLine();
        if (concluidaStr.equalsIgnoreCase("s")) {
            tarefaParaAtualizar.setConcluida(true);
        } else if (concluidaStr.equalsIgnoreCase("n")) {
            tarefaParaAtualizar.setConcluida(false);
        }

        tarefaDAO.atualizarTarefa(tarefaParaAtualizar);
    }

    private static void excluirTarefa() {
        System.out.println("\n--- Excluir Tarefa ---");
        listarTarefas();
        System.out.print("Digite o ID da tarefa que deseja excluir: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        tarefaDAO.excluirTarefa(id);
    }

    private static void pressioneEnterParaContinuar() {
        System.out.println("\nPressione Enter para continuar...");
        scanner.nextLine();
    }
}