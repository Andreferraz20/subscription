package com.ferraz.subscription.auth;

import com.ferraz.subscription.auth.dto.LoginRequest;
import com.ferraz.subscription.auth.dto.SignupRequest;
import com.ferraz.subscription.auth.dto.TokenResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AuthResource {

    final AuthService authService;

    @Inject
    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    @POST
    @Path("/signup")
    public Response signup(final SignupRequest request) {
        this.authService.signup(request);
        return Response.ok().build();
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest loginRequest) {
        final TokenResponse tokenResponse = this.authService.login(loginRequest);
        return Response.ok(tokenResponse).build();
    }
}
