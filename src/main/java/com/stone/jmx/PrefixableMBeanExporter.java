package com.stone.jmx;

/**
 * PrefixableMBeanExporter
 *
 * @author Young
 * @date 2016/2/17 0017
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.MBeanExporter;

import javax.management.JMException;
import javax.management.ObjectName;

public class PrefixableMBeanExporter extends MBeanExporter {

    private static final Logger logger =
            LoggerFactory.getLogger(PrefixableMBeanExporter.class);

    private String prefix;

    public PrefixableMBeanExporter(String prefix) {
        this.prefix = prefix;
        logger.debug("prefix {} setted.", this.prefix);
    }

    private ObjectName replacePrefix(ObjectName objectName) {
        if (null != prefix) {
            String domain = objectName.getDomain();
            if ("prefix".equals(domain)) {
                try {
                    //	prefix:
                    return new ObjectName(prefix + objectName.toString().substring(7));
                } catch (Exception e) {
                    logger.warn("failed to add prefix {} to ObjectName{}", prefix, objectName);
                }
            }
        }

        return objectName;
    }

    @Override
    protected void doRegister(Object mbean, ObjectName objectName)
            throws JMException {
        ObjectName newObjectName = replacePrefix(objectName);
        if (logger.isDebugEnabled()) {
            logger.debug("doRegister with oldObjectName {} / newObjectName {}",
                    objectName, newObjectName);
        }
        super.doRegister(mbean, newObjectName);
    }

    @Override
    protected void doUnregister(ObjectName objectName) {
        ObjectName newObjectName = replacePrefix(objectName);
        logger.debug("doRegister with oldObjectName {} / newObjectName {}", objectName, newObjectName);
        super.doUnregister(newObjectName);
    }
}
