package org.palladiosimulator.experimentautomation.application.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.palladiosimulator.analyzer.workflow.blackboard.PCMResourceSetPartition;
import org.palladiosimulator.simulizar.launcher.jobs.LoadSimuLizarModelsIntoBlackboardJob;
import org.palladiosimulator.simulizar.reconfiguration.storydiagram.jobs.LoadSDMModelsIntoBlackboardJob;
import org.palladiosimulator.simulizar.reconfiguration.storydiagram.modelaccess.SDMResourceSetPartition;

import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.SequentialBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;

/**
 * Prepares the MDSD blackboard for partitions needed by Experiment Automation's initial model.
 * 
 * @author Sebastian Lehrig
 */
public class PrepareBlackboardJob extends SequentialBlackboardInteractingJob<MDSDBlackboard> {

    public PrepareBlackboardJob() {
        super(false);
    }

    @Override
    public void execute(final IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        // configure the original PCM model partition
        this.getBlackboard().addPartition(LoadModelsIntoBlackboardJob.PCM_MODELS_ORIGINAL_PARTITION_ID,
                new PCMResourceSetPartition());

        // configure SDM models partition
        this.getBlackboard().addPartition(LoadSDMModelsIntoBlackboardJob.SDM_MODEL_PARTITION_ID,
                new SDMResourceSetPartition());

        // configure the analyzed PCM model partition
        this.getBlackboard().addPartition(LoadSimuLizarModelsIntoBlackboardJob.PCM_MODELS_ANALYZED_PARTITION_ID,
                new PCMResourceSetPartition());
    }

}
