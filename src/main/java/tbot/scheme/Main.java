package tbot.scheme;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        info();

        switch (args.length) {
            case 0: {
                System.out.println("Generating scheme...");
                System.out.println(new JsonWritter().toStringPretty());
                break;
            }

            case 1: {
                checkCommand(args);
                write(args[0], WritterType.classic);
                break;
            }

            case 2: {
                checkCommand(args);
                switch (args[1]) {
                    case "classic": {
                        write(args[0], WritterType.classic);
                        break;
                    }

                    case "java": {
                        write(args[0], WritterType.java);
                        break;
                    }

                    case "javaprimitive": {
                        write(args[0], WritterType.javaprimitive);
                        break;
                    }

                    case "raw": {
                        write(args[0], WritterType.raw);
                        break;
                    }

                    default: {
                        info();
                        System.exit(1);
                    }
                }
            }
        }
    }

    private static void write(String pathStr, WritterType writterType) {
        var path = Paths.get(pathStr);
        System.out.println("Generating scheme and writing to \"" + path.toAbsolutePath() + "\" ...");

        try {
            new JsonWritter(writterType).writePretty(path);
        } catch (IOException e) {
            System.out.println("\t[ERROR] Writing scheme to \"" + path.toAbsolutePath() + "\" failed");
            System.exit(1);
        }

        System.out.println("\t[OK] Scheme successful writted to \"" + path.toAbsolutePath() + "\"");
    }

    private static void info() {
        var builder = new StringBuilder();
        builder.append("TBot Scheme");
        builder.append(System.lineSeparator());
        builder.append("Copyright 2018 Ernesto Castellotti <erny.castell@gmail.com>");
        builder.append(System.lineSeparator());
        System.out.println(builder);
    }
    private static void help() {
        var builder = new StringBuilder();
        builder.append(System.lineSeparator());
        builder.append("Usage: ");
        builder.append(System.lineSeparator());
        builder.append("\tjava -jar ").append(getJarPath()).append(" <path to write json>");
        builder.append(System.lineSeparator());
        builder.append("\t\tor: java -jar ").append(getJarPath()).append(" <path to write json> <writer type>");
        builder.append(System.lineSeparator());
        builder.append(System.lineSeparator());
        builder.append("The writter type can be: ");
        builder.append(System.lineSeparator());
        builder.append("\t - classic [default], TBot Scheme only apply small patch to scheme for improve use");
        builder.append(System.lineSeparator());
        builder.append("\t - java, TBot Scheme change scheme for improve use in Java project but don't use Java primitive");
        builder.append(System.lineSeparator());
        builder.append("\t - javaprimitive, TBot Scheme change scheme for improve use in Java project use Java primitive");
        builder.append(System.lineSeparator());
        builder.append("\t - raw, TBot Scheme don't apply any small patch to scheme");
        builder.append(System.lineSeparator());
        System.out.print(builder);
    }

    private static void checkCommand(String[] args) {
        if (args[0].equals("help")) {
            help();
            System.exit(0);
        }
    }

    private static String getJarPath() {
        String jarPath;

        try {
            jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException e) {
            return "\"" + "tbotscheme.jar" + "\"";
        }

        if (jarPath.contains(".jar")) {
            return "\"" + jarPath + "\"";
        } else {
            return "\"" + "tbotscheme.jar" + "\"";
        }
    }
}
