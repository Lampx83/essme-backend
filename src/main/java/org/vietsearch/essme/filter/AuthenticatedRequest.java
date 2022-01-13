package org.vietsearch.essme.filter;

import org.vietsearch.essme.model.user.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class AuthenticatedRequest extends HttpServletRequestWrapper {

    private String userId;
    private Role role;
    public AuthenticatedRequest(HttpServletRequest req, String userId, Role role) {
        super(req);
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public Role getRole(){
        return  role;
    }

}