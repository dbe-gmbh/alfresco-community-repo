/*
 * Copyright (C) 2006 Alfresco, Inc.
 *
 * Licensed under the Mozilla Public License version 1.1 
 * with a permitted attribution clause. You may obtain a
 * copy of the License at
 *
 *   http://www.alfresco.org/legal/license.txt
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 */
package org.alfresco.repo.avm;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * Interface for file content.  FileContent can be shared between files.
 * @author britt
 */
interface FileContent
{
    /**
     * Get the number of files that refer to this content.
     * @return The reference count.
     */
    public int getRefCount();

    /**
     * Set the reference count.
     * @param count The count to set.
     */
    public void setRefCount(int count);

    /**
     * Get an input stream from the content.
     * @param superRepo The SuperRepository.
     * @return An InputStream.
     */
    public InputStream getInputStream(SuperRepository superRepo);

    /**
     * Get an output stream to the content.
     * @param superRepo The SuperRepository.
     * @return an OutputStream.
     */
    public OutputStream getOutputStream(SuperRepository superRepo);
    
    /**
     * Get a random access file to this content.
     * @param superRepo The SuperRepository.
     * @param access The mode to open the file in.
     * @return A RandomAccessFile.
     */
    public RandomAccessFile getRandomAccess(SuperRepository superRepo, String access);
    
    /**
     * Get the length of the file.
     * @param superRepo
     * @return The length of the file.
     */
    public long getLength(SuperRepository superRepo);
    
    /**
     * Get the object id.
     * @return object id.
     */
    public long getId();
}