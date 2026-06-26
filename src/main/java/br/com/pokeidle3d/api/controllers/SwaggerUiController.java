package br.com.pokeidle3d.api.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@Controller
public class SwaggerUiController {

    private static final String SWAGGER_UI_VERSION = "5.17.14";

    @ResponseBody
    @GetMapping(value = {"/swagger-ui.html", "/webjars/swagger-ui/5.17.14/index.html"}, produces = TEXT_HTML_VALUE)
    public String swaggerUi() {
        String basePath = "/webjars/swagger-ui/" + SWAGGER_UI_VERSION;
        return """
                <!doctype html>
                <html lang="pt-BR">
                <head>
                    <meta charset="UTF-8">
                    <title>Poke Idle 3D API - Swagger UI</title>
                    <link rel="stylesheet" href="%s/swagger-ui.css">
                    <link rel="icon" href="%s/favicon-32x32.png">
                    <style>
                        html { box-sizing: border-box; overflow-y: scroll; }
                        *, *::before, *::after { box-sizing: inherit; }
                        body { margin: 0; background: #fafafa; }
                    </style>
                </head>
                <body>
                    <div id="swagger-ui"></div>
                    <script src="%s/swagger-ui-bundle.js"></script>
                    <script src="%s/swagger-ui-standalone-preset.js"></script>
                    <script>
                        window.onload = function() {
                            window.ui = SwaggerUIBundle({
                                url: "/v3/api-docs",
                                dom_id: "#swagger-ui",
                                deepLinking: true,
                                presets: [
                                    SwaggerUIBundle.presets.apis,
                                    SwaggerUIStandalonePreset
                                ],
                                plugins: [
                                    SwaggerUIBundle.plugins.DownloadUrl
                                ],
                                layout: "StandaloneLayout",
                                operationsSorter: "method",
                                tagsSorter: "alpha"
                            });
                        };
                    </script>
                </body>
                </html>
                """.formatted(basePath, basePath, basePath, basePath);
    }
}
