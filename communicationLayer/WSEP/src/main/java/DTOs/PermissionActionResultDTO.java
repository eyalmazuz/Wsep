package DTOs;

import DTOs.SimpleDTOS.PermissionDTO;

public class PermissionActionResultDTO extends ActionResultDTO {

    PermissionDTO permission;


    public PermissionActionResultDTO(ResultCode resultCode, String details, PermissionDTO permission) {
        super(resultCode, details);
        this.permission = permission;
    }

    public PermissionDTO getPermission() {
        return permission;
    }

    public void setPermission(PermissionDTO permission) {
        this.permission = permission;
    }
}
