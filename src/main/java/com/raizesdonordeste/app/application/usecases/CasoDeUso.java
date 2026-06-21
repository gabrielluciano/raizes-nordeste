package com.raizesdonordeste.app.application.usecases;

public interface CasoDeUso<INPUT, OUTPUT> {

    OUTPUT executar(INPUT input);
}
