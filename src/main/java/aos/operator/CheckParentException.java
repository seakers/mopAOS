/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aos.operator;

/**
 * Exception that is thrown when the parent solutions do not meet some criteria
 * @author nozomihitomi
 */
public class CheckParentException extends RuntimeException{
    
    private static final long serialVersionUID = 3326190758938108251L;

    public CheckParentException() {
        super("Parents did not meet citeria");
    }
    
    public CheckParentException(String message) {
        super(message);
    }
    
}
