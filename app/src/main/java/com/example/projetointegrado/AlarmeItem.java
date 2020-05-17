package com.example.projetointegrado;

public class AlarmeItem {
    private int imagem;
    private String horario;
    private String nome;

    public AlarmeItem(String nome, int imagem, String horario) {
        this.imagem = imagem;
        this.horario = horario;
        this.nome = nome;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
