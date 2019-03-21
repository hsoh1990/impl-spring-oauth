package com.wellstone.implspringoauth.oauthclient;

import com.wellstone.implspringoauth.account.DTO.AccountResponseDTO;
import com.wellstone.implspringoauth.common.ResponseData;
import com.wellstone.implspringoauth.common.ResponseDataType;
import com.wellstone.implspringoauth.exception.BadValidationException;
import com.wellstone.implspringoauth.exception.DuplicatedException;
import com.wellstone.implspringoauth.exception.NotFoundException;
import com.wellstone.implspringoauth.oauthclient.DTO.OAuthClientInsertDTO;
import com.wellstone.implspringoauth.oauthclient.DTO.OAuthClientQueryDTO;
import com.wellstone.implspringoauth.oauthclient.DTO.OAuthClientResponseDTO;
import com.wellstone.implspringoauth.oauthclient.DTO.OAuthClientUpdateDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Slf4j
@Controller
@RequestMapping(value = "/api/oauth/clients")
public class OAuthClientController {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    OAuthClientService oAuthClientService;

    @PostMapping
    public ResponseEntity insertOAuthClient(@RequestBody @Valid OAuthClientInsertDTO insertDTO,
                                            BindingResult result) {
        if (result.hasErrors()) {
            throw new BadValidationException(result.getFieldErrors());
        }

        OAuthClient oAuthClient = oAuthClientService.insertOAuthClient(insertDTO);
        OAuthClientResponseDTO responseDTO = generateOAuthClientResponseDTO(oAuthClient);
        URI createUri = linkTo(OAuthClientController.class).slash(oAuthClient.getClientId()).toUri();

        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.SUCCESS)
                .result(responseDTO)
                .message("create OAuth Client")
                .build();

        return ResponseEntity.created(createUri).body(responseData);
    }

    @GetMapping
    public ResponseEntity getOAuthClientList(Pageable pageable,
                                             @ModelAttribute OAuthClientQueryDTO queryDTO) {
        Page<OAuthClient> oAuthClientPage = oAuthClientService.getOAuthClientList(pageable, queryDTO);
        List<OAuthClientResponseDTO> content = oAuthClientPage.getContent()
                .stream()
                .map(this::generateOAuthClientResponseDTO)
                .collect(Collectors.toList());

        final PageImpl<OAuthClientResponseDTO> responseDTOS = new PageImpl<>(content, pageable, oAuthClientPage.getTotalElements());

        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.SUCCESS)
                .result(responseDTOS)
                .message("Get OAuth Client list")
                .build();

        return ResponseEntity.ok().body(responseData);
    }

    @GetMapping(value = "/{clientId}")
    public ResponseEntity getOAuthClient(@PathVariable String clientId) {
        OAuthClient oAuthClient = oAuthClientService.getOAuthClient(clientId);
        OAuthClientResponseDTO responseDTO = generateOAuthClientResponseDTO(oAuthClient);

        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.SUCCESS)
                .result(responseDTO)
                .message("get OAuth Client")
                .build();

        return ResponseEntity.ok().body(responseData);
    }


    @PutMapping(value = "/{clientId}")
    public ResponseEntity updateOAuthClient(@PathVariable String clientId, @RequestBody OAuthClientUpdateDTO updateDTO){
        OAuthClient oAuthClient = oAuthClientService.updateOAuthClient(clientId, updateDTO);
        OAuthClientResponseDTO responseDTO = generateOAuthClientResponseDTO(oAuthClient);

        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.SUCCESS)
                .result(responseDTO)
                .message("update OAuth Client")
                .build();

        return ResponseEntity.ok().body(responseData);
    }

    @DeleteMapping(value = "/{clientId}")
    public ResponseEntity deleteOAuthClient(@PathVariable String clientId){
        OAuthClient oAuthClient = oAuthClientService.deleteOAuthClient(clientId);
        OAuthClientResponseDTO responseDTO = generateOAuthClientResponseDTO(oAuthClient);

        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.SUCCESS)
                .result(responseDTO)
                .message("delete OAuth Client")
                .build();

        return ResponseEntity.ok().body(responseData);
    }


    private OAuthClientResponseDTO generateOAuthClientResponseDTO(OAuthClient oAuthClient) {
        oAuthClient.setClientSecret(oAuthClient.getClientSecret().replace("{noop}", ""));
        OAuthClientResponseDTO responseDTO = modelMapper.map(oAuthClient, OAuthClientResponseDTO.class);
        responseDTO.setAccount(modelMapper.map(oAuthClient.getAccount(), AccountResponseDTO.class));
        return responseDTO;
    }

    /**
     * Exception handler
     */
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity NotFoundExceptionHandler(NotFoundException e) {
        log.error("OAuthClient Not found exception");
        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.FAILED)
                .result(e.getInformation())
                .build();

        return ResponseEntity.badRequest().body(responseData);
    }

    @ExceptionHandler(value = DuplicatedException.class)
    public ResponseEntity DuplicatedExceptionHandler(DuplicatedException e) {
        log.error("OAuthClient Duplicated exception");
        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.FAILED)
                .result(e.getInformation())
                .build();

        return ResponseEntity.badRequest().body(responseData);
    }

    @ExceptionHandler(value = BadValidationException.class)
    public ResponseEntity BadValidationExceptionHandler(BadValidationException e) {
        log.error("OAuthClient Validation exception");
        ResponseData responseData = ResponseData.builder()
                .type(ResponseDataType.FAILED)
                .result(e.getInformation())
                .build();

        return ResponseEntity.badRequest().body(responseData);
    }
}
