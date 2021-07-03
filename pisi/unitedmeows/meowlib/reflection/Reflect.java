package pisi.unitedmeows.meowlib.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import pisi.unitedmeows.meowlib.clazz.type;

public class Reflect {
	private Reflect() { throw new IllegalStateException("Utility class"); }

	public static Object instance(final Class<?> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <X> X instance_c(final Class<?> clazz) {
		try {
			return (X) clazz.newInstance();
		} catch (InstantiationException
				| IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Field field(final Class<?> clazz, final String fieldName) {
		try {
			final Field field = clazz.getClass().getDeclaredField(fieldName);
			if (!field.isAccessible()) field.setAccessible(true);
			return field;
		} catch (final NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <X> X field(final Object object, final String fieldName) {
		try {
			final Field field = object.getClass().getDeclaredField(fieldName);
			if (!field.isAccessible()) field.setAccessible(true);
			return (X) field.get(object);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Method method(final Class<?> owner, final String methodName) {
		try {
			final Method method = owner.getDeclaredMethod(methodName);
			if (!method.isAccessible()) method.setAccessible(true);
			return method;
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Method method(final Class<?> owner, final String methodName, final Object... parameters) {
		try {
			final Method method = owner.getDeclaredMethod(methodName, type.type_array(parameters));
			if (!method.isAccessible()) method.setAccessible(true);
			return method;
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <X> X call_method(final Object instance, final String method) {
		/* retarded code */
		return call_method(instance, method, (Object) null);
	}

	public static <X> X call_method(final Object instance, final String method, final Object... parameters) {
		try {
			Method theMethod;
			if (parameters == null /* this could probably fix the nullpointerexception in call_method(Object instance,String method)
											 * not tested.. */ || parameters.length == 0) theMethod = instance.getClass().getDeclaredMethod(method);
			else theMethod = instance.getClass().getDeclaredMethod(method, type.type_array(parameters));
			if (!theMethod.isAccessible()) theMethod.setAccessible(true);
			return (X) theMethod.invoke(instance, parameters);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
