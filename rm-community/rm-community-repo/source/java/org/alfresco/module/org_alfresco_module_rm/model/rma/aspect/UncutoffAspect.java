package org.alfresco.module.org_alfresco_module_rm.model.rma.aspect;

import org.alfresco.module.org_alfresco_module_rm.model.BaseBehaviourBean;
import org.alfresco.repo.copy.CopyBehaviourCallback;
import org.alfresco.repo.copy.CopyDetails;
import org.alfresco.repo.copy.DoNothingCopyBehaviourCallback;
import org.alfresco.repo.policy.annotation.Behaviour;
import org.alfresco.repo.policy.annotation.BehaviourBean;
import org.alfresco.repo.policy.annotation.BehaviourKind;
import org.alfresco.service.namespace.QName;

/**
 * rma:uncutoff behaviour bean
 *
 * @author Mark Hibbins
 * @since 2.2
 */
@BehaviourBean
(
   defaultType = "rma:uncutOff"
)
public class UncutoffAspect extends BaseBehaviourBean
{
    /**
     * Copy callback.
     *
     * Uncutoff aspect should not be copied.
     */
    @Behaviour
    (
            kind = BehaviourKind.CLASS,
            policy = "alf:getCopyCallback"
    )
    public CopyBehaviourCallback getCopyCallback(QName classRef, CopyDetails copyDetails)
    {
        return new DoNothingCopyBehaviourCallback();
    }
}
