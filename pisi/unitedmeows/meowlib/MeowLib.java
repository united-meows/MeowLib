package pisi.unitedmeows.meowlib;

import pisi.unitedmeows.meowlib.etc.IAction;
public class MeowLib {

    public static Exception run(IAction action) {
        try {
            action.run();
            return null;
        } catch (Exception ex) {
            return ex;
        }
    }
}
