package com.example.projetointegrado;

public class CaixaItem {
    private String nome;
    private String IP;

    public CaixaItem(String nome, String IP) {
        this.nome = nome;
        this.IP = IP;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }
}
