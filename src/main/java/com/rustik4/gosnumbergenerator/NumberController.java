package com.rustik4.gosnumbergenerator;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "number", produces = MediaType.TEXT_PLAIN_VALUE)
public class NumberController {
    private final Repository repository = new Repository();

    @GetMapping("/random")
    public String random() {
        GosNumber random;
        try {
            random = repository.random();
        } catch (FullRepositoryException e) {
            return e.getMessage();
        }
        return random.toString();
    }

    @GetMapping("/next")
    public String next() {
        GosNumber next;
        try {
            next = repository.next();
        } catch (FullRepositoryException e) {
            return e.getMessage();
        }
        return next.toString();
    }

    @GetMapping("/hasNext")
    public String hasNext() {
        return String.valueOf(repository.hasNext());
    }

    @PostMapping(value = "/setCurrent", consumes = MediaType.TEXT_PLAIN_VALUE)
    public String setCurrent(@RequestBody String value) {
        GosNumber gosNumber;
        try {
            gosNumber = new GosNumber(value);
        } catch (GosNumberGeneratorException e) {
            return e.getMessage();
        }
        repository.setCurrent(gosNumber);
        return String.format("Current registration number updated to %s.", value);
    }

    @GetMapping("/clear")
    public void clear() {
        repository.clear();
    }
}
