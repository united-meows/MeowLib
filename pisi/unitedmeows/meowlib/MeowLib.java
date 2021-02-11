package pisi.unitedmeows.meowlib;

import pisi.unitedmeows.meowlib.etc.IAction;

import java.util.ArrayList;
import java.util.List;

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
