/**
 */
package de.uka.ipd.sdq.experimentautomation.eventsim.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import de.uka.ipd.sdq.experimentautomation.abstractsimulation.AbstractsimulationPackage;
import de.uka.ipd.sdq.experimentautomation.abstractsimulation.impl.AbstractsimulationPackageImpl;
import de.uka.ipd.sdq.experimentautomation.eventsim.EventSimConfiguration;
import de.uka.ipd.sdq.experimentautomation.eventsim.EventsimFactory;
import de.uka.ipd.sdq.experimentautomation.eventsim.EventsimPackage;
import de.uka.ipd.sdq.experimentautomation.experiments.ExperimentsPackage;
import de.uka.ipd.sdq.experimentautomation.experiments.impl.ExperimentsPackageImpl;
import de.uka.ipd.sdq.experimentautomation.simucom.SimucomPackage;
import de.uka.ipd.sdq.experimentautomation.simucom.impl.SimucomPackageImpl;
import de.uka.ipd.sdq.experimentautomation.variation.VariationPackage;
import de.uka.ipd.sdq.experimentautomation.variation.impl.VariationPackageImpl;
import de.uka.ipd.sdq.identifier.IdentifierPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 * @generated
 */
public class EventsimPackageImpl extends EPackageImpl implements EventsimPackage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private EClass eventSimConfigurationEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see de.uka.ipd.sdq.experimentautomation.eventsim.EventsimPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private EventsimPackageImpl() {
        super(eNS_URI, EventsimFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     * 
     * <p>This method is used to initialize {@link EventsimPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static EventsimPackage init() {
        if (isInited) return (EventsimPackage)EPackage.Registry.INSTANCE.getEPackage(EventsimPackage.eNS_URI);

        // Obtain or create and register package
        EventsimPackageImpl theEventsimPackage = (EventsimPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EventsimPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new EventsimPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        IdentifierPackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        VariationPackageImpl theVariationPackage = (VariationPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(VariationPackage.eNS_URI) instanceof VariationPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(VariationPackage.eNS_URI) : VariationPackage.eINSTANCE);
        ExperimentsPackageImpl theExperimentsPackage = (ExperimentsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ExperimentsPackage.eNS_URI) instanceof ExperimentsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ExperimentsPackage.eNS_URI) : ExperimentsPackage.eINSTANCE);
        AbstractsimulationPackageImpl theAbstractsimulationPackage = (AbstractsimulationPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(AbstractsimulationPackage.eNS_URI) instanceof AbstractsimulationPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(AbstractsimulationPackage.eNS_URI) : AbstractsimulationPackage.eINSTANCE);
        SimucomPackageImpl theSimucomPackage = (SimucomPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(SimucomPackage.eNS_URI) instanceof SimucomPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(SimucomPackage.eNS_URI) : SimucomPackage.eINSTANCE);

        // Create package meta-data objects
        theEventsimPackage.createPackageContents();
        theVariationPackage.createPackageContents();
        theExperimentsPackage.createPackageContents();
        theAbstractsimulationPackage.createPackageContents();
        theSimucomPackage.createPackageContents();

        // Initialize created meta-data
        theEventsimPackage.initializePackageContents();
        theVariationPackage.initializePackageContents();
        theExperimentsPackage.initializePackageContents();
        theAbstractsimulationPackage.initializePackageContents();
        theSimucomPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theEventsimPackage.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(EventsimPackage.eNS_URI, theEventsimPackage);
        return theEventsimPackage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EClass getEventSimConfiguration() {
        return eventSimConfigurationEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EventsimFactory getEventsimFactory() {
        return (EventsimFactory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents() {
        if (isCreated) return;
        isCreated = true;

        // Create classes and their features
        eventSimConfigurationEClass = createEClass(EVENT_SIM_CONFIGURATION);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents() {
        if (isInitialized) return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Obtain other dependent packages
        AbstractsimulationPackage theAbstractsimulationPackage = (AbstractsimulationPackage)EPackage.Registry.INSTANCE.getEPackage(AbstractsimulationPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        eventSimConfigurationEClass.getESuperTypes().add(theAbstractsimulationPackage.getAbstractSimulationConfiguration());

        // Initialize classes and features; add operations and parameters
        initEClass(eventSimConfigurationEClass, EventSimConfiguration.class, "EventSimConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        // Create resource
        createResource(eNS_URI);
    }

} // EventsimPackageImpl
