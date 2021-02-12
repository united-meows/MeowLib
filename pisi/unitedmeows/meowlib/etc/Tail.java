package pisi.unitedmeows.meowlib.etc;

import java.lang.reflect.Array;

public class Tail<X> {

    private Tail<X> down;

    private X[] elements;



    /*
    tail 1
    1 anan
    2 baban
    3 asdad
    4 adsasdas
    5 anan

    tail 2
    1 baban
    2 asdad
    3 asdad
    4 adsasdas
    5

    tail 2
    1 baban
    2 asdad
    3 asdad
    4 adsasdas
    5
     */


    public Tail(Class<X> clazz) {
        elements = (X[]) Array.newInstance(clazz, 5);
    }

    public void removeAt(int index) {
    elements[index] = null;
        for (int i = index + 1; i < elements.length; i++) {

        }

    }
}
