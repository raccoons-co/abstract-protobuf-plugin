/*
 * Copyright 2023, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.gradle.jacoco

import org.gradle.testing.jacoco.tasks.JacocoReportsContainer

enum class JacocoReportFormat {

    HTML {
        /**@inheritDoc */
        override fun subscribeTo(jacocoReportContainer: JacocoReportsContainer) {
            jacocoReportContainer.html.required.set(true);
        }
    },

    XML {
        /**@inheritDoc */
        override fun subscribeTo(jacocoReportContainer: JacocoReportsContainer) {
            jacocoReportContainer.xml.required.set(true);
        }
    },

    CSV {
        /**@inheritDoc */
        override fun subscribeTo(jacocoReportContainer: JacocoReportsContainer) {
            jacocoReportContainer.csv.required.set(true);
        }
    };

    /** Subscribes to generate report type. */
    abstract fun subscribeTo(jacocoReportContainer: JacocoReportsContainer)
}
