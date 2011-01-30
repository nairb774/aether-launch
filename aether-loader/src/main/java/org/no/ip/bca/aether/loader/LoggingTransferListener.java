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

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.sonatype.aether.transfer.TransferEvent;
import org.sonatype.aether.transfer.TransferListener;

public class LoggingTransferListener implements TransferListener {
    private static final Logger LOGGER = Logger.getLogger(LoggingTransferListener.class.getName());
    
    public void transferCorrupted(final TransferEvent event) {
        logEvent(Level.WARNING, event);
    }
    
    private void logEvent(final Level level, final TransferEvent event) {
        final LogRecord logRecord = new LogRecord(level, "{0}");
        logRecord.setParameters(new Object[] { event });
        logRecord.setThrown(event.getException());
        LOGGER.log(logRecord);
    }
    
    public void transferFailed(final TransferEvent event) {
        logEvent(Level.FINE, event);
    }
    
    public void transferInitiated(final TransferEvent event) {
        logEvent(Level.FINER, event);
    }
    
    public void transferStarted(final TransferEvent event) {
        logEvent(Level.FINE, event);
    }
    
    public void transferProgressed(final TransferEvent event) {
        logEvent(Level.FINEST, event);
    }
    
    public void transferSucceeded(final TransferEvent event) {
        logEvent(Level.FINE, event);
    }
}
