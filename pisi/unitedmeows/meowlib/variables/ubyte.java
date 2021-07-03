package pisi.unitedmeows.meowlib.variables;

import pisi.unitedmeows.meowlib.variables.exceptions.UnsignedObjectException;

public class ubyte extends Number implements Comparable<ubyte>, INumberOperations<ubyte> {
	private static final long serialVersionUID = 8046938436345097778L;
	// didn't expect that one did you? <- cringe
	private byte value;
	public static int MAX_VALUE = Byte.MAX_VALUE * 2;
	public static int MIN_VALUE = Byte.MAX_VALUE * -2;

	public ubyte(final byte val) { value = val; }

	@Override
	public void plus(final ubyte value) {
		final int current = intValue();
		final int addValue = value.intValue();
		if (MAX_VALUE > current + addValue) {
			if (current + addValue >= Byte.MAX_VALUE) this.value = convert(current + addValue);
			else this.value += addValue;
		} else throw new UnsignedObjectException("Added values are bigger than MAX_VALUE (254)");
	}

	@Override
	public void minus(final ubyte value) {
		final int current = intValue();
		final int removeValue = value.intValue();
		if (current - removeValue >= 0) {
			final int newValue = current - removeValue;
			this.value = convert(newValue);
		} else throw new UnsignedObjectException("Subtracted values shouldnt be smaller than MIN_VALUE (0)");
	}

	@Override
	public byte byteValue() { return value; }

	@Override
	public int compareTo(final ubyte o) { return Integer.compare(intValue(), o.intValue()); }

	public static byte convert(final int value) {
		if (Math.abs(value) > MAX_VALUE) throw new UnsignedObjectException("Converted value is bigger than MAX_VALUE (254)");
		if (value > 127) return (byte) -(value - 127);
		return (byte) value;
	}

	@Override
	public int intValue() {
		int toInt;
		if (value < 0) toInt = Byte.MAX_VALUE + Math.abs(value);
		else toInt = value;
		return toInt;
	}

	public byte raw() { return value; }

	@Override
	public long longValue() { return intValue(); }

	@Override
	public float floatValue() { return intValue(); }

	@Override
	public double doubleValue() { return intValue(); }

	@Override
	public boolean bigger(final ubyte otherVal) {
		if (raw() > 0 && otherVal.raw() > 0) return raw() > otherVal.raw();
		if (raw() < 0 && otherVal.raw() < 0) return raw() < otherVal.raw();
		if (otherVal.raw() < 0 && raw() > 0) return false;
		if (raw() < 0 && otherVal.raw() > 0) return true;
		return intValue() > otherVal.intValue();
	}

	@Override
	public boolean smaller(final ubyte otherVal) {
		if (raw() > 0 && otherVal.raw() > 0) return raw() < otherVal.raw();
		if (raw() < 0 && otherVal.raw() < 0) return raw() > otherVal.raw();
		if (otherVal.raw() < 0 && raw() > 0) return true;
		if (raw() < 0 && otherVal.raw() > 0) return false;
		return intValue() < otherVal.intValue();
	}

	@Override
	public boolean same(final ubyte otherVal) { return raw() == otherVal.raw(); }
}
