/*
 * Copyright 2011 Brian Atkinson
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

package org.no.ip.bca.aether.runner;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class Main {
    private static Main getRun(String[] args) throws Exception {
        final File libFolder = new File("lib");
        final File[] files = libFolder.listFiles();
        final URL[] urls = new URL[files.length];
        for (int i = 0; i < files.length; i++) {
            urls[i] = files[i].toURI().toURL();
        }
        final URLClassLoader classLoader = new URLClassLoader(urls);
        return (Main) Class.forName("org.no.ip.bca.aether.loader.MainLoader", false, classLoader).getMethod("getRun",
                String[].class).invoke(null, new Object[] { args });
    }
    
    public static void main(String[] args) throws Throwable {
        getRun(args).run();
    }
    
    private final String[] args;
    private final Method method;
    
    public Main(final String[] args, final Method method) {
        this.args = args;
        this.method = method;
    }
    
    public void run() throws Throwable {
        Thread.currentThread().setContextClassLoader(method.getDeclaringClass().getClassLoader());
        try {
            method.invoke(null, new Object[] { args });
        } catch (final InvocationTargetException e) {
            if (e.getCause() != null) {
                throw e.getCause();
            }
            throw e;
        }
    }
}
