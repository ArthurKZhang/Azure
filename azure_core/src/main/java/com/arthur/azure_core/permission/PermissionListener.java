package com.arthur.azure_core.permission;

public interface PermissionListener {
    public void permissionGranted();

    public void permissionDenied();
}
