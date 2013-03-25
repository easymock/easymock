/**
 * Copyright 2001-2013 the original author or authors.
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
package org.easymock;

/**
 * See {@link EasyMock#cmp}
 * 
 * @author Henri Tremblay
 */
public enum LogicalOperator {
    LESS_THAN("<") {
        @Override
        public boolean matchResult(final int result) {
            return result < 0;
        }
    },
    LESS_OR_EQUAL("<=") {
        @Override
        public boolean matchResult(final int result) {
            return result <= 0;
        }
    },
    EQUAL("==") {
        @Override
        public boolean matchResult(final int result) {
            return result == 0;
        }
    },
    GREATER_OR_EQUAL(">=") {
        @Override
        public boolean matchResult(final int result) {
            return result >= 0;
        }
    },
    GREATER(">") {
        @Override
        public boolean matchResult(final int result) {
            return result > 0;
        }
    };

    private String symbol;

    private LogicalOperator(final String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public abstract boolean matchResult(int result);
}
