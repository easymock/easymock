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
 * Defines how arguments will be captured by a <tt>Capture</tt> object
 * 
 * @author Henri Tremblay
 * @see Capture
 */
public enum CaptureType {
    /**
     * Do not capture anything
     */
    NONE,

    /**
     * Will capture the argument of the first matching call
     */
    FIRST,

    /**
     * Will capture the argument of the last matching call
     */
    LAST,

    /**
     * Will capture, in order, the arguments of each matching calls
     */
    ALL
}
