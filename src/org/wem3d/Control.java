package org.wem3d;


import org.wem3d.util.Util;

import java.util.Map;

public interface Control
{
    Map<String, Integer> KEY = Util.buildKeyMap();

    void control(int delta);

    void nextControl(int delta);

    boolean isActive();

    void setActive(boolean b);

    int id();
}
