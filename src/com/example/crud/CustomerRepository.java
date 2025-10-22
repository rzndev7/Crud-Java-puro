package com.example.crud;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Repositório simples que persiste em CSV (sem libs externas).
 */
public class CustomerRepository {
    private final File csvFile;
    private final List<Customer> customers = new ArrayList<>();

    public CustomerRepository(File csvFile) throws IOException {
        this.csvFile = csvFile;
        if (!csvFile.getParentFile().exists()) {
            if (!csvFile.getParentFile().mkdirs()) {
                throw new IOException("Não foi possível criar diretório de dados: " + csvFile.getParent());
            }
        }
        if (!csvFile.exists()) {
            // cria com cabeçalho
            save();
        } else {
            load();
        }
    }

    public synchronized List<Customer> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(customers));
    }

    public synchronized Optional<Customer> findById(int id) {
        return customers.stream().filter(c -> c.getId() == id).findFirst();
    }

    public synchronized Customer add(Customer c) throws IOException {
        int nextId = nextId();
        c.setId(nextId);
        customers.add(c);
        save();
        return c;
    }

    public synchronized boolean update(Customer updated) throws IOException {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getId() == updated.getId()) {
                customers.set(i, updated);
                save();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean delete(int id) throws IOException {
        boolean removed = customers.removeIf(c -> c.getId() == id);
        if (removed) save();
        return removed;
    }

    private void load() throws IOException {
        customers.clear();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), StandardCharsets.UTF_8))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { // ignora cabeçalho
                    first = false;
                    if (line.trim().startsWith("id,")) continue;
                }
                if (line.trim().isEmpty()) continue;
                Customer c = parseCsvLine(line);
                if (c != null) customers.add(c);
            }
        }
    }

    private void save() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile, false), StandardCharsets.UTF_8))) {
            bw.write("id,nome,email,telefone\n");
            for (Customer c : customers) {
                bw.write(toCsvLine(c));
                bw.write('\n');
            }
        }
    }

    private int nextId() {
        int max = 0;
        for (Customer c : customers) {
            if (c.getId() > max) max = c.getId();
        }
        return max + 1;
    }

    // CSV simples com escape de aspas: " -> "". Não suporta vírgula.
    private String toCsv(String s) {
        if (s == null) return "";
        String escaped = s.replace("\"", "\"\"");
        return '"' + escaped + '"';
    }

    private String toCsvLine(Customer c) {
        return c.getId() + "," + toCsv(c.getNome()) + "," + toCsv(c.getEmail()) + "," + toCsv(c.getTelefone());
    }

    private Customer parseCsvLine(String line) {
        // parsing simples: id, "nome", "email", "telefone"
        // assume que cada campo string está entre aspas e aspas internas foram duplicadas
        try {
            int firstComma = line.indexOf(',');
            if (firstComma < 0) return null;
            int id = Integer.parseInt(line.substring(0, firstComma).trim());
            List<String> parts = parseQuotedCsv(line.substring(firstComma + 1));
            String nome = parts.size() > 0 ? parts.get(0) : "";
            String email = parts.size() > 1 ? parts.get(1) : "";
            String telefone = parts.size() > 2 ? parts.get(2) : "";
            return new Customer(id, nome, email, telefone);
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> parseQuotedCsv(String s) {
        List<String> out = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            // espera '"'
            while (i < s.length() && Character.isWhitespace(s.charAt(i))) i++;
            if (i < s.length() && s.charAt(i) == '"') {
                i++; // abre aspas
                StringBuilder sb = new StringBuilder();
                while (i < s.length()) {
                    char ch = s.charAt(i);
                    if (ch == '"') {
                        // pode ser fim de campo ou aspas escapada
                        if (i + 1 < s.length() && s.charAt(i + 1) == '"') {
                            sb.append('"');
                            i += 2; // consome aspas dupla
                        } else {
                            i++; // fecha aspas
                            break;
                        }
                    } else {
                        sb.append(ch);
                        i++;
                    }
                }
                out.add(sb.toString());
                // consome vírgula
                if (i < s.length() && s.charAt(i) == ',') i++;
            } else {
                // campo vazio/não esperado, procura próxima vírgula
                int next = s.indexOf(',', i);
                if (next < 0) next = s.length();
                out.add(s.substring(i, next).trim());
                i = next + 1;
            }
        }
        return out;
    }
}