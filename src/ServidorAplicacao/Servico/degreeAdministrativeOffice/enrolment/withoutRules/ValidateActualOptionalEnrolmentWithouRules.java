package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;

/**
 * @author dcs-rjao
 *
 * 9/Abr/2003
 */

public class ValidateActualOptionalEnrolmentWithouRules implements IServico {

	private static ValidateActualOptionalEnrolmentWithouRules _servico = new ValidateActualOptionalEnrolmentWithouRules();

	public static ValidateActualOptionalEnrolmentWithouRules getService() {
		return _servico;
	}

	private ValidateActualOptionalEnrolmentWithouRules() {
	}

	public final String getNome() {
		return "ValidateActualOptionalEnrolmentWithouRules";
	}

	public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext) throws FenixServiceException{
//		List currentEnroloments = infoEnrolmentContext.getInfoOptionalCurricularCoursesEnrolments();
//
//		if( (currentEnroloments == null) || (currentEnroloments.isEmpty()) ) {
//			infoEnrolmentContext.getEnrolmentValidationResult().setErrorMessage(EnrolmentValidationResult.NO_OPTIONAL_CURRICULAR_COURSES_TO_ENROLL);
//		} else {
//			infoEnrolmentContext.getEnrolmentValidationResult().setSucess(true);
//		}
		return infoEnrolmentContext;
	}
}