/**
 */
package org.palladiosimulator.experimentautomation.variation.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.palladiosimulator.experimentautomation.abstractsimulation.AbstractsimulationPackage;
import org.palladiosimulator.experimentautomation.abstractsimulation.impl.AbstractsimulationPackageImpl;
import org.palladiosimulator.experimentautomation.experiments.ExperimentsPackage;
import org.palladiosimulator.experimentautomation.experiments.impl.ExperimentsPackageImpl;
import org.palladiosimulator.experimentautomation.variation.StructuralVariation;
import org.palladiosimulator.experimentautomation.variation.ValueVariation;
import org.palladiosimulator.experimentautomation.variation.VariationFactory;
import org.palladiosimulator.experimentautomation.variation.VariationPackage;
import org.palladiosimulator.experimentautomation.variation.VariationRepository;
import org.palladiosimulator.experimentautomation.variation.VariationType;
import org.palladiosimulator.servicelevelobjective.ServicelevelObjectivePackage;
import org.palladiosimulator.simulizar.pms.PmsPackage;
import org.scaledl.usageevolution.UsageevolutionPackage;

import de.uka.ipd.sdq.identifier.IdentifierPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 *
 * @generated
 */
public class VariationPackageImpl extends EPackageImpl implements VariationPackage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass variationTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass valueVariationEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass structuralVariationEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass variationRepositoryEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package package URI
     * value.
     * <p>
     * Note: the correct way to create the package is via the static factory method {@link #init
     * init()}, which also performs initialization of the package, or returns the registered
     * package, if one already exists. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.palladiosimulator.experimentautomation.variation.VariationPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private VariationPackageImpl() {
        super(eNS_URI, VariationFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others
     * upon which it depends.
     *
     * <p>
     * This method is used to initialize {@link VariationPackage#eINSTANCE} when that field is
     * accessed. Clients should not invoke it directly. Instead, they should simply access that
     * field to obtain the package. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static VariationPackage init() {
        if (isInited) {
            return (VariationPackage) EPackage.Registry.INSTANCE.getEPackage(VariationPackage.eNS_URI);
        }

        // Obtain or create and register package
        final VariationPackageImpl theVariationPackage = (VariationPackageImpl) (EPackage.Registry.INSTANCE
                .get(eNS_URI) instanceof VariationPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
                        : new VariationPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        PmsPackage.eINSTANCE.eClass();
        ServicelevelObjectivePackage.eINSTANCE.eClass();
        UsageevolutionPackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        final ExperimentsPackageImpl theExperimentsPackage = (ExperimentsPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(ExperimentsPackage.eNS_URI) instanceof ExperimentsPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(ExperimentsPackage.eNS_URI) : ExperimentsPackage.eINSTANCE);
        final AbstractsimulationPackageImpl theAbstractsimulationPackage = (AbstractsimulationPackageImpl) (EPackage.Registry.INSTANCE
                .getEPackage(AbstractsimulationPackage.eNS_URI) instanceof AbstractsimulationPackageImpl ? EPackage.Registry.INSTANCE
                .getEPackage(AbstractsimulationPackage.eNS_URI) : AbstractsimulationPackage.eINSTANCE);

        // Create package meta-data objects
        theVariationPackage.createPackageContents();
        theExperimentsPackage.createPackageContents();
        theAbstractsimulationPackage.createPackageContents();

        // Initialize created meta-data
        theVariationPackage.initializePackageContents();
        theExperimentsPackage.initializePackageContents();
        theAbstractsimulationPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theVariationPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(VariationPackage.eNS_URI, theVariationPackage);
        return theVariationPackage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getVariationType() {
        return this.variationTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getVariationType_Name() {
        return (EAttribute) this.variationTypeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getVariationType_StrategyClass() {
        return (EAttribute) this.variationTypeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getVariationType_VariedEntityInterface() {
        return (EAttribute) this.variationTypeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getValueVariation() {
        return this.valueVariationEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getStructuralVariation() {
        return this.structuralVariationEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getVariationRepository() {
        return this.variationRepositoryEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getVariationRepository_Variation() {
        return (EReference) this.variationRepositoryEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public VariationFactory getVariationFactory() {
        return (VariationFactory) this.getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package. This method is guarded to have no affect on
     * any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public void createPackageContents() {
        if (this.isCreated) {
            return;
        }
        this.isCreated = true;

        // Create classes and their features
        this.variationTypeEClass = this.createEClass(VARIATION_TYPE);
        this.createEAttribute(this.variationTypeEClass, VARIATION_TYPE__NAME);
        this.createEAttribute(this.variationTypeEClass, VARIATION_TYPE__STRATEGY_CLASS);
        this.createEAttribute(this.variationTypeEClass, VARIATION_TYPE__VARIED_ENTITY_INTERFACE);

        this.valueVariationEClass = this.createEClass(VALUE_VARIATION);

        this.structuralVariationEClass = this.createEClass(STRUCTURAL_VARIATION);

        this.variationRepositoryEClass = this.createEClass(VARIATION_REPOSITORY);
        this.createEReference(this.variationRepositoryEClass, VARIATION_REPOSITORY__VARIATION);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model. This method is guarded to have
     * no affect on any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public void initializePackageContents() {
        if (this.isInitialized) {
            return;
        }
        this.isInitialized = true;

        // Initialize package
        this.setName(eNAME);
        this.setNsPrefix(eNS_PREFIX);
        this.setNsURI(eNS_URI);

        // Obtain other dependent packages
        final IdentifierPackage theIdentifierPackage = (IdentifierPackage) EPackage.Registry.INSTANCE
                .getEPackage(IdentifierPackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        this.variationTypeEClass.getESuperTypes().add(theIdentifierPackage.getIdentifier());
        this.valueVariationEClass.getESuperTypes().add(this.getVariationType());
        this.structuralVariationEClass.getESuperTypes().add(this.getVariationType());

        // Initialize classes and features; add operations and parameters
        this.initEClass(this.variationTypeEClass, VariationType.class, "VariationType", IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);
        this.initEAttribute(this.getVariationType_Name(), this.ecorePackage.getEString(), "name", null, 1, 1,
                VariationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
                !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getVariationType_StrategyClass(), this.ecorePackage.getEString(), "strategyClass",
                null, 1, 1, VariationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID,
                IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        this.initEAttribute(this.getVariationType_VariedEntityInterface(), this.ecorePackage.getEString(),
                "variedEntityInterface", null, 1, 1, VariationType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
                !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        this.initEClass(this.valueVariationEClass, ValueVariation.class, "ValueVariation", !IS_ABSTRACT, !IS_INTERFACE,
                IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.structuralVariationEClass, StructuralVariation.class, "StructuralVariation", !IS_ABSTRACT,
                !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        this.initEClass(this.variationRepositoryEClass, VariationRepository.class, "VariationRepository", !IS_ABSTRACT,
                !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getVariationRepository_Variation(), this.getVariationType(), null, "variation", null,
                0, -1, VariationRepository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE,
                !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        // Create resource
        this.createResource(eNS_URI);
    }

} // VariationPackageImpl
