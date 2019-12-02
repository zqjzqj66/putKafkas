package com.zhiyou.putKafka;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.tree.OverrideCombiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

public class BoneProperty implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(BoneProperty.class);
    private static CombinedConfiguration props = null;

    public BoneProperty() {
    }

    public String getString(String key) {
        return props.getString(key);
    }

    public int getInteger(String key) {
        return props.getInt(key);
    }

    public List<Object> getList(String key) {
        return props.getList(key);
    }

    public AbstractConfiguration getProperties() {
        return props;
    }

    static {
        props = new CombinedConfiguration();
        props.setNodeCombiner(new OverrideCombiner());

        URL defaultPropUrl;
        PropertiesConfiguration defaultProps;
        try {
            defaultPropUrl = BoneProperty.class.getResource("/bone-prod.properties");
            defaultProps = new PropertiesConfiguration(defaultPropUrl);
            logger.info("load prod config: {}", defaultPropUrl);
            props.addConfiguration(defaultProps);
        } catch (Exception var3) {
            logger.warn("No prod property file");
        }

        try {
            defaultPropUrl = BoneProperty.class.getResource("/bone-local.properties");
            defaultProps = new PropertiesConfiguration(defaultPropUrl);
            logger.info("load default config: {}", defaultPropUrl);
            props.addConfiguration(defaultProps);
        } catch (Exception var2) {
            throw new RuntimeException("No default app property file");
        }
    }
}

