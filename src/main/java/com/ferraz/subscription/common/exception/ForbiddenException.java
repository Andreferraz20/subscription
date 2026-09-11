package com.ferraz.subscription.common.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class ForbiddenException extends WebApplicationException {
    public ForbiddenException() {
        super(Response.Status.FORBIDDEN);
    }
}
