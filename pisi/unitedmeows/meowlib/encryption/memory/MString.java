package pisi.unitedmeows.meowlib.encryption.memory;

import java.util.Arrays;

/**
 * memory safe strings
 */
public class MString implements Comparable<MString> {
	private final char[] value;

	public MString(final String input) {
		final String encrypted = input;
		value = new char[encrypted.length()];
		int i = 0;
		for (final char c : encrypted.toCharArray()) { value[i] = c; i++; }
	}

	@Override
	public String toString() { return Arrays.toString(value); }

	@Override
	public int compareTo(final MString o) { return 0; }

	/**
	 * Auto generated code.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(value);
		return result;
	}

	/**
	 * Auto generated code.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final MString other = (MString) obj;
		if (!Arrays.equals(value, other.value)) return false;
		return true;
	}
}
