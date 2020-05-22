package com.example.projetointegrado;

public class AlarmeFixItem {
    private int status;
    private int hora;
    private int minuto;
    private int dosagem;
    private String nome;

    public AlarmeFixItem(int status, String nome, int dosagem, int hora, int minuto) {
        this.status = status;
        this.dosagem = dosagem;
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

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDosagem() {
        return dosagem;
    }

    public void setDosagem(int dosagem) {
        this.dosagem = dosagem;
    }
}
