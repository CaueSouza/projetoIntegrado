package com.example.projetointegrado;

public class AlarmeFixItem {
    private int status;
    private int hora;
    private int minuto;
    private int dosagem;
    private int quantidade;
    private int quantidadeCaixa;
    private String nome;
    private int medTipo;

    public AlarmeFixItem(int status, String nome, int quantidade, int quantidadeCaixa, int hora, int minuto) {
        this.status = status;
        this.quantidade = quantidade;
        this.quantidadeCaixa = quantidadeCaixa;
        this.hora = hora;
        this.minuto = minuto;
        this.nome = nome;
        this.medTipo = 1;
    }

    public AlarmeFixItem(int status, String nome, int dosagem, int hora, int minuto) {
        this.status = status;
        this.dosagem = dosagem;
        this.hora = hora;
        this.minuto = minuto;
        this.nome = nome;
        this.medTipo = 2;
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

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQuantidadeCaixa() {
        return quantidadeCaixa;
    }

    public void setQuantidadeCaixa(int quantidadeCaixa) {
        this.quantidadeCaixa = quantidadeCaixa;
    }

    public int getMedTipo() {
        return medTipo;
    }

    public void setMedTipo(int medTipo) {
        this.medTipo = medTipo;
    }
}
