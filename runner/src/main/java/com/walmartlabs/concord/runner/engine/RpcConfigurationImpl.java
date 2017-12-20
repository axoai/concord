package com.walmartlabs.concord.runner.engine;

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

import com.walmartlabs.concord.sdk.RpcConfiguration;

import javax.inject.Named;
import javax.inject.Singleton;

import static com.walmartlabs.concord.runner.ConfigurationUtils.getEnv;

@Named
@Singleton
public class RpcConfigurationImpl implements RpcConfiguration {

    private static final String SERVER_HOST_KEY = "rpc.server.host";
    private static final String SERVER_PORT_KEY = "rpc.server.port";

    private final String rpcServerHost;
    private final int rpcServerPort;

    public RpcConfigurationImpl() {
        this.rpcServerHost = getEnv(SERVER_HOST_KEY, "localhost");
        this.rpcServerPort = Integer.parseInt(getEnv(SERVER_PORT_KEY, "8101"));
    }

    @Override
    public String getServerHost() {
        return rpcServerHost;
    }

    @Override
    public int getServerPort() {
        return rpcServerPort;
    }
}
