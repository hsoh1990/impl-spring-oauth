package com.wellstone.implspringoauth.account;

import com.wellstone.implspringoauth.oauthclient.OAuthClient;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "idx")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @NotNull
    @Column(name = "accountId", nullable = false, unique = true)
    private String accountId;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;


    @Column(name = "company")
    private String company;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "join_date", nullable = false)
    private Date joinDate;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "modify_date", nullable = false)
    private Date updateDate;


    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;

    @OneToMany(targetEntity = OAuthClient.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_idx")
    private Collection<OAuthClient> oAuthClients;
}
