/*
 * Created on 17/Ago/2004
 */
package net.sourceforge.fenixedu.domain;


/**
 * @author joaosa & rmalo
 */

public class ExportGrouping extends ExportGrouping_Base {

    public ExportGrouping() {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
    }

    public ExportGrouping(Grouping groupProperties,
            ExecutionCourse executionCourse) {
    	this();
        super.setGrouping(groupProperties);
        super.setExecutionCourse(executionCourse);
    }

    public void delete(){
        this.setExecutionCourse(null);
        this.setGrouping(null);        
        removeRootDomainObject();
        super.deleteDomainObject();
    }

}
