UPDATE FILE
SET PERMITTED_GROUP = REPLACE(PERMITTED_GROUP, "currentDegreeCoordinators", "currentDegreeScientificComissionMembers")
WHERE KEY_DISSERTATION_THESIS IS NOT NULL 
   OR KEY_ABSTRACT_THESIS IS NOT NULL;
