package Utils;

import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewsportalUtil {

    public static Resource getComponentResource(Resource resource,String resourceType){
        for(Resource childResource:resource.getChildren()){
            if(resourceType.equals(childResource.getResourceType())){
                return childResource;
            }
            Resource found = getComponentResource(childResource,resourceType);
            if(Objects.nonNull(found)){
                return found;
            }
        }
        return null;
    }

    public static List<Resource> getComponentResourceList(Resource resource,String resourceType){
        List<Resource> childResources = new ArrayList<>();
        for(Resource childResource:resource.getChildren()){
            if(resourceType.equals(childResource.getResourceType())){
                childResources.add(childResource);
            }
            childResources.addAll(getComponentResourceList(childResource,resourceType));
        }
        return childResources;
    }
}
