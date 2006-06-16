/**
 * 
 */
package org.alfresco.repo.avm;

/**
 * Layered nodes share this method.
 * @author britt
 */
interface Layered
{
    /**
     * Get the indirection, or underlying path that this 
     * node points to.
     * @param lookup The lookup path.  Needed for most nodes to determine
     * underlying path.
     * @return
     */
    public String getUnderlying(Lookup lookup);

    /**
     * Get the raw indirection of a layered node.
     * @return The raw indirection, which will be null for
     * LayeredDirectoryNodes that are not primary indirections.
     */
    public String getIndirection();
}
