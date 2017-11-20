package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;



/**
 * Section is a kind of activity
 */
@Entity 
public class Section extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String name;

}

