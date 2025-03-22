package com.bhasaka.newsportal.core.services;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import org.apache.commons.mail.EmailException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.util.Objects;

@Component(
        service = WorkflowProcess.class,
        immediate = true,
        property = {
                "process.label=NP Page Publish Process"
        }
)
public class NPPagePublishProcess implements WorkflowProcess {

    @Reference
    Replicator replicator;

    @Reference
    NPUtilService npUtilService;

    @Reference
    NPEmailService npEmailService;

//    @Reference
//    NPEmailService2 npEmailService;

    //Newsportal Mail Delivery System
    //vroz hjgt lvku ivqm
    //vrozhjgtlvkuivqm

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
//        Session session = workflowSession.adaptTo(Session.class);
        String payload = workItem.getWorkflowData().getPayload().toString();
//        ResourceResolver resolver = npUtilService.getResourceResolver();
//        Resource resource = resolver.getResource(payload + "/jcr:content");
//
//        if (Objects.nonNull(resource)) {
//            ModifiableValueMap mprops = resource.adaptTo(ModifiableValueMap.class);
//            mprops.put("isPublished", "true");
//            try {
//                resolver.commit();
//            } catch (PersistenceException e) {
//                throw new RuntimeException(e);
//            }
//        }
        try {
//            replicator.replicate(session, ReplicationActionType.ACTIVATE,payload);

            npEmailService.sendEmailWithTemplate("vinaychowdarypolina@gmail.com", "Page Published Notification", payload);


        } catch (RepositoryException | IOException | EmailException e) {
            throw new RuntimeException(e);
        }

    }
}
