// restful_mapper.aidl
package com.example.playd.gmtest;

// Declare any non-default types here with import statements

interface IRemoteService {
    //Print message to show proper connection.
    String getMessage();

    //JSON call to receive data from API. Convert JSONObj to String on return.
    String getJSON(String condURL);

    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}
