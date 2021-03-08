package pisi.unitedmeows.meowlib.etc;

import pisi.unitedmeows.meowlib.thread.kThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


/* after i made this i realized Async was using Hashmap not List
* https://www.youtube.com/watch?v=VfPe1hUt3c0
*/
public class ForgettableList<X> extends CopyOnWriteArrayList<X> {

    private int limit;
    private boolean exists;
    private Thread thread;
    private int deleteAfter;

    private HashMap<X, Long> existTimeMap;


    public static final int DEFAULT_DELETE_AFTER = 15000;


    public ForgettableList(int limit) {
        this(limit, DEFAULT_DELETE_AFTER);
    }

    public ForgettableList() {
        this(300);
    }

    public ForgettableList(int deleteAfter, int limit) {
        this.deleteAfter = deleteAfter;
        if (limit == -1) {
            this.limit = Integer.MAX_VALUE;
        }
        exists = true;


        existTimeMap = new HashMap<>();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (exists) {
                    long deleteTime = System.currentTimeMillis() - deleteAfter;
                    List<X> deleteList = new ArrayList<>();
                    for (Map.Entry<X, Long> timeEntry : existTimeMap.entrySet()) {
                        if (timeEntry.getValue() < deleteTime) {
                            deleteList.add(timeEntry.getKey());
                        }
                    }

                    for (X key : deleteList) {
                        remove(key);
                        existTimeMap.remove(key);
                    }

                    kThread.sleep(deleteAfter);
                }
            }
        });
        thread.start();
    }

    @Override
    public void add(int index, X element) {
        register(element);
        super.add(index, element);
    }

    @Override
    public boolean add(X x) {
        register(x);
        return super.add(x);
    }

    public void register(X element) {
        existTimeMap.put(element, System.currentTimeMillis());
    }

    public void stop() {
        clear();
        existTimeMap.clear();
        exists = false;
    }
}
