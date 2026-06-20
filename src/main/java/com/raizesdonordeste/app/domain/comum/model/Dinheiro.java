package com.raizesdonordeste.app.domain.comum.model;

public record Dinheiro(long centavos) {

    public Dinheiro somar(Dinheiro d) {
        return new Dinheiro(Math.addExact(centavos, d.centavos));
    }

    public Dinheiro subtrair(Dinheiro d) {
        return new Dinheiro(Math.subtractExact(centavos, d.centavos));
    }

    public Dinheiro porcentagem(double porcentagem) {
        if (porcentagem < 0.0 || porcentagem > 100.0) {
            throw new IllegalArgumentException("percentual deve ser um valor entre 0.0 e 100.0");
        }

        long resultado = Math.round(centavos * (porcentagem / 100.0));

        return new Dinheiro(resultado);
    }

    public Dinheiro multiplicar(int multiplicador) {
        return new Dinheiro(Math.multiplyExact(centavos, multiplicador));
    }

    public Dinheiro multiplicar(float multiplicador) {
        return new Dinheiro(Math.round(centavos * multiplicador));
    }

    public Dinheiro dividir(int divisor) {
        return new Dinheiro(Math.divideExact(centavos, divisor));
    }

    public static Dinheiro ZERO = new Dinheiro(0);
}
