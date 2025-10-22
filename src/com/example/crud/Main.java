package com.example.crud;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final String DATA_PATH = "data/customers.csv";

    public static void main(String[] args) {
        File csv = new File(DATA_PATH);
        try {
            CustomerRepository repo = new CustomerRepository(csv);
            runCli(repo);
        } catch (IOException e) {
            System.err.println("Erro ao iniciar repositório: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void runCli(CustomerRepository repo) {
        Scanner sc = new Scanner(System.in, java.nio.charset.Charset.defaultCharset());
        while (true) {
            printMenu();
            System.out.print("Escolha: ");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": listar(repo); break;
                    case "2": criar(sc, repo); break;
                    case "3": buscar(sc, repo); break;
                    case "4": atualizar(sc, repo); break;
                    case "5": remover(sc, repo); break;
                    case "0": System.out.println("Saindo..."); return;
                    default: System.out.println("Opção inválida");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("==== CRUD de Clientes ====");
        System.out.println("1) Listar");
        System.out.println("2) Cadastrar");
        System.out.println("3) Buscar por ID");
        System.out.println("4) Atualizar");
        System.out.println("5) Remover");
        System.out.println("0) Sair");
    }

    private static void listar(CustomerRepository repo) {
        List<Customer> all = repo.findAll();
        if (all.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        all.forEach(System.out::println);
    }

    private static void criar(Scanner sc, CustomerRepository repo) throws IOException {
        System.out.print("Nome: ");
        String nome = sc.nextLine().trim();
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Telefone: ");
        String telefone = sc.nextLine().trim();
        Customer criado = repo.add(new Customer(nome, email, telefone));
        System.out.println("Criado: " + criado);
    }

    private static void buscar(Scanner sc, CustomerRepository repo) {
        int id = readInt(sc, "ID: ");
        Optional<Customer> opt = repo.findById(id);
        if (opt.isPresent()) {
            System.out.println(opt.get());
        } else {
            System.out.println("Não encontrado.");
        }
    }

    private static void atualizar(Scanner sc, CustomerRepository repo) throws IOException {
        int id = readInt(sc, "ID: ");
        Optional<Customer> opt = repo.findById(id);
        if (opt.isEmpty()) {
            System.out.println("Não encontrado.");
            return;
        }
        Customer atual = opt.get();
        System.out.println("Atual: " + atual);
        System.out.print("Novo nome (enter para manter): ");
        String nome = sc.nextLine();
        System.out.print("Novo email (enter para manter): ");
        String email = sc.nextLine();
        System.out.print("Novo telefone (enter para manter): ");
        String telefone = sc.nextLine();
        if (!nome.isBlank()) atual.setNome(nome.trim());
        if (!email.isBlank()) atual.setEmail(email.trim());
        if (!telefone.isBlank()) atual.setTelefone(telefone.trim());
        boolean ok = repo.update(atual);
        System.out.println(ok ? "Atualizado." : "Falha ao atualizar.");
    }

    private static void remover(Scanner sc, CustomerRepository repo) throws IOException {
        int id = readInt(sc, "ID: ");
        boolean ok = repo.delete(id);
        System.out.println(ok ? "Removido." : "Não encontrado.");
    }

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Número inválido.");
            }
        }
    }
}