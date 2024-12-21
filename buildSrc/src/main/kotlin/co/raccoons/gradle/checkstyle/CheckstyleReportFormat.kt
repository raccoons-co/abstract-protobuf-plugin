/*
 * Copyright 2023, Raccoons. Developing simple way to change.
 *
 * @license MIT
 */

package co.raccoons.gradle.checkstyle

import org.gradle.api.plugins.quality.CheckstyleReports

enum class CheckstyleReportFormat {

    HTML {
        /**@inheritDoc */
        override fun subscribeTo(checkstyleReports: CheckstyleReports) {
            checkstyleReports.html.required.set(true)
        }
    },

    XML {
        /**@inheritDoc */
        override fun subscribeTo(checkstyleReports: CheckstyleReports) {
            checkstyleReports.xml.required.set(true)
        }
    };

    /** Subscribes to generate report type. */
    abstract fun subscribeTo(checkstyleReports: CheckstyleReports)
}
