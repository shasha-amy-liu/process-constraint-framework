package edu.uga.cs.pcf.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.switchyard.config.model.ModelPuller;
import org.switchyard.config.model.composite.ComponentModel;
import org.switchyard.config.model.composite.CompositeModel;
import org.switchyard.config.model.composite.CompositeReferenceModel;
import org.switchyard.config.model.composite.CompositeServiceModel;
import org.switchyard.config.model.switchyard.SwitchYardModel;

public class SwitchyardConfigParser {

    private static final Logger logger = Logger.getLogger(SwitchyardConfigParser.class);

    private SwitchYardModel switchyard;
    private CompositeModel composite;
    private List<CompositeServiceModel> services;
    private List<CompositeReferenceModel> references;
    private List<ComponentModel> components;

    public SwitchyardConfigParser(InputStream is) {
        super();
        ModelPuller<SwitchYardModel> _puller = new ModelPuller<SwitchYardModel>();
        try {
            this.switchyard = _puller.pull(is);
            this.composite = this.switchyard.getComposite();
            this.services = this.composite.getServices();
            this.references = this.composite.getReferences();
            this.components = this.composite.getComponents();
        } catch (IOException e) {
            logger.error("failed to parse switchyard config", e);
        }
    }

    public SwitchYardModel getSwitchyard() {
        return switchyard;
    }

    public void setSwitchyard(SwitchYardModel switchyard) {
        this.switchyard = switchyard;
    }

    public CompositeModel getComposite() {
        return composite;
    }

    public void setComposite(CompositeModel composite) {
        this.composite = composite;
    }

    public List<CompositeServiceModel> getServices() {
        return services;
    }

    public void setServices(List<CompositeServiceModel> services) {
        this.services = services;
    }

    public List<CompositeReferenceModel> getReferences() {
        return references;
    }

    public void setReferences(List<CompositeReferenceModel> references) {
        this.references = references;
    }

    public List<ComponentModel> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentModel> components) {
        this.components = components;
    }

    @Override
    public String toString() {
        return "SwitchyardConfigParser [switchyard=" + switchyard
                + ", composite=" + composite + ", services=" + services
                + ", references=" + references + ", components=" + components
                + "]";
    }

    public static void main(String[] args) throws Exception {
        InputStream is = PcfLinker.getDefaultSwitchyard();
        SwitchyardConfigParser parser = new SwitchyardConfigParser(is);
        logger.info(parser);
    }
}
