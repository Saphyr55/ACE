package org.utours.ace.common.notify;

public interface Observable<T> {

    void addProperty(Property<T> property);
    void removeProperty(Property<T> property);

    void notifyProperties();

}
