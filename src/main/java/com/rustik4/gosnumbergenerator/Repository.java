package com.rustik4.gosnumbergenerator;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

public class Repository {
    private final SortedSet<GosNumber> issued = new TreeSet<>();
    private GosNumber current = GosNumber.FIRST;

    public boolean hasNext() {
        return current.hasNext();
    }

    public GosNumber next() throws FullRepositoryException {
        while (contains(current) && hasNext()) {
            current = current.next();
        }
        checkNext();
        GosNumber result = current;
        issued.add(result);
        current = current.next();
        return result;
    }

    public GosNumber random() throws FullRepositoryException {
        checkNext();
        int counter = 10;
        GosNumber result;
        int nextInt;
        do {
            nextInt = ThreadLocalRandom.current().nextInt(current.getGosNumberCode(), GosNumber.MAX);
            result = new GosNumber(nextInt);
            counter--;
        } while (contains(result) && counter > 0);
        // After trying random numbers let's seek free number nearest to result.
        if (contains(result)) {
            int oldNextInt = nextInt;
            do {
                nextInt--;
                result = new GosNumber(nextInt);
            } while (contains(result) && nextInt >= current.getGosNumberCode());
            if (contains(result)) {
                do {
                    oldNextInt++;
                    result = new GosNumber(oldNextInt);
                } while (contains(result) && oldNextInt <= GosNumber.MAX);
                if (oldNextInt == GosNumber.MAX) {
                    throw new FullRepositoryException();
                }
            }
        }
        issued.add(result);
        if (result.equals(current)) {
            do {
                current = current.next();
            } while (contains(current) && hasNext());
        }
        return result;
    }

    public void setCurrent(GosNumber current) {
        this.current = current;
    }


    public void clear() {
        current = GosNumber.FIRST;
        issued.clear();
    }

    private boolean contains(GosNumber gosNumber) {
        return issued.contains(gosNumber);
    }

    private void checkNext() throws FullRepositoryException {
        if (!hasNext()) {
            throw new FullRepositoryException();
        }
    }

}
