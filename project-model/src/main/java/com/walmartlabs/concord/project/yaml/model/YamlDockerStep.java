package com.walmartlabs.concord.project.yaml.model;

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

import com.fasterxml.jackson.core.JsonLocation;

import java.util.Map;

public class YamlDockerStep extends YamlStep {

    private final String image;
    private final String cmd;
    private final boolean forcePull;
    private final boolean debug;
    private final Map<String, Object> env;

    public YamlDockerStep(JsonLocation location,
                          String image,
                          String cmd,
                          boolean forcePull,
                          boolean debug,
                          Map<String, Object> env) {

        super(location);

        this.image = image;
        this.cmd = cmd;
        this.forcePull = forcePull;
        this.debug = debug;
        this.env = env;
    }

    public String getImage() {
        return image;
    }

    public String getCmd() {
        return cmd;
    }

    public boolean isForcePull() {
        return forcePull;
    }

    public boolean isDebug() {
        return debug;
    }

    public Map<String, Object> getEnv() {
        return env;
    }
}
