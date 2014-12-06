// IRemote.aidl
package com.jch.aidlserver;
import com.jch.aidlserver.Location;

// Declare any non-default types here with import statements

interface IRemote {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int add(int a, int b);
    void sendLocal(String local);

    void sendCustomLocal(in Location local);
}
