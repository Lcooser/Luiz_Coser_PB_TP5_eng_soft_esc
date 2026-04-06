# TP5 Integrado

[![CI](https://github.com/luizc/Luiz_Coser_PB_TP5_eng_soft_esc/actions/workflows/ci.yml/badge.svg)](https://github.com/luizc/Luiz_Coser_PB_TP5_eng_soft_esc/actions/workflows/ci.yml)
[![Security](https://github.com/luizc/Luiz_Coser_PB_TP5_eng_soft_esc/actions/workflows/security.yml/badge.svg)](https://github.com/luizc/Luiz_Coser_PB_TP5_eng_soft_esc/actions/workflows/security.yml)
[![Deploy](https://github.com/luizc/Luiz_Coser_PB_TP5_eng_soft_esc/actions/workflows/deploy.yml/badge.svg)](https://github.com/luizc/Luiz_Coser_PB_TP5_eng_soft_esc/actions/workflows/deploy.yml)

Aplicacao unica em Spring Boot e Gradle para catalogo integrado de produtos e recursos, finalizada com cobertura minima de 90%, pipeline de CI/CD, SAST, DAST e validacao pos-deploy com Selenium.

## Execucao local

```powershell
.\gradlew.bat bootRun
```

## Validacao local

```powershell
.\gradlew.bat check
.\gradlew.bat bootJar
.\gradlew.bat postDeployTest -PappBaseUrl=http://localhost:8080
```
