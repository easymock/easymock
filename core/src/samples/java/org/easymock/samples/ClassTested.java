/**
 * Copyright 2001-2015 the original author or authors.
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

    private final Map<String, String> documents = new HashMap<String, String>();

    public void setListener(Collaborator listener) {
        this.listener = listener;
    }

    public void addDocument(String title, String content) {
        boolean documentChange = documents.containsKey(title);
        documents.put(title, content);
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

    public boolean removeDocuments(String... titles) {
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
        listener.documentAdded(title);
    }

    private void notifyListenersDocumentChanged(String title) {
        listener.documentChanged(title);
    }

    private void notifyListenersDocumentRemoved(String title) {
        listener.documentRemoved(title);
    }

    private boolean listenersAllowRemoval(String title) {
        return listener.voteForRemoval(title) > 0;
    }

    private boolean listenersAllowRemovals(String... titles) {
        return listener.voteForRemovals(titles) > 0;
    }

}
