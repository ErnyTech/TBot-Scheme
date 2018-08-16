package tbot.scheme.wrapper;

public class ParameterWrapper {
    private final String name;
    private final String type;
    private final boolean isRequired;
    private final String description;

    public ParameterWrapper(String name, String type, boolean isRequired, String description) {
        this.name = name;
        this.type = type;
        this.isRequired = isRequired;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public boolean isRequired() {
        return this.isRequired;
    }

    public String getDescription() {
        return this.description;
    }
}
