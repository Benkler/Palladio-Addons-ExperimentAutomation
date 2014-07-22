package org.palladiosimulator.experimentautomation.application.tooladapter.simucom.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.palladiosimulator.experimentautomation.experiments.Experiment;

import de.uka.ipd.sdq.simucomframework.SimuComConfig;
import de.uka.ipd.sdq.workflow.jobs.JobFailedException;
import de.uka.ipd.sdq.workflow.jobs.SequentialBlackboardInteractingJob;
import de.uka.ipd.sdq.workflow.jobs.UserCanceledException;
import de.uka.ipd.sdq.workflow.mdsd.blackboard.MDSDBlackboard;

public class LogExperimentInformationJob extends SequentialBlackboardInteractingJob<MDSDBlackboard> {

    // private static final Logger LOGGER = Logger.getLogger(LogExperimentInformationJob.class);

    final private Experiment experiment;
    final private SimuComConfig simuComConfig;
    final private int repetition;

    public LogExperimentInformationJob(Experiment experiment, final SimuComConfig simuComConfig, final int repetition) {
        this.experiment = experiment;
        this.simuComConfig = simuComConfig;
        this.repetition = repetition;
    }

    /**
     * TODO How to enable logging? [Lehrig]
     */
    @Override
    public void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException {
        // if(LOGGER.isInfoEnabled()) {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\n");
        stringBuilder.append("============= Experiment Automation: Experiment Run =============\n");

        stringBuilder.append("NAME: \"");
        stringBuilder.append(this.simuComConfig.getNameBase());
        stringBuilder.append("\"\n");

        stringBuilder.append("REPETITION: ");
        stringBuilder.append(this.repetition);
        stringBuilder.append(" of ");
        stringBuilder.append(this.experiment.getRepetitions());
        stringBuilder.append("\n");

        stringBuilder.append("SIMULATOR ID: \"");
        stringBuilder.append(this.simuComConfig.getSimulatorId());
        stringBuilder.append("\"\n");

        stringBuilder.append("RECORDER: \"");
        stringBuilder.append(this.simuComConfig.getRecorderName());
        stringBuilder.append("\"\n");

        System.out.print(stringBuilder.toString());

        // LOGGER.info(stringBuilder.toString());
        // }

        super.execute(monitor);
    }
}
