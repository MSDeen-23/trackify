package com.beworkerbee.cloudgateway.constants;

import java.util.Arrays;
import java.util.List;

public final class GenericConstants {
    private GenericConstants(){};

    public static final List<String> linksWithoutAuthentication = Arrays.asList(new String[]{
            "/api/v1/user/auth/register-admin",
            "/api/v1/user/login",
//            "/api/v1/user/verify-user"
    });

}
