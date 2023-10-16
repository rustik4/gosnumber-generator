package com.rustik4.gosnumbergenerator;

public class LetterPart {
    public static final int BASE = Letter.values().length;
    private final int code;

    public LetterPart(int code) {
        this.code = code;
    }

    public LetterPart(char leftCh, char right1Ch, char right2Ch) throws GosNumberGeneratorException {
        var left = Letter.findByChar(leftCh);
        var right1 = Letter.findByChar(right1Ch);
        var right2 = Letter.findByChar(right2Ch);
        this.code = left.ordinal() * BASE * BASE + right1.ordinal() * BASE + right2.ordinal();
    }

    public int getCode() {
        return code;
    }

    public Letter getLeft() {
        return Letter.values()[code / (BASE * BASE)];
    }

    public Letter getRight1() {
        return Letter.values()[code % (BASE * BASE) / BASE];
    }

    public Letter getRight2() {
        return Letter.values()[code % BASE];
    }
}
