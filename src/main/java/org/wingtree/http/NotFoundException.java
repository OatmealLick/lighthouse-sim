package org.wingtree.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception to be thrown when user tries to access entity which does not exists in simulation.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Not found")
public class NotFoundException extends RuntimeException
{
    public NotFoundException(final String message)
    {
        super(message);
    }
}
