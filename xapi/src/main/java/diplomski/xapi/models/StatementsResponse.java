/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.models;

/**
 *
 * @author Dusan
 */
public class StatementsResponse {

    private String message;

    private Object data;

    public StatementsResponse() {

    }

    public StatementsResponse(String message) {
        this.message = message;
    }

    public StatementsResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
