package io.github.carbon.carbonmc.utils.permission;

import java.util.HashMap;
import java.util.UUID;

public class UserPermissionTable {
    private UUID user;
    private HashMap<String, Boolean> permissions;

    public UserPermissionTable(UUID user, HashMap<String, Boolean> permissions){
        this.user = user;
        this.permissions = permissions;
    }

    public UUID getUser(){
        return user;
    }

    public HashMap<String, Boolean> getPermissions(){
        return permissions;
    }

    public void applyPermissions(){
        //TODO: Apply permissions to user
    }
}
