/*
 * Copyright 2001-2018 the original author or authors.
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
package org.easymock.internal;

/**
 * Android-specific support.
 */
public final class AndroidSupport {
    // ///CLOVER:OFF
    private static boolean isAndroid;
    static {
        try {
            Class.forName("dalvik.system.PathClassLoader");

            // Also verify that dexmaker is present, if not we might
            // be running under something like robolectric, which
            // means we should not use dexmaker
            Class.forName("org.droidparts.dexmaker.Code");

            isAndroid = true;
        } catch (ClassNotFoundException e) {
            isAndroid = false;
        }
    }

    public static boolean isAndroid() {
        return isAndroid;
    }
    // ///CLOVER:ON
}
