package com.veniture.rest;


import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.*;
import com.atlassian.sal.api.net.RequestFactory;


@Path("/main")
public class rest {
    @JiraImport
    private RequestFactory requestFactory;
    @JiraImport
    private SearchService searchService;
    @JiraImport
    private JiraAuthenticationContext authenticationContext;
    @JiraImport
    private IssueManager issueManager;

    String link = "https://api.exchangeratesapi.io/latest?base=USD";

    public String currencyCalculator() throws IOException {

        URL url = new URL(link);
        HttpURLConnection req = (HttpURLConnection)url.openConnection();
        req.connect();

        JsonElement curr = JsonParser.parseReader(new InputStreamReader((InputStream)
                req.getContent()));
        JsonObject currency = curr.getAsJsonObject();

        String dollarrate = currency.getAsJsonObject("rates").get("TRY").getAsString();
        return dollarrate;
    }


    public rest(RequestFactory requestFactory, SearchService searchService, JiraAuthenticationContext authenticationContext){
        this.requestFactory = requestFactory;
        this.issueManager= ComponentAccessor.getIssueManager();
        this.searchService = searchService;
        this.authenticationContext = authenticationContext;
    }

    @GET
    @Path("/usd-try")
    public String getUsdToTry(@Context HttpServletRequest req, @Context HttpServletResponse resp)  {

        try{

            String currencyCalculator = "USD to TRY: " + currencyCalculator();
            return currencyCalculator;

        }
        catch (IOException e){

            System.out.println("An unexpected error has occurred.");
            return "An unexpected error has occured";

        }

       }

    }

