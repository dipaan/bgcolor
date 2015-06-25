package com.dipaan.bgcolor;

public class Tuple {
    private final String id;
    private final byte red;
    private final byte green;
    private final byte blue;
    private long expiry;

    public Tuple(String id, byte red, byte green, byte blue) {
        this.id = id;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public String getId() {
        return id;
    }

    public int getValue() {
        return (((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF)));
    }

    public boolean isExpired() {
        return (expiry < System.currentTimeMillis());
    }

    public void setExpiration(long expirationPeriodMillis) {
        this.expiry = System.currentTimeMillis() + expirationPeriodMillis;
    }
}
