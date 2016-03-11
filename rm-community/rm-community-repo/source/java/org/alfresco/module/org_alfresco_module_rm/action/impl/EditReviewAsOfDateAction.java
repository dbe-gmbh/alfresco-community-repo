package org.alfresco.module.org_alfresco_module_rm.action.impl;

import java.util.Date;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.module.org_alfresco_module_rm.action.RMActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.repository.NodeRef;
import org.springframework.extensions.surf.util.I18NUtil;

/**
 *
 * Edit review as of date action
 *
 * @author Roy Wetherall
 */
public class EditReviewAsOfDateAction extends RMActionExecuterAbstractBase
{
    /** I18N */
    private static final String MSG_SPECIFY_VALID_DATE = "rm.action.specify-avlid-date";
    private static final String MSG_REVIEW_DETAILS_ONLY = "rm.action.review-details-only";

    public static final String PARAM_AS_OF_DATE = "asOfDate";

	/**
	 * @see org.alfresco.repo.action.executer.ActionExecuterAbstractBase#executeImpl(org.alfresco.service.cmr.action.Action,
	 *      org.alfresco.service.cmr.repository.NodeRef)
	 */
	@Override
	protected void executeImpl(Action action, NodeRef actionedUponNodeRef)
	{
	    if (getRecordService().isRecord(actionedUponNodeRef) &&
	            getNodeService().hasAspect(actionedUponNodeRef, ASPECT_VITAL_RECORD))
	    {
	        // Get the action parameter
	        Date reviewAsOf = (Date)action.getParameterValue(PARAM_AS_OF_DATE);
            if (reviewAsOf == null)
            {
                throw new AlfrescoRuntimeException(I18NUtil.getMessage(MSG_SPECIFY_VALID_DATE));
            }

	        // Set the as of date
	        this.getNodeService().setProperty(actionedUponNodeRef, PROP_REVIEW_AS_OF, reviewAsOf);

	    }
	    else
	    {
	        throw new AlfrescoRuntimeException(I18NUtil.getMessage(MSG_REVIEW_DETAILS_ONLY));
	    }
	}
}
