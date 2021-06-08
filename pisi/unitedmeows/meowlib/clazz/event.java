package pisi.unitedmeows.meowlib.clazz;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class event<X extends delegate> {

    private ArrayList<X> delegates;

    public event() {
        delegates = new ArrayList<>();
    }

    public void bind (X delegate) {
        delegates.add(delegate);
    }

    public void run(Object... params) {
        delegates.forEach(x-> {
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
