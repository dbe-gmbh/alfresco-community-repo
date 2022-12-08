package org.alfresco.rest.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.alfresco.rest.core.IRestModel;
import org.alfresco.utility.model.TestModel;

/**
 * Generated by 'mpichura' on '2022-12-01 13:41' from 'Alfresco Content Services REST API' swagger file 
 * Generated from 'Alfresco Content Services REST API' swagger file
 * Base Path {@linkplain /alfresco/api/-default-/public/alfresco/versions/1}
 */
public class RestCategoryModel extends TestModel implements IRestModel<RestCategoryModel>
{
    @JsonProperty(value = "entry")
    RestCategoryModel model;

    @Override
    public RestCategoryModel onModel()
    {
        return model;
    }

    /**
    The identifier for the category.
    */	        

    @JsonProperty(required = true)    
    private String id;	    
    /**
    The name of the category.

This must be unique within the parent category.

    */	        

    @JsonProperty(required = true)    
    private String name;	    
    /**
    The id of the parent category (or -root- if this is a top level category).
    */	        

    private String parentId;	    
    /**
    True if the category has at least one child category.
    */	        

    private boolean hasChildren;	    
    /**
    The number of nodes that are assigned to this category.
    */
    private long count;

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }				

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }				

    public String getParentId()
    {
        return this.parentId;
    }

    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }				

    public boolean getHasChildren()
    {
        return this.hasChildren;
    }

    public void setHasChildren(boolean hasChildren)
    {
        this.hasChildren = hasChildren;
    }				

    public long getCount()
    {
        return this.count;
    }

    public void setCount(long count)
    {
        this.count = count;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestCategoryModel that = (RestCategoryModel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

    @Override
    public String toString()
    {
        return "RestCategoryModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parentId='" + parentId + '\'' +
                ", hasChildren=" + hasChildren +
                ", count=" + count +
                '}';
    }
}
 
