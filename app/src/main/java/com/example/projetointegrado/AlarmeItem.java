package com.example.projetointegrado;

public class AlarmeItem {
    private int status;
    private int hora;
    private int minuto;
    private String nome;


    public AlarmeItem(int status, String nome, int hora, int minuto) {
        this.status = status;
        this.hora = hora;
        this.minuto = minuto;
        this.nome = nome;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHora() {
        return hora;
    }

    public int getMinuto() {
        return minuto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
