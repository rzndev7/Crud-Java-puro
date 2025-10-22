package com.example.crud;

import java.util.Objects;

public class Customer {
    private int id; // 0 = não atribuído (novo)
    private String nome;
    private String email;
    private String telefone;

    public Customer() {}

    public Customer(int id, String nome, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public Customer(String nome, String email, String telefone) {
        this(0, nome, email, telefone);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    @Override
    public String toString() {
        return String.format("[#%d] %s | %s | %s", id, nullSafe(nome), nullSafe(email), nullSafe(telefone));
    }

    private String nullSafe(String s) { return s == null ? "" : s; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return id == customer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}