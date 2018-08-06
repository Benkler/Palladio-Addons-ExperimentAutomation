package org.palladiosimulator.experimentautomation.application.variation;

import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.experimentautomation.application.utils.EcoreHelper;
import org.palladiosimulator.experimentautomation.application.utils.PCMModelHelper;

import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;

public class AbstractUserActionReplication implements IVariationStrategy<Long> {

    /** the action that is to be replicated */
    private AbstractUserAction action;

    @Override
    public void setVariedObject(final EObject o) {
        if (!AbstractUserAction.class.isInstance(o)) {
            throw new RuntimeException("Expected an instance of " + AbstractUserAction.class.getName()
                    + ", but encountered a " + o.getClass().getName());
        }
        this.action = (AbstractUserAction) o;
    }

    @Override
    public String vary(final Long value) {
        AbstractUserAction lastAction = this.action;
        for (long l = 0; l < value; l++) {
            final AbstractUserAction previousSuccessor = lastAction.getSuccessor();
            final AbstractUserAction copy = EcoreHelper.copy(this.action);
            lastAction.setSuccessor(copy);
            copy.setScenarioBehaviour_AbstractUserAction(this.action.getScenarioBehaviour_AbstractUserAction());
            copy.setPredecessor(lastAction);
            copy.setSuccessor(previousSuccessor);
            copy.setEntityName(copy.getEntityName() + (l + 1));
            lastAction = copy;
        }

        return "Replication count = " + value + ": " + PCMModelHelper.toString(this.action);
    }

}
