package com.aem.training.site.core.services.impl;

import com.aem.training.site.core.models.Bike;
import com.aem.training.site.core.services.SimpleService;
import com.aem.training.site.core.services.SimpleServiceConfiguration;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.*;

@Component(
        service = { SimpleService.class },
        immediate = true,
        property = { "service.description=" + "A Simple service" })
@Designate(ocd = SimpleServiceConfiguration.class)
public class SimpleServiceImpl implements SimpleService {

    public static final String SAMPLESYSTEMUSER = "vikas";
    private static final Logger log = LoggerFactory.getLogger(SimpleServiceImpl.class);

    String path = StringUtils.EMPTY;

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Reference
    private QueryBuilder queryBuilder;

    @Activate
    @Modified
    protected void activateOrModified(SimpleServiceConfiguration config){
        path = config.service_path();
        System.out.println(path);
    }

    @Override
    public List<Bike> getBikeList(){
        ResourceResolver resourceResolver=getResourceResolver(resourceResolverFactory,SAMPLESYSTEMUSER);
        List<Bike> bikesList = new ArrayList<>();
            Resource parentResource = resourceResolver.getResource("/var/commerce/products/we-retail/eq/biking");
            if (Objects.nonNull(parentResource)){

                for (Resource res: parentResource.getChildren()) {
                    ValueMap vm = res.getValueMap();
                    Bike bike = new Bike();
                    bike.setTitle(vm.get("jcr:title",String.class));
                    bike.setPrice(vm.get("price", String.class));
                    bike.setDescription(vm.get("summary", String.class));
                    bike.setRating(vm.get("rating", String.class));
                    bike.setReviews(vm.get("reviews", String.class));
                    bike.setImage(vm.get("image",String.class));

                    bikesList.add(bike);
                }
            }
        return bikesList;
    }

    private ResourceResolver getResourceResolver(final ResourceResolverFactory resourceResolverFactory,
                                                       final String subService) {
        ResourceResolver resourceResolver = null;
        if (null != resourceResolverFactory && null != subService) {
            try {
                final Map<String, Object> authInfo = new HashMap<>();
                authInfo.put(ResourceResolverFactory.SUBSERVICE, subService);
                resourceResolver = resourceResolverFactory.getServiceResourceResolver(authInfo);
            } catch (final LoginException loginException) {
                log.error("exception occured");
            }
        }
        return resourceResolver;
    }


//    @Deactivate
//    protected void deactivate(){
//
//    }
}
