package com.zerofive.user.domain;

public interface UserRepository {

    void save(User user);

    boolean existsByEmail(String email);
}
