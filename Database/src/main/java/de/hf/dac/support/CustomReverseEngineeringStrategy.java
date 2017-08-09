/** ----------------------------------------------------------------------------
 *
 * ---          HF - Application Development                       ---
 *              Copyright (c) 2013, ... All Rights Reserved
 *
 *
 *  Project     : ccr-domain
 *
 *  File        : CustomReverseEngineeringStrategy.java
 *
 *  Author(s)   : hf
 *
 *  Created     : 24.09.2013
 *
 * ----------------------------------------------------------------------------
 */
package de.hf.dac.support;

import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;

import java.util.List;

/**
 * Overrides the default naming of the fields for some custom things, to satisfy check style.
 */
public class CustomReverseEngineeringStrategy extends DelegatingReverseEngineeringStrategy {

    /**
     * Constructor for the strategy.
     * 
     * @param delegate
     *        THe delegate
     */
    public CustomReverseEngineeringStrategy(ReverseEngineeringStrategy delegate) {
        super(delegate);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean isForeignKeyCollectionLazy(String name, TableIdentifier foreignKeyTable, List columns, TableIdentifier foreignKeyReferencedTable,
        List referencedColumns) {
        System.out.println(name);
        return super.isForeignKeyCollectionLazy(name, foreignKeyTable, columns, foreignKeyReferencedTable, referencedColumns);

    }

    @Override
    public String columnToPropertyName(TableIdentifier table, String column) {
        String propertyName = super.columnToPropertyName(table, column);
        if (column.startsWith("M_F_")) {
            // special cases for MX_RCR_ tables
            propertyName = propertyName.substring(2);
            // make the first two letter lower case, so that the variable satisfies java convention and
            // hibernate tools correctly make a sensible setter/getter because it checks the second letter!
            propertyName = propertyName.substring(0, 2).toLowerCase() + propertyName.substring(2);
        } else if (column.startsWith("M_") || column.startsWith("F_")) {
            propertyName = propertyName.substring(1);
            // make the first letter lower case, so that the variable satisfies java convention
            propertyName = propertyName.substring(0, 2).toLowerCase() + propertyName.substring(2);
            if ("package".equals(propertyName)) {
            	propertyName = "packageId";
            }
        }
        return propertyName;
    }
}
