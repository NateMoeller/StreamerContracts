package com.nicknathanjustin.streamercontracts.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.ArrayList;
import java.util.HashMap;

public class AuthenticationHelper {

    private static HashMap<String, String> getAuthenticationProperties(final OAuth2Authentication auth) {
        final Authentication userAuth = auth.getUserAuthentication();
        final HashMap<String, ArrayList<HashMap<String, String>>> details = (HashMap<String, ArrayList<HashMap<String, String>>>) userAuth.getDetails();
        final ArrayList<HashMap<String, String>> data = (ArrayList<HashMap<String, String>>) details.get("data");
        return data.get(0);
    }

    public static String getUserName(OAuth2Authentication auth) {
        final HashMap<String, String> properties = AuthenticationHelper.getAuthenticationProperties(auth);
        return properties.get("display_name");
    }
}
