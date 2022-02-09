package backend.domain.enums;

import java.io.Serializable;

public enum Role implements Serializable {

    PATIENT("ROLE_PATIENT");

    String role;

    Role(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

}
