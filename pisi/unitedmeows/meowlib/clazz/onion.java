package pisi.unitedmeows.meowlib.clazz;

import pisi.unitedmeows.meowlib.memory.MMemory;

public class onion<X> {

    private int pointer;

    public onion() {
        pointer = -1;
    }

    public onion(X value) {
        set(value);
    }

    public void set(X value) {
        if (pointer == -1) {
            pointer = MMemory.write(value);
        } else {
            MMemory.write(pointer, value);
        }
    }

    public X get() {
        if (pointer == -1) {
            return null;
        }

        return MMemory.read(pointer);
    }

    public void remove() {
        if (pointer != -1) {
            MMemory.remove(pointer);
            pointer = -1;
        }
    }
}
