package eu.profinit.githubgitlabservice.mapper;

import eu.profinit.githubgitlabservice.dto.GitProperties;
import eu.profinit.githubgitlabservice.dto.UserResponse;
import eu.profinit.githubgitlabservice.model.dto.github.GitHubResponseDto;
import eu.profinit.githubgitlabservice.model.dto.gitlab.GitLabResponseDto;
import eu.profinit.githubgitlabservice.model.entity.User;
import eu.profinit.githubgitlabservice.model.enums.SourceEnum;
import eu.profinit.githubgitlabservice.model.internal.GitUserWithProjects;
import org.mapstruct.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring", uses = {ProjectMapper.class})
public interface UserMapper {

    @Mapping(target = "gitId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "url", source = "user.webUrl")
    @Mapping(target = "lastUpdate", source = "updated")
    @Mapping(target = "projects", source = "projects")
    GitUserWithProjects gitLabUserToInternal(GitLabResponseDto responseDto);

    @Mapping(target = "gitId", source = "id")
    @Mapping(target = "username", source = "login")
    @Mapping(target = "url", source = "htmlUrl")
    @Mapping(target = "lastUpdate", expression = "java(java.time.LocalDateTime.now().toString())")
    @Mapping(target = "projects", source = "projects")
    GitUserWithProjects gitHubUserToInternal(GitHubResponseDto responseDto);

    @Mapping(target = "username", source = "username.name")
    @Mapping(target = "lastUpdate", expression = "java(userEntity.getLastUpdate() != null ? userEntity.getLastUpdate().toString() : null)")
    @Mapping(target = "projects", source = "projects")
    GitUserWithProjects entityToInternal(User userEntity);

    @Mapping(target = "id", source = "userWithProjects.gitId")
    @Mapping(target = "username", source = "userWithProjects.username")
    @Mapping(target = "repositories", source = "userWithProjects.projects")
    @Mapping(target = "source", source = "source")
    GitProperties internalToResponse(SourceEnum source, GitUserWithProjects userWithProjects);

    default List<GitProperties> internalToResponseList(Map<SourceEnum, GitUserWithProjects> usersWithProjects) {
        if (usersWithProjects == null) {
            return Collections.emptyList();
        }

        List<GitProperties> result = new ArrayList<>();
        for (Map.Entry<SourceEnum, GitUserWithProjects> entry : usersWithProjects.entrySet()) {
            result.add(internalToResponse(entry.getKey(), entry.getValue()));
        }

        return result;
    }

    @Mapping(target = "data", source = "users")
    UserResponse internalMapToUserResponse(Map<SourceEnum, GitUserWithProjects> users);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "source", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "lastUpdate", source = "lastUpdate")
    void internalToEntity(GitUserWithProjects dto, @MappingTarget User user);


    default LocalDateTime mapLastUpdate(String lastUpdate) {
        if (lastUpdate == null || lastUpdate.isBlank()) {
            return LocalDateTime.now();
        }

        try {
            return Instant.parse(lastUpdate)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (DateTimeParseException ignored) {}

        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            return LocalDateTime.parse(lastUpdate, inputFormatter);
        } catch (DateTimeParseException ignored) {}

        return LocalDateTime.now();
    }

}
