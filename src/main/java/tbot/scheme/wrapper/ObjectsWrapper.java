package tbot.scheme.wrapper;

import java.util.ArrayList;
import java.util.List;

public class ObjectsWrapper {
    private final List<ObjectWrapper> objects = new ArrayList<>();

    public void addObject(ObjectWrapper objectWrapper) {
        this.objects.add(objectWrapper);
    }

    public List<ObjectWrapper> getObjectWrapperList() {
        return this.objects;
    }
}
