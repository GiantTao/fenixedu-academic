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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.register.confirmation" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p class="mtop15">
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<fr:view name="personBean" schema="student.show.personInformation">
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
        <fr:property name="columnClasses" value="width18em,,tdclear tderror1"/>
	</fr:layout>
</fr:view>

<fr:view name="executionDegreeBean" schema="student.show.registrationInformation" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
        <fr:property name="columnClasses" value="width18em,,tdclear tderror1"/>
	</fr:layout>
</fr:view>

<fr:view name="ingressionInformationBean" schema="student.show.ingressionInformation" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
        <fr:property name="columnClasses" value="width18em,,tdclear tderror1"/>
	</fr:layout>
</fr:view>

<fr:form action="/createStudent.do">	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createStudent"/>
	<fr:edit id="executionDegree" name="executionDegreeBean" visible="false" />
	<fr:edit id="person" name="personBean" visible="false" />	
	<fr:edit id="chooseIngression" name="ingressionInformationBean" visible="false" />
	<fr:edit id="precedentDegreeInformation" name="precedentDegreeInformationBean" visible="false" />
	<fr:edit id="originInformation" name="originInformationBean" visible="false" />

	<fr:edit name="ingressionInformationBean" schema="student.createRegistrationStartDates" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width18em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<h3><bean:message key="message.student.registerStudent.confirm" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" ><bean:message key="button.confirm" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit> 	
	<!--<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="document.forms[0].method.value='choosePerson'" ><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>	-->
</fr:form>