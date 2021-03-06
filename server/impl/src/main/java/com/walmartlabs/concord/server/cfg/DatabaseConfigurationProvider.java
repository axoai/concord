package com.walmartlabs.concord.server.cfg;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 - 2018 Walmart Inc.
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

import com.walmartlabs.concord.db.DatabaseConfiguration;
import com.walmartlabs.ollie.config.Config;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

@Named
@Singleton
public class DatabaseConfigurationProvider implements Provider<DatabaseConfiguration> {

    @Inject
    @Config("db.url")
    private String url;

    @Inject
    @Config("db.appUsername")
    private String appUsername;

    @Inject
    @Config("db.appPassword")
    private String appPassword;

    @Inject
    @Config("db.inventoryUsername")
    private String inventoryUsername;

    @Inject
    @Config("db.inventoryPassword")
    private String inventoryPassword;

    @Inject
    @Config("db.maxPoolSize")
    private int maxPoolSize;

    @Override
    public DatabaseConfiguration get() {
        return new DatabaseConfiguration("org.postgresql.Driver", url,
                appUsername, appPassword, inventoryUsername, inventoryPassword, maxPoolSize);
    }
}
