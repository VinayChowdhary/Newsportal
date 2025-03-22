package com.bhasaka.newsportal.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(service = Servlet.class)
@SlingServletPaths(value = {"/newsportal/recent-articles","/newsportal/featured-articles"})
//@SlingServletResourceTypes(
//        resourceTypes = "newsportal/services/recent-articles",
//        methods = {"GET","POST","PUT","DELETE"},
//        selectors = {"recent","featured"},
//        extensions = {"json","txt"}
//)
public class RecentArticles extends SlingAllMethodsServlet {

    @Override
    protected void doGet(SlingHttpServletRequest request,SlingHttpServletResponse response) throws ServletException, IOException {

       ResourceResolver resolver = request.getResourceResolver();
       Resource usersResource = resolver.getResource("/content/users/");

        JsonArrayBuilder usersJson = Json.createArrayBuilder(); //[]

       if(usersResource !=null){
           Iterator<Resource> users =  usersResource.listChildren();
           while(users.hasNext()){
               Resource userResource = users.next();
               ValueMap props = userResource.getValueMap();

               JsonObjectBuilder userJson = Json.createObjectBuilder(); //{}
               userJson.add("firstName",props.get("firstName",String.class));
               userJson.add("lastName",props.get("lastName",String.class));
               userJson.add("email",props.get("email",String.class));
               userJson.add("phone",props.get("phone",String.class));

               usersJson.add(userJson);
           }
       }
        response.setContentType("application/json");
        response.getWriter().write(usersJson.build().toString());

//        response.getWriter().write("response from path based servlet - do get method ");
    }
    @Override
    protected void doPost(SlingHttpServletRequest request,SlingHttpServletResponse response) throws ServletException, IOException {

        String userId = request.getParameter("userId");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");


        ResourceResolver resolver = request.getResourceResolver();
        Resource usersResource = resolver.getResource("/content/users");
        Resource userResource = resolver.getResource("/content/users/"+userId);

        if(usersResource !=null && userResource == null){

            Map<String,Object> props = new HashMap<>();
            props.put("firstName",firstName);
            props.put("lastName",lastName);
            props.put("email",email);
            props.put("phone",phone);
            props.put("password",password);

            resolver.create(usersResource,userId,props);
            resolver.commit();
        }

//        response.getWriter().write("response from path based servlet - do post method ");
        response.getWriter().write("User created successfully ");
    }
    @Override
    protected void doPut(SlingHttpServletRequest request,SlingHttpServletResponse response) throws ServletException, IOException {

        String userId = request.getParameter("userId");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        ResourceResolver resolver = request.getResourceResolver();
        Resource userResource = resolver.getResource("/content/users/"+userId);

        if(userResource != null) {
            ModifiableValueMap mprop = userResource.adaptTo(ModifiableValueMap.class);

            if(firstName!=null){
                mprop.put("firstName",firstName);
            }
            if(lastName!=null){
                mprop.put("lastName",lastName);
            }
            if(email!=null){
                mprop.put("email",email);
            }
            if(phone!=null){
                mprop.put("phone",phone);
            }
            if(password!=null){
                mprop.put("password",password);

            }
            //mprop.remove("phone");
            resolver.commit();
        }
        response.getWriter().write("User updated successfully ");
//        response.getWriter().write("response from path based servlet - do put method ");

    }
    @Override
    protected void doDelete(SlingHttpServletRequest request,SlingHttpServletResponse response) throws ServletException, IOException {

        String userId = request.getParameter("userId");

        ResourceResolver resolver = request.getResourceResolver();
        Resource userResource = resolver.getResource("/content/users/"+userId);
        if(userResource!=null){
            resolver.delete(userResource);
            resolver.commit();
        }

        response.getWriter().write("User deleted successfully ");
//        response.getWriter().write("response from path based servlet - do delete method ");
    }
}
