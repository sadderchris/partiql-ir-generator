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

package org.partiql.pig.generator.kotlin

/*
  Note a big design consideration for the classes in this file is that they are easy to consume by the
  Apache FreeMarker template.  FreeMarker, like all template languages, is not great at expressing complex
  logic so we pre-compute most of the complicated aspects of generating the Kotlin code and populate the
  results to an instance of this domain model.  This helps keep the template much simpler than it would
  otherwise be.
 */
data class KTypeUniverse(val domains: List<KTypeDomain>, val transforms: List<KTransform>)

/**
 * Models the *difference* between a source domain and a destination domain.
 */
data class KTransform(
    /**
     * This KTypeDomain instance will be derived from the *difference* between the destination
     * domain and the source domain.  See [TypeUniverse.computeTypeDomains] to learn how this
     * difference is computed.
     */
    val sourceDomainDifference: KTypeDomain,
    val destDomainKotlinName: String
)

data class KTypeDomain(
    /** The name of the type domain in the generated Kotlin code. .*/
    val kotlinName: String,
    /** The name of the type domain as defined in the type universe. */
    val tag: String,
    val tuples: List<KTuple>,
    val sums: List<KSum>
)

data class KProperty(
    /** The name of the property in the generated Kotlin code, in `camelCase`. */
    val kotlinName: String,
    /** The name of the property s-exp representation and as defined in the type universe. */
    val tag: String,
    /** The qualified Kotlin type name... */
    val kotlinTypeName: String,
    val isVariadic: Boolean,
    val isNullable: Boolean,
    val transformExpr: String,
    val rawTypeName: String
)

data class KParameter(
    val kotlinName: String,
    val kotlinType: String,
    val defaultValue: String?,
    val isVariadic: Boolean
)

data class KConstructorArgument(
    val kotlinName: String,
    val value: String
)

data class KBuilderFunction(
    val kotlinName: String,
    val parameters: List<KParameter>,
    val constructorArguments: List<KConstructorArgument>
)

data class KTuple(
    /** The name of the tuple in the Kotlin code. */
    val kotlinName: String,
    /** The name of the tuple in the s-exp representation and as defined in the type universe. */
    val tag: String,
    /** The name of the constructor class, fully qualified. */
    val constructorName: String,
    val superClass: String,
    val properties: List<KProperty>,
    val arity: IntRange,
    val builderFunctions: List<KBuilderFunction>,
    val isRecord: Boolean,
    /**
     * Set to true if this [KTuple] is a member of a [KTypeDomain] that represents the differences between
     * two type domains and this particular tuple has been removed or is different in the second domain.  If
     * `true`, its generated visitor transform `transform*` method will be `abstract`.
     */
    val isTransformAbstract: Boolean,
    val hasVariadicElement: Boolean
)

data class KSum(
    val kotlinName: String,
    val superClass: String,
    val variants: List<KTuple>,
    /**
     * Set to true if this [KSum] is a member of a [KTypeDomain] that represents the differences between
     * two type domains and this particular sum has been removed from the second.  When this is `true`, its
     * generated visitor transform `transform*` method will be `abstract`.
     */
    val isTransformAbstract: Boolean
)
