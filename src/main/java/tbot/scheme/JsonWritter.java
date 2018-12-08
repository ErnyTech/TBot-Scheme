package tbot.scheme;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tbot.scheme.wrapper.ObjectWrapper;
import tbot.scheme.wrapper.VersionObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class JsonWritter {
    private final Gson gson = new Gson();
    private final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final List<Object> toJson = new ArrayList<>();
    private final WritterType writterType;

    public JsonWritter() {
        this.writterType = WritterType.classic;
        var tbotscheme = new TBotScheme();
        var objects = tbotscheme.getObjects();
        this.toJson.add(new VersionObject(this.writterType, tbotscheme.getBotApi()));
        this.toJson.addAll(Utils.patchObjects(objects, this.writterType).getObjectWrapperList());
        System.out.println("\t[OK] object wrapper ready!");
    }

    public JsonWritter(WritterType writterType) {
        this.writterType = writterType;
        var tbotscheme = new TBotScheme();
        var objects = tbotscheme.getObjects();
        this.toJson.add(new VersionObject(this.writterType, tbotscheme.getBotApi()));
        this.toJson.addAll(Utils.patchObjects(objects, this.writterType).getObjectWrapperList());
        System.out.println("\t[OK] object wrapper ready!");
    }

    public String toString() {
        return this.gson.toJson(this.toJson);
    }

    public String toStringPretty() {
        return this.gsonPretty.toJson(this.toJson);
    }

    public void write(Path path, String toWrite) throws IOException {
        System.out.println("\t[OK] start writing object to file...");
        Files.deleteIfExists(path);
        Files.write(path, toWrite.getBytes(), StandardOpenOption.CREATE_NEW);
    }

    public void write(Path path) throws IOException {
        write(path, toString());
    }

    public void writePretty(Path path) throws IOException {
        write(path, toStringPretty());
    }
}
