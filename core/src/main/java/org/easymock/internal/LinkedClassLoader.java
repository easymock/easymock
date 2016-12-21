package org.easymock.internal;

import java.util.Collection;
import java.util.LinkedList;

public class LinkedClassLoader extends ClassLoader {

    private final Collection<ClassLoader> classLoaders;

    public LinkedClassLoader() {
        super();
        classLoaders = new LinkedList<ClassLoader>();
    }

    public void addClassLoader(ClassLoader loader) {
        classLoaders.add(loader);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> clazz = null;
        for (ClassLoader classLoader : classLoaders) {
            try {
                clazz = classLoader.loadClass(name);
            } catch (ClassNotFoundException e) {
                //Retry with nextClassLoader in List
            }
        }
        if (clazz == null) {
            throw new ClassNotFoundException();
        }
        return clazz;
    }
}
