package tbot.scheme;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tbot.scheme.wrapper.ObjectWrapper;
import tbot.scheme.wrapper.ObjectsWrapper;
import tbot.scheme.wrapper.ParameterWrapper;
import tbot.scheme.wrapper.ParametersWrapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class JsonWritter {
    private final Gson gson = new Gson();
    private final Gson gsonPretty = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final List<ObjectWrapper> toJson;
    private final WritterType writterType;

    public JsonWritter() {
        this.writterType = WritterType.classic;
        var tbotscheme = new TBotScheme();
        var objects = tbotscheme.getObjects();
        this.toJson = patchObjects(objects).getObjectWrapperList();
        System.out.println("\t[OK] object wrapper ready!");
    }

    public JsonWritter(WritterType writterType) {
        this.writterType = writterType;
        var tbotscheme = new TBotScheme();
        var objects = tbotscheme.getObjects();
        this.toJson = patchObjects(objects).getObjectWrapperList();
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

    private ObjectsWrapper patchObjects(ObjectsWrapper rawObjects) {
        var objects = new ObjectsWrapper();

        for (ObjectWrapper rawObject : rawObjects.getObjectWrapperList()) {
            var name = Utils.patchObjectName(rawObject.getObjectName(), this.writterType);
            ParametersWrapper parameters = new ParametersWrapper();
            String returnObject;

            for (ParameterWrapper rawParameter : rawObject.getParametersWrapper()) {
                var parameterName = Utils.patchParameterName(rawParameter.getName(), this.writterType);
                var parameterType = Utils.patchParameterType(rawParameter.getType(), rawParameter.getName(), rawParameter.getDescription(), this.writterType);
                parameters.addParameter(new ParameterWrapper(parameterName, parameterType, rawParameter.isRequired(), rawParameter.getDescription()));
            }

            if (this.writterType != WritterType.raw && rawObject.getObjectReturn() != null) {
                returnObject = Utils.patchParameterType(rawObject.getObjectReturn(), this.writterType);
            } else {
                returnObject = null;
            }

            var object = new ObjectWrapper(name, parameters, rawObject.isMethod(), returnObject);
            objects.addObject(object);
        }

        System.out.println("\t[OK] \""+ this.writterType + "\" patch successfully applied!");
        return objects;
    }
}
