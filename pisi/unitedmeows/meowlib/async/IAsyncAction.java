package pisi.unitedmeows.meowlib.async;

import pisi.unitedmeows.meowlib.etc.CoID;

import java.util.UUID;

@FunctionalInterface
public interface IAsyncAction {
    void start(CoID uuid);
}
