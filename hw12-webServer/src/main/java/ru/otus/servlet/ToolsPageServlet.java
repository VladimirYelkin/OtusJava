package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;


public class ToolsPageServlet extends HttpServlet {

    private static final String TOOLS_HTML = "tools.html";

    private final TemplateProcessor templateProcessor;

    public ToolsPageServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(TOOLS_HTML, new HashMap<String,Object>()));
    }

}
