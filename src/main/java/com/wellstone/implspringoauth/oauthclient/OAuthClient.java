package com.wellstone.implspringoauth.oauthclient;

import com.wellstone.implspringoauth.account.Account;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "clientId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "oauth_client_details")
public class OAuthClient {
    @Id
    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "resource_ids")
    private String resourceIds;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "scope")
    private String scope;

    @Column(name = "authorized_grant_types")
    private String authGrantTypes;

    @Column(name = "web_server_redirect_uri")
    private String webServiceRedirectUri;

    @Column(name = "authorities")
    private String authorities;

    @Column(name = "access_token_validity", length = 11)
    private int accessTokenValidity;

    @Column(name = "refresh_token_validity", length = 11)
    private int refreshTokenValidity;

    @Column(name = "additional_information", length = 4096)
    private String additionalInfo;

    @Column(name = "autoapprove")
    private String autoapprove;

    //custom attribute
    @Column(name = "name")
    private String name;

    //custom attribute
    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_idx")
    Account account;
}
