package com.htp;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MySimpleException extends Exception {
    public MySimpleException(String message) {
        super(message);
    }
}
