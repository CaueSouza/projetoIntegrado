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
    private int domingo;
    private int segunda;
    private int terca;
    private int quarta;
    private int quinta;
    private int sexta;
    private int sabado;

    public AlarmeFixItem(int status, String nome, int quantidade, int quantidadeCaixa, int hora, int minuto, int[] dias) {
        this.status = status;
        this.quantidade = quantidade;
        this.quantidadeCaixa = quantidadeCaixa;
        this.hora = hora;
        this.minuto = minuto;
        this.nome = nome;
        this.medTipo = 1;
        this.domingo = dias[0];
        this.segunda = dias[1];
        this.terca = dias[2];
        this.quarta = dias[3];
        this.quinta = dias[4];
        this.sexta = dias[5];
        this.sabado = dias[6];
    }

    public AlarmeFixItem(int status, String nome, int dosagem, int hora, int minuto, int[] dias) {
        this.status = status;
        this.dosagem = dosagem;
        this.hora = hora;
        this.minuto = minuto;
        this.nome = nome;
        this.medTipo = 2;
        this.domingo = dias[0];
        this.segunda = dias[1];
        this.terca = dias[2];
        this.quarta = dias[3];
        this.quinta = dias[4];
        this.sexta = dias[5];
        this.sabado = dias[6];
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

    public int getDomingo() {
        return domingo;
    }

    public void setDomingo(int domingo) {
        this.domingo = domingo;
    }

    public int getSegunda() {
        return segunda;
    }

    public void setSegunda(int segunda) {
        this.segunda = segunda;
    }

    public int getTerca() {
        return terca;
    }

    public void setTerca(int terca) {
        this.terca = terca;
    }

    public int getQuarta() {
        return quarta;
    }

    public void setQuarta(int quarta) {
        this.quarta = quarta;
    }

    public int getQuinta() {
        return quinta;
    }

    public void setQuinta(int quinta) {
        this.quinta = quinta;
    }

    public int getSexta() {
        return sexta;
    }

    public void setSexta(int sexta) {
        this.sexta = sexta;
    }

    public int getSabado() {
        return sabado;
    }

    public void setSabado(int sabado) {
        this.sabado = sabado;
    }
}
