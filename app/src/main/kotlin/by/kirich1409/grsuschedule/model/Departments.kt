package by.kirich1409.grsuschedule.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by kirillrozov on 9/13/15.
 */
public class Departments(
        @JsonProperty("items") departments: Array<Department>) : ItemList<Department>(departments)
