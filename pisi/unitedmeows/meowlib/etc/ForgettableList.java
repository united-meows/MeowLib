package pisi.unitedmeows.meowlib.etc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import pisi.unitedmeows.meowlib.thread.kThread;

/* after i made this i realized Async was using Hashmap not List
 * https://www.youtube.com/watch?v=VfPe1hUt3c0 */
// FIXME: should override equals...
public class ForgettableList<X> extends CopyOnWriteArrayList<X> {
	private static final long serialVersionUID = -3531846144090196627L;
	private int limit; // not used
	private int deleteAfter; // not used
	private boolean exists;
	/**
	 * Eclipse was crying about these not being transient or serializable, explanation from Eclipse:
	 *
	 * Fields in a Serializable class must themselves be either Serializable or transient even if the
	 * class is never explicitly serialized or deserialized.
	 *
	 * For instance, under load, most J2EE application frameworks flush objects to disk, and an
	 * allegedly Serializable object with non-transient, non-serializable data members could cause
	 * program crashes, and open the door to attackers. In general a Serializable class is expected to
	 * fulfil its contract and not have an unexpected behaviour when an instance is serialized.
	 *
	 * This warning raises an issue on non-Serializable f ields, and on collection fields when they are
	 * not private ( because they could be assigned non-Serializable values externally), and when they
	 * are assigned non-Serializable types within the class.
	 *
	 */
	private transient Thread thread;
	private transient HashMap<X, Long> existTimeMap;
	public static final int DEFAULT_DELETE_AFTER = 15000;

	public ForgettableList(final int limit) { this(limit, DEFAULT_DELETE_AFTER); }

	public ForgettableList() { this(300); }

	public ForgettableList(final int deleteAfter, final int limit) {
		this.deleteAfter = deleteAfter;
		if (limit == -1) this.limit = Integer.MAX_VALUE;
		exists = true;
		existTimeMap = new HashMap<>();
		thread = new Thread(() -> {
			while (exists) {
				final long deleteTime = System.currentTimeMillis() - deleteAfter;
				final List<X> deleteList = new ArrayList<>();
				for (final Map.Entry<X, Long> timeEntry : existTimeMap.entrySet()) if (timeEntry.getValue() < deleteTime) deleteList.add(timeEntry.getKey());
				for (final X key : deleteList) { remove(key); existTimeMap.remove(key); }
				kThread.sleep(deleteAfter);
			}
		});
		thread.start();
	}

	@Override
	public void add(final int index, final X element) {
		register(element);
		super.add(index, element);
	}

	@Override
	public boolean add(final X x) {
		register(x);
		return super.add(x);
	}

	public void register(final X element) { existTimeMap.put(element, System.currentTimeMillis()); }

	public void stop() {
		clear();
		existTimeMap.clear();
		exists = false;
	}
}
