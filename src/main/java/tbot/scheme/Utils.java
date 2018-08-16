package tbot.scheme;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Utils {
    public static String getAsString(String url) {
        return download(url);
    }

    private static String download(String url)  {
        System.out.println("\t[OK] downloading \"" + url + "\"...");
        try {
            var connection = new URL(url).openConnection();
            var scanner = new Scanner(connection.getInputStream()).useDelimiter("\\A");

            if (scanner.hasNext()) {
                System.out.println("\t[OK] downloaded \"" + url + "\" successful!");
                return scanner.next();
            } else {
                System.out.println("\t[ERROR] Failed when downloading \"" + url + "\"");
                System.exit(1);
                return null;
            }
        } catch (IOException e) {
            System.out.println("\t[ERROR] Failed when downloading \"" + url + "\"");
            System.exit(1);
            return null;
        }
    }

    public static String patchObjectName(String objectName, WritterType writterType) {
        switch (writterType) {
            case java: {
                return objectName.substring(0, 1).toUpperCase() + objectName.substring(1);
            }

            case javaprimitive: {
                return objectName.substring(0, 1).toUpperCase() + objectName.substring(1);
            }

            default: {
                return objectName;
            }
        }
    }

    public static String patchParameterName(String name, WritterType writterType) {
        switch (writterType) {
            case java: {
                return patchParameterNameJava(name);
            }

            case javaprimitive: {
                return patchParameterNameJava(name);
            }

            default: {
                return name;
            }
        }
    }

    public static String patchParameterType(String type, String name, String description, WritterType writterType) {
        switch (writterType) {
            case java: {
                if (name.contains("chatId") && type.equals("Integer")) {
                    return  "Long";
                }

                if (description.contains("This number may be greater than 32 bits and some programming languages may have difficulty/silent defects in interpreting it. But it is smaller than 52 bits, so a signed 64 bit integer or double-precision float type are safe for storing this identifier.")) {
                    return  "Long";
                }
            }

            case javaprimitive: {
                if (name.contains("chatId") && type.equals("Integer")) {
                    return  "long";
                }

                if (description.contains("This number may be greater than 32 bits and some programming languages may have difficulty/silent defects in interpreting it. But it is smaller than 52 bits, so a signed 64 bit integer or double-precision float type are safe for storing this identifier.")) {
                    return  "long";
                }
            }
        }

        return patchParameterType(type, writterType);
    }

    public static String patchParameterType(String type, WritterType writterType) {
        switch (writterType) {
            case classic: {
                if (type.equals("True")) {
                    return "Boolean";
                }

                return type;
            }

            case java: {
                if (type.equals("True")) {
                    type = "Boolean";
                }

                if (type.contains(" or String")) {
                    return type.replace(" or String", "");
                }



                if (type.matches("Array of (.*)")) {
                    type = type.replace("Array of ", "");
                    return  "List<" + type + ">";
                }

                return type.substring(0, 1).toUpperCase() + type.substring(1);
            }

            case javaprimitive: {
                if (type.equals("True")) {
                    type = "boolean";
                }

                if (type.contains(" or String")) {
                    return type.replace(" or String", "");
                }

                if (type.matches("Array of (.*)")) {
                    type = type.replace("Array of ", "");
                    return  "List<" + type + ">";
                }

                switch (type) {
                    case "Integer": {
                        return "int";
                    }

                    case "Boolean": {
                        return "boolean";
                    }
                }

                return type.substring(0, 1).toUpperCase() + type.substring(1);
            }

            default: {
                return type;
            }
        }
    }

    private static String patchParameterNameJava(String parameterName) {
        parameterName = parameterName.replace("_", " ");
        var nameBuilder = new StringBuilder();
        var names = parameterName.split("\\s+");

        for (int index = 0; index < names.length; index++) {
            if (index != 0) {
                nameBuilder.append(names[index].substring(0, 1).toUpperCase()).append(names[index].substring(1));
            } else {
                nameBuilder.append(names[index]);
            }
        }

        return nameBuilder.toString();
    }
}
