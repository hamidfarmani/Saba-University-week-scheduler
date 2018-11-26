package controller;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.WeakHashMap;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "lessonConverter")
public class lessonConverter implements Converter {

    private static Map<Object, String> lessons = new WeakHashMap<Object, String>();

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object lesson) {
        synchronized (lessons) {
            if (!lessons.containsKey(lesson)) {
                String uuid = UUID.randomUUID().toString();
                lessons.put(lesson, uuid);
                return uuid;
            } else {
                return lessons.get(lesson);
            }
        }
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String uuid) {
        for (Entry<Object, String> entry : lessons.entrySet()) {
            if (entry.getValue().equals(uuid)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
