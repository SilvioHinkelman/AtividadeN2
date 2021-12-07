package br.com.silviohinkelman.atividadesilvio;

public class Cars {

    private String id;
    public String marca, modelo, cor, valor, telefone, idUsuario;

    public Cars() {
    }

//    public Cars(String id, String marca, String modelo, String cor, String valor, String telefone) {
//        this.id = id;
//        this.marca = marca;
//        this.modelo = modelo;
//        this.cor = cor;
//        this.valor = valor;
//        this.telefone = telefone;
//    }

    public Cars(String id, String marca, String modelo, String cor, String valor, String telefone, String idUsuario) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.cor = cor;
        this.valor = valor;
        this.telefone = telefone;
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return marca + " - " +
                modelo + " - " +
                cor + "\n" +
                "Valor: " + valor + ",00 R$\n" +
                "Telefone: " + telefone ;   //apagarLinha
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
