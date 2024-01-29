package com.a503.onjeong.domain.usergroup.repository;

import com.a503.onjeong.domain.usergroup.UserGroup;
import com.a503.onjeong.domain.usergroup.UserGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupId> {

    void deleteAllByGroupId(Long groupId);

    Optional<List<UserGroup>> findAllByGroupId(Long groupId);
}
