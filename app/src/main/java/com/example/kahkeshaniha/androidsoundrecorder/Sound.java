package com.example.kahkeshaniha.androidsoundrecorder;

/**
 * Created by Ehsani on 11/14/2017.
 */

public class Sound {
    private String name;
    private byte[] voice;
    private String length;
    private String size;

    public Sound(String name, byte[] voice, String length, String size) {
        this.name = name;
        this.voice = voice;
        this.length = length;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public byte[] getVoice() {
        return voice;
    }

    public String getLength() {
        return length;
    }

    public String getSize() {
        return size;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
