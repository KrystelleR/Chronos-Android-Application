package com.example.opsc7311_poe_group20

var timesheet_List = mutableListOf<timesheet_items>()
class timesheet_items (private var timesheet_elements_name: String,private var timesheet_element_image: Int) {
    fun gettimesheet_elements_name(): String {
        return timesheet_elements_name
    }
    fun settimesheet_elements_name(timesheet_element_name: String) {
        this.timesheet_elements_name = timesheet_element_name
    }
    fun gettimesheet_element_image(): Int {
        return timesheet_element_image
    }
    fun settimesheet_element_image(timesheet_element_image: Int) {
        this.timesheet_element_image = timesheet_element_image
    }
}