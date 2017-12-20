package com.walmartlabs.concord.server.api;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmartlabs.concord.server.api.user.CreateUserRequest;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class SerializationTest {

    @Test
    public void testRoundtrip() throws Exception {
        String src = "{\"username\":\"myUser\",\"permissions\":[\"project:myProject\",\"process:*:myProject\"]}";

        // ---

        ObjectMapper om = new ObjectMapper();
        CreateUserRequest req = om.readValue(src, CreateUserRequest.class);

        assertEquals("myUser", req.getUsername());

        // ---

        Set<String> s = req.getPermissions();
        assertNotNull(s);
        assertEquals(2, s.size());

        assertTrue(s.contains("project:myProject"));
        assertTrue(s.contains("process:*:myProject"));

        // ---

        om.writeValueAsString(req);
    }
}
