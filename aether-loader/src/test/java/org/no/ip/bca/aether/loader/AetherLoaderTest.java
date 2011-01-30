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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class AetherLoaderTest {
    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();
    
    @Test
    public void foo() throws Exception {
        final AetherLoader aetherLoader = new AetherLoader();
        aetherLoader.setLocalRepoLocation(temporaryFolder.getRoot());
        aetherLoader.getClasspath("file:///home/brian/.m2/repository/", "org.no-ip.bca:web-scripts:0.1-SNAPSHOT");
    }
    
    @Test
    public void testResolution() throws Exception {
        final AetherLoader aetherLoader = new AetherLoader();
        aetherLoader.setLocalRepoLocation(temporaryFolder.getRoot());
        aetherLoader.getClasspath("http://repo1.maven.org/maven2/", "org.sonatype.aether:aether-api:1.9");
    }
}
