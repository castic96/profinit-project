package eu.profinit.githubgitlabservice.model.enums;

public enum SourceEnum {
    GITHUB("github"),
    GITLAB("gitlab");

    private final String sourceName;

    SourceEnum(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceName() {
        return sourceName;
    }

    @Override
    public String toString() {
        return sourceName;
    }
}
