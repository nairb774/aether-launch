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

import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.providers.file.FileWagon;
import org.apache.maven.wagon.providers.http.LightweightHttpWagon;
import org.apache.maven.wagon.providers.http.LightweightHttpsWagon;
import org.sonatype.aether.connector.wagon.WagonProvider;

public class LightWagonProvider implements WagonProvider {
    public Wagon lookup(final String roleHint) throws Exception {
        if ("http".equals(roleHint)) {
            return new LightweightHttpWagon();
        } else if ("file".equals(roleHint)) {
            return new FileWagon();
        } else if ("https".equals(roleHint)) {
            return new LightweightHttpsWagon();
        }
        throw new IllegalStateException(roleHint);
    }
    
    public void release(final Wagon wagon) {
    }
}
