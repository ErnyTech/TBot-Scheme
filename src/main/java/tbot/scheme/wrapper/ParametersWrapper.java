package tbot.scheme.wrapper;

import java.util.ArrayList;
import java.util.List;

public class ParametersWrapper {
    private List<ParameterWrapper> parameterWrapperList = new ArrayList<>();

    public void addParameter(ParameterWrapper parameterWrapper) {
        this.parameterWrapperList.add(parameterWrapper);
    }

    public List<ParameterWrapper> getParameterWrapperList() {
        return this.parameterWrapperList;
    }
}
