package org.easymock.java8;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.stream.Stream;

class NewGatherers {

    @Test
    void test() {
        System.out.println(Stream.of(5,4,2,1,6,12,8,9)
            .gather(new BiggestInt(11))
            .findFirst()
            .get());

    }
}
