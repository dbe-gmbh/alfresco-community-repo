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

import java.util.ArrayList;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Session;

/**
 * This holds all the information necessary to perform operations
 * on AVMNodes, and is internall structured as a list of path components
 * from the root directory of a repository.
 * @author britt
 */
class Lookup
{
    /**
     * The Repository.
     */
    private RepositoryImpl fRepository;

    /**
     * The name of the Repository.
     */
    private String fRepName;
    
    /**
     * The components that make up this path.
     */
    private List<LookupComponent> fComponents;
    
    /**
     * Whether, at this point, a layered node has been hit.
     * Used while building a Lookup.
     */
    private boolean fLayeredYet;
 
    /**
     * The first LayeredDirectoryNode in the path.
     */
    private LayeredDirectoryNode fTopLayer;
    
    /**
     * The path index of the top LayeredDirectoryNode in the path.
     */
    private int fTopLayerIndex;
    
    /**
     * The lowest layered directory node's index seen so far.
     */
    private int fLowestLayerIndex;
    
    /**
     * The current component being looked at by this lookup.
     */
    private int fPosition;
    
    /**
     * Whether a needs-to-be-copied component has been seen.
     */
    private boolean fNeedsCopying;
    
    /**
     * Create a new one.
     * @param repository The Repository that's being looked in.
     * @param repName The name of that Repsository.
     */
    public Lookup(RepositoryImpl repository, String repName)
    {
        fRepository = repository;
        fRepName = repName;
        fComponents = new ArrayList<LookupComponent>();
        fLayeredYet = false;
        fTopLayer = null;
        fPosition = -1;
        fTopLayerIndex = -1;
        fLowestLayerIndex = -1;
        fNeedsCopying = false;
    }
    
    /**
     * Add a new node to the lookup.
     * @param node The node to add.
     * @param name The name of the node in the path.
     */
    public void add(AVMNode node, String name)
    {
        LookupComponent comp = new LookupComponent();
        comp.setName(name);
        comp.setNode(node);
//        SuperRepository.GetInstance().getSession().lock(node, LockMode.READ);
        if (!node.getIsNew())
        {
            fNeedsCopying = true;
        }
        else
        {
            if (fPosition >= 0 && (!((DirectoryNode)fComponents.get(fPosition).getNode()).directlyContains(node) ||
                                   !isDirectlyContained()))
            {
                fNeedsCopying = true;
            }
        }
        comp.setNeedsCopy(fNeedsCopying);
        // Record various things if this is layered.
        if (node.getType() == AVMNodeType.LAYERED_DIRECTORY)
        {
            LayeredDirectoryNode oNode = (LayeredDirectoryNode)node;
            // Record the indirection path that should be used.
            if (oNode.getPrimaryIndirection())
            {
                comp.setIndirection(oNode.getUnderlying());
            }
            else
            {
                String parentIndirection = fComponents.get(fPosition).getIndirection();
                if (parentIndirection == null)
                {
                    System.out.println("Oink!");
                }
                if (parentIndirection.endsWith("/"))  // This currently is impossible because
                                                      // root dirs are always plain.
                {
                    comp.setIndirection(parentIndirection + name);
                }
                else
                {
                    comp.setIndirection(parentIndirection + "/" + name);
                }
            }
            fLayeredYet = true;
            // Record the first layer seen.
            if (fTopLayer == null)
            {
                fTopLayer = oNode;
                fTopLayerIndex = fPosition + 1;
            }
            fLowestLayerIndex = fPosition + 1;
        }
        comp.setLowestLayerIndex(fLowestLayerIndex);
        comp.setLayered(fLayeredYet);
        fComponents.add(comp);
        fPosition++;
    }
    
    /**
     * Get the current node we're looking at.
     * @return The current node.   
     */
    public AVMNode getCurrentNode()
    {
        return fComponents.get(fPosition).getNode();
    }
    
    /**
     * Set the current node to one higher in the lookup.  This is used
     * repeatedly during copy on write.
     */
    public void upCurrentNode()
    {
        fPosition--;
    }
    
    /**
     * Is the current path layered.
     * @return Whether the current position in the path is layered.
     */
    public boolean isLayered()
    {
        assert fPosition >= 0;
        return fComponents.get(fPosition).isLayered();
    }

    /**
     * Determine if a node is directly contained.
     */
    private boolean isDirectlyContained()
    {
        if (!isLayered())
        {
            return true;
        }
        int pos = fPosition;
        while (pos > 1)
        {
            DirectoryNode dir = (DirectoryNode)fComponents.get(pos - 1).getNode();
            if (!dir.directlyContains(fComponents.get(pos).getNode()))
            {
                return false;
            }
            pos--;
        }
        return true;
    }
    
    /**
     * Determine if a node is directly in this layer.
     * @return Whether this node is directly in this layer.
     */
    public boolean isInThisLayer()
    {
        if (!isLayered())
        {
            return false;
        }
        int pos = fPosition;
        // Special case of the top layer.
        if (fComponents.get(pos).getNode() == fTopLayer)
        {
            return true;
        }
        // Walk up the containment chain and determine if each parent-child
        // relationship is one of direct containment.
        while (pos > 1)
        {
            DirectoryNode dir = (DirectoryNode)fComponents.get(pos - 1).getNode();
            if (!dir.directlyContains(fComponents.get(pos).getNode()))
            {
                return false;
            }
            if (dir.equals(fTopLayer))
            {
                return true;
            }
            pos--;
        }
        return false;
    }
    
    /**
     * Get the name of the current component.
     * @return The name.
     */
    public String getName()
    {
        return fComponents.get(fPosition).getName();
    }
    
    /**
     * Get the number of nodes.
     * @return The number of nodes.
     */
    public int size()
    {
        return fComponents.size();
    }
    
    /**
     * Get the current position within the lookup.
     * @return The current position.
     */
    public int getPosition()
    {
        return fPosition;
    }
    
    /**
     * Calculate the indirection path at this node.
     * @return The indirection path all the way down to the current node.
     */
    public String getIndirectionPath()
    {
        int lowestLayerIndex = fComponents.get(fPosition).getLowestLayerIndex();
        // The path is the underlying path of the lowest layer that is directly contained
        // by the top layer that is a primary indirection node.
        for (int pos = lowestLayerIndex; pos >= fTopLayerIndex; pos--)
        {
            AVMNode node = fComponents.get(pos).getNode();
            if (node.getType() != AVMNodeType.LAYERED_DIRECTORY)
            {
                continue;
            }
            LayeredDirectoryNode oNode =
                (LayeredDirectoryNode)node;
            if (oNode.getLayerID() == fTopLayer.getLayerID() &&
                oNode.getPrimaryIndirection())
            {
                StringBuilder builder = new StringBuilder();
                builder.append(oNode.getUnderlying());
                for (int i = pos + 1; i <= fPosition; i++)
                {
                    builder.append("/");
                    builder.append(fComponents.get(i).getName());
                }
                return builder.toString();
            }
        }
        // TODO This is gross.  There has to be a neater way to do this.
        assert false : "Not reached.";
        return "bogus";
    }
    
    /**
     * Get the computed indirection for the current node.
     * @return The indirection.
     */
    public String getCurrentIndirection()
    {
        String value = fComponents.get(fPosition).getIndirection();
        return value;
    }
    
    /**
     * Get the topmost Layered directory node.  Topmost in the
     * path lookup sense.
     * @return The topmost layered directory node.
     */
    public LayeredDirectoryNode getTopLayer()
    {
        return fTopLayer;
    }
    
    /**
     * Get the repository that this path is in.
     * @return The repository.
     */
    public Repository getRepository()
    {
        return fRepository;
    }
    
    /**
     * Get the path represented by this lookup.
     * @return The canonical path for this lookup.
     */
    public String getRepresentedPath()
    {
        if (fComponents.size() == 1)
        {
            return fRepName + ":/";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(fRepName);
        builder.append(':');
        int count = fComponents.size();
        for (int i = 1; i < count; i++)
        {
            builder.append('/');
            builder.append(fComponents.get(i).getName());
        }
        return builder.toString();
    }
    
    /**
     * Get whether the current node needs copying.
     * @return Whether the current node needs copying.
     */
    public boolean needsCopying()
    {
        return fComponents.get(fPosition).getNeedsCopy();
    }
    
    /**
     * Acquire locks for writing, in path lookup order.
     */
//    public void acquireLocks()
//    {
//        Session sess = SuperRepository.GetInstance().getSession();
//        for (LookupComponent comp : fComponents)
//        {
//            sess.lock(comp.getNode(), LockMode.UPGRADE);
//        }
//    }
}
