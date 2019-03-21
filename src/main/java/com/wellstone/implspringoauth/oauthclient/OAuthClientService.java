package com.wellstone.implspringoauth.oauthclient;

import com.wellstone.implspringoauth.account.Account;
import com.wellstone.implspringoauth.account.AccountRepository;
import com.wellstone.implspringoauth.exception.NotFoundException;
import com.wellstone.implspringoauth.oauthclient.DTO.OAuthClientInsertDTO;
import com.wellstone.implspringoauth.oauthclient.DTO.OAuthClientQueryDTO;
import com.wellstone.implspringoauth.oauthclient.DTO.OAuthClientResponseDTO;
import com.wellstone.implspringoauth.oauthclient.DTO.OAuthClientUpdateDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.Optional;
import java.util.UUID;

@Service
public class OAuthClientService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    OAuthClientRepository oAuthClientRepository;

    @Autowired
    AccountRepository accountRepository;

    public OAuthClient insertOAuthClient(OAuthClientInsertDTO insertDTO) {
        Account account = accountRepository.findById(insertDTO.getAccountIdx())
                .orElseThrow(() -> new NotFoundException("Not Found account by idx = " + insertDTO.getAccountIdx()));

        String clientId = "client_id_" + UUID.randomUUID().toString();
        String clientSecret = "{noop}client_secret_" + UUID.randomUUID().toString();

        OAuthClient oAuthClient = modelMapper.map(insertDTO, OAuthClient.class);
        oAuthClient.setAccount(account);
        oAuthClient.setClientId(clientId);
        oAuthClient.setClientSecret(clientSecret);

        return oAuthClientRepository.save(oAuthClient);
    }

    public Page<OAuthClient> getOAuthClientList(Pageable pageable, OAuthClientQueryDTO queryDTO) {
        Specifications<OAuthClient> spec = generateDynamicSpec(queryDTO);
        return oAuthClientRepository.findAll(spec, pageable);
    }

    public OAuthClient getOAuthClient(String clientId) {
        return oAuthClientRepository.findByClientId(clientId)
                .orElseThrow(() -> new NotFoundException("Not Found account by clientId = " + clientId));
    }


    public OAuthClient updateOAuthClient(String clientId, OAuthClientUpdateDTO updateDTO) {
        Optional<OAuthClient> oAuthClient = oAuthClientRepository.findByClientId(clientId);

        if (!StringUtils.isEmpty(updateDTO.getResourceIds())) {
            oAuthClient.get().setResourceIds(updateDTO.getResourceIds());
        }

        if (!StringUtils.isEmpty(updateDTO.getScope())) {
            oAuthClient.get().setScope(updateDTO.getScope());
        }

        if (!StringUtils.isEmpty(updateDTO.getAuthGrantTypes())) {
            oAuthClient.get().setAuthGrantTypes(updateDTO.getAuthGrantTypes());
        }

        if (!StringUtils.isEmpty(updateDTO.getWebServiceRedirectUri())) {
            oAuthClient.get().setWebServiceRedirectUri(updateDTO.getWebServiceRedirectUri());
        }

        if (updateDTO.getAccessTokenValidity() != 0) {
            oAuthClient.get().setAccessTokenValidity(updateDTO.getAccessTokenValidity());
        }

        if (updateDTO.getRefreshTokenValidity() != 0) {
            oAuthClient.get().setRefreshTokenValidity(updateDTO.getRefreshTokenValidity());
        }

        if (!StringUtils.isEmpty(updateDTO.getAdditionalInfo())) {
            oAuthClient.get().setAdditionalInfo(updateDTO.getAdditionalInfo());
        }

        return oAuthClientRepository.save(oAuthClient.get());
    }

    public OAuthClient deleteOAuthClient(String clientId) {
        OAuthClient oAuthClient = oAuthClientRepository.findByClientId(clientId)
                .orElseThrow(() -> new NotFoundException("Not Found account by clientId = " + clientId));

        oAuthClientRepository.delete(oAuthClient);
        return oAuthClient;
    }

    private Specifications<OAuthClient> generateDynamicSpec(OAuthClientQueryDTO queryDTO) {
        Specifications<OAuthClient> spec = Specifications.where(null);

        if (!StringUtils.isEmpty(queryDTO.getName())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("name"), queryDTO.getName()));
        }

        if (queryDTO.getAccountIdx() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("account").get("idx"), queryDTO.getAccountIdx()));
        }

        if (!StringUtils.isEmpty(queryDTO.getAccountId())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("account").get("accountId"), queryDTO.getAccountId()));
        }

        return spec;
    }


}
