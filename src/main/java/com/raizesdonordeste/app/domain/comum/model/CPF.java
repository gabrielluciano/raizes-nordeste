package com.raizesdonordeste.app.domain.comum.model;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;

public record CPF(String valor) {

    private static final Pattern FORMAT_REGEX = Pattern.compile("^\\d{11}$");

    private static final Set<String> CPF_INVALIDOS = Set.of(
            "00000000000",
            "11111111111",
            "22222222222",
            "33333333333",
            "44444444444",
            "55555555555",
            "66666666666",
            "77777777777",
            "88888888888",
            "99999999999"
    );

    public CPF {
        if (!valido(valor)) {
            throw new IllegalArgumentException("cpf inválido.");
        }
    }

    public static boolean valido(String cpf) {
        return (
                cpf != null
                        && FORMAT_REGEX.matcher(cpf).matches()
                        && !CPF_INVALIDOS.contains(cpf)
                        && dvValido(cpf)
        );
    }

    private static boolean dvValido(String cpf) {
        final int[] digits = Arrays.stream(cpf.split(""))
                .mapToInt(Integer::parseInt)
                .toArray();

        int sum1 = 0;
        for (int i = 10; i >= 2; i--) {
            sum1 += digits[10 - i] * i;
        }
        int div1 = (sum1 * 10) % 11;
        int d1 = div1 == 10 ? 0 : div1;

        int sum2 = 0;
        for (int i = 0; i < 9; i++) {
            sum2 += digits[i] * (11 - i);
        }
        sum2 += d1 * 2;
        int div2 = (sum2 * 10) % 11;
        int d2 = div2 == 10 ? 0 : div2;

        return d1 == digits[9] && d2 == digits[10];
    }
}
