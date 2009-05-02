/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.samples;

public interface Collaborator {
    void documentAdded(String title);

    void documentChanged(String title);

    void documentRemoved(String title);

    byte voteForRemoval(String title);

    byte voteForRemovals(String[] title);
}
