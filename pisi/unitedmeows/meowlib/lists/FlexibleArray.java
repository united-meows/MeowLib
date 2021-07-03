package pisi.unitedmeows.meowlib.lists;

import java.util.Arrays;

/* dont use it */
/**
 * @deprecated probably has some issues, or very inefficient (because it looks very inefficient)
 */
@Deprecated
public class FlexibleArray<X> {
	private X[] array;
	private int count;
	private int resizeCap;

	public FlexibleArray(final int capacity) { this(capacity, 1); }

	public FlexibleArray(final int capacity, final int resizeCap) {
		array = newArray(capacity);
		this.resizeCap = resizeCap;
	}

	public FlexibleArray() { this(10); }

	public void resize(final int newCount) {
		final X[] old = array;
		array = newArray(newCount);
		for (int i = 0; i < newCount && i < old.length; i++) array[i] = old[i];
	}

	public void add(final X element) {
		if (++count >= array.length) resize(array.length + resizeCap);
		array[count] = element;
	}

	public boolean remove(final X element) {
		int index = 0;
		boolean found = false;
		for (final X x : array) {
			if (x == element) {
				found = true;
				break;
			}
			index++;
		}
		if (found && index + 1 != array.length) {
			for (int i = index + 1; i < array.length; i++) array[index - 1] = array[i];
			count--;
		}
		if (count + resizeCap <= array.length) resize(count + 1);
		return found;
	}

	public boolean remove(final int index) {
		if (index >= array.length || index < 0) return false;
		if (index + 1 != array.length) {
			for (int i = index + 1; i < array.length; i++) array[index - 1] = array[i];
			count--;
		}
		if (count + resizeCap <= array.length) resize(count + 1);
		return true;
	}

	public X[] array() { return array; }

	private X[] newArray(final int length, final X... array) { return Arrays.copyOf(array, length); }

	public X get(final int index) { return array[index]; }
}
