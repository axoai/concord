package com.walmartlabs.concord.server.cfg;

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

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Named
@Singleton
public class TemplateConfiguration {

    public static final String CACHE_DIR_KEY = "TEMPLATE_CACHE_DIR";

    private final Path cacheDir;

    public TemplateConfiguration() throws IOException {
        String s = System.getenv(CACHE_DIR_KEY);
        this.cacheDir = s != null ? Paths.get(s) : Files.createTempDirectory("templateCache");
    }

    public Path getCacheDir() {
        return cacheDir;
    }
}
