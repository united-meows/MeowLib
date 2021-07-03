package pisi.unitedmeows.meowlib.lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;

public abstract class WarnList<X> extends ArrayList<X> {
	private static final long serialVersionUID = -4406578021545673006L;

	public abstract void onChange(WarnList<X> list);

	@Override
	public boolean add(final X x) {
		final boolean result = super.add(x);
		onChange(this);
		return result;
	}

	@Override
	public void add(final int index, final X element) {
		super.add(index, element);
		onChange(this);
	}

	@Override
	public boolean remove(final Object o) {
		final boolean result = super.remove(o);
		if (result) {
			onChange(this);
			return true;
		}
		return false;
	}

	@Override
	public X remove(final int index) {
		final X result = super.remove(index);
		onChange(this);
		return result;
	}

	@Override
	public boolean addAll(final Collection<? extends X> c) {
		final boolean result = super.addAll(c);
		if (result) {
			onChange(this);
			return true;
		}
		return false;
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends X> c) {
		final boolean result = super.addAll(index, c);
		if (result) {
			onChange(this);
			return true;
		}
		return false;
	}

	@Override
	public X set(final int index, final X element) {
		final X result = super.set(index, element);
		onChange(this);
		return result;
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		final boolean result = super.removeAll(c);
		if (result) {
			onChange(this);
			return true;
		}
		return false;
	}

	@Override
	public void sort(final Comparator<? super X> c) {
		super.sort(c);
		onChange(this);
	}

	@Override
	public boolean removeIf(final Predicate<? super X> filter) {
		final boolean result = super.removeIf(filter);
		if (result) {
			onChange(this);
			return true;
		}
		return false;
	}
}
