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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.maven.repository.internal.DefaultServiceLocator;
import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.collection.CollectRequest;
import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.connector.wagon.WagonProvider;
import org.sonatype.aether.connector.wagon.WagonRepositoryConnectorFactory;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.graph.DependencyNode;
import org.sonatype.aether.repository.LocalRepository;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.aether.spi.connector.RepositoryConnectorFactory;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.util.graph.PreorderNodeListGenerator;

public class AetherLoader {
    private static final Logger LOGGER = Logger.getLogger(AetherLoader.class.getName());
    
    private File localRepoLocation = new File("repo");
    
    public File getLocalRepoLocation() {
        return localRepoLocation;
    }
    
    public void setLocalRepoLocation(File localRepoLocation) {
        this.localRepoLocation = localRepoLocation;
    }
    
    private RepositorySystem getRepositorySystem() {
        DefaultServiceLocator locator = new DefaultServiceLocator();
        locator.setServices(WagonProvider.class, new LightWagonProvider());
        locator.addService(RepositoryConnectorFactory.class, WagonRepositoryConnectorFactory.class);
        
        return locator.getService(RepositorySystem.class);
    }
    
    public URL[] getClasspath(final String rootRepo, final String coord) throws DependencyCollectionException,
            ArtifactResolutionException, MalformedURLException {
        final Thread currentThread = Thread.currentThread();
        final ClassLoader orig = currentThread.getContextClassLoader();
        try {
            currentThread.setContextClassLoader(getClass().getClassLoader());
            return getClasspath0(rootRepo, coord);
        } finally {
            currentThread.setContextClassLoader(orig);
        }
    }
    
    private URL[] getClasspath0(final String rootRepoUrl, final String coord) throws DependencyCollectionException,
            ArtifactResolutionException, MalformedURLException {
        final RepositorySystem repositorySystem = getRepositorySystem();
        final MavenRepositorySystemSession repositorySession = new MavenRepositorySystemSession();
        
        final LocalRepository localRepository = new LocalRepository(localRepoLocation);
        repositorySession.setLocalRepositoryManager(repositorySystem.newLocalRepositoryManager(localRepository));
        repositorySession.setTransferListener(new LoggingTransferListener());
        
        final Dependency dependency = new Dependency(new DefaultArtifact(coord), "compile");
        final RemoteRepository rootRepo = new RemoteRepository("root-repo", "default", rootRepoUrl);
        
        final CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(dependency);
        collectRequest.addRepository(rootRepo);
        final DependencyNode node = repositorySystem.collectDependencies(repositorySession, collectRequest).getRoot();
        repositorySystem.resolveDependencies(repositorySession, node, null);
        final PreorderNodeListGenerator nodeListGenerator = new PreorderNodeListGenerator();
        node.accept(nodeListGenerator);
        final List<Artifact> artifacts = nodeListGenerator.getArtifacts(false);
        LOGGER.log(Level.CONFIG, "Using artifacts: {0}", artifacts);
        final URL[] urls = new URL[artifacts.size()];
        int i = 0;
        for (final Artifact artifact : artifacts) {
            urls[i++] = artifact.getFile().toURI().toURL();
        }
        return urls;
    }
}
