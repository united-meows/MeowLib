package pisi.unitedmeows.meowlib.lists;

public class Tail<X> {
	private X x1 , x2 , x3 , x4 , x5;

	public X get(final int index) {
		switch (index) {
		case 0:
			return x1;
		case 1:
			return x2;
		case 2:
			return x3;
		case 3:
			return x4;
		case 4:
			return x5;
		default:
			throw new NullPointerException("Invalid index, Index cant be higher than 4 and lower than 0.");
		}
	}

	public void push(final X x) {
		if (x1 == null) x1 = x;
		else if (x2 == null) x2 = x;
		else if (x3 == null) x3 = x;
		else if (x4 == null) x4 = x;
		else if (x5 == null) x5 = x;
	}

	public void remove(final int index) {
		switch (index) {
		case 0: {
			x1 = x2;
			x2 = x3;
			x3 = x4;
			x4 = x5;
			x5 = null;
			break;
		}
		case 1: {
			x2 = x3;
			x3 = x4;
			x4 = x5;
			x5 = null;
			break;
		}
		case 2: {
			x3 = x4;
			x4 = x5;
			x5 = null;
			break;
		}
		case 3: {
			x4 = x5;
			x5 = null;
			break;
		}
		case 4: {
			x5 = null;
			break;
		}
		default:
			throw new NullPointerException("Invalid index, Index cant be higher than 4 and lower than 0.");
		}
	}
}
