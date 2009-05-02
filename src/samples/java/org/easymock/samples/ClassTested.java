/*
 * Copyright (c) 2001-2009 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.samples;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClassTested {

    private Set<Collaborator> listeners = new HashSet<Collaborator>();

    private Map<String, byte[]> documents = new HashMap<String, byte[]>();

    public void addListener(Collaborator listener) {
        listeners.add(listener);
    }

    public void addDocument(String title, byte[] document) {
        boolean documentChange = documents.containsKey(title);
        documents.put(title, document);
        if (documentChange) {
            notifyListenersDocumentChanged(title);
        } else {
            notifyListenersDocumentAdded(title);
        }
    }

    public boolean removeDocument(String title) {
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

    public boolean removeDocuments(String[] titles) {
        if (!listenersAllowRemovals(titles)) {
            return false;
        }

        for (String title : titles) {
            documents.remove(title);
            notifyListenersDocumentRemoved(title);
        }
        return true;
    }

    private void notifyListenersDocumentAdded(String title) {
        for (Collaborator listener : listeners) {
            listener.documentAdded(title);
        }
    }

    private void notifyListenersDocumentChanged(String title) {
        for (Collaborator listener : listeners) {
            listener.documentChanged(title);
        }
    }

    private void notifyListenersDocumentRemoved(String title) {
        for (Collaborator listener : listeners) {
            listener.documentRemoved(title);
        }
    }

    private boolean listenersAllowRemoval(String title) {
        int result = 0;
        for (Collaborator listener : listeners) {
            result += listener.voteForRemoval(title);
        }
        return result > 0;
    }

    private boolean listenersAllowRemovals(String[] titles) {
        int result = 0;
        for (Collaborator listener : listeners) {
            result += listener.voteForRemovals(titles);
        }
        return result > 0;
    }

}
