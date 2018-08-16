package tbot.scheme.wrapper;

import java.util.ArrayList;
import java.util.List;

public class ObjectWrapper {
    private final String name;
    private final List<ParameterWrapper> parameters = new ArrayList<>();
    private final boolean isMethod;
    private final String objectReturn;

    public ObjectWrapper(String objectName, ParametersWrapper parametersWrapper) {
        this.name = objectName;

        if (parametersWrapper != null) {
            this.parameters.addAll(parametersWrapper.getParameterWrapperList());
        }

        this.isMethod = false;
        this.objectReturn = null;
    }

    public ObjectWrapper(String objectName, ParametersWrapper parametersWrapper, boolean isMethod, String objectReturn) {
        this.name = objectName;

        if (parametersWrapper != null) {
            this.parameters.addAll(parametersWrapper.getParameterWrapperList());
        }

        if (objectReturn != null) {
            this.isMethod = isMethod;
        } else {
            this.isMethod = false;
        }

        if (isMethod) {
            this.objectReturn = objectReturn;
        } else {
            this.objectReturn = null;
        }
    }

    public String getObjectName() {
        return this.name;
    }

    public List<ParameterWrapper> getParametersWrapper() {
        return this.parameters;
    }

    public boolean isMethod() {
        return this.isMethod;
    }

    public String getObjectReturn() {
        return this.objectReturn;
    }
}
