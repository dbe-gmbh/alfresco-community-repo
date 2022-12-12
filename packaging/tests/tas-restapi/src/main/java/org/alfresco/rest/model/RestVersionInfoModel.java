/*-
 * #%L
 * alfresco-tas-restapi
 * %%
 * Copyright (C) 2005 - 2022 Alfresco Software Limited
 * %%
 * This file is part of the Alfresco software. 
 * If the software was purchased under a paid Alfresco license, the terms of 
 * the paid license agreement will prevail.  Otherwise, the software is 
 * provided under the following open source license terms:
 * 
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package org.alfresco.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.alfresco.rest.core.IRestModel;
import org.alfresco.utility.model.TestModel;

/**
 * Generated by 'aforascu' on '2018-01-10 16:02' from 'Alfresco Content Services REST API' swagger file 
 * Generated from 'Alfresco Content Services REST API' swagger file
 * Base Path {@linkplain /alfresco/api}
 */
public class RestVersionInfoModel extends TestModel implements IRestModel<RestVersionInfoModel>
{
    @JsonProperty(value = "entry")
    RestVersionInfoModel model;

    @Override
    public RestVersionInfoModel onModel()
    {
        return model;
    }


    @JsonProperty(required = true)    
    private String major;	    

    @JsonProperty(required = true)    
    private String minor;	    

    @JsonProperty(required = true)    
    private String patch;	    

    @JsonProperty(required = true)    
    private String hotfix;	    

    @JsonProperty(required = true)    
    private int schema;	    

    @JsonProperty(required = true)    
    private String label;	    

    @JsonProperty(required = true)    
    private String display;	    

    public String getMajor()
    {
        return this.major;
    }

    public void setMajor(String major)
    {
        this.major = major;
    }				

    public String getMinor()
    {
        return this.minor;
    }

    public void setMinor(String minor)
    {
        this.minor = minor;
    }				

    public String getPatch()
    {
        return this.patch;
    }

    public void setPatch(String patch)
    {
        this.patch = patch;
    }				

    public String getHotfix()
    {
        return this.hotfix;
    }

    public void setHotfix(String hotfix)
    {
        this.hotfix = hotfix;
    }				

    public int getSchema()
    {
        return this.schema;
    }

    public void setSchema(int schema)
    {
        this.schema = schema;
    }				

    public String getLabel()
    {
        return this.label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }				

    public String getDisplay()
    {
        return this.display;
    }

    public void setDisplay(String display)
    {
        this.display = display;
    }				
}
 
