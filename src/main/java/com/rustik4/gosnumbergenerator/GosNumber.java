package com.rustik4.gosnumbergenerator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GosNumber implements Iterator<GosNumber>, Comparable<GosNumber> {
    public static final String LETTERS = Arrays.stream(Letter.values()).map(Letter::toString).collect(Collectors.joining());
    public static final String REGION_PART = "116 RUS";
    public static final Pattern PATTERN = Pattern.compile(String.format("^[%s]\\d{3}[%s]{2}\\s%s$",
            LETTERS, LETTERS, REGION_PART));
    public static final int BASE = 1000;
    public static final GosNumber FIRST = new GosNumber(0);
    public static final int MAX = BASE * (LetterPart.BASE * LetterPart.BASE * LetterPart.BASE);
    private final int gosNumberCode;

    public GosNumber(int gosNumberCode) {
        this.gosNumberCode = gosNumberCode;
    }

    public GosNumber(String text) throws GosNumberGeneratorException {
        if (PATTERN.matcher(text).matches()) {
            var numberPart = Integer.parseInt(text.substring(1, 4));
            var letterPart = new LetterPart(text.charAt(0), text.charAt(4), text.charAt(5));
            this.gosNumberCode = letterPart.getCode() * BASE + numberPart;
        } else {
            throw new GosNumberGeneratorException("Given input doesn't match the pattern.");
        }
    }

    public int getGosNumberCode() {
        return gosNumberCode;
    }

    public int getNumberPart() {
        return gosNumberCode % BASE;
    }

    public LetterPart getLetterPart() {
        return new LetterPart(gosNumberCode / BASE);
    }

    @Override
    public boolean hasNext() {
        return gosNumberCode < MAX;
    }

    @Override
    public GosNumber next() {
        if (hasNext()) {
            return new GosNumber(gosNumberCode + 1);
        }
        return FIRST;
    }

    @Override
    public int compareTo(GosNumber o) {
        return Integer.compare(this.gosNumberCode, o.getGosNumberCode());
    }

    @Override
    public int hashCode() {
        return gosNumberCode;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GosNumber other && this.gosNumberCode == other.getGosNumberCode();
    }

    @Override
    public String toString() {
        return getLetterPart().getLeft().toString() + String.format("%03d", getNumberPart()) +
                getLetterPart().getRight1().toString() + getLetterPart().getRight2().toString() + ' ' + REGION_PART;
    }
}
