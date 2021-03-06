package org.vietsearch.essme.filter;


import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.*;
import com.google.firebase.auth.internal.FirebaseTokenFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import org.vietsearch.essme.model.user.Role;
import org.vietsearch.essme.model.user.User;
import org.vietsearch.essme.repository.UserRepository;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class FireBaseTokenFilter extends OncePerRequestFilter {
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if(!request.getMethod().equals("GET")) {
            String authenticationHeader = request.getHeader("Authorization");
            //checks if token is there
            if (authenticationHeader == null)
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing token!");
            else {
                FirebaseToken decodedToken = null;
                try {
                    //Extracts token from header
                    String token = authenticationHeader.split(" ")[1];
                    //verifies token to firebase server
                    decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
                    //if token is invalid
                    if (decodedToken == null) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token!");
                    }
                    else {
                        String uid = decodedToken.getUid();
                        AuthenticatedRequest authenticatedRequest = new AuthenticatedRequest(request, uid,saveUser(uid));
                        chain.doFilter(authenticatedRequest, response);
                    }
                } catch (FirebaseAuthException e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error! " + e.toString());
                }
            }
        }
        else chain.doFilter(new AuthenticatedRequest(request,null,null),response);
    }

    private Role saveUser(String uid) throws FirebaseAuthException {
        Role role = null;
        User user;

        UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);

        if(userRepository.existsById(uid)) {
            user = userRepository.findById(uid).orElse(null);
            role = user.getRole();
            System.out.println(role);
            user.setRole(role);
        }
        else user = new User();

        user.setUid(uid);
        user.setEmail(userRecord.getEmail());
        user.setDisplayName(userRecord.getDisplayName());
        user.setPhoneNumber(userRecord.getPhoneNumber());
        user.setPhotoURL(userRecord.getPhotoUrl());
        System.out.println(userRecord.getEmail());

        userRepository.save(user);
        return role;
    }
}