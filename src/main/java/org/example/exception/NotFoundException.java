package org.example.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(Long id){
        super(String.format("Could not find object with id = %d",id));
    }
}
