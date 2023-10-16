package com.rustik4.gosnumbergenerator;

public enum Letter {
    A('А'),
    B('В'),
    E('Е'),
    K('К'),
    M('М'),
    H('Н'),
    O('О'),
    P('Р'),
    C('С'),
    T('Т'),
    Y('У'),
    X('Х');

    private final char rusLetter;

    Letter(char rusLetter) {
        this.rusLetter = rusLetter;
    }

    public static Letter findByChar(char ch) throws GosNumberGeneratorException {
        for (Letter letter : values()) {
            if (letter.rusLetter == ch) {
                return letter;
            }
        }
        throw new GosNumberGeneratorException(String.format("Char %s is incorrect.", ch));
    }

    @Override
    public String toString() {
        return String.valueOf(rusLetter);
    }
}
