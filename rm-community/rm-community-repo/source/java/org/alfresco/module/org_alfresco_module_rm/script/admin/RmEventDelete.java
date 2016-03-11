package org.alfresco.module.org_alfresco_module_rm.script.admin;

import java.util.HashMap;
import java.util.Map;

import org.alfresco.module.org_alfresco_module_rm.event.RecordsManagementEventService;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;

/**
 * Records management event delete web script
 *
 * @author Roy Wetherall
 */
public class RmEventDelete extends DeclarativeWebScript
{
    /** Reccords management event service */
    private RecordsManagementEventService rmEventService;

    /**
     * Set the records management event service
     *
     * @param rmEventService
     */
    public void setRecordsManagementEventService(RecordsManagementEventService rmEventService)
    {
        this.rmEventService = rmEventService;
    }

    /**
     * @see org.alfresco.web.scripts.DeclarativeWebScript#executeImpl(org.alfresco.web.scripts.WebScriptRequest, org.alfresco.web.scripts.Status, org.alfresco.web.scripts.Cache)
     */
    @Override
    public Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache)
    {
        Map<String, Object> model = new HashMap<String, Object>();

        // Event name
        Map<String, String> templateVars = req.getServiceMatch().getTemplateVars();
        String eventName = templateVars.get("eventname");
        if (eventName == null)
        {
            throw new WebScriptException(Status.STATUS_NOT_FOUND, "No event name was provided on the URL.");
        }

        // Check the event exists
        if (!rmEventService.existsEvent(eventName))
        {
            throw new WebScriptException(Status.STATUS_NOT_FOUND, "The event " + eventName + " does not exist.");
        }

        // Remove the event
        rmEventService.removeEvent(eventName);

        return model;
    }
}
