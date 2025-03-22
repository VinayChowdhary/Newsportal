package com.bhasaka.newsportal.core.services;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.io.InputStream;
import javax.jcr.Node;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Component(service = NPEmailService2.class,immediate = true)
@Designate(ocd = NPEmailServiceConfiguration2.class)
public class NPEmailService2 {

    private static final String EMAIL_TEMPLATE_PATH = "/content/dam/newsportal/documents/Email.html";

    private String smtpHost;
    private int smtpPort;
    private String senderEmail;
    private String senderPassword;

    @Reference
    private NPUtilService npUtilService;

    @Activate
    @Modified
    protected void activate(NPEmailServiceConfiguration config) {
        smtpHost = config.host();
        smtpPort = config.port();
        senderEmail = config.userName();
        senderPassword = config.password();
    }

    public void sendEmailWithTemplate(String recipient, String subject, String payload) throws AddressException {
        try {
            String emailBody = getEmailTemplateFromDAM(payload);
            if (emailBody != null) {
                sendEmail(recipient, subject, emailBody);
            }
        } catch (RepositoryException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getEmailTemplateFromDAM(String payload) throws RepositoryException, IOException {
        ResourceResolver resolver = npUtilService.getResourceResolver();
        Resource emailResource = resolver.getResource(EMAIL_TEMPLATE_PATH);
        if (emailResource != null) {
            Node fileNode = emailResource.adaptTo(Node.class);
            if (fileNode != null && fileNode.hasNode("jcr:content/renditions/original/jcr:content")) {
                Node contentNode = fileNode.getNode("jcr:content/renditions/original/jcr:content");
                if (contentNode.hasProperty("jcr:data")) {
                    InputStream is = contentNode.getProperty("jcr:data").getBinary().getStream();
                    String emailTemplate = IOUtils.toString(is, StandardCharsets.UTF_8);
                    return emailTemplate.replace("{{DYNAMIC_PAYLOAD}}", "http://localhost:4502" + payload + ".html");
                }
            }
        }
        return null;
    }

    private void sendEmail(String recipient, String subject, String emailBody) throws AddressException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setContent(emailBody, "text/html");

            Transport.send(message);
        } catch (MessagingException ignored) {
        }
    }

}
