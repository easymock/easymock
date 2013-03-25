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
package org.easymock.samples;

import java.util.HashMap;
import java.util.Map;

/**
 * @author OFFIS, Tammo Freese
 */
public class ClassTested {

    private Collaborator listener;

    private final Map<String, byte[]> documents = new HashMap<String, byte[]>();

    public void setListener(final Collaborator listener) {
        this.listener = listener;
    }

    public void addDocument(final String title, final byte[] document) {
        final boolean documentChange = documents.containsKey(title);
        documents.put(title, document);
        if (documentChange) {
            notifyListenersDocumentChanged(title);
        } else {
            notifyListenersDocumentAdded(title);
        }
    }

    public boolean removeDocument(final String title) {
        if (!documents.containsKey(title)) {
            return true;
        }

        if (!listenersAllowRemoval(title)) {
            return false;
        }

        documents.remove(title);
        notifyListenersDocumentRemoved(title);

        return true;
    }

    public boolean removeDocuments(final String... titles) {
        if (!listenersAllowRemovals(titles)) {
            return false;
        }

        for (final String title : titles) {
            documents.remove(title);
            notifyListenersDocumentRemoved(title);
        }
        return true;
    }

    private void notifyListenersDocumentAdded(final String title) {
        listener.documentAdded(title);
    }

    private void notifyListenersDocumentChanged(final String title) {
        listener.documentChanged(title);
    }

    private void notifyListenersDocumentRemoved(final String title) {
        listener.documentRemoved(title);
    }

    private boolean listenersAllowRemoval(final String title) {
        return listener.voteForRemoval(title) > 0;
    }

    private boolean listenersAllowRemovals(final String... titles) {
        return listener.voteForRemovals(titles) > 0;
    }

}
