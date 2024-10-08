package com.dennisbrink.mt.global.myregistration;

public interface IRegisterActivityListener {

    void onlineRegistrationSuccess();
    void onlineRegistrationFailure(String msg);
    void localRegistrationSuccess();
    void localRegistrationFailure(String msg);
}
