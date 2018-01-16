/*
 * #%L
 * Alfresco Records Management Module
 * %%
 * Copyright (C) 2005 - 2018 Alfresco Software Limited
 * %%
 * This file is part of the Alfresco software.
 * -
 * If the software was purchased under a paid Alfresco license, the terms of
 * the paid license agreement will prevail.  Otherwise, the software is
 * provided under the following open source license terms:
 * -
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * -
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * -
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

package org.alfresco.module.org_alfresco_module_rm.test.legacy.service;

import java.util.ArrayList;
import java.util.Arrays;

import org.alfresco.model.ContentModel;
import org.alfresco.module.org_alfresco_module_rm.model.RecordsManagementModel;
import org.alfresco.module.org_alfresco_module_rm.query.RecordsManagementQueryDAO;
import org.alfresco.module.org_alfresco_module_rm.record.RecordService;
import org.alfresco.module.org_alfresco_module_rm.test.util.BaseRMTestCase;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;
import org.alfresco.util.GUID;

/**
 * Records Management Query DAO
 * 
 * @author Roy Wetherall
 * @since 2.1
 */
public class RecordsManagementQueryDAOImplTest extends BaseRMTestCase implements RecordsManagementModel
{
    protected RecordsManagementQueryDAO queryDAO;
    
    /**
     * @see org.alfresco.module.org_alfresco_module_rm.test.util.BaseRMTestCase#initServices()
     */
    @Override
    protected void initServices()
    {
        super.initServices();

        queryDAO = (RecordsManagementQueryDAO)applicationContext.getBean("recordsManagementQueryDAO");
    }

    /**
     * This is a record test
     * 
     * @see org.alfresco.module.org_alfresco_module_rm.test.util.BaseRMTestCase#isRecordTest()
     */
    @Override
    protected boolean isRecordTest()
    {
        return true;
    }

    /**
     * @see RecordService#getRecordMetaDataAspects()
     */
    public void testGetRecordMetaDataAspects() throws Exception
    {
        doTestInTransaction(new Test<Void>()
        {
            @Override
            public Void run()
            {
                int count = queryDAO.getCountRmaIdentifier("abc-123");
                assertEquals(0, count);
                
                String existingID = (String)nodeService.getProperty(recordOne, PROP_IDENTIFIER);
                count = queryDAO.getCountRmaIdentifier(existingID);
                assertEquals(1, count);    
                
                return null;
            }
        });
    }

    /**
     * Given a folder containing 3 files with the descriptions set
     * When I check if the folder contains children having the description of file2 or file2
     * Then the answer is positive
     */
    @org.junit.Test
    public void testHasChildrenWithPropertyValues_someChildrenWithValues() throws Exception
    {
        doBehaviourDrivenTest(new BehaviourDrivenTest()
        {
            NodeRef parentFolder;
            NodeRef file1;
            NodeRef file2;
            NodeRef file3;
            String propValue1 = "descr1"; // set on file1
            String propValue2 = "descr2"; // set on file2
            String propValue3 = "descr3"; // set on file3
            String propValue4 = "descr4"; // not set on any file
            Boolean result;

            @Override
            public void given() throws Exception
            {
                setupCollaborationSiteTestDataImpl();
                parentFolder = fileFolderService.create(documentLibrary, GUID.generate(), ContentModel.TYPE_FOLDER).getNodeRef();
                file1 = fileFolderService.create(parentFolder, GUID.generate(), ContentModel.TYPE_FOLDER).getNodeRef();
                file2 = fileFolderService.create(parentFolder, GUID.generate(), ContentModel.TYPE_FOLDER).getNodeRef();
                file3 = fileFolderService.create(parentFolder, GUID.generate(), ContentModel.TYPE_FOLDER).getNodeRef();

                nodeService.setProperty(file1, PROP_DESCRIPTION, propValue1);
                nodeService.setProperty(file2, PROP_DESCRIPTION, propValue2);
                nodeService.setProperty(file3, PROP_DESCRIPTION, propValue3);
            }

            @Override
            public void when() throws Exception
            {
                result = queryDAO.hasChildrenWithPropertyValues(parentFolder, PROP_DESCRIPTION, Arrays.asList(propValue1, propValue2, propValue4));
            }

            @Override
            public void then() throws Exception
            {
                assertTrue(result);
            }

            @Override
            public void after() throws Exception
            {
                if (parentFolder != null && nodeService.exists(parentFolder))
                {
                    nodeService.deleteNode(parentFolder);
                }
            }
        });
    }

    /**
     * Given a folder containing 3 files with the descriptions unset
     * When I check if the folder contains children having certain descriptions
     * Then the answer is negative
     */
    @org.junit.Test
    public void testHasChildrenWithPropertyValues_propertyNotSetOnChildren() throws Exception
    {
        doBehaviourDrivenTest(new BehaviourDrivenTest()
        {
            NodeRef parentFolder;
            NodeRef file1;
            NodeRef file2;
            NodeRef file3;
            Boolean result;

            @Override
            public void given() throws Exception
            {
                setupCollaborationSiteTestDataImpl();
                parentFolder = fileFolderService.create(documentLibrary, GUID.generate(), ContentModel.TYPE_FOLDER).getNodeRef();
                file1 = fileFolderService.create(parentFolder, GUID.generate(), ContentModel.TYPE_FOLDER).getNodeRef();
                file2 = fileFolderService.create(parentFolder, GUID.generate(), ContentModel.TYPE_FOLDER).getNodeRef();
                file3 = fileFolderService.create(parentFolder, GUID.generate(), ContentModel.TYPE_FOLDER).getNodeRef();
            }

            @Override
            public void when() throws Exception
            {
                result = queryDAO.hasChildrenWithPropertyValues(parentFolder, PROP_DESCRIPTION, Arrays.asList("descr1", "descr2", "descr3"));
            }

            @Override
            public void then() throws Exception
            {
                assertFalse(result);
            }

            @Override
            public void after() throws Exception
            {
                if (parentFolder != null && nodeService.exists(parentFolder))
                {
                    nodeService.deleteNode(parentFolder);
                }
            }
        });
    }

    /**
     * Given a folder with no children but the property set on itself
     * When I check if the folder contains children having certain descriptions
     * Then the answer is negative
     */
    @org.junit.Test
    public void testHasChildrenWithPropertyValues_noChildren() throws Exception
    {
        doBehaviourDrivenTest(new BehaviourDrivenTest()
        {
            NodeRef folder;
            String propValue = "descr";
            Boolean result;

            @Override
            public void given() throws Exception
            {
                setupCollaborationSiteTestDataImpl();
                folder = fileFolderService.create(documentLibrary, GUID.generate(), ContentModel.TYPE_FOLDER).getNodeRef();
                nodeService.setProperty(folder, PROP_DESCRIPTION, propValue);
            }

            @Override
            public void when() throws Exception
            {
                result = queryDAO.hasChildrenWithPropertyValues(folder, PROP_DESCRIPTION, Arrays.asList("descr"));
            }

            @Override
            public void then() throws Exception
            {
                assertFalse(result);
            }

            @Override
            public void after() throws Exception
            {
                if (folder != null && nodeService.exists(folder))
                {
                    nodeService.deleteNode(folder);
                }
            }
        });
    }

    /**
     * Given a folder with children and an unused property
     * When I check if the folder contains children having the unused property
     * Then the answer is negative
     */
    @org.junit.Test
    public void testHasChildrenWithPropertyValues_propertyNotUsed() throws Exception
    {
        doBehaviourDrivenTest(new BehaviourDrivenTest()
        {
            NodeRef parentFolder;
            QName property;
            Boolean result;

            @Override
            public void given() throws Exception
            {
                setupCollaborationSiteTestDataImpl();
                parentFolder = fileFolderService.create(documentLibrary, GUID.generate(), ContentModel.TYPE_FOLDER).getNodeRef();
                fileFolderService.create(parentFolder, GUID.generate(), ContentModel.TYPE_FOLDER).getNodeRef();
                fileFolderService.create(parentFolder, GUID.generate(), ContentModel.TYPE_FOLDER).getNodeRef();
                property = QName.createQName(URI, "customProp-" + GUID.generate());
            }

            @Override
            public void when() throws Exception
            {
                result = queryDAO.hasChildrenWithPropertyValues(folder, property, Arrays.asList("descr"));
            }

            @Override
            public void then() throws Exception
            {
                assertFalse(result);
            }

            @Override
            public void after() throws Exception
            {
                if (folder != null && nodeService.exists(folder))
                {
                    nodeService.deleteNode(folder);
                }
            }
        });
    }

    /**
     * Given any folder and any property
     * When I pass an empty array to the hasChildrenWithPropertyValues method
     * Then the answer is negative
     */
    @org.junit.Test
    public void testHasChildrenWithPropertyValues_emptyArray() throws Exception
    {
        doBehaviourDrivenTest(new BehaviourDrivenTest()
        {
            NodeRef parentFolder;
            NodeRef file1;
            Boolean result;

            @Override
            public void given() throws Exception
            {
                setupCollaborationSiteTestDataImpl();
                parentFolder = fileFolderService.create(documentLibrary, GUID.generate(), ContentModel.TYPE_FOLDER).getNodeRef();
                file1 = fileFolderService.create(parentFolder, GUID.generate(), ContentModel.TYPE_FOLDER).getNodeRef();

                nodeService.setProperty(file1, PROP_DESCRIPTION, "descr1");
            }

            @Override
            public void when() throws Exception
            {
                result = queryDAO.hasChildrenWithPropertyValues(folder, PROP_DESCRIPTION, new ArrayList());
            }

            @Override
            public void then() throws Exception
            {
                assertFalse(result);
            }

            @Override
            public void after() throws Exception
            {
                if (folder != null && nodeService.exists(folder))
                {
                    nodeService.deleteNode(folder);
                }
            }
        });
    }
}
