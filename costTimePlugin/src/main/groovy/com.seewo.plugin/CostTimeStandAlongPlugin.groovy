package com.seewo.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class CostTimeStandAlongPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        println("CostTimeStandAlongPlugin~")
    }
}