package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationStateBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.domain.util.workflow.IState;
import net.sourceforge.fenixedu.domain.util.workflow.StateBean;
import net.sourceforge.fenixedu.domain.util.workflow.StateMachine;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public abstract class RegistrationState extends RegistrationState_Base implements IState {

    public static Comparator<RegistrationState> DATE_COMPARATOR = new Comparator<RegistrationState>() {
	public int compare(RegistrationState leftState, RegistrationState rightState) {
	    int comparationResult = leftState.getStateDate().compareTo(rightState.getStateDate());
	    return (comparationResult == 0) ? leftState.getIdInternal().compareTo(rightState.getIdInternal()) : comparationResult;
	}
    };

    public static Comparator<RegistrationState> DATE_AND_STATE_TYPE_COMPARATOR = new Comparator<RegistrationState>() {
	public int compare(RegistrationState leftState, RegistrationState rightState) {
	    int comparationResult = DATE_COMPARATOR.compare(leftState, rightState);
	    if (comparationResult != 0) {
		return comparationResult;
	    }
	    comparationResult = leftState.getStateType().compareTo(rightState.getStateType());
	    return (comparationResult == 0) ? leftState.getIdInternal().compareTo(rightState.getIdInternal()) : comparationResult;
	}
    };

    public RegistrationState() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    private static RegistrationState createState(Registration registration, Person person, DateTime dateTime,
	    RegistrationStateType stateType) {

	switch (stateType) {
	case REGISTERED:
	    return new RegisteredState(registration, person, dateTime);
	case CANCELED:
	    return new CanceledState(registration, person, dateTime);
	case CONCLUDED:
	    return new ConcludedState(registration, person, dateTime);
	case FLUNKED:
	    return new FlunkedState(registration, person, dateTime);
	case INTERRUPTED:
	    return new InterruptedState(registration, person, dateTime);
	case SCHOOLPARTCONCLUDED:
	    return new SchoolPartConcludedState(registration, person, dateTime);
	case STUDYPLANCONCLUDED:
	    return new StudyPlanConcludedState(registration, person, dateTime);
	case INTERNAL_ABANDON:
	    return new InternalAbandonState(registration, person, dateTime);
	case EXTERNAL_ABANDON:
	    return new ExternalAbandonState(registration, person, dateTime);
	case MOBILITY:
	    return new MobilityState(registration, person, dateTime);
	case TRANSITION:
	    return new TransitionalState(registration, person, dateTime);
	case TRANSITED:
	    return new TransitedState(registration, person, dateTime);
	}

	return null;
    }

    protected void init(Registration registration, Person responsiblePerson, DateTime stateDate) {
	setRegistration(registration != null ? registration : null);
	setResponsiblePerson(selectPerson(responsiblePerson));
	setStateDate(stateDate != null ? stateDate : new DateTime());
    }

    private Person selectPerson(final Person responsiblePerson) {
	if (responsiblePerson != null) {
	    return responsiblePerson.hasRole(RoleType.MANAGER) ? null : responsiblePerson;
	} else {
	    final Person loggedPerson = AccessControl.getPerson();
	    return (loggedPerson == null) ? null : (loggedPerson.hasRole(RoleType.MANAGER) ? null : loggedPerson);
	}
    }

    protected void init(Registration registration) {
	init(registration, null, null);
    }

    final public IState nextState() {
	return nextState(new StateBean(defaultNextStateType().toString()));
    }

    protected RegistrationStateType defaultNextStateType() {
	throw new DomainException("error.no.default.nextState.defined");
    }

    public IState nextState(final StateBean bean) {
	return createState(getRegistration(), bean.getResponsible(), bean.getStateDateTime(), RegistrationStateType.valueOf(bean
		.getNextState()));
    }

    @Override
    final public void checkConditionsToForward() {
	checkConditionsToForward(new RegistrationStateBean(defaultNextStateType()));
    }

    @Override
    public void checkConditionsToForward(final StateBean bean) {
	checkCurriculumLinesForStateDate(bean);
    }

    private void checkCurriculumLinesForStateDate(final StateBean bean) {
	final ExecutionYear year = ExecutionYear.readByDateTime(bean.getStateDateTime());
	final RegistrationStateType nextStateType = RegistrationStateType.valueOf(bean.getNextState());

	if (!nextStateType.canHaveCurriculumLinesOnCreation() && getRegistration().hasAnyEnrolmentsIn(year)) {
	    throw new DomainException("RegisteredState.error.registration.has.enrolments.for.execution.year", year.getName());
	}
    }

    @Override
    public Set<String> getValidNextStates() {
	return Collections.emptySet();
    }

    public abstract RegistrationStateType getStateType();

    public ExecutionYear getExecutionYear() {
	return ExecutionYear.readByDateTime(getStateDate());
    }

    @Checked("RegistrationStatePredicates.deletePredicate")
    public void delete() {
	RegistrationState nextState = getNext();
	RegistrationState previousState = getPrevious();
	if (nextState != null && previousState != null
		&& !previousState.getValidNextStates().contains(nextState.getStateType().name())) {
	    throw new DomainException("error.cannot.delete.registrationState.incoherentState: "
		    + previousState.getStateType().name() + " -> " + nextState.getStateType().name());
	}
	removeRegistration();
	removeResponsiblePerson();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public void deleteWithoutCheckRules() {
	removeRegistration();
	removeResponsiblePerson();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public RegistrationState getNext() {
	List<RegistrationState> sortedRegistrationsStates = new ArrayList<RegistrationState>(getRegistration()
		.getRegistrationStates());
	Collections.sort(sortedRegistrationsStates, DATE_COMPARATOR);
	for (ListIterator<RegistrationState> iter = sortedRegistrationsStates.listIterator(); iter.hasNext();) {
	    RegistrationState state = (RegistrationState) iter.next();
	    if (state.equals(this)) {
		if (iter.hasNext()) {
		    return iter.next();
		}
		return null;
	    }
	}
	return null;
    }

    private RegistrationState getPrevious() {
	List<RegistrationState> sortedRegistrationsStates = new ArrayList<RegistrationState>(getRegistration()
		.getRegistrationStates());
	Collections.sort(sortedRegistrationsStates, DATE_COMPARATOR);
	for (ListIterator<RegistrationState> iter = sortedRegistrationsStates.listIterator(sortedRegistrationsStates.size()); iter
		.hasPrevious();) {
	    RegistrationState state = (RegistrationState) iter.previous();
	    if (state.equals(this)) {
		if (iter.hasPrevious()) {
		    return iter.previous();
		}
		return null;
	    }
	}
	return null;
    }

    public DateTime getEndDate() {
	RegistrationState state = getNext();
	return (state != null) ? state.getStateDate() : null;
    }

    public void setStateDate(YearMonthDay yearMonthDay) {
	super.setStateDate(yearMonthDay.toDateTimeAtMidnight());
    }

    public static class RegistrationStateDeleter extends VariantBean implements FactoryExecutor {

	public RegistrationStateDeleter(Integer idInternal) {
	    super();
	    setInteger(idInternal);
	}

	public Object execute() {
	    RootDomainObject.getInstance().readRegistrationStateByOID(getInteger()).delete();
	    return null;
	}
    }

    public static class RegistrationStateCreator extends RegistrationStateBean implements FactoryExecutor {

	public RegistrationStateCreator(Registration registration) {
	    super(registration);
	}

	private RegistrationStateCreator(Registration reg, Person responsible, DateTime creation, RegistrationStateType stateType) {
	    this(reg);
	    setResponsible(responsible);
	    setStateDateTime(creation);
	    setStateType(stateType);
	}

	public static RegistrationState createState(Registration reg, Person responsible, DateTime creation,
		RegistrationStateType stateType) {
	    return (RegistrationState) new RegistrationStateCreator(reg, responsible, creation, stateType).execute();
	}

	public Object execute() {
	    RegistrationState createdState = null;

	    final RegistrationState previousState = getRegistration().getStateInDate(getStateDateTime());
	    if (previousState == null) {
		createdState = RegistrationState.createState(getRegistration(), null, getStateDateTime(), getStateType());
	    } else {
		createdState = (RegistrationState) StateMachine.execute(previousState, this);
	    }
	    createdState.setRemarks(getRemarks());

	    final RegistrationState nextState = createdState.getNext();
	    if (nextState != null && !createdState.getValidNextStates().contains(nextState.getStateType().name())) {
		throw new DomainException("error.cannot.add.registrationState.incoherentState");
	    }

	    return createdState;
	}

    }

    public boolean isActive() {
	return false;
    }

    public boolean includes(final ExternalEnrolment externalEnrolment) {
	if (getStateType() == RegistrationStateType.MOBILITY) {
	    final DateTime mobilityDate = getStateDate();
	    return externalEnrolment.hasExecutionPeriod() && externalEnrolment.getExecutionYear().containsDate(mobilityDate);
	}

	throw new DomainException("RegistrationState.external.enrolments.only.included.in.mobility.states");
    }

    public boolean getCanDeleteActualInfo() {
	return getStateType().deleteActualPeriodInfo();
    }

    static public boolean hasAnyState(final Collection<RegistrationState> states, final Collection<RegistrationStateType> types) {
	for (final RegistrationState state : states) {
	    if (types.contains(state.getStateType())) {
		return true;
	    }
	}

	return false;
    }

}
