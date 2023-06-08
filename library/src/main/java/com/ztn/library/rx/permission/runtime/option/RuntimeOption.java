package com.ztn.library.rx.permission.runtime.option;

import androidx.annotation.NonNull;

import com.ztn.library.rx.permission.runtime.PermissionDef;
import com.ztn.library.rx.permission.runtime.PermissionRequest;
import com.ztn.library.rx.permission.runtime.setting.SettingRequest;

/**
 */
public interface RuntimeOption {

    /**
     * One or more permissions.
     */
    PermissionRequest permission(@NonNull @PermissionDef String... permissions);

    /**
     * One or more permission groups.
     *
     * @param groups use constants in {@link Permission.Group}.
     */
    PermissionRequest permission(@NonNull String[]... groups);

    /**
     * Permission settings.
     */
    SettingRequest setting();
}