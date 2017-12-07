package models;

import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import models.BaseModel;
import models.Section;

/**
 * Category is a type of item
 */
@Entity
public class Category extends BaseModel {

    private static final long serialVersionUID = 1L;
    
    @Constraints.Required
    public String name;

    public boolean hasStandartSize;

    public boolean hasCustomSize;

    @ManyToOne
    public Section section;
}