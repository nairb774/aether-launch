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

package org.no.ip.bca.aether.loader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.no.ip.bca.aether.runner.Main;
import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.resolution.ArtifactResolutionException;

public class MainLoader {
    private static String[] getRealArgs(final String[] args) {
        final String[] realArgs = new String[args.length - 2];
        System.arraycopy(args, 2, realArgs, 0, realArgs.length);
        return realArgs;
    }
    
    public static Main getRun(final String[] args) throws Exception {
        final String artifact = args[0];
        final String className = args[1];
        return new Main(getRealArgs(args), Class.forName(className, false, getClassloader(artifact)).getMethod("main",
                String[].class));
    }
    
    private static URLClassLoader getClassloader(final String artifact) throws DependencyCollectionException,
            ArtifactResolutionException, MalformedURLException {
        final String mainRepo;
        final String coords;
        if (artifact.indexOf('/') == -1) {
            mainRepo = "http://repo1.maven.org/maven2/";
            coords = artifact;
        } else {
            final int lastIndexOf = artifact.lastIndexOf('/');
            mainRepo = artifact.substring(0, lastIndexOf + 1);
            coords = artifact.substring(lastIndexOf + 1);
        }
        final URL[] classpath = new AetherLoader().getClasspath(mainRepo, coords);
        final URLClassLoader urlClassLoader = new URLClassLoader(classpath);
        return urlClassLoader;
    }
}
