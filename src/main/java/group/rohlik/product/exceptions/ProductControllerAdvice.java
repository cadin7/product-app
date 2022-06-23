package group.rohlik.product.exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ProductControllerAdvice {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    ApiError handleProductNotFoundException(ProductNotFoundException exception) {
        return new ApiError(exception.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ProductValidationException.class)
    ApiError handleProductValidationException(ProductValidationException exception) {
        return new ApiError(exception.getMessage());
    }
}

record ApiError(String message) {
}

