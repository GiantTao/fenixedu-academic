/*
 * Created on 6/Jan/2004
 *  
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.GratuitySituation;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IGratuitySituation;
import Dominio.IStudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuitySituation;
import Util.GratuitySituationType;
import Util.Specialization;

/**
 * @author T�nia Pous�o
 *  
 */
public class GratuitySituationOJB extends ObjectFenixOJB implements IPersistentGratuitySituation
{

	public IGratuitySituation readGratuitySituatuionByStudentCurricularPlan(IStudentCurricularPlan studentCurricularPlan)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("studentCurricularPlan.idInternal", studentCurricularPlan.getIdInternal());

		return (IGratuitySituation) queryObject(GratuitySituation.class, criteria);
	}

	public List readGratuitySituationsByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"studentCurricularPlan.degreeCurricularPlan.idInternal",
			degreeCurricularPlan.getIdInternal());

		return queryList(GratuitySituation.class, criteria);
	}

	public List readGratuitySituationListByExecutionDegreeAndSpecialization(
		ICursoExecucao executionDegree,
		Specialization specialization)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		if (specialization != null)
		{
			criteria.addEqualTo(
				"studentCurricularPlan.Specialization.specialization",
				specialization.getSpecialization());
		}
		else
		{
			criteria.addNotNull("studentCurricularPlan.Specialization.specialization");
		}

		if (executionDegree != null)
		{
			criteria.addEqualTo(
				"gratuityValues.executionDegree.idInternal",
				executionDegree.getIdInternal());
		}
		return queryList(GratuitySituation.class, criteria);
	}

	public List readGratuitySituationListByExecutionDegreeAndSpecializationAndSituation(
		ICursoExecucao executionDegree,
		Specialization specialization,
		GratuitySituationType situation)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		if (executionDegree != null)
		{
			if (executionDegree.getIdInternal() != null)
			{
				criteria.addEqualTo(
					"gratuityValues.executionDegree.idInternal",
					executionDegree.getIdInternal());
			}
			else if (
				executionDegree.getExecutionYear() != null
					&& executionDegree.getExecutionYear().getYear() != null)
			{
				criteria.addEqualTo(
					"gratuityValues.executionDegree.executionYear.year",
					executionDegree.getExecutionYear().getYear());
			}
		}

		if (specialization != null)
		{
			criteria.addEqualTo(
				"studentCurricularPlan.Specialization.specialization",
				specialization.getSpecialization());
		}
		else
		{
			criteria.addNotNull("studentCurricularPlan.Specialization.specialization");
		}

		if (situation != null)
		{
			switch (situation.getValue())
			{
				case GratuitySituationType.CREDITOR_TYPE :
					//CREDITOR situation: remainingValue < 0 || payedValue > gratuityValue
					break;
				case GratuitySituationType.DEBTOR_TYPE :
					//DEBTOR situation: remainingValue > 0 || payedValue < gratuityValue
					break;
				case GratuitySituationType.REGULARIZED_TYPE :
					//REGULARIZED situation: remainingValue == 0|| payedValue == gratuityValue 					
					break;
				default :
					break;
			}
		}
		
		return queryList(GratuitySituation.class, criteria);
	}
}
