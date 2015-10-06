package by.kirich1409.grsuschedule.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by kirillrozov on 9/13/15.
 */
public class Groups : ItemList<Group> {

    @JsonCreator
    constructor(@JsonProperty("items") groups: Array<Group>) : super(groups)
}
