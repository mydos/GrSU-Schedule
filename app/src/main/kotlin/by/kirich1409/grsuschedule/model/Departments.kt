package by.kirich1409.grsuschedule.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by kirillrozov on 9/13/15.
 */
public class Departments : ItemList<Department> {

    @JsonCreator
    constructor(@JsonProperty("items") departments: Array<Department>) : super(departments)
}
