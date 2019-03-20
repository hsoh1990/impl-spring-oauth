package com.wellstone.implspringoauth.oauthclient;

import com.wellstone.implspringoauth.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OAuthClientRepository extends JpaRepository<OAuthClient, String> {

    List<OAuthClient> findAllByAccount(Account account);
 }
