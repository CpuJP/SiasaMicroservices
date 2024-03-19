package com.siasa.auth.repository;

import com.siasa.auth.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    @Transactional(readOnly = true)
    public Optional<Usuario> findByName(String name);

    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.roles WHERE u.id = :userId")
    Optional<Usuario> findByIdWithRoles(@Param("userId") Long userId);

    @Query("SELECT DISTINCT u FROM Usuario u JOIN FETCH u.roles")
    List<Usuario> findAllWithRoles();

}