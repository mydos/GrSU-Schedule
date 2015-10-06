package by.kirich1409.grsuschedule.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by kirillrozov on 9/13/15.
 */
public class Faculties : ItemList<Faculty> {

    @JsonCreator
    constructor(@JsonProperty("items") faculties: Array<Faculty>) : super(faculties)
}
