package tbot.scheme.wrapper;

import tbot.scheme.WritterType;

public class VersionObject {
    private final String name;
    private final String schemeType;
    private final String version;

    public VersionObject(WritterType schemeType, String version) {
        this.schemeType = schemeType.name();
        this.version = version;

        if (schemeType == WritterType.java || schemeType == WritterType.javaprimitive) {
            this.name = "BotApi";
        } else {
            this.name = "bot_api";
        }
    }

    public String getName() {
        return this.name;
    }

    public String getSchemeType() {
        return this.schemeType;
    }

    public String getVersion() {
        return this.version;
    }
}
