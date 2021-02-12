package pisi.unitedmeows.meowlib.etc;

import pisi.unitedmeows.meowlib.MeowLib;

public class MLibSetting<X> {

    private MLibSettings label;
    private X value;

    public MLibSetting(MLibSettings label, X value) {
        this.label = label;
        this.value = value;
    }

    public X getValue() {
        return value;
    }

    public MLibSettings getLabel() {
        return label;
    }
}
