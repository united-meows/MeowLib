package pisi.unitedmeows.meowlib.clazz;

import pisi.unitedmeows.meowlib.predefined.WRandom;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class event<X extends delegate> {

    private HashMap<Integer, X> delegates;

    public event() {
        delegates = new HashMap<>();
    }

    public int bind(X delegate) {
        int id = WRandom.BASIC.nextInt();
        delegates.put(id, delegate);
        return id;
    }

    public void unbindAll() {
        delegates.clear();
    }

    public void unbind(int id) {
        delegates.remove(id);
    }

    public void run(Object... params) {
        delegates.values().forEach(x-> {
            try {

                Method method = x.getClass().getDeclaredMethods()[0];
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }

                method.invoke(x, params);
            } catch (IllegalAccessException e) {

                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

}
