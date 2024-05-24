package cn.icheny.channel_pkg.gui;

public class Channel {
    String code;
    String name;

    @Override
    public String toString() {
        return Util.isEmpty(name) ? code : (name + "-" + code);
    }
}
