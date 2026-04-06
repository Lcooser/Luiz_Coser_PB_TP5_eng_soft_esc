package br.edu.esc.tp5.postdeploy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CatalogoPosDeploySeleniumTest {

    private static final Duration TIMEOUT = Duration.ofSeconds(20);

    private WebDriver driver;

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void validaFluxoBasicoDaInterfacePublicada() {
        WebDriverWait wait = new WebDriverWait(novoDriver(), TIMEOUT);

        driver.get(baseUrl());
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("app-title")));

        assertEquals(
                "Catalogo Integrado de Produtos e Recursos",
                driver.findElement(By.id("app-title")).getText()
        );
        assertFalse(driver.findElement(By.id("app-environment")).getText().isBlank());

        driver.findElement(By.id("nav-produtos")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title-produtos")));
        assertEquals("Produtos", driver.findElement(By.id("page-title-produtos")).getText());

        driver.navigate().back();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-recursos"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("page-title-recursos")));
        assertEquals("Recursos", driver.findElement(By.id("page-title-recursos")).getText());
    }

    @Test
    void validaHealthCheckDoAmbientePublicado() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl() + "/actuator/health"))
                .timeout(TIMEOUT)
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("UP"));
    }

    private WebDriver novoDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--window-size=1440,1080");
        driver = new ChromeDriver(options);
        return driver;
    }

    private String baseUrl() {
        return System.getProperty("app.base-url", "http://localhost:8080");
    }
}
