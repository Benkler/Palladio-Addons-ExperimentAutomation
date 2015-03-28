/**
 */
package monitorrepository;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.edp2.models.measuringpoint.MeasuringPoint;

import de.uka.ipd.sdq.pcm.core.entity.Entity;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Monitor</b></em>'. <!--
 * end-user-doc -->
 *
 * <!-- begin-model-doc --> A performance measurement for a pcm element (type level). <!--
 * end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link monitorrepository.Monitor#getMeasurementSpecification <em>Measurement Specification
 * </em>}</li>
 * <li>{@link monitorrepository.Monitor#getMeasuringPoint <em>Measuring Point</em>}</li>
 * </ul>
 * </p>
 *
 * @see monitorrepository.MonitorrepositoryPackage#getMonitor()
 * @model
 * @generated
 */
public interface Monitor extends EObject, Entity {
    /**
     * Returns the value of the '<em><b>Measurement Specification</b></em>' containment reference
     * list. The list contents are of type {@link monitorrepository.MeasurementSpecification}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Measurement Specification</em>' containment reference list isn't
     * clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Measurement Specification</em>' containment reference list.
     * @see monitorrepository.MonitorrepositoryPackage#getMonitor_MeasurementSpecification()
     * @model containment="true" required="true"
     * @generated
     */
    EList<MeasurementSpecification> getMeasurementSpecification();

    /**
     * Returns the value of the '<em><b>Measuring Point</b></em>' reference. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Measuring Point</em>' reference isn't clear, there really should
     * be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Measuring Point</em>' reference.
     * @see #setMeasuringPoint(MeasuringPoint)
     * @see monitorrepository.MonitorrepositoryPackage#getMonitor_MeasuringPoint()
     * @model required="true"
     * @generated
     */
    MeasuringPoint getMeasuringPoint();

    /**
     * Sets the value of the '{@link monitorrepository.Monitor#getMeasuringPoint
     * <em>Measuring Point</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Measuring Point</em>' reference.
     * @see #getMeasuringPoint()
     * @generated
     */
    void setMeasuringPoint(MeasuringPoint value);

} // Monitor