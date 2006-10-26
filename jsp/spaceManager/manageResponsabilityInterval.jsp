<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
<h2><bean:message key="label.space.responsibility.management" bundle="SPACE_RESOURCES"/></h2>

<logic:present name="selectedSpaceInformation">

	<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>
	<bean:define id="selectedSpaceInformationId" name="selectedSpaceInformation" property="idInternal" />
	<jsp:include page="spaceCrumbs.jsp"/>

	<bean:define id="backLink">/manageSpaceResponsibility.do?method=showSpaceResponsibility&page=0&spaceInformationID=<bean:write name="selectedSpaceInformationId"/></bean:define>	
	<bean:define id="exceptionLink">/manageSpaceResponsibility.do?method=manageResponsabilityInterval&page=0&spaceInformationID=<bean:write name="selectedSpaceInformationId"/>&unitID=<bean:write name="unit" property="idInternal"/></bean:define>	

	<ul class="mvert15 list5">
		<li>
			<html:link page="<%= backLink %>">
				<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
			</html:link>
		</li>
	</ul>

	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		</p>
	</logic:messagesPresent>
	
	<fr:hasMessages>
		<p>
			<span class="error0">			
				<fr:message show="message"/>
			</span>
		</p>
	</fr:hasMessages>

	<logic:empty name="spaceResponsibility">
		<div class="infoop2 mtop15">
			<p><bean:message key="label.unit.name" bundle="SPACE_RESOURCES"/>: <strong><bean:write name="unit" property="name"/></strong></p>
			<p><bean:message key="label.unit.costCenterCode" bundle="SPACE_RESOURCES"/>: <strong><bean:write name="unit" property="costCenterCode"/></strong></p>
		</div>
		<fr:create id="create" action="<%= backLink %>" type="net.sourceforge.fenixedu.domain.space.SpaceResponsibility" schema="CreateSpaceResponsibilityInterval">
			<fr:hidden slot="space" name="selectedSpaceInformation" property="space" />
			<fr:hidden slot="unit" name="unit" />
			<fr:destination name="exception" path="<%= exceptionLink %>"/>
			<fr:destination name="invalid" path="<%= exceptionLink %>"/>			
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thmiddle thright thlight mtop15 mbottom1"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:create>
	</logic:empty>


	<logic:notEmpty name="spaceResponsibility">
		<div class="infoop2 mvert1">
			<p><bean:message key="label.unit.name" bundle="SPACE_RESOURCES"/>: <strong><bean:write name="spaceResponsibility" property="unit.name"/></strong></p>
			<p><bean:message key="label.unit.costCenterCode" bundle="SPACE_RESOURCES"/>: <strong><bean:write name="spaceResponsibility" property="unit.costCenterCode"/></strong></p>
		</div>
		<fr:edit id="edit" name="spaceResponsibility" action="<%= backLink %>" schema="EditSpaceResponsibility">
			<fr:destination name="exception" path="<%= exceptionLink %>"/>
			<fr:destination name="invalid" path="<%= exceptionLink %>"/>			
			<fr:layout name="tabular">
	   			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05 mbottom1"/>
	   			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	    	</fr:layout>
		</fr:edit>
	</logic:notEmpty>

</logic:present>