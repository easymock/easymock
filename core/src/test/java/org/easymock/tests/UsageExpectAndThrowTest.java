/*
 * Copyright 2001-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.easymock.tests;

import static org.easymock.EasyMock.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author OFFIS, Tammo Freese
 */
class UsageExpectAndThrowTest {

    private IMethods mock;

    private static RuntimeException EXCEPTION = new RuntimeException();

    @BeforeEach
    void setup() {
        mock = createMock(IMethods.class);
    }

    @Test
    void booleanType() {
        expect(mock.booleanReturningMethod(4)).andThrow(EXCEPTION);
        replay(mock);
        try {
            mock.booleanReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void longType() {
        expect(mock.longReturningMethod(4)).andThrow(EXCEPTION);
        replay(mock);
        try {
            mock.longReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void floatType() {
        expect(mock.floatReturningMethod(4)).andThrow(EXCEPTION);
        replay(mock);
        try {
            mock.floatReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void doubleType() {
        expect(mock.doubleReturningMethod(4)).andThrow(EXCEPTION);
        replay(mock);
        try {
            mock.doubleReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void object() {
        expect(mock.objectReturningMethod(4)).andThrow(EXCEPTION);
        replay(mock);
        try {
            mock.objectReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void booleanAndRange() {
        expect(mock.booleanReturningMethod(4)).andThrow(EXCEPTION).once();
        replay(mock);
        try {
            mock.booleanReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void longAndRange() {
        expect(mock.longReturningMethod(4)).andThrow(EXCEPTION).once();
        replay(mock);
        try {
            mock.longReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void floatAndRange() {
        expect(mock.floatReturningMethod(4)).andThrow(EXCEPTION).once();
        replay(mock);
        try {
            mock.floatReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void doubleAndRange() {
        expect(mock.doubleReturningMethod(4)).andThrow(EXCEPTION).once();
        replay(mock);
        try {
            mock.doubleReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void objectAndRange() {
        expect(mock.objectReturningMethod(4)).andThrow(EXCEPTION).once();
        replay(mock);
        try {
            mock.objectReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void booleanAndCount() {
        expect(mock.booleanReturningMethod(4)).andThrow(EXCEPTION).times(2);
        replay(mock);
        try {
            mock.booleanReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        try {
            mock.booleanReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void longAndCount() {
        expect(mock.longReturningMethod(4)).andThrow(EXCEPTION).times(2);
        replay(mock);
        try {
            mock.longReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        try {
            mock.longReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void floatAndCount() {
        expect(mock.floatReturningMethod(4)).andThrow(EXCEPTION).times(2);
        replay(mock);
        try {
            mock.floatReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        try {
            mock.floatReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void doubleAndCount() {
        expect(mock.doubleReturningMethod(4)).andThrow(EXCEPTION).times(2);
        replay(mock);
        try {
            mock.doubleReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        try {
            mock.doubleReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void objectAndCount() {
        expect(mock.objectReturningMethod(4)).andThrow(EXCEPTION).times(2);
        replay(mock);
        try {
            mock.objectReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        try {
            mock.objectReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void booleanAndMinMax() {
        expect(mock.booleanReturningMethod(4)).andThrow(EXCEPTION).times(2, 3);
        replay(mock);
        try {
            mock.booleanReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        try {
            mock.booleanReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
        try {
            mock.booleanReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void longAndMinMax() {
        expect(mock.longReturningMethod(4)).andThrow(EXCEPTION).times(2, 3);
        replay(mock);
        try {
            mock.longReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        try {
            mock.longReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
        try {
            mock.longReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void floatAndMinMax() {
        expect(mock.floatReturningMethod(4)).andThrow(EXCEPTION).times(2, 3);
        replay(mock);
        try {
            mock.floatReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        try {
            mock.floatReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
        try {
            mock.floatReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void doubleAndMinMax() {
        expect(mock.doubleReturningMethod(4)).andThrow(EXCEPTION).times(2, 3);
        replay(mock);
        try {
            mock.doubleReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        try {
            mock.doubleReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
        try {
            mock.doubleReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

    @Test
    void objectAndMinMax() {
        expect(mock.objectReturningMethod(4)).andThrow(EXCEPTION).times(2, 3);
        replay(mock);
        try {
            mock.objectReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        try {
            mock.objectReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
        try {
            mock.objectReturningMethod(4);
            Assertions.fail();
        } catch (RuntimeException exception) {
            Assertions.assertSame(EXCEPTION, exception);
        }
        verify(mock);
    }

}
