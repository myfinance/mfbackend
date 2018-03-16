/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2014, ... All Rights Reserved
 *
 *
 *  Project     : dac
 *
 *  File        : DateTimePropertyConstructor.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 13.10.2017
 *
 * ----------------------------------------------------------------------------
 */

package de.hf.dac.testrunner.datetime;

import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Tag;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class DateTimePropertyConstructor extends Constructor {

    public DateTimePropertyConstructor(Class clazz) {
        super(clazz);
        yamlClassConstructors.put(NodeId.scalar, new TimeStampConstruct());
    }
    public DateTimePropertyConstructor() {

        yamlClassConstructors.put(NodeId.scalar, new TimeStampConstruct());
    }

    class TimeStampConstruct extends Constructor.ConstructScalar {
        @Override
        public Object construct(Node nnode) {
            if (nnode.getTag().equals(new Tag("tag:yaml.org,2002:timestamp"))) {
                Construct dateConstructor = yamlConstructors.get(Tag.TIMESTAMP);
                Date date = (Date) dateConstructor.construct(nnode);
                return LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
            } else {
                return super.construct(nnode);
            }
        }

    }
}