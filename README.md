# 인증 서버
OAuth2.0를 이용한 인증서버 저장소 

### key gen
```
keytool -genkey -v -keystore impl_oauth.jks -alias impl_oauth_private -keyalg RSA -sigalg MD5withRSA -keysize 1024 -validity 365
keytool -export -alias impl_oauth_private -keystore impl_oauth.jks -rfc -file impl_oauth.cer
```