package com.aem.training.site.core.models;

import com.adobe.cq.export.json.ExporterConstants;

import com.aem.training.site.core.services.SimpleService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

@Model(adaptables = {SlingHttpServletRequest.class,Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = ProductMModel.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)

public class ProductMModel<productList> {

    protected static final String RESOURCE_TYPE = "training/components/product";


    @ValueMapValue(name="text")
    private String title;

    @ValueMapValue
    private String text;



    @OSGiService
    private SlingSettingsService settings;

    @OSGiService
    private SimpleService simpleService;



    @Inject
    SlingHttpServletRequest request;

    private List<Bike> productList = Collections.emptyList();





    @PostConstruct
    protected void init() {




        ResourceResolver resolver = request.getResourceResolver();
        Resource resource = resolver.getResource("/var/commerce/products/we-retail/eq/product");
        Iterable<Resource> childResources = resource.getChildren();

        try {
            for (Resource sresource : childResources
            ) {
                ValueMap vm = sresource.getValueMap();
                String price = vm.get("price", String.class);
                Bike bike = new Bike();
                bike.setPrice(price);
                bike.setTitle(vm.get("title", String.class));
                bike.setImage(vm.get("image", String.class));
                productList.add(bike);
            }
        }
        catch (Exception e){

        }


        productList=simpleService.getBikeList();








    }

    public List<Bike> getProductList() {
        return productList;
    }

    public String getTitle() {
        return title;
    }



    public String getSample(){
        return "sample content";
    }
}
