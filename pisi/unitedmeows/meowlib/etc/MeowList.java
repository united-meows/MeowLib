package pisi.unitedmeows.meowlib.etc;

import pisi.unitedmeows.meowlib.MeowLib;

public class MeowList<X> {

    private Tail<X> tailStart;
    private Tail lastTail;

    public MeowList() {
        tailStart = new Tail<X>();
    }

    public void push(X element) {
        
    }

}
