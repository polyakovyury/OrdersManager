package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;



/**
 * Items may have improvements.
 */
@Entity 
public class Improvement extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String name;

}