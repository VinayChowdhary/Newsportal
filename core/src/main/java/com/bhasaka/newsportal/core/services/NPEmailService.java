package com.bhasaka.newsportal.core.services;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component(service = NPEmailService.class, immediate = true)
@Designate(ocd = NPEmailServiceConfiguration.class)
public class NPEmailService {

    String hostName;
    int portNumber;
    String userName;
    String password;

    private static final String EMAIL_TEMPLATE_PATH = "/content/dam/newsportal/documents/Email.html";

    @Reference
    NPUtilService npUtilService;

    @Activate
    @Modified
    public void activate(NPEmailServiceConfiguration config) {
        hostName = config.host();
        portNumber = config.port();
        userName = config.userName();
        password = config.password();
    }

    public void sendEmailWithTemplate(String recipient, String subject, String payload) throws RepositoryException, IOException, EmailException {
        String emailBody = getEmailTemplateFromDAM(payload);
        if (Objects.nonNull(emailBody)) {
            sendEmail(recipient, subject, emailBody);
        }
    }

    public String getEmailTemplateFromDAM(String payload) throws RepositoryException, IOException {
        ResourceResolver resolver = npUtilService.getResourceResolver();
        Resource emailResource = resolver.getResource(EMAIL_TEMPLATE_PATH);
        if (Objects.nonNull(emailResource)) {
            Node fileNode = emailResource.adaptTo(Node.class);
            if (Objects.nonNull(fileNode) && fileNode.hasNode("jcr:content/renditions/original/jcr:content")) {
                Node contentNode = fileNode.getNode("jcr:content/renditions/original/jcr:content");
                if (contentNode.hasProperty("jcr:data")) {
                    InputStream is = contentNode.getProperty("jcr:data").getBinary().getStream();
                    String emailTemplate = IOUtils.toString(is, StandardCharsets.UTF_8);
                    return emailTemplate.replace("{{DYNAMIC_PAYLOAD}}","http://localhost:4502"+ payload +".html");
                }
            }
        }
        return null;
    }

    private void sendEmail(String recipient, String subject, String emailBody) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName(hostName);
        email.setSmtpPort(portNumber);
        email.setAuthentication(userName, password);
        email.setStartTLSEnabled(true);
        email.setSSLOnConnect(false);
        email.setSSLCheckServerIdentity(true);
        email.setFrom("no-reply@bhasaka.com");
        email.setSubject(subject);
        email.setHtmlMsg(emailBody);
        email.addTo(recipient);

        email.send();
    }
}
