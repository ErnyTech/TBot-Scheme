package tbot.scheme;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tbot.scheme.wrapper.ObjectWrapper;
import tbot.scheme.wrapper.ObjectsWrapper;
import tbot.scheme.wrapper.ParameterWrapper;
import tbot.scheme.wrapper.ParametersWrapper;

import java.util.ArrayList;
import java.util.List;

public class TBotScheme {
    private final static String urlTBot = "https://core.telegram.org/bots/api";
    private final static String urlTBotScheme = "https://raw.githubusercontent.com/ErnyTech/TBot-Scheme/master/scheme/";
    private final Document document;
    private final List<String> objectNames = new ArrayList<>();
    private final ObjectsWrapper objectsWrapper = new ObjectsWrapper();

    public TBotScheme() {
        var htmlSource = Utils.getAsString(urlTBot);
        System.out.println("\t[OK] parsing html source...");
        this.document = Jsoup.parse(htmlSource).normalise();
    }

    public ObjectsWrapper getObjects() {
        var objectsNames = getObjectNames();
        setObjectNames(objectsNames);
        setObjectsWrapper(getBody());
        return this.objectsWrapper;
    }

    public static List<ObjectWrapper> getScheme(WritterType writterType) {
        var scheme = Utils.getAsStringHide(urlTBotScheme + "/" + writterType + ".json");
        var objectWrapperListTypeToken = new TypeToken<List<ObjectWrapper>>() {};
        return new Gson().fromJson(scheme, objectWrapperListTypeToken.getType());
    }

    private Element getBody() {
        return this.document.body();
    }

    private Elements getObjectNames() {
        var h4tags = getBody().getElementsByTag("h4");
        var objectNames = new Elements();

        for (int i = 3; i < h4tags.size(); i++) {
            if (h4tags.get(i).text().equals("Formatting options")) {
                continue;
            }

            if (h4tags.get(i).text().equals("Inline mode methods")) {
                continue;
            }

            if (h4tags.get(i).text().equals("CallbackGame")) {
                continue;
            }

            objectNames.add(h4tags.get(i));
        }

        return objectNames;
    }

    private void setObjectNames(Elements objectNames) {
        for (Element objectName : objectNames) {
            this.objectNames.add(objectName.text());
        }

        System.out.println("\t[OK] object names parsed!");
    }

    private void setObjectsWrapper(Element body) {
        for (String objectName : this.objectNames) {
            var table = getTableOfName(objectName, body.getAllElements());
            var desc = getMethodDesc(objectName, body.getAllElements());
            var isMethod = false;
            ParametersWrapper parameters;
            String methodReturn;

            if (table != null) {
                parameters = getParameters(table);
            } else {
                parameters = null;
            }

            if (desc != null) {
                methodReturn = getMethodReturn(desc);
            } else {
                methodReturn = null;
            }

            if (Character.isLowerCase(objectName.charAt(0))) {
                isMethod = true;
            }

            if (!isMethod) {
                this.objectsWrapper.addObject(new ObjectWrapper(objectName, parameters));
            } else {
                this.objectsWrapper.addObject(new ObjectWrapper(objectName, parameters, true, methodReturn));
            }
        }
        System.out.println("\t[OK] object parameters and returns parsed!");
    }

    private Element getTableOfName(String name, Elements allBodyElements) {
        for (int i = 0; i < allBodyElements.size(); i++) {
            var element = allBodyElements.get(i);

            if (element.tag().toString().equals("h4")) {
                if (element.text().equals(name)) {
                    return searchNextTable(allBodyElements, i);
                }
            }
        }

        return null;
    }

    private Element getMethodDesc(String name, Elements allBodyElements) {
        for (int i = 0; i < allBodyElements.size(); i++) {
            var element = allBodyElements.get(i);

            if (element.tag().toString().equals("h4")) {
                if (element.text().equals(name)) {
                    return searchNextP(allBodyElements, i);
                }
            }
        }

        return null;
    }

    private Element searchNextTable(Elements allBodyElements, int i) {
        int elementIndex = i + 1;

        while (true) {
            var table = allBodyElements.get(elementIndex);

            switch (table.tag().toString()) {
                case "h4": {
                    return null;
                }

                case "table": {
                    return table;
                }
            }

            elementIndex++;
        }
    }

    private Element searchNextP(Elements allBodyElements, int i) {
        int elementIndex = i + 1;

        while (true) {
            var p = allBodyElements.get(elementIndex);

            switch (p.tag().toString()) {
                case "h4": {
                    return null;
                }

                case "table": {
                    return null;
                }

                case "p": {
                    return p;
                }
            }

            elementIndex++;
        }
    }

    private ParametersWrapper getParameters(Element table) {
        var parameters = new ParametersWrapper();
        var tbody = table.getElementsByTag("tbody").get(0);
        var trs = tbody.getElementsByTag("tr");
        var requiredColumn = trs.get(0).getElementsByTag("td").get(2).text();
        var alwaysRequired = !requiredColumn.equals("Required");

        for (int i = 1; i < trs.size(); i++) {
            var tr = trs.get(i);
            var tds = tr.getElementsByTag("td");
            var name = tds.get(0).text();
            var type = tds.get(1).text();
            boolean isRequired;
            String description;

            if (alwaysRequired) {
                isRequired = true;
                description = tds.get(2).text();
            } else {
                isRequired = tds.get(2).text().equals("Yes");
                description = tds.get(3).text();
            }

            parameters.addParameter(new ParameterWrapper(name, type, isRequired, description));
        }

        return parameters;
    }

    private String getMethodReturn(Element methodDesc) {
        var texts = methodDesc.text().trim().split("\\.");

        for(String text : texts) {
            if (text.trim().equals("Returns True on success") || text.trim().equals("On success, True is returned")) {
                return "Boolean";
            }

            if (text.trim().equals("Returns Int on success")) {
                return "Integer";
            }

            if (text.trim().equals("Returns the new invite link as String on success")) {
                return "String";
            }

            if (text.trim().equals("On success, an array of the sent Messages is returned")) {
                return "Array of Message";
            }

            var returnTexts = text.split("\\.");

            for (String returnText : returnTexts) {
                if (returnText.contains("Returns") || returnText.contains("is returned") || returnText.contains("returns")) {
                    var words = returnText.split("\\s++");

                    for (String word : words) {
                        if (word.trim().isEmpty()) {
                            continue;
                        }

                        if (text.contains("Array of " + word)) {
                            return "Array of " + word;
                        }

                        if (this.objectNames.stream().anyMatch(str -> str.trim().equals(word))) {
                           return word;
                        }
                    }
                }
            }

        }

        return null;
    }
}
