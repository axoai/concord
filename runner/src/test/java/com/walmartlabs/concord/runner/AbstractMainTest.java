package com.walmartlabs.concord.runner;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 Wal-Mart Store, Inc.
 * -----
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =====
 */

import com.google.inject.Injector;
import com.walmartlabs.concord.common.IOUtils;
import com.walmartlabs.concord.project.InternalConstants;
import com.walmartlabs.concord.runner.engine.EngineFactory;
import com.walmartlabs.concord.runner.engine.NamedTaskRegistry;
import com.walmartlabs.concord.sdk.RpcClient;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.mock;

public abstract class AbstractMainTest {

    protected Main createMain(Injector injector, String instanceId, String resource) throws Exception {
        NamedTaskRegistry taskRegistry = new NamedTaskRegistry(injector, null);
        EngineFactory engineFactory = new EngineFactory(taskRegistry, mock(RpcClient.class));

        URI baseDir = this.getClass().getResource(resource).toURI();
        Path tmpDir = Files.createTempDirectory("test");
        IOUtils.copy(Paths.get(baseDir), tmpDir);
        System.setProperty("user.dir", tmpDir.toString());

        Path idPath = tmpDir.resolve(InternalConstants.Files.INSTANCE_ID_FILE_NAME);
        Files.write(idPath, instanceId.getBytes());

        return new Main(engineFactory, mock(ProcessHeartbeat.class));
    }
}
