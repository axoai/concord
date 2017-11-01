package com.walmartlabs.concord.server.api.landing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.walmartlabs.concord.common.validation.ConcordKey;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

@JsonInclude(Include.NON_NULL)
public class LandingEntry implements Serializable {

    private final UUID id;

    @NotNull
    @ConcordKey
    private final String projectName;

    @NotNull
    @ConcordKey
    private final String repositoryName;

    @NotNull
    @Size(max = 128)
    private final String name;

    @Size(max = 512)
    private final String description;

    private final String icon;

    @JsonCreator
    public LandingEntry(@JsonProperty("id") UUID id,
                        @JsonProperty("projectName") String projectName,
                        @JsonProperty("repositoryName") String repositoryName,
                        @JsonProperty("name") String name,
                        @JsonProperty("description") String description,
                        @JsonProperty("icon") String icon) {

        this.id = id;
        this.projectName = projectName;
        this.repositoryName = repositoryName;
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public UUID getId() {
        return id;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return "LandingEntry{" +
                "id=" + id +
                ", projectName=" + projectName + '\'' +
                ", repositoryName=" + repositoryName + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}