/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */

package org.partiql.pig.cmdline

import java.io.File

sealed class Command {
    object ShowHelp : Command()

    object ShowVersion : Command()
    data class InvalidCommandLineArguments(val message: String) : Command()
    data class Generate(val typeUniverseFile: File, val target: TargetLanguage) : Command()
}
