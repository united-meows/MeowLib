package pisi.unitedmeows.meowlib.variables;

public class uint extends Number implements Comparable<uint>, INumberOperations<uint> {
	private static final long serialVersionUID = -7384695490407442563L;
	public static final long MAX_VALUE = Integer.MAX_VALUE * 2L;
	public static final long MIN_VALUE = Integer.MAX_VALUE * -2L;
	private int value;

	public uint(final int val) { value = val; }

	@Override
	public void plus(final uint otherVal) {
		final long current = longValue();
		final long addValue = otherVal.longValue();
		if (MAX_VALUE > current + addValue) {
			if (current + addValue >= Integer.MAX_VALUE) this.value = convert(current + addValue);
			else this.value += addValue;
		} else this.value = -Integer.MAX_VALUE;
		// TODO: throw ex or something
	}

	@Override
	public void minus(final uint otherVal) {
		final long current = longValue();
		final long removeValue = otherVal.longValue();
		if (current - removeValue >= 0) {
			final long newValue = current - removeValue;
			this.value = convert(newValue);
		} else this.value = 0;
		// TODO: throw ex or something
	}

	@Override
	public int compareTo(final uint o) { return Long.compare(longValue(), o.longValue()); }

	public static int convert(final long value) {
		if (value > Integer.MAX_VALUE) return (int) (Integer.MAX_VALUE - value);
		return (int) value;
	}

	public int raw() { return value; }

	@Override
	public boolean bigger(final uint otherVal) {
		if (raw() > 0 && otherVal.raw() > 0) return raw() > otherVal.raw();
		if (raw() < 0 && otherVal.raw() < 0) return raw() < otherVal.raw();
		if (otherVal.raw() < 0 && raw() > 0) return false;
		if (raw() < 0 && otherVal.raw() > 0) return true;
		return intValue() > otherVal.intValue();
	}

	@Override
	public boolean smaller(final uint otherVal) {
		if (raw() > 0 && otherVal.raw() > 0) return raw() < otherVal.raw();
		if (raw() < 0 && otherVal.raw() < 0) return raw() > otherVal.raw();
		if (otherVal.raw() < 0 && raw() > 0) return true;
		if (raw() < 0 && otherVal.raw() > 0) return false;
		return intValue() < otherVal.intValue();
	}

	@Override
	public boolean same(final uint otherVal) { return false; }

	@Override
	public int intValue() { return (int) longValue(); }

	@Override
	public long longValue() {
		long toLong;
		if (raw() < 0) {
			toLong = Integer.MAX_VALUE;
			toLong += Math.abs(raw());
		} else toLong = value;
		return toLong;
	}

	@Override
	public float floatValue() { return longValue(); }

	@Override
	public double doubleValue() { return longValue(); }
}
