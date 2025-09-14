package eu.profinit.githubgitlabservice.mapper;

import eu.profinit.githubgitlabservice.dto.GitPropertiesRepositoriesInner;
import eu.profinit.githubgitlabservice.model.dto.github.GitHubProjectResponseDto;
import eu.profinit.githubgitlabservice.model.dto.gitlab.GitLabProjectResponseDto;
import eu.profinit.githubgitlabservice.model.entity.Project;
import eu.profinit.githubgitlabservice.model.internal.GitProject;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(target = "gitId", source = "id")
    @Mapping(target = "projectName", source = "name")
    @Mapping(target = "url", source = "webUrl")
    GitProject gitLabProjectToInternal(GitLabProjectResponseDto projectDto);

    @Mapping(target = "gitId", source = "id")
    @Mapping(target = "projectName", source = "name")
    @Mapping(target = "url", source = "htmlUrl")
    GitProject gitHubProjectToInternal(GitHubProjectResponseDto projectDto);

    @Mapping(target = "projectName", source = "name")
    GitProject entityToInternal(Project projectEntity);

    @Mapping(target = "id", source = "gitId")
    @Mapping(target = "name", source = "projectName")
    GitPropertiesRepositoriesInner internalToResponse(GitProject project);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "name", source = "projectName")
    void internalToEntity(GitProject dto, @MappingTarget Project project);

}
