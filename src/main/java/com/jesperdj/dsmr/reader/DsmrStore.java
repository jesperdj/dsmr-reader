package com.jesperdj.dsmr.reader;

import java.util.function.Consumer;

public class DsmrStore implements Consumer<String> {

    @Override
    public void accept(String message) {
        // TODO: parse date out of message
        // line looks like this: 0-0:1.0.0(181104123918W)
        // number between parenthesis is a timestamp (yyMMddHHmmss'W')
        // append message to a file named 20yyMMdd.dsmr; create file if it does not yet exist
    }
}
