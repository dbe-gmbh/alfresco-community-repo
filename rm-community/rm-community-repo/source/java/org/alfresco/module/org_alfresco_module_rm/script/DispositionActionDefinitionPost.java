package org.alfresco.module.org_alfresco_module_rm.script;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.module.org_alfresco_module_rm.disposition.DispositionActionDefinition;
import org.alfresco.module.org_alfresco_module_rm.disposition.DispositionSchedule;
import org.alfresco.module.org_alfresco_module_rm.model.RecordsManagementModel;
import org.alfresco.service.namespace.QName;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;

/**
 * Implementation for Java backed webscript to create a new dispositon action definition.
 *
 * @author Gavin Cornwell
 */
public class DispositionActionDefinitionPost extends DispositionAbstractBase
{
    /*
     * @see org.alfresco.web.scripts.DeclarativeWebScript#executeImpl(org.alfresco.web.scripts.WebScriptRequest, org.alfresco.web.scripts.Status, org.alfresco.web.scripts.Cache)
     */
    @Override
    protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache)
    {
        // parse the request to retrieve the schedule object
        DispositionSchedule schedule = parseRequestForSchedule(req);

        // retrieve the rest of the post body and create the action
        //       definition
        JSONObject json = null;
        DispositionActionDefinition actionDef = null;
        try
        {
            json = new JSONObject(new JSONTokener(req.getContent().getContent()));
            actionDef = createActionDefinition(json, schedule);
        }
        catch (IOException iox)
        {
            throw new WebScriptException(Status.STATUS_BAD_REQUEST,
                    "Could not read content from req.", iox);
        }
        catch (JSONException je)
        {
            throw new WebScriptException(Status.STATUS_BAD_REQUEST,
                        "Could not parse JSON from req.", je);
        }

        // create model object with just the action data
        Map<String, Object> model = new HashMap<String, Object>(1);
        model.put("action", createActionDefModel(actionDef, req.getURL() + "/" + actionDef.getId()));
        return model;
    }

    /**
     * Creates a dispositionActionDefinition node in the repo.
     *
     * @param json The JSON to use to create the action definition
     * @param schedule The DispositionSchedule the action is for
     * @return The DispositionActionDefinition representing the new action definition
     */
    protected DispositionActionDefinition createActionDefinition(JSONObject json,
              DispositionSchedule schedule) throws JSONException
    {
        // extract the data from the JSON request
        if (!json.has("name"))
        {
            throw new WebScriptException(Status.STATUS_BAD_REQUEST,
                    "Mandatory 'name' parameter was not provided in request body");
        }

        // create the properties for the action definition
        Map<QName, Serializable> props = new HashMap<QName, Serializable>(8);
        String name = json.getString("name");
        props.put(RecordsManagementModel.PROP_DISPOSITION_ACTION_NAME, name);

        if (json.has("description"))
        {
            props.put(RecordsManagementModel.PROP_DISPOSITION_DESCRIPTION, json.getString("description"));
        }

        if (json.has("period"))
        {
            props.put(RecordsManagementModel.PROP_DISPOSITION_PERIOD, json.getString("period"));
        }

        if (json.has("periodProperty"))
        {
            QName periodProperty = QName.createQName(json.getString("periodProperty"), getNamespaceService());
            props.put(RecordsManagementModel.PROP_DISPOSITION_PERIOD_PROPERTY, periodProperty);
        }

        if (json.has("eligibleOnFirstCompleteEvent"))
        {
            props.put(RecordsManagementModel.PROP_DISPOSITION_EVENT_COMBINATION,
                        json.getBoolean("eligibleOnFirstCompleteEvent") ? "or" : "and");
        }

        if (json.has("location"))
        {
            props.put(RecordsManagementModel.PROP_DISPOSITION_LOCATION,
                      json.getString("location"));
        }

        if (json.has("events"))
        {
            JSONArray events = json.getJSONArray("events");
            List<String> eventsList = new ArrayList<String>(events.length());
            for (int x = 0; x < events.length(); x++)
            {
                eventsList.add(events.getString(x));
            }
            props.put(RecordsManagementModel.PROP_DISPOSITION_EVENT, (Serializable)eventsList);
        }

        if (json.has("name") && "destroy".equals(json.getString("name")))
        {
            if (json.has("ghostOnDestroy"))
            {
                props.put(RecordsManagementModel.PROP_DISPOSITION_ACTION_GHOST_ON_DESTROY, "ghost");
            }
            else
            {
                props.put(RecordsManagementModel.PROP_DISPOSITION_ACTION_GHOST_ON_DESTROY, "delete");
            }
        }

        // add the action definition to the schedule
        return getDispositionService().addDispositionActionDefinition(schedule, props);
    }
}
