package com.urssa.urssaAppPressing.v2.user;

import com.urssa.urssaAppPressing.v2.appConfig.response.PageResponse;
import com.urssa.urssaAppPressing.v2.user.dto.AddUserDto;
import com.urssa.urssaAppPressing.v2.user.dto.UserDto;
import com.urssa.urssaAppPressing.v2.user.dto.UpdateUserDto;

import java.util.List;
import java.util.UUID;

public interface UserServices {

    UserDto loadUserById(UUID id);

    UserDto addUser(AddUserDto request);

    UserDto updateUser(UpdateUserDto request, UUID id);

    String softDeleteUser(UUID id);

    String deleteUser(UUID id);

    String updatePassword(String newPassword, UUID id);

    UserDto updateUserRoles(UUID roleId, UUID id);

    List<UserDto> loadOrSearchActiveUser(UUID companyId, UUID agencyId, String term);

    PageResponse<UserDto> loadOrSearchActiveUserPaged(Long page, Long size, UUID companyId, UUID agencyId, String term);

    PageResponse<UserDto> loadOrSearchDeletedUserPaged(Long page, Long size, UUID companyId, UUID agencyId, String term);

}
