package com.walmartlabs.concord.server.user;

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

import com.walmartlabs.concord.server.api.org.team.TeamRole;
import com.walmartlabs.concord.server.api.user.UserEntry;
import com.walmartlabs.concord.server.org.team.TeamDao;
import com.walmartlabs.concord.server.org.team.TeamManager;
import com.walmartlabs.concord.server.security.UserPrincipal;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Named
public class UserManager {

    private final UserDao userDao;
    private final TeamDao teamDao;

    @Inject
    public UserManager(UserDao userDao, TeamDao teamDao) {
        this.userDao = userDao;
        this.teamDao = teamDao;
    }

    public UserEntry getOrCreate(String username) {
        return getOrCreate(username, null, false);
    }

    public UserEntry getOrCreate(String username, Set<String> permissions, boolean admin) {
        UUID id = userDao.getId(username);
        if (id == null) {
            return create(username, permissions, admin);
        }
        return userDao.get(id);
    }

    public Optional<UserEntry> get(UUID id) {
        return Optional.of(userDao.get(id));
    }

    public Optional<UUID> getId(String username) {
        UUID id = userDao.getId(username);
        return Optional.ofNullable(id);
    }

    public UserEntry create(String username, Set<String> permissions, boolean admin) {
        UUID id = userDao.insert(username, permissions, admin);

        // add the new user to the default org/team
        UUID teamId = TeamManager.DEFAULT_TEAM_ID;
        teamDao.addUser(teamId, id, TeamRole.MEMBER);

        return userDao.get(id);
    }

    public boolean isInOrganization(UUID orgId) {
        UserPrincipal p = UserPrincipal.getCurrent();
        UUID userId = p.getId();
        return userDao.isInOrganization(userId, orgId);
    }
}
