package com.minlia.modules.rbac.service;

import com.minlia.cloud.code.ApiCode;
import com.minlia.cloud.service.AbstractWriteOnlyService;
import com.minlia.cloud.utils.ApiPreconditions;
import com.minlia.modules.rbac.dao.UserDao;
import com.minlia.modules.rbac.domain.Role;
import com.minlia.modules.rbac.domain.User;
import com.minlia.modules.rbac.repository.RoleRepository;
import com.minlia.modules.rbac.repository.UserRepository;
import com.minlia.modules.rbac.v1.backend.user.body.UserGarentRoleRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by will on 8/14/17.
 */
@Service
public class UserWriteOnlyServiceImpl extends
    AbstractWriteOnlyService<UserDao, User, Long> implements
    UserWriteOnlyService {

  @Autowired
  private UserDao userDao;
  @Autowired
  private UserRepository repository;

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public User grantRole(UserGarentRoleRequestBody requestBody) {
    User user = repository.findOne(requestBody.getUserId());
    ApiPreconditions.checkNotNull(user, ApiCode.NOT_FOUND);
    Role role = roleRepository.findOneByCode(requestBody.getRoleCode());
    ApiPreconditions.checkNotNull(role, ApiCode.NOT_FOUND);
    user.getRoles().add(role);
    return super.update(user);
  }

  @Override
  public User revokeRole(UserGarentRoleRequestBody requestBody) {
    User user = repository.findOne(requestBody.getUserId());
    ApiPreconditions.checkNotNull(user, ApiCode.NOT_FOUND);
    Role role = roleRepository.findOneByCode(requestBody.getRoleCode());
    ApiPreconditions.checkNotNull(role, ApiCode.NOT_FOUND);
    user.getRoles().remove(role);
    return super.update(user);
  }
}