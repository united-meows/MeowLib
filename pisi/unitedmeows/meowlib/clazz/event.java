package pisi.unitedmeows.meowlib.clazz;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import pisi.unitedmeows.meowlib.etc.Tuple;
import pisi.unitedmeows.meowlib.random.WRandom;

public class event<X extends delegate> {
	private final HashMap<Integer, Tuple<X, Method>> delegates;

	public event() { delegates = new HashMap<>(); }

	public int bind(final X delegate) {
		final int id = WRandom.BASIC.nextInt();
		final Method method = delegate.getClass().getDeclaredMethods()[0];
		if (!method.isAccessible()) method.setAccessible(true);
		delegates.put(id, new Tuple<>(delegate, method));
		return id;
	}

	public void unbindAll() { delegates.clear(); }

	public void unbind(final int id) { delegates.remove(id); }

	public void run(final Object... params) {
		delegates.values().forEach(x -> {
			try {
				x.getSecond().invoke(x.getFirst(), params);
			} catch (IllegalAccessException
					| IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		});
	}
}
