<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ page isELIgnored="true"%>

<html:xhtml/>

	<h2><bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
	<bean:define id="registrationId" name="registration" property="externalId" />
	
	<ul class="mtop2 list5">
		<li>
			<html:link page="/student.do?method=visualizeRegistration" paramId="registrationId" paramName="registrationId">
				<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>
			</html:link>
		</li>
	</ul>
	
	<div style="float: right;">
		<bean:define id="personID" name="registration" property="student.person.username"/>
		<html:img align="middle" src="<%= request.getContextPath() + "/user/photo/" + personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
	</div>
	
	<p class="mvert2">
		<span class="showpersonid">
		<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
			<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
				</fr:layout>
			</fr:view>
		</span>
	</p>
	
	<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<logic:present name="registration" property="ingressionType">
		<fr:view name="registration" schema="student.registrationDetail" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
				<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:present>
	<logic:notPresent name="registration" property="ingressionType">
		<fr:view name="registration" schema="student.registrationsWithStartData" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
				<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
			</fr:layout>
		</fr:view>
	</logic:notPresent>

	<br/>
	
	<logic:present name="registrationConclusionBean">
		<fr:form action="/registration.do?method=chooseProgramConclusion">

			<fr:edit id="registrationConclusionBean"
				name="registrationConclusionBean">
				
				<fr:schema type="org.fenixedu.academic.dto.student.IRegistrationBean" bundle="APPLICATION_RESOURCES">
					<fr:slot name="programConclusion" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
						<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.candidacy.ProgramConclusionProvider" />
						<fr:property name="format" value="${name.content}" />
					</fr:slot>
				</fr:schema>
			
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thright thlight thmiddle dinline"/>
		     		<fr:property name="columnClasses" value=",,tdclear tderror1"/>			
				</fr:layout>
				<fr:destination name="invalid" path="<%="/registration.do?method=prepareRegistrationConclusionProcessInvalid&registrationId=" + registrationId %>"/>
				<fr:destination name="cancel" path="<%="/student.do?method=visualizeRegistration&registrationId=" + registrationId %>"/>
			</fr:edit>
			<br/><br/>
		
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.continue"/>
			</html:submit>
		</fr:form>
	</logic:present>

	<logic:notPresent name="registrationConclusionBean">
		<em><strong><bean:message bundle="APPLICATION_RESOURCES" key="label.chooseCycleForRegistrationConclusion.no.cycle"/></strong></em>
		<br/>
	</logic:notPresent>
