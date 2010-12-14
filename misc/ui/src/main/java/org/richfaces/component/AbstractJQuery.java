/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.component;

import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;

/**
 * @author nick
 *
 */
@JsfComponent(type = AbstractJQuery.COMPONENT_TYPE, family = AbstractJQuery.COMPONENT_FAMILY, 
    renderer = @JsfRenderer(type = "org.richfaces.JQueryRenderer"), tag = @Tag(type = TagType.Facelets))
public abstract class AbstractJQuery extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.richfaces.JQuery";

    public static final String COMPONENT_FAMILY = "org.richfaces.JQuery";

    //TODO nick - CDK should be doing this
    public AbstractJQuery() {
        setRendererType("org.richfaces.JQueryRenderer");
    }

    @Attribute
    public abstract String getName();

    @Attribute
    public abstract String getSelector();

    @Attribute
    public abstract String getEvent();

    @Attribute
    public abstract String getQuery();

    @Attribute(defaultValue = "JQueryAttachType.DEFAULT")
    public abstract JQueryAttachType getAttachType();

    @Attribute(defaultValue = "JQueryTiming.DEFAULT")
    public abstract JQueryTiming getTiming();

}

