package com.wellstone.implspringoauth.oauthclient;

import com.wellstone.implspringoauth.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OAuthClientRepository extends JpaRepository<OAuthClient, String>, JpaSpecificationExecutor<OAuthClient> {

    List<OAuthClient> findAllByAccount(Account account);

    Optional<OAuthClient> findByClientId(String clientId);
 }
