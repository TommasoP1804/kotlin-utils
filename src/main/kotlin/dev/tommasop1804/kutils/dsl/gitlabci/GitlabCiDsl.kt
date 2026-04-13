/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("unused")
@file:Since("3.3.0")

package dev.tommasop1804.kutils.dsl.gitlabci

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.*
import dev.tommasop1804.kutils.classes.time.*
import org.intellij.lang.annotations.Language
import java.io.Writer
import java.nio.file.Path
import kotlin.io.path.writeText

// --- MARKER ---

@DslMarker
annotation class GitlabCiDslMarker

// --- MODEL ---

/**
 * Represents a pipeline configuration for CI/CD processes.
 *
 * @property stages List of stage names defining the execution order of jobs within the pipeline.
 * @property variables Key-value pairs of variables used in the pipeline. Each variable is represented by a [VariableEntry].
 * @property workflow Optional workflow configuration using rules to control when the pipeline is executed.
 * @property defaults Optional default configuration that applies settings such as image, scripts, or timing to pipeline jobs.
 * @property includes List of external configurations to include in the pipeline. Each entry represents various source types such as local, remote, or project-scoped inclusions.
 * @property jobs Mapping of job names to their respective configurations. Each job specifies details like scripts, stages, rules, services, and other attributes.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class Pipeline(
    val stages: List<String>,
    val variables: Map<String, VariableEntry>,
    val workflow: WorkflowConfig?,
    val defaults: DefaultsConfig?,
    val includes: List<IncludeEntry>,
    val jobs: Map<String, Job>,
)

/**
 * Represents a single variable entry in a configuration.
 *
 * This data class is used to define variables within a pipeline or other related components. Each variable
 * can have an associated value, optional description, optional predefined options, and a flag to indicate
 * whether it should be expanded automatically.
 *
 * @property value The actual value of the variable. This is a required field.
 * @property description An optional description providing additional details regarding the variable.
 * @property options An optional list of predefined values that the variable can take.
 * @property expand A flag indicating whether the variable value should be expanded. Defaults to `true`.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class VariableEntry(
    val value: String,
    val description: String? = null,
    val options: List<String>? = null,
    val expand: Boolean = true,
)

/**
 * Configuration for defining workflow behavior in a CI/CD pipeline.
 *
 * This class represents the configuration for workflows, which can include
 * a set of rules to determine conditions and actions for pipeline execution.
 * It is utilized in pipeline definitions to customize the behavior of workflows
 * based on various conditions.
 *
 * @property rules A list of `WorkflowRule` objects, where each rule defines
 *     conditions and actions to be evaluated during the pipeline's workflow execution.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class WorkflowConfig(val rules: List<WorkflowRule>)

/**
 * Represents a rule within a workflow configuration.
 *
 * A workflow rule is used to define conditions and policies that dictate
 * when and how a specific workflow step should be executed.
 * This includes specifying conditional logic, execution policies, and
 * dynamic environment variables for the rule.
 *
 * @property ifCondition The conditional expression as a string. This defines
 * whether this rule applies based on the given condition.
 * @property when The execution policy for the rule, defined using an instance
 * of [WhenPolicy]. This determines under what circumstances the workflow step
 * should be executed.
 * @property variables A mapping of environment variables (key-value pairs)
 * that will be applied specifically when this rule is triggered.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class WorkflowRule(
    val ifCondition: String?,
    val `when`: WhenPolicy?,
    val variables: StringMap,
)

/**
 * Represents a policy that dictates the conditions under which a certain action or rule should execute.
 * This is typically used in defining CI/CD pipelines or workflow rules.
 *
 * @property yaml The string representation of the policy used in YAML configuration.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
enum class WhenPolicy(val yaml: String) {
    /**
     * Represents a policy that triggers an action only when the previous step succeeds.
     *
     * This policy is often used to ensure that subsequent actions or stages are executed
     * under successful conditions, such as when a prior pipeline step completes without errors.
     * @since 3.3.0
     */
    ON_SUCCESS("on_success"),
    /**
     * Represents the policy to execute an action only when the associated job has failed.
     *
     * This value can be assigned to the `whenPolicy` property in the `ArtifactsBuilder` class
     * to specify that the associated artifacts should only be handled if the job execution fails.
     *
     * The `ON_FAILURE` policy is one of several policies used to control behavior based on
     * the outcome of the job.
     *
     * For instance, `ON_FAILURE` is commonly utilized in scenarios where recovery mechanisms
     * or logging of failure-specific data need to be executed.
     * @since 3.3.0
     */
    ON_FAILURE("on_failure"),
    /**
     * Represents a policy that ensures the associated behavior is executed unconditionally, regardless of other conditions or outcomes.
     *
     * The `ALWAYS` policy is typically used to signify that a particular task, rule, or process
     * should be executed in every scenario. This can be beneficial in contexts where finalization,
     * cleanup, or guaranteed execution steps are required.
     *
     * @since 3.3.0
     */
    ALWAYS("always"),
    /**
     * Represents the `MANUAL` policy in the `WhenPolicy` enum.
     *
     * The `MANUAL` policy is typically used to denote that a job or rule
     * in a GitLab CI pipeline should only be executed manually.
     *
     * This policy can be applied in scenarios where user intervention is required
     * to explicitly trigger the execution of the associated job or rule.
     * @since 3.3.0
     */
    MANUAL("manual"),
    /**
     * Represents a delayed execution policy.
     *
     * This value is used to indicate that a job execution must be delayed.
     * It can be useful for scenarios where a specific amount of time or
     * condition needs to be satisfied before a job is executed.
     *
     * Associated YAML value: "delayed".
     * @since 3.3.0
     */
    DELAYED("delayed"),
    /**
     * Represents a policy that indicates an action should never be executed.
     *
     * This value is used in specific contexts where the user wants to explicitly
     * disable certain pipeline stages or rules in a GitLab CI configuration. When
     * applied, the corresponding action will not run under any circumstances.
     *
     * Typically used in DSL builders for rules and workflows where conditions and
     * execution policies are specified, such as `RuleBuilder` and `WorkflowBuilder`.
     * @since 3.3.0
     */
    NEVER("never"),
}

/**
 * Represents the default configuration settings for a process or operation, encapsulating parameters
 * such as scripts to execute before and after actions, tags, retry behavior, timeout, and caching.
 *
 * @property image Optional identifier for the image used in the configuration.
 * @property beforeScript A list of scripts to execute before the main operation.
 * @property afterScript A list of scripts to execute after the main operation.
 * @property tags A list of tags associated with the configuration.
 * @property retry Configuration for retry behavior, including conditions and maximum retry attempts.
 * @property timeout Optional duration specifying the timeout for the operation.
 * @property interruptible Optional flag indicating if the operation can be interrupted.
 * @property cache Configuration settings related to caching for the operation.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class DefaultsConfig(
    val image: String?,
    val beforeScript: List<String>,
    val afterScript: List<String>,
    val tags: List<String>,
    val retry: RetryConfig?,
    val timeout: Duration?,
    val interruptible: Boolean?,
    val cache: CacheConfig?,
)

/**
 * Represents an entry that specifies an inclusion configuration within a pipeline.
 *
 * The `IncludeEntry` class is used to define various forms of external resources or templates
 * that can be included in a pipeline configuration. It supports multiple ways of defining
 * inclusions such as local files, remote URLs, templates, or project-based inclusions.
 *
 * @property local Specifies a local file path to be included.
 * @property project Specifies the project from which a file should be included.
 * @property ref Defines the reference (branch, tag, or commit) for the included file from a project.
 * @property file Specifies the file path within a project to be included.
 * @property remote Specifies a remote URL to include a pipeline configuration.
 * @property template Specifies the name of a predefined template to be included.
 * @property component Specifies the name of a custom component for inclusion.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class IncludeEntry(
    val local: String? = null,
    val project: String? = null,
    val ref: String? = null,
    val file: String? = null,
    val remote: String? = null,
    val template: String? = null,
    val component: String? = null,
)

/**
 * Represents a job configuration in a CI/CD pipeline.
 *
 * @property stage The stage in the pipeline the job belongs to.
 * @property image The Docker image to use for the job execution.
 * @property script The main script to execute in the job.
 * @property beforeScript The script to execute before the main script.
 * @property afterScript The script to execute after the main script.
 * @property variables Custom environment variables specific to this job.
 * @property rules A list of rules that determine when the job should run.
 * @property only Defines the conditions under which the job will run based on inclusion criteria.
 * @property except Defines the conditions under which the job will not run based on exclusion criteria.
 * @property tags A list of tags used to determine the job runner.
 * @property services A list of services required for the job execution.
 * @property artifacts Configuration for job artifacts.
 * @property cache Configuration for caching data during the job.
 * @property dependencies A list of dependent jobs whose artifacts are used.
 * @property needs A list of jobs that must complete before this job starts.
 * @property environment Configuration for the environment in which the job is executed.
 * @property retry Configuration for retrying the job in case of failure.
 * @property timeout The maximum duration for the job execution.
 * @property interruptible Indicates if the job execution can be interrupted.
 * @property allowFailure Indicates if the job is allowed to fail without failing the pipeline.
 * @property parallel The number of parallel instances of the job to run.
 * @property resourceGroup The resource group this job belongs to.
 * @property trigger Configuration to trigger downstream pipelines.
 * @property extends Defines the job this configuration extends.
 * @property inherit Specifies configuration inheritance for the job.
 * @property coverage Expression to capture code coverage from the job output.
 * @property releaseConfig Configuration for releasing an artifact.
 * @property secretsConfig Secrets required for the job execution.
 * @property idTokens ID tokens required for authorization in the job.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class Job(
    val stage: String?,
    val image: String?,
    val script: List<String>,
    val beforeScript: List<String>,
    val afterScript: List<String>,
    val variables: StringMap,
    val rules: List<JobRule>,
    val only: OnlyExcept?,
    val except: OnlyExcept?,
    val tags: List<String>,
    val services: List<ServiceConfig>,
    val artifacts: ArtifactsConfig?,
    val cache: CacheConfig?,
    val dependencies: List<String>,
    val needs: List<NeedConfig>,
    val environment: EnvironmentConfig?,
    val retry: RetryConfig?,
    val timeout: Duration?,
    val interruptible: Boolean?,
    val allowFailure: Boolean,
    val parallel: Int?,
    val resourceGroup: String?,
    val trigger: TriggerConfig?,
    val extends: String?,
    val inherit: InheritConfig?,
    val coverage: String?,
    val releaseConfig: ReleaseConfig?,
    val secretsConfig: Map<String, SecretVaultConfig>,
    val idTokens: Map<String, IdTokenConfig>,
)

/**
 * Represents a rule for defining conditions and behavior for executing a job in a CI/CD pipeline.
 *
 * @property ifCondition A condition that determines whether the job rule applies, expressed as
 *   a string evaluation.
 * @property changes A list of file paths whose changes should trigger the rule.
 * @property exists A list of file paths that must exist for the rule to apply.
 * @property when Specifies when the job should be executed, using a predefined execution policy.
 * @property allowFailure Indicates whether the job is allowed to fail without causing the pipeline
 *   to fail.
 * @property variables A map of environment variables to set for the job when this rule is applied.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class JobRule(
    val ifCondition: String?,
    val changes: List<String>,
    val exists: List<String>,
    val `when`: WhenPolicy?,
    val allowFailure: Boolean?,
    val variables: StringMap,
)

/**
 * Represents a configuration for including or excluding certain criteria.
 *
 * @property refs A list of specific references used to define inclusion or exclusion.
 * @property variables A list of variables considered when determining inclusion or exclusion.
 * @property changes A list of changes that impact the inclusion or exclusion logic.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class OnlyExcept(
    val refs: List<String> = emptyList(),
    val variables: List<String> = emptyList(),
    val changes: List<String> = emptyList(),
)

/**
 * Represents the configuration for a service used in a CI/CD pipeline.
 *
 * @property name The name of the service.
 * @property alias An optional alias for the service.
 * @property command A list of commands to execute for the service.
 * @property variables A map of key-value pairs representing environment variables for the service.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class ServiceConfig(
    val name: String,
    val alias: String?,
    val command: List<String>,
    val variables: StringMap,
)

/**
 * Represents the configuration for managing artifacts in a system.
 *
 * @property paths A list of file paths to be included as artifacts.
 * @property reports A map containing key-value pairs for report artifacts.
 * @property expireIn The duration after which the artifacts should expire and be deleted.
 * @property when Specifies the policy for when artifacts should be created.
 * @property name An optional name for the artifacts.
 * @property untracked A flag indicating whether untracked files should be included as artifacts.
 * @property exclude A list of file paths or patterns to be excluded from the artifacts.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class ArtifactsConfig(
    val paths: List<String>,
    val reports: StringMap,
    val expireIn: Duration?,
    val `when`: WhenPolicy?,
    val name: String?,
    val untracked: Boolean,
    val exclude: List<String>,
)

/**
 * Configuration for caching in CI/CD pipelines.
 *
 * This class represents the caching configuration that can be used to define
 * how artifacts and files are cached during pipeline executions.
 *
 * @property key The unique key used to identify the cache. When set to null,
 *               the cache key is generated automatically.
 * @property keyFiles A list of file paths used to compute the cache key. When
 *                    specified, the contents of these files are used to generate
 *                    a consistent cache key.
 * @property paths A list of paths that should be cached and restored during pipeline
 *                 execution.
 * @property policy The caching policy that determines whether the cache is pulled,
 *                  pushed, or both. This property uses the values defined in the
 *                  `CachePolicy` enum.
 * @property untracked A boolean value indicating whether untracked files in the
 *                     repository should be included in the cache.
 * @property unprotect A boolean value indicating whether the cache should be
 *                     accessible to protected branches or tags only.
 * @property `when` The condition or event during which the cache is created or restored.
 *                  This property uses the values defined in the `WhenPolicy` enum.
 * @property fallbackKeys A list of fallback keys to use if the primary cache key does not
 *                        match any existing cache. The fallback keys are tried in the order
 *                        they are specified.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class CacheConfig(
    val key: String?,
    val keyFiles: List<String>,
    val paths: List<String>,
    val policy: CachePolicy?,
    val untracked: Boolean,
    val unprotect: Boolean,
    val `when`: WhenPolicy?,
    val fallbackKeys: List<String>,
)

/**
 * Represents the caching policies available for managing cache behavior in a CI/CD pipeline.
 *
 * The `CachePolicy` enum defines three modes:
 * - `PULL`: Retrieves existing cached artifacts without attempting to update or modify the cache.
 * - `PUSH`: Updates or creates new artifacts in the cache without retrieving existing ones.
 * - `PULL_PUSH`: Combines both policies to allow retrieving and updating the cached artifacts.
 *
 * Each policy is mapped to its corresponding YAML representation, enabling seamless integration
 * with YAML-based configuration files (e.g., `.gitlab-ci.yml`).
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
enum class CachePolicy(val yaml: String) {
    /**
     * Represents a cache policy used to specify that the caching behavior should only pull
     * artifacts from a remote cache without pushing any changes back.
     *
     * This is typically used in configurations where only the retrieval of existing
     * cached artifacts is necessary, and modifications to the cache are not required.
     *
     * The `PULL` policy is particularly useful for ensuring that builds or processes
     * depend solely on the previously stored cache state without generating or updating
     * new cache entries.
     * @since 3.3.0
     */
    PULL("pull"),
    /**
     * Represents a cache policy that allows caching artifacts to be pushed to a remote server.
     *
     * This policy is used in scenarios where the build process needs to store cache artifacts
     * for later use, typically on a remote location accessible by subsequent jobs or pipelines.
     *
     * The `PUSH` policy ensures that the cache is uploaded to the configured remote cache storage.
     * @since 3.3.0
     */
    PUSH("push"),
    /**
     * Represents a caching policy where both pull and push mechanisms are used.
     *
     * This policy allows pulling data that is not yet cached and pushing
     * updates to the cache as changes occur. It combines the features of both
     * pull and push strategies to maintain synchronized and up-to-date data within the system.
     *
     * @since 3.3.0
     */
    PULL_PUSH("pull-push"),
}

/**
 * Represents the configuration for a dependency or prerequisite job needed by the current job.
 *
 * @property job The name of the dependent job.
 * @property artifacts Indicates whether to download the artifacts from the dependent job.
 * @property optional Indicates if the dependent job is optional. When true, the current job does not fail
 *                    if the dependent job does not exist or has not run.
 * @property pipeline The ID or name of the specific pipeline from which the dependency job originates, if applicable.
 * @property project The name or ID of the project where the dependency job is located, if it is in a different project.
 * @property ref The branch or tag in the specified project to use for the dependency job.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class NeedConfig(
    val job: String,
    val artifacts: Boolean,
    val optional: Boolean,
    val pipeline: String?,
    val project: String?,
    val ref: String?,
)

/**
 * Represents the configuration settings for an environment.
 *
 * @property name The name of the environment.
 * @property url The optional URL associated with the environment.
 * @property action The action to be performed on the environment. This is represented by the [EnvironmentAction] enum.
 * @property autoStopIn The optional duration after which the environment will be automatically stopped.
 * @property onStop The optional script or action to be triggered upon stopping the environment.
 * @property kubernetes The optional Kubernetes-specific configuration for the environment.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class EnvironmentConfig(
    val name: String,
    val url: String?,
    val action: EnvironmentAction?,
    val autoStopIn: Duration?,
    val onStop: String?,
    val kubernetes: KubernetesConfig?,
)

/**
 * Represents actions that can be performed on an environment in a CI/CD pipeline.
 *
 * Each enum value corresponds to a specific action, defined by its YAML representation.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
enum class EnvironmentAction(val yaml: String) {
    /**
     * Represents the action to initiate or launch an environment process.
     *
     * @since 3.3.0
     */
    START("start"),
    /**
     * Represents the action to stop an environment or process.
     * Corresponds to the "stop" value in YAML representation.
     * @since 3.3.0
     */
    STOP("stop"),
    /**
     * Represents the action to prepare the environment before execution or deployment.
     * Typically used in scenarios where pre-configuration or setup is required.
     * @since 3.3.0
     */
    PREPARE("prepare"),
    /**
     * Represents the `verify` action in the context of environment-related operations.
     *
     * This action is used to confirm or validate the state or conditions of an environment.
     * @since 3.3.0
     */
    VERIFY("verify"),
    /**
     * Represents the action of accessing an environment.
     * This action corresponds to operations that provide entry
     * or retrieval capabilities within the environment.
     *
     * @since 3.3.0
     */
    ACCESS("access"),
}

/**
 * Represents the configuration settings specific to a Kubernetes environment.
 *
 * @property namespace The Kubernetes namespace where the environment resides. This can be null
 * if no namespace is explicitly specified.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class KubernetesConfig(val namespace: String?)

/**
 * Configuration for defining retry behavior in workflow jobs.
 *
 * @property max The maximum number of retry attempts allowed for a task.
 * @property when A list of conditions under which the retry attempts should occur.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class RetryConfig(
    val max: Int,
    val `when`: List<String>,
)

/**
 * Configuration for triggering downstream pipelines within a CI/CD job.
 *
 * @property project The name of the downstream project to trigger. Can be null if not specified.
 * @property branch The branch of the downstream project to trigger. Can be null to use the default branch.
 * @property include Additional files or configurations to include when triggering the downstream pipeline. Can be null.
 * @property strategy The triggering strategy, which defines how the downstream pipeline should be initiated. Can be null.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class TriggerConfig(
    val project: String?,
    val branch: String?,
    val include: String?,
    val strategy: String?,
)

/**
 * Represents the inheritance configuration for a job, specifying whether
 * certain properties should inherit values from their parent scope.
 *
 * @property default Indicates whether the default inheritance behavior is enabled.
 * @property variables Specifies whether variables should be inherited.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class InheritConfig(
    val default: Boolean?,
    val variables: Boolean?,
)

/**
 * Represents the configuration for creating or updating a release in a CI/CD pipeline.
 *
 * @property tagName The name of the tag associated with the release. This is a mandatory field.
 * @property description An optional description for the release, providing additional context or details.
 * @property name An optional name for the release, which may be used for display purposes in a CI/CD system.
 * @property ref An optional reference specifying the Git commit or branch associated with the release.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class ReleaseConfig(
    val tagName: String,
    val description: String?,
    val name: String?,
    val ref: String?,
)

/**
 * Represents the configuration for accessing a secret stored in a vault.
 *
 * This class defines the vault identifier and the optional field within the
 * vault that contains the secret value.
 *
 * @property vault The identifier of the vault where the secret is stored.
 * @property field The specific field in the vault containing the secret value.
 *                 This field is optional and may be null if not applicable.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class SecretVaultConfig(
    val vault: String,
    val field: String?,
)

/**
 * Represents the configuration for an OpenID Connect (OIDC) ID Token used in CI/CD jobs.
 *
 * @property aud The audience (aud) claim to include in the ID Token,
 *               which identifies the intended recipient(s) of the token.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class IdTokenConfig(val aud: String)

// --- BUILDERS ---

/**
 * A DSL builder class for defining rules in a GitLab CI/CD pipeline.
 *
 * This class provides methods for configuring the conditions, policies,
 * and behaviors of a pipeline rule. It allows the user to specify when
 * a particular job or step should be triggered, based on various factors
 * like branches, tags, and pipeline sources.
 *
 * The configured rules can then be built into a `JobRule` object.
 * @author Tommaso Pastorelli
 * @since 3.3.0
 */
@GitlabCiDslMarker
class RuleBuilder {
    /**
     * Defines the condition under which a rule or action should be executed within the pipeline.
     *
     * The value of this variable is a `String?` and can be used to represent conditional execution logic
     * in CI/CD pipelines. It supports expressions that evaluate specific runtime variables or states
     * such as branch names, tags, schedules, and pipeline sources.
     *
     * This property is often set using helper functions like `ifMergeRequest`, `ifBranch`, `ifTag`,
     * `ifSchedule`, etc., which populate it with appropriate predefined conditions.
     * @since 3.3.0
     */
    @Language("gitlabciexpressionlanguage") var ifCondition: String? = null
    /**
     * Represents a collection of file or directory paths that determine when the associated
     * pipeline rule or job is triggered based on changes to the specified paths.
     *
     * The `changes` property is typically used in CI/CD pipeline configuration to enable
     * conditional execution of rules or jobs for specific file or directory modifications.
     * When set, the pipeline rule will evaluate whether the defined paths in this list have
     * been modified as part of a commit or merge request, triggering execution if applicable.
     *
     * Defaults to an empty list, indicating no change-based conditions are initially specified.
     * @since 3.3.0
     */
    var changes: List<String> = emptyList()
    /**
     * A variable representing a list of strings.
     * This list is initialized as empty by default.
     * @since 3.3.0
     */
    var exists: List<String> = emptyList()
    /**
     * Specifies the execution policy that determines under which conditions a rule or job should run.
     *
     * The `whenPolicy` property is used in pipeline or workflow builders (e.g., `RuleBuilder`)
     * to define specific triggers for a rule or job. It can take values from the `WhenPolicy`
     * enumeration, such as `MANUAL`, `ALWAYS`, `NEVER`, `DELAYED`, or `ON_SUCCESS`.
     * If set to `null`, the default behavior relies on context or other properties of the rule.
     *
     * This property enables precise control over job execution, allowing developers
     * to customize the conditions under which stages or actions in a CI/CD pipeline occur.
     * @since 3.3.0
     */
    var whenPolicy: WhenPolicy? = null
    /**
     * Determines whether the associated rule allows execution despite a failure in previous steps.
     *
     * When set to `true`, the pipeline rule will execute even if prior steps have failed.
     * If set to `false`, the rule will only execute when all preceding steps succeed.
     * When `null`, the behavior depends on the default pipeline configuration or other contextual factors.
     *
     * This property is commonly used for defining failure-tolerant rules or stages in CI/CD pipelines.
     * @since 3.3.0
     */
    var allowFailure: Boolean? = null
    /**
     * Represents a mapping of additional variables utilized for configuring rule behavior
     * within a CI/CD pipeline. This property stores a collection of key-value pairs
     * where the key is a string identifier, and the value is a list of associated strings.
     *
     * This field is typically used to define dynamic or static variables that can be
     * accessed by pipeline jobs or used in conditional logic during rule execution.
     * @since 3.3.0
     */
    val variables: StringMMap = emptyMMap()

    /**
     * Sets the condition for the rule to execute only when the GitLab CI pipeline is triggered
     * by a merge request event.
     *
     * This method updates the `ifCondition` property to check whether the `CI_PIPELINE_SOURCE`
     * environment variable equals `"merge_request_event"`. It is typically used to define
     * pipeline rules that apply specifically to merge request events in GitLab.
     * @since 3.3.0
     */
    // Common shortcuts
    fun ifMergeRequest() { ifCondition = $$"$CI_PIPELINE_SOURCE == \"merge_request_event\"" }
    /**
     * Sets a condition for the rule based on the specified branch name.
     * The rule will apply only if the current branch matches the given name.
     *
     * @param name The name of the branch to match against.
     * @since 3.3.0
     */
    fun ifBranch(name: String) { ifCondition = $$"$CI_COMMIT_BRANCH == \"$$name\"" }
    /**
     * Sets the condition to execute the rule only when a GitLab CI/CD pipeline
     * is triggered by a tag.
     *
     * This method updates the `ifCondition` property to evaluate whether the
     * pipeline execution source is associated with a tag (`CI_COMMIT_TAG`).
     * It allows conditional rule application based on the presence of a commit tag.
     * @since 3.3.0
     */
    fun ifTag() { ifCondition = $$"$CI_COMMIT_TAG" }
    /**
     * Sets a condition for the rule to execute only if the current GitLab tag matches
     * the specified regular expression pattern.
     *
     * The condition is applied to the CI/CD pipeline configuration for triggering jobs
     * based on tag patterns.
     *
     * @param pattern The regular expression pattern that the GitLab commit tag must match.
     * @since 3.3.0
     */
    fun ifTagPattern(pattern: String) { ifCondition = $$"$CI_COMMIT_TAG =~ /$$pattern/" }
    /**
     * Adds a condition that targets the default branch within a CI/CD pipeline rule.
     *
     * When invoked, this function sets the `ifCondition` field to a string expression
     * that evaluates whether the current branch (`CI_COMMIT_BRANCH`) is the default branch (`CI_DEFAULT_BRANCH`).
     * This enables the rule to apply only when the pipeline is executed on the default branch.
     * @since 3.3.0
     */
    fun ifDefaultBranch() { ifCondition = $$"$CI_COMMIT_BRANCH == $CI_DEFAULT_BRANCH" }
    /**
     * Sets the `ifCondition` property to determine whether the current rule should apply
     * when the pipeline is triggered by a scheduled event.
     *
     * This function is typically used within the context of configuring CI/CD pipeline rules
     * to conditionally execute tasks or define rules that are specific to schedules.
     *
     * The internal condition evaluates whether the environment variable `CI_PIPELINE_SOURCE`
     * matches the value "schedule", which identifies a scheduled pipeline trigger in GitLab.
     * @since 3.3.0
     */
    fun ifSchedule() { ifCondition = $$"$CI_PIPELINE_SOURCE == \"schedule\"" }
    /**
     * Specifies that the rule should be applied only when the pipeline is triggered via the web interface.
     *
     * This method sets the `ifCondition` property to evaluate whether the `CI_PIPELINE_SOURCE`
     * environment variable equals "web". It is typically used in the context of defining rules
     * within a GitLab CI pipeline to restrict execution to pipelines triggered from the web interface.
     * @since 3.3.0
     */
    fun ifWeb() { ifCondition = $$"$CI_PIPELINE_SOURCE == \"web\"" }
    /**
     * Sets the condition to execute the associated rule or job only when the pipeline is triggered
     * by an API call. This is determined by checking whether the `CI_PIPELINE_SOURCE` environment
     * variable is equal to `"api"`.
     *
     * This function is typically used in defining rules for GitLab CI/CD pipelines,
     * allowing customization of pipeline behavior based on the source of the trigger.
     * @since 3.3.0
     */
    fun ifApi() { ifCondition = $$"$CI_PIPELINE_SOURCE == \"api\"" }
    /**
     * Sets the rule condition to execute only when the GitLab CI pipeline source is of type "push".
     *
     * This method updates the `ifCondition` property to evaluate whether the pipeline execution
     * is triggered by a push event. This can be used to enforce rules or conditions that are specific
     * to pipelines initiated by push actions.
     * @since 3.3.0
     */
    fun ifPush() { ifCondition = $$"$CI_PIPELINE_SOURCE == \"push\"" }

    /**
     * Configures the rule to use the `MANUAL` execution policy.
     *
     * The `MANUAL` policy ensures that the associated job or rule
     * will only be executed when explicitly triggered by a user.
     *
     * Use this method when defining rules or workflows that require
     * manual intervention to proceed.
     * @since 3.3.0
     */
    fun whenManual() { whenPolicy = WhenPolicy.MANUAL }
    /**
     * Specifies that the `whenPolicy` for the current rule is set to `WhenPolicy.ALWAYS`.
     *
     * This method is typically used in CI/CD rule configurations to ensure that the
     * associated rule or job executes unconditionally, regardless of the execution
     * status of previous steps or conditions.
     *
     * Use this in scenarios where guaranteed execution is required, such as finalization
     * tasks, cleanup processes, or other operations that must occur in any case.
     * @since 3.3.0
     */
    fun whenAlways() { whenPolicy = WhenPolicy.ALWAYS }
    /**
     * Sets the execution policy of a rule to `NEVER`, indicating that the associated
     * action or job should never be executed.
     *
     * This method is commonly used within a DSL for defining CI/CD pipeline rules,
     * where specifying `whenPolicy` as `NEVER` explicitly excludes the rule
     * from being triggered under any circumstances.
     * @since 3.3.0
     */
    fun whenNever() { whenPolicy = WhenPolicy.NEVER }
    /**
     * Sets the execution policy of a rule to `DELAYED`.
     *
     * This method configures the rule to delay its execution until specific conditions are satisfied
     * or a defined period has elapsed. It is often used in CI/CD pipeline configurations where a job's
     * execution needs to be deferred intentionally.
     *
     * Modifies the `whenPolicy` property to `WhenPolicy.DELAYED` to reflect the delayed execution state.
     * @since 3.3.0
     */
    fun whenDelayed() { whenPolicy = WhenPolicy.DELAYED }
    /**
     * Sets the execution policy to `ON_SUCCESS`.
     *
     * The `ON_SUCCESS` policy ensures that the associated rule or action
     * is executed only if the preceding step completes successfully. This is
     * commonly used in defining workflows or CI/CD pipelines, where subsequent stages
     * depend on the successful completion of prior stages.
     *
     * This method updates the internal `whenPolicy` property to reflect this behavior.
     * @since 3.3.0
     */
    fun whenOnSuccess() { whenPolicy = WhenPolicy.ON_SUCCESS }

    /**
     * Adds a list of paths to the `changes` property, which represents the file or directory
     * changes that will trigger the associated pipeline rule or job.
     *
     * @param paths A variable number of file or directory paths to be monitored for changes.
     * Each specified path can be used to define conditional execution within the pipeline.
     * @since 3.3.0
     */
    fun changes(vararg paths: String) { changes = paths.toList() }
    /**
     * Specifies the paths that must exist for the associated rule to be valid.
     *
     * This method allows defining a list of file or directory paths that are
     * required to exist for the rule to be matched or applied. It is commonly
     * used in CI/CD pipeline configurations to enforce dependency checks.
     *
     * @param paths The vararg of file or directory paths that must exist.
     * @since 3.3.0
     */
    fun exists(vararg paths: String) { exists = paths.toList() }
    /**
     * Updates or adds a variable with the specified key and value to the variables map.
     *
     * @param key The key used to identify the variable.
     * @param value The value to be associated with the specified key.
     * @since 3.3.0
     */
    fun variable(key: String, value: String) { variables[key] = value }

    /**
     * Constructs and returns a new `JobRule` instance.
     *
     * This method initializes the `JobRule` object with the provided parameters
     * including the conditional logic, changes, state existence, execution policy,
     * failure handling behavior, and a map of additional variables.
     *
     * @return A newly created `JobRule` instance based on the configured parameters.
     * @since 3.3.0
     */
    fun build() = JobRule(ifCondition, changes, exists, whenPolicy, allowFailure, variables.toMap())
}

/**
 * Provides a DSL scope for defining rules associated with CI/CD job workflows in a GitLab pipeline.
 *
 * The `RulesScope` class facilitates the creation and management of rules using a simple and
 * expressive DSL. It allows developers to define conditional logic and policies that dictate
 * job execution within a pipeline configuration.
 *
 * This class aggregates a collection of rules and provides utility methods for creating common
 * rule types, such as those based on merge request conditions, branch names, tags, and execution
 * policies. By leveraging the DSL, users can write cleaner and more maintainable pipeline configurations.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@GitlabCiDslMarker
class RulesScope {
    /**
     * Represents a mutable list of job-specific rules.
     *
     * This variable is initialized with an empty mutable list of `JobRule`.
     * It is intended to be used for storing and managing various rules
     * associated with job-related operations or configurations.
     * @since 3.3.0
     */
    val rules = emptyMList<JobRule>()

    /**
     * Adds a rule to the `rules` collection within the `RulesScope`.
     *
     * This method allows defining a custom rule by using a `RuleBuilder` block to configure
     * specific conditions or policies for the associated job or workflow.
     *
     * @param block A lambda with receiver that provides a `RuleBuilder` instance for configuring the rule.
     * @since 3.3.0
     */
    fun rule(block: ReceiverConsumer<RuleBuilder>) {
        rules += RuleBuilder().apply(block).build()
    }

    /**
     * Adds a rule that is applied when a merge request is present.
     *
     * @param whenPolicy An optional policy that specifies the condition or timing
     *                   for the rule to be applied. If provided, it will be set
     *                   as the `whenPolicy` for the rule.
     * @since 3.3.0
     */
    // Convenience shortcuts that add a single rule
    fun ifMergeRequest(whenPolicy: WhenPolicy? = null) = rule {
        ifMergeRequest()
        whenPolicy?.let { this.whenPolicy = it }
    }

    /**
     * Adds a job rule that applies when the specified branch name matches the current branch.
     *
     * @param name The name of the branch for which the rule should apply.
     * @param whenPolicy An optional execution policy specifying when the job should be executed. If not provided, no specific policy is applied.
     * @since 3.3.0
     */
    fun ifBranch(name: String, whenPolicy: WhenPolicy? = null) = rule {
        ifBranch(name)
        whenPolicy?.let { this.whenPolicy = it }
    }

    /**
     * Adds a rule to the pipeline configuration that applies when the current CI/CD execution is triggered by a tag.
     * This rule targets scenarios where specific actions or jobs should execute only for tagged commits.
     *
     * @param whenPolicy An optional execution policy that determines when the rule is applied.
     *            If provided, it specifies the conditions or timing for executing the associated job or workflow.
     * @since 3.3.0
     */
    fun ifTag(whenPolicy: WhenPolicy? = null) = rule {
        ifTag()
        whenPolicy?.let { this.whenPolicy = it }
    }

    /**
     * Adds a rule to the pipeline that applies only when the current branch is the default branch.
     * An optional execution policy can be specified to define the behavior when the rule is triggered.
     *
     * @param whenPolicy The execution policy that dictates when the rule should execute.
     *                   If no policy is provided, the default behavior will be applied.
     * @since 3.3.0
     */
    fun ifDefaultBranch(whenPolicy: WhenPolicy? = null) = rule {
        ifDefaultBranch()
        whenPolicy?.let { this.whenPolicy = it }
    }

    /**
     * Adds a rule to the `rules` collection that is configured to use the `MANUAL` execution policy.
     *
     * This method specifies that the associated job or workflow will only be executed
     * when explicitly triggered by user action. It serves as a convenient abstraction
     * for defining manual execution workflows within the pipeline configuration.
     *
     * Utilize this method to define steps that require manual intervention to start.
     * @since 3.3.0
     */
    fun whenManual() = rule { whenManual() }
    /**
     * Adds a rule specifying that the job should never be executed.
     *
     * This method applies a rule to the parent `RulesScope` with the `when` policy set to `WhenPolicy.NEVER`.
     * It indicates that the job defined by this rule will never run, regardless of conditions or context.
     *
     * Use this function to explicitly exclude certain jobs from being executed in the pipeline.
     * @since 3.3.0
     */
    fun whenNever() = rule { whenNever() }
    /**
     * Adds a rule to the `rules` collection within the `RulesScope` that is always executed,
     * regardless of specific conditions or circumstances. The associated execution policy
     * for this rule is set to `ALWAYS`.
     *
     * This method simplifies the creation of a job rule that should apply unconditionally,
     * ensuring it executes in all scenarios. It is a convenient wrapper around the internal
     * `rule` method, pre-configured with the `whenAlways` behavior.
     * @since 3.3.0
     */
    fun whenAlways() = rule { whenAlways() }
}

/**
 * Represents a builder class for configuring and creating `ArtifactsConfig` objects.
 * This builder is used to define artifact settings in CI/CD pipelines.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@GitlabCiDslMarker
class ArtifactsBuilder {
    /**
     * Specifies a list of file or directory paths to be included in the artifacts.
     *
     * This property defines the paths that should be collected as artifacts when the
     * job is executed. The paths can point to both files and directories. Use the
     * `paths(vararg p: String)` function when adding multiple paths at once. The
     * list is mutable and can be dynamically updated.
     * @since 3.3.0
     */
    val paths: MList<String> = emptyMList()
    /**
     * Stores a mutable map of report types to their associated file paths.
     *
     * This property is used to define and manage different types of reports that
     * are associated with artifacts in a CI/CD pipeline. Each entry in the map
     * represents a specific type of report (key) and its corresponding file path (value).
     *
     * The reports can be configured using helper functions like `report`, `junitReport`,
     * and `coberturaReport`. These functions allow adding specific report types to the map.
     *
     * The resulting map is included in the configuration generated by the `build` function.
     * @since 3.3.0
     */
    val reports: StringMMap = emptyMMap()
    /**
     * Specifies the expiration duration for the artifacts.
     * When set, the artifacts will be automatically removed after the given duration.
     * A value of `null` indicates that the artifacts will not expire.
     * @since 3.3.0
     */
    var expireIn: Duration? = null
    /**
     * Dictates the conditions under which artifacts are handled during a CI/CD pipeline execution.
     *
     * The `whenPolicy` variable determines the execution policy for managing artifacts
     * based on the pipeline job's status or other specified conditions.
     *
     * Possible values are defined in the `WhenPolicy` enum, including:
     * - `ON_SUCCESS` for actions triggered when the preceding job succeeds.
     * - `ON_FAILURE` for actions executed only if the job fails.
     * - `ALWAYS` for unconditional execution.
     *
     * Used within the `ArtifactsBuilder` class, this property is modified using
     * specific helper functions such as `whenAlways()`, `whenOnSuccess()`, or `whenOnFailure()`.
     *
     * If not explicitly assigned, it defaults to `null`, indicating no specific execution policy.
     * @since 3.3.0
     */
    var whenPolicy: WhenPolicy? = null
    /**
     * Represents the name of an entity or object.
     * This variable can hold a nullable string value, which may be used
     * to store or reference the name associated with this entity.
     * @since 3.3.0
     */
    var name: String? = null
    /**
     * Indicates whether all files in the project directory should be included by default as artifacts.
     *
     * - If set to `true`, all untracked files will be included as artifacts in the associated job.
     * - If set to `false`, only the files explicitly defined in the `paths` property or added through other configurations will be included.
     *
     * This property is commonly used in CI/CD pipelines to determine the inclusion of untracked files
     * when generating artifacts during a job's execution.
     * @since 3.3.0
     */
    var untracked: Boolean = false
    /**
     * Represents a list of file paths or patterns to exclude from the artifact collection process.
     *
     * This property is used within the context of defining CI/CD artifacts.
     * It contains file paths or glob patterns specifying files or directories
     * that should not be included in the artifact when paths are defined.
     *
     * By default, the list is empty, indicating that no exclusions are applied.
     * @since 3.3.0
     */
    val exclude: MList<String> = emptyMList()

    /**
     * Adds one or more paths to the artifacts collection.
     *
     * This method appends the specified paths to the list of paths
     * that will be included as artifacts when the build process is executed.
     *
     * @param p One or more paths to be added to the artifacts collection.
     * @since 3.3.0
     */
    fun paths(vararg p: String) { paths += p }
    /**
     * Adds a report of a specified type with the associated path to the reports collection.
     *
     * @param type The type of the report to add (e.g., "junit", "coverage_report").
     * @param path The file path associated with the report type.
     * @since 3.3.0
     */
    fun report(type: String, path: String) { reports[type] = path }
    /**
     * Adds a JUnit report to the artifacts configuration.
     *
     * This method stores the specified file path as a JUnit report in the artifact's
     * reports collection. JUnit reports are commonly used for test result tracking and
     * analysis in continuous integration pipelines.
     *
     * @param path The file path where the JUnit report is located.
     * @since 3.3.0
     */
    fun junitReport(path: String) { report("junit", path) }
    /**
     * Adds a Cobertura coverage report to the artifacts configuration.
     *
     * This method stores the specified file path as a Cobertura report in the artifact's
     * reports collection. Cobertura reports are used for analyzing test code coverage
     * in software projects, commonly utilized in continuous integration pipelines for
     * monitoring and improving code quality.
     *
     * @param path The file path where the Cobertura report is located.
     * @since 3.3.0
     */
    fun coberturaReport(path: String) { report("coverage_report", path) }
    /**
     * Adds the provided paths to the `exclude` list. The paths specified will be excluded
     * from the artifacts configuration.
     *
     * @param p A variable number of strings representing the paths to exclude.
     * @since 3.3.0
     */
    fun exclude(vararg p: String) { exclude += p }
    /**
     * Sets the `whenPolicy` property to `WhenPolicy.ALWAYS`.
     *
     * This ensures that the specified behavior or action will always execute,
     * regardless of any prior conditions or outcomes in the pipeline.
     *
     * Typically used in pipeline artifact configurations to guarantee that
     * the associated steps are executed unconditionally in all scenarios.
     * @since 3.3.0
     */
    fun whenAlways() { whenPolicy = WhenPolicy.ALWAYS }
    /**
     * Sets the `whenPolicy` property to `WhenPolicy.ON_SUCCESS`.
     *
     * Use this method to specify that a particular action or behavior should
     * be triggered only when the preceding job or step in the CI pipeline succeeds.
     * This policy ensures that subsequent steps are executed under successful conditions,
     * such as when all prior tasks complete without errors.
     * @since 3.3.0
     */
    fun whenOnSuccess() { whenPolicy = WhenPolicy.ON_SUCCESS }
    /**
     * Specifies that the `whenPolicy` for the current artifacts configuration
     * should be set to `ON_FAILURE`.
     *
     * The `ON_FAILURE` policy ensures that the associated artifacts will only
     * be processed when the job execution results in a failure.
     *
     * This function is typically used in contexts where failure-specific artifacts,
     * such as logs or debugging information, need to be captured or handled
     * for analysis or recovery purposes.
     *
     * It modifies the `whenPolicy` property in the `ArtifactsBuilder` to
     * control the behavior of the artifacts according to this policy.
     * @since 3.3.0
     */
    fun whenOnFailure() { whenPolicy = WhenPolicy.ON_FAILURE }

    /**
     * Constructs and returns an instance of `ArtifactsConfig` using the provided configuration data.
     *
     * The method initializes the `ArtifactsConfig` with the following parameters:
     * - A list created from the `paths` collection.
     * - A map created from the `reports` collection.
     * - The `expireIn` value, which defines the expiration period.
     * - The `whenPolicy` value, which specifies the triggering policy.
     * - The `name` for the artifact configuration.
     * - The `untracked` flag indicating whether untracked files are included.
     * - A list created from the `exclude` collection, defining items to exclude.
     * @since 3.3.0
     */
    fun build() = ArtifactsConfig(
        paths.toList(), reports.toMap(), expireIn, whenPolicy, name, untracked, exclude.toList()
    )
}

/**
 * A builder class for configuring cache-related settings in a GitLab CI/CD pipeline.
 *
 * This class provides a DSL-style interface for defining cache configurations
 * such as cache keys, paths, policies, untracked files, and fallback keys.
 *
 * The resulting configuration can be built into a `CacheConfig` object,
 * which is used to define caching behavior in pipeline jobs.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@GitlabCiDslMarker
class CacheBuilder {
    /**
     * Represents a configurable key that can hold a nullable string value.
     * This key may be used for identification, mapping, or configuration purposes
     * within the application.
     * @since 3.3.0
     */
    var key: String? = null
    /**
     * Specifies a mutable list of files that will be used to generate a cache key.
     *
     * This property allows you to define multiple file paths whose contents are used
     * to compute the cache key, ensuring that cache invalidation occurs whenever any
     * of the specified files are modified.
     *
     * The `keyFiles` list directly affects cache behavior by including the file contents
     * in the cache key generation process, making it highly useful for caching workflows
     * that are sensitive to file-based changes.
     * @since 3.3.0
     */
    val keyFiles: MList<String> = emptyMList()
    /**
     * Represents a collection of file or directory paths that should be considered
     * when configuring caching in a GitLab CI/CD pipeline.
     *
     * This property is used to specify the paths to be cached during pipeline execution. Paths can
     * include files or directories that should be stored and restored as part of the caching process.
     *
     * For example, this can be used to cache dependency directories like `node_modules` or build
     * outputs to improve pipeline performance by avoiding redundant computations.
     *
     * This property is mutable and initialized as an empty mutable list. It can be configured using
     * the `paths` function in the `CacheBuilder` class by adding one or more paths dynamically.
     *
     * @since 3.3.0
     */
    val paths: MList<String> = emptyMList()
    /**
     * Defines the caching policy for managing how cache artifacts are pulled and pushed in a CI/CD pipeline.
     *
     * This property can be assigned one of the values from the `CachePolicy` enum to specify the behavior:
     * - `PULL`: Only retrieve existing cached artifacts.
     * - `PUSH`: Only update or create artifacts in the cache.
     * - `PULL_PUSH`: Both retrieve and update cached artifacts.
     *
     * By default, the value is `null`, meaning no specific caching policy is applied.
     * @since 3.3.0
     */
    var policy: CachePolicy? = null
    /**
     * Indicates whether the cache should exclude untracked files.
     *
     * If set to `true`, the cache will not include untracked files when storing or retrieving cache data.
     * This is typically used to optimize cache performance by disregarding files that are not managed
     * by version control.
     *
     * Default value: `false`.
     * @since 3.3.0
     */
    var untracked: Boolean = false
    /**
     * Indicates whether cache protection is disabled, allowing modifications to cached artifacts.
     *
     * When set to `true`, the caching mechanism permits overwriting or manual changes to the cache.
     * Conversely, when set to `false`, the cache remains protected from unintended changes,
     * ensuring greater data integrity and security.
     * @since 3.3.0
     */
    var unprotect: Boolean = false
    /**
     * Specifies the policy for defining when an operation or action should be triggered in a CI/CD pipeline.
     *
     * The `whenPolicy` variable allows configuring conditions for cache-related behavior,
     * such as determining whether actions like caching paths or keys should be executed
     * depending on the outcome of a pipeline stage or job.
     *
     * It accepts a value of the `WhenPolicy` enum, which provides predefined options like:
     * - `ON_SUCCESS` for actions triggered upon successful completion of a job.
     * - `ON_FAILURE` for actions taken if the job execution fails.
     * - `ALWAYS` for unconditional execution of actions regardless of the pipeline result.
     * - Other scenarios such as `MANUAL`, `DELAYED`, or `NEVER`.
     *
     * This configuration is optional, and when left unset, the default behavior depends on the overarching
     * settings in the build pipeline context.
     * @since 3.3.0
     */
    var whenPolicy: WhenPolicy? = null
    /**
     * Represents a collection of fallback keys used in cache configuration.
     *
     * Fallback keys are alternative identifiers that provide additional options for cache lookups
     * when the primary cache key does not match any existing cached artifacts. This ensures greater
     * flexibility and robustness in caching mechanisms, particularly in scenarios where the cache
     * may not contain an exact match for the primary key.
     *
     * This property is initialized to an empty mutable list and can be modified by invoking specific
     * methods that add new fallback keys to the collection.
     * @since 3.3.0
     */
    val fallbackKeys: MList<String> = emptyMList()

    /**
     * Adds the specified file paths to the list of key files used for caching.
     *
     * Key files are used as part of the cache key definition, allowing the cache
     * to uniquely identify the stored artifacts based on the provided file paths.
     *
     * @param files The file paths to be included as key files for caching.
     * @since 3.3.0
     */
    fun keyFiles(vararg files: String) { keyFiles += files }
    /**
     * Adds the specified paths to the list of cache paths.
     *
     * This method appends the provided paths to the `paths` property,
     * which represents the set of file paths or directories to be cached
     * or used in a CI/CD pipeline configuration.
     *
     * @param p The file paths or directories to be added to the cache paths.
     * @since 3.3.0
     */
    fun paths(vararg p: String) { paths += p }
    /**
     * Sets the cache policy to `PULL`, indicating that the caching behavior should only retrieve
     * artifacts from a remote cache without pushing any changes back.
     *
     * Use this method when you need to configure the caching mechanism to strictly rely on
     * the existing cache state, without modifying or creating new cache entries.
     * @since 3.3.0
     */
    fun pullOnly() { policy = CachePolicy.PULL }
    /**
     * Configures the cache policy to `PUSH`, indicating that only artifact uploads
     * to the caching system are allowed, without retrieving any pre-existing cached
     * artifacts.
     *
     * This method is typically used when the caching mechanism needs to store artifacts
     * from the current build process for subsequent reuse, without relying on previously
     * cached artifacts.
     *
     * By setting this policy, the cache behavior becomes write-only, ensuring that existing
     * cached data is not accessed during the operation.
     * @since 3.3.0
     */
    fun pushOnly() { policy = CachePolicy.PUSH }
    /**
     * Adds the specified fallback keys to the cache configuration.
     *
     * Fallback keys are used when the primary cache key fails to match any existing cache.
     * These keys define alternative cache lookup options, allowing for greater flexibility
     * and resilience in cache management.
     *
     * @param keys The fallback keys to add to the cache configuration.
     * @since 3.3.0
     */
    fun fallbackKeys(vararg keys: String) { fallbackKeys += keys }

    /**
     * Builds and returns a CacheConfig object using the provided configuration parameters.
     *
     * This method initializes a CacheConfig instance by aggregating various settings and
     * converting collections to immutable lists for processing. The generated CacheConfig
     * object contains all the necessary data to operate based on the provided properties.
     *
     * @return A fully constructed CacheConfig instance.
     * @since 3.3.0
     */
    fun build() = CacheConfig(
        key, keyFiles.toList(), paths.toList(), policy, untracked, unprotect, whenPolicy, fallbackKeys.toList()
    )
}

/**
 * A DSL builder for defining and configuring a service to be used within a CI/CD pipeline.
 *
 * The `ServiceBuilder` class allows for specifying service-related configuration details,
 * such as the service name, alias, commands to run, and environment variables.
 * Once fully configured, the service information can be built into a `ServiceConfig` instance.
 *
 * @constructor Initializes a `ServiceBuilder` with the specified service name.
 * @param name The name of the service being configured.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@GitlabCiDslMarker
class ServiceBuilder(private val name: String) {
    /**
     * An optional alias for the service being defined.
     *
     * This property can be used to assign a custom name to the service,
     * which may be referenced elsewhere in the pipeline configuration.
     * If not specified, the service will use its default name.
     * @since 3.3.0
     */
    var alias: String? = null
    /**
     * A mutable list of commands associated with the service configuration.
     *
     * This property stores a collection of command strings that can be executed
     * as part of the service's lifecycle in a CI/CD pipeline. The list is initialized
     * as empty and can be modified using the `command(vararg cmds: String)` function.
     *
     * The list's final state is used when building the service via the `build()` function.
     * @since 3.3.0
     */
    val command: MList<String> = emptyMList()
    /**
     * A mutable map that holds key-value pairs representing environment variables for the service.
     *
     * This map is used to configure the environment variables associated with a service
     * within a CI/CD pipeline. Keys represent the variable names, and values represent the
     * corresponding variable values.
     *
     * Users can add variables to this map using the `variable` function, which allows dynamic
     * configuration of service-specific environment settings.
     *
     * Initially, the map is empty and can be populated as required during the service configuration process.
     * @since 3.3.0
     */
    val variables: StringMMap = emptyMMap()

    /**
     * Executes a command by appending the provided string arguments to the existing list of commands.
     *
     * @param cmds A variable number of string arguments representing the commands to be added.
     * @since 3.3.0
     */
    fun command(vararg cmds: String) { command += cmds }
    /**
     * Adds or updates a key-value pair in the service's environment variables.
     *
     * @param key The key of the environment variable to add or update.
     * @param value The value of the environment variable to associate with the specified key.
     * @since 3.3.0
     */
    fun variable(key: String, value: String) { variables[key] = value }

    /**
     * Builds and returns a ServiceConfig instance with the provided configuration.
     *
     * @return A ServiceConfig object initialized with the name, alias, a list of commands,
     *         and a map of variables.
     * @since 3.3.0
     */
    fun build() = ServiceConfig(name, alias, command.toList(), variables.toMap())
}

/**
 * Builder class for constructing environment configurations in a DSL-style manner.
 *
 * This class is used to define and configure the properties of an environment,
 * such as its name, associated URL, actions, auto-stop behavior, and Kubernetes namespace.
 *
 * By utilizing the provided methods, users can incrementally set up an environment's parameters
 * and finally invoke the `build` method to generate an immutable `EnvironmentConfig` instance.
 *
 * @constructor Creates a new instance of `EnvironmentBuilder` with the specified environment name.
 * @param name The name of the environment to be configured.
 * @since 3.3.0
 * @author Tomaso Pastorelli
 */
@GitlabCiDslMarker
class EnvironmentBuilder(private val name: String) {
    /**
     * Represents the URL associated with a specific resource or endpoint.
     *
     * This variable holds an instance of the `Url` class, which is used
     * to define a web address or network location. It can be null if
     * the URL is not yet assigned or available.
     * @since 3.3.0
     */
    var url: Url? = null
    /**
     * Represents an action that can be performed within a specific environment.
     *
     * This variable holds an optional reference to an `EnvironmentAction`, which defines
     * the behavior or operation associated with the environment. It can be set to `null`
     * if no action is currently assigned or required.
     * @since 3.3.0
     */
    var action: EnvironmentAction? = null
    /**
     * Specifies the optional duration after which the environment should automatically stop.
     *
     * This variable can be used to configure an auto-stop mechanism for the environment,
     * allowing it to terminate gracefully after the specified amount of time.
     *
     * A value of `null` indicates that no auto-stop duration has been set.
     * @since 3.3.0
     */
    var autoStopIn: Duration? = null
    /**
     * Specifies the job or action to be executed when the environment stops.
     *
     * This property allows for configuration of cleanup or finalization tasks
     * that should be triggered during the shutdown process of the environment.
     * It can be null if no specific stop action is defined.
     * @since 3.3.0
     */
    var onStop: String? = null
    /**
     * Defines the Kubernetes namespace to be utilized for the environment configuration.
     *
     * This property specifies the namespace scope for Kubernetes resources.
     * If set, it ensures that the resources managed in the environment are limited
     * to the specified namespace. A null value indicates that no specific namespace
     * is defined, and the default or global context may be used.
     * @since 3.3.0
     */
    var namespace: String? = null

    /**
     * Constructs and returns an instance of `EnvironmentConfig` with the provided configuration details.
     *
     * @return An `EnvironmentConfig` object initialized with the specified parameters:
     * - `name`: The name of the environment.
     * - `url`: The URL of the environment as a string, derived from the optional `url` parameter.
     * - `action`: The action to be performed in the environment.
     * - `autoStopIn`: The duration after which auto-stop should occur.
     * - `onStop`: A callback or action to be executed when stopping.
     * - `namespace`: If specified, it is used to initialize an instance of `KubernetesConfig`.
     */
    fun build() = EnvironmentConfig(
        name, url?.toString(), action, autoStopIn, onStop,
        namespace?.let { KubernetesConfig(it) }
    )
}

/**
 * Represents a builder class for configuring the dependencies or "needs" of a CI/CD job in a GitLab pipeline.
 * Allows for the specification of artifacts, optionality, linked pipeline, project, and ref settings
 * associated with the dependency.
 * @author Tommaso Pastorelli
 * @since 3.3.0
 */
@GitlabCiDslMarker
class NeedBuilder(private val job: String) {
    /**
     * Determines whether to download artifacts from the dependent job specified in the pipeline configuration.
     *
     * When set to `true`, the artifacts of the dependency job will be downloaded and made available
     * for use in the current job. Setting it to `false` skips downloading the artifacts, which
     * can save resources if they are not needed.
     * @since 3.3.0
     */
    var artifacts: Boolean = true
    /**
     * Specifies whether the dependent job is optional in the context of a CI/CD pipeline configuration.
     *
     * When `true`, the current job will not fail if the dependent job is unavailable or has not been executed.
     * When `false`, the current job requires the dependent job to exist and succeed.
     * @since 3.3.0
     */
    var optional: Boolean = false
    /**
     * Specifies the ID or name of a pipeline from which the dependency job originates, if applicable.
     *
     * This property is used to define cross-pipeline dependencies within a GitLab CI/CD configuration.
     * When set, it allows the current job to depend on a job in another pipeline.
     *
     * The value can be null, indicating no specific pipeline is linked.
     * @since 3.3.0
     */
    var pipeline: String? = null
    /**
     * Specifies the name or ID of an external project where the dependency job is located.
     *
     * This property is optional and is used when the dependency job resides in a different
     * GitLab project. When set, it indicates the target project to be referenced for retrieving
     * the dependency job. If null, the dependency is assumed to belong to the current project.
     * @since 3.3.0
     */
    var project: String? = null
    /**
     * Specifies the branch or tag in the associated project to be used for the dependency job.
     *
     * This property is utilized when defining a dependency on a job from a specific branch or tag
     * within the same or another project. If not set, the default branch or tag of the project will be used.
     *
     * Can be `null` to indicate no specific branch or tag is required.
     * @since 3.3.0
     */
    var ref: String? = null

    /**
     * Sets the project and optional reference branch or tag for the dependency.
     *
     * @param p The name or ID of the project where the dependency job is located.
     * @param ref The branch or tag in the specified project to use for the dependency job. This parameter is optional.
     * @since 3.3.0
     */
    fun project(p: String, ref: String? = null) { project = p; this.ref = ref }

    /**
     * Constructs and returns an instance of `NeedConfig` with the specified parameters.
     *
     * The method uses predefined properties including `job`, `artifacts`, `optional`,
     * `pipeline`, `project`, and `ref` to create the `NeedConfig` instance.
     *
     * @return an instance of `NeedConfig` built using the provided properties.
     * @since 3.3.0
     */
    fun build() = NeedConfig(job, artifacts, optional, pipeline, project, ref)
}

// --- JOB BUILDER ---

/**
 * A builder class for defining and constructing GitLab CI/CD job configurations.
 *
 * This class provides a DSL for specifying the properties and behaviors of a CI/CD job,
 * including its stage, scripts, artifacts, caching, and various other configurations.
 * The resulting job configuration can be built into an instance of `Job`.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@GitlabCiDslMarker
class JobBuilder(val name: String) {
    /**
     * Specifies the stage where this job will run in the GitLab CI/CD pipeline.
     *
     * This property can be set to define the stage that this job belongs to,
     * determining the order of execution relative to other jobs in the pipeline.
     * If unspecified, the job will not be associated with any particular stage.
     *
     * The value should match one of the stages defined in the pipeline configuration.
     * It is commonly used to group jobs into logical phases such as "build", "test", or "deploy".
     * @since 3.3.0
     */
    var stage: String? = null
    /**
     * Specifies the Docker image to be used for the job execution.
     *
     * This variable defines the container image in which the job's scripts and commands
     * will be executed. If no image is specified, the default environment will be used.
     * Typically set to the name of a Docker-compatible image, which can include a specific
     * tag or version (e.g., `node:14`, `python:3.9`, `alpine:latest`).
     *
     * A null value indicates that no specific image has been configured.
     * @since 3.3.0
     */
    var image: String? = null
    /**
     * Represents a mutable list of strings which is initialized as empty.
     * This variable can be used to store and manipulate a collection of strings dynamically.
     * @since 3.3.0
     */
    val script: MList<String> = emptyMList()
    /**
     * Holds a list of commands to be executed as part of the `before_script` section of a job.
     *
     * This mutable list allows the definition of pre-execution steps or setup commands
     * that need to run prior to the main script execution in the CI/CD pipeline.
     * It is populated using the `beforeScript` function.
     * @since 3.3.0
     */
    val beforeScript: MList<String> = emptyMList()
    /**
     * Defines the list of commands to be executed after the main script completes.
     *
     * This property holds a mutable list of strings representing commands
     * that are run post the primary execution flow defined in the `script` section.
     * These commands are typically used for cleanup, logging, or other finalization tasks.
     *
     * The list is initially empty, and commands can be appended to it using the
     * `afterScript(vararg cmds: String)` function.
     * @since 3.3.0
     */
    val afterScript: MList<String> = emptyMList()
    /**
     * A map-like data structure where the keys are strings, and the values are sets of strings.
     * This variable is initialized as an empty map and can be populated as needed.
     * @since 3.3.0
     */
    val variables: StringMMap = emptyMMap()
    /**
     * Represents an optional instance of `OnlyExcept`, which can either hold a value of type `OnlyExcept`
     * or be null. Typically used in scenarios where the presence of a value is optional or conditional.
     * @since 3.3.0
     */
    var only: OnlyExcept? = null
    /**
     * Represents an instance of `RulesScope` within the `JobBuilder` class.
     *
     * The `rulesScope` variable serves as the primary entry point for defining and managing job rules
     * in the context of a CI/CD pipeline configuration. It provides a dedicated DSL scope (`RulesScope`)
     * that enables developers to set up conditional logic and execution policies for jobs in an expressive
     * and structured manner.
     *
     * By leveraging `rulesScope`, users can define rules such as branch-specific execution, tag-specific actions,
     * merge request conditions, and other pipeline behaviors. This allows for more flexible and maintainable
     * pipeline configurations.
     *
     * The `rulesScope` variable is invoked via the `rules` function within the `JobBuilder` class,
     * providing a streamlined way to configure job rules using a declarative approach.
     * @since 3.3.0
     */
    private val rulesScope = RulesScope()
    /**
     * Holds a list of tags associated with the job.
     *
     * Tags are used to identify specific runners that can execute the job.
     * By associating tags with a job, you ensure it is picked up by runners
     * equipped to handle jobs requiring the specified tags.
     * @since 3.3.0
     */
    val tags: MList<String> = emptyMList()
    /**
     * A mutable list of service configurations used in the CI/CD pipeline.
     *
     * This property holds the collection of `ServiceConfig` objects that define
     * the services required for the job. Each service includes its name, alias,
     * commands to execute, and any environment variables it needs.
     *
     * Services can be added to this list using the `service` function, which allows
     * defining additional configurations dynamically.
     * @since 3.3.0
     */
    private val services = emptyMList<ServiceConfig>()
    /**
     * Represents the configuration for artifacts associated with a job.
     *
     * This property holds an `ArtifactsConfig` object that defines the settings
     * for managing artifacts generated or used during the job execution.
     * It includes details such as paths, reports, expiration policy, and other
     * options related to artifact management.
     * @since 3.3.0
     */
    private var artifacts: ArtifactsConfig? = null
    /**
     * Represents the caching configuration for a job in a CI/CD pipeline.
     *
     * This variable holds the caching settings that determine how artifacts and files
     * are cached during the execution of the pipeline. The cache can be configured
     * using the `cache` method, and the final configuration is applied when the job is built.
     *
     * When set to null, caching is disabled for the job.
     * @since 3.3.0
     */
    private var cache: CacheConfig? = null
    /**
     * Represents a list of dependencies required by a specific component, module, or process.
     * This list is initialized as empty and can be populated as needed.
     *
     * The dependencies are stored as a mutable collection of strings, where each string
     * typically describes the name or identifier of a single dependency.
     * @since 3.3.0
     */
    val dependencies: MList<String> = emptyMList()
    /**
     * Holds a collection of dependency configurations that define the jobs
     * required for the current job to execute. Each dependency is represented
     * as a `NeedConfig` object.
     *
     * This list is populated using the `needs` or `need` functions, which allow
     * specifying dependent jobs and their configurations within the job definition.
     * @since 3.3.0
     */
    private val needs = emptyMList<NeedConfig>()
    /**
     * Represents the configuration settings for the environment in the context of the job being built.
     *
     * This variable holds an instance of [EnvironmentConfig], which can define settings such as the
     * name, URL, actions, and stop conditions for an environment. It can be initialized through the
     * `environment` function, which applies a configuration block provided by the user.
     *
     * If `null`, the job will not have any associated environment configuration.
     * @since 3.3.0
     */
    private var environment: EnvironmentConfig? = null
    /**
     * Configuration for retry behavior within the job definition.
     *
     * This property determines whether retry attempts are enabled for a given
     * job, and provides the associated configuration details such as the maximum
     * number of retry attempts and the conditions under which retries should occur.
     *
     * The retry configuration helps ensure robustness in workflows by allowing
     * tasks to be retried automatically in case of transient failures.
     *
     * @since 3.3.0
     */
    private var retry: RetryConfig? = null
    /**
     * Specifies the maximum duration allowed for the execution of the job.
     * If the job exceeds this duration, it will be terminated.
     *
     * A null value indicates that no timeout is configured for this job.
     * @since 3.3.0
     */
    var timeout: Duration? = null
    /**
     * Represents a flag that determines whether an operation or process can be interrupted.
     *
     * A value of `true` indicates that the operation is interruptible. A value of `false` means
     * the operation is not interruptible. A `null` value signals that the interruptibility
     * state has not been explicitly defined.
     * @since 3.3.0
     */
    var interruptible: Boolean? = null
    /**
     * Determines whether the job is allowed to fail without impacting the overall pipeline status.
     *
     * If set to `true`, the failure of the job will be considered non-critical,
     * and it will not cause the pipeline to fail. When set to `false`, the job's failure
     * will result in the pipeline being marked as failed.
     *
     * This property is useful for optional or non-critical jobs that do not need
     * to influence the success of the entire pipeline.
     * @since 3.3.0
     */
    var allowFailure: Boolean = false
    /**
     * Represents the number of parallel jobs to execute for a given job configuration.
     *
     * This property can be used to define the level of concurrency for executing
     * certain tasks within the job. By default, it is null, which indicates no
     * parallelism is specified.
     *
     * Assigning a positive integer to this property enables the execution of the
     * specified number of parallel jobs.
     * @since 3.3.0
     */
    var parallel: Int? = null
    /**
     * Specifies the name of the resource group associated with the job.
     *
     * The `resourceGroup` defines the group of resources that the job will target during execution.
     * This value can be utilized to control concurrency and ensure that jobs with the same resource
     * group do not run simultaneously, offering proper coordination in shared resource environments.
     *
     * A null value indicates that no specific resource group is assigned to the job.
     * @since 3.3.0
     */
    var resourceGroup: String? = null
    /**
     * Represents the configuration for triggering downstream pipelines within the CI/CD job.
     *
     * This variable holds an optional instance of [TriggerConfig], which specifies the project,
     * branch, files to include, and triggering strategy for a downstream pipeline.
     * It is used to dynamically configure and link dependent job pipelines in the build process.
     *
     * The value of this variable may be explicitly set during the job definition using the
     * `trigger` function, or it may remain null if no downstream triggering is required.
     * @since 3.3.0
     */
    private var trigger: TriggerConfig? = null
    /**
     * Specifies the base job from which this job should inherit properties and configuration.
     *
     * This variable allows you to establish a hierarchical structure for CI/CD jobs by reusing
     * and extending the definitions of a parent job. When set, the job will inherit all attributes
     * from the specified base job unless explicitly overridden in the current configuration.
     *
     * A null value indicates that the job does not extend any other job and operates independently.
     * @since 3.3.0
     */
    var extends: String? = null
    /**
     * Holds the inheritance configuration for the job, determining whether certain
     * properties are inherited from the parent scope.
     *
     * This variable is used to configure inheritance behavior, allowing customization
     * of which settings (e.g., variables or default configurations) propagate from
     * the parent job definition.
     * @since 3.3.0
     */
    private var inherit: InheritConfig? = null
    /**
     * Specifies the code coverage configuration for the job.
     *
     * This property allows defining a regex pattern or tool-specific configuration
     * to extract and report test coverage information from the job output.
     * It integrates with tools or systems that parse coverage data to display it
     * on the pipeline or CI system.
     *
     * By default, this value is null, meaning that no coverage configuration
     * is applied to the job.
     * @since 3.3.0
     */
    var coverage: String? = null
    /**
     * Holds the configuration details for creating or updating a release within the job.
     *
     * This variable is used to store an instance of [ReleaseConfig], which contains details
     * such as the tag name, description, release name, and reference associated with a release.
     * It can be set through the `release` method and included in the final build output.
     * @since 3.3.0
     */
    private var releaseConfig: ReleaseConfig? = null
    /**
     * A mutable map that holds configurations for secrets used in the build process.
     *
     * This map associates a secret name with its corresponding `SecretVaultConfig`,
     * which specifies the vault and the optional field within the vault where the
     * secret is stored. The configurations are utilized to securely retrieve and
     * inject sensitive information into the job execution environment.
     *
     * The keys in the map represent the unique names of the secrets, while the values
     * are instances of `SecretVaultConfig` containing the details for accessing these
     * secrets.
     * @since 3.3.0
     */
    private val secretsConfig = emptyMMap<String, SecretVaultConfig>()
    /**
     * A mutable map that stores configurations for ID Tokens used in CI/CD jobs.
     *
     * The keys in the map represent unique names or identifiers for the ID Tokens,
     * while the values are instances of [IdTokenConfig], containing the configuration details
     * such as the audience (aud) claim for the respective token.
     *
     * This map is utilized primarily when defining or building CI/CD jobs that require
     * OpenID Connect (OIDC) integration, enabling secure access to resources such as APIs.
     * @since 3.3.0
     */
    private val idTokens = emptyMMap<String, IdTokenConfig>()

    /**
     * Adds the specified commands to the script section of a job configuration.
     *
     * This method allows appending one or more script commands to the job.
     * The commands are executed in sequence when the job runs.
     *
     * @param cmds The script commands to be added. Each command represents a single line in the job's script section.
     * @since 3.3.0
     */
    fun script(@Language("sh") vararg cmds: String) { script += cmds }
    /**
     * Appends one or more commands to the list of commands that will be executed before the main script.
     *
     * @param cmds A variable number of command strings to be added to the `beforeScript` pipeline stage.
     * @since 3.3.0
     */
    fun beforeScript(@Language("sh") vararg cmds: String) { beforeScript += cmds }
    /**
     * Appends the specified commands to the `afterScript` list.
     *
     * This method is used to define a series of commands that will be executed
     * after the primary script stage of a job in the CI/CD pipeline. Multiple commands
     * can be passed as arguments, and they will be added to the list of after-script commands.
     *
     * @param cmds The commands to be added to the `afterScript` list.
     * @since 3.3.0
     */
    fun afterScript(@Language("sh") vararg cmds: String) { afterScript += cmds }
    /**
     * Adds or updates a variable with the specified key and value.
     *
     * This method stores the provided key-value pair in the `variables` map.
     * If the key already exists, its value will be updated with the new value.
     *
     * @param key The unique identifier for the variable.
     * @param value The value associated with the specified key.
     * @since 3.3.0
     */
    fun variable(key: String, value: String) { variables[key] = value }
    /**
     * Adds the given variable arguments to the existing collection of variables.
     *
     * @param vars A variable number of String2 elements to be added.
     * @since 3.3.0
     */
    fun variables(vararg vars: String2) { variables += vars }
    /**
     * Adds the specified tags to the current job configuration.
     *
     * Tags are used to specify the job's requirements, such as runner selection.
     * Adding tags helps ensure jobs are executed on specific runners that match the tags provided.
     *
     * @param t The tags to be added to the current job.
     * @since 3.3.0
     */
    fun tags(vararg t: String) { tags += t }

    /**
     * Defines a set of rules to be applied using the provided `RulesScope`.
     *
     * This method allows specifying conditional logic or execution policies for a job
     * by leveraging the DSL provided by the `RulesScope` class. It provides a clean and
     * expressive way to define job-specific behaviors or directives within a build pipeline.
     *
     * @param block A lambda with receiver that provides a `RulesScope` instance where the
     *              rules can be defined.
     * @since 3.3.0
     */
    fun rules(block: ReceiverConsumer<RulesScope>) { rulesScope.apply(block) }

    /**
     * Adds a service configuration to the job definition.
     *
     * This function allows adding and configuring a service that will be used
     * during the execution of the job in the CI/CD pipeline. The service is defined
     * using the specified `name` and further customized within the provided `block`.
     *
     * @param name The name of the service to be added.
     * @param block A lambda with a receiver of type `ServiceBuilder`
     *              for configuring the service's properties and behavior.
     * @since 3.3.0
     */
    fun service(name: String, block: ReceiverConsumer<ServiceBuilder> = {}) {
        services += ServiceBuilder(name).apply(block).build()
    }

    /**
     * Configures the artifacts for the current job using the provided DSL block.
     *
     * @param block A DSL block to configure the artifacts using an instance of [ArtifactsBuilder].
     * @since 3.3.0
     */
    fun artifacts(block: ArtifactsBuilder.() -> Unit) {
        artifacts = ArtifactsBuilder().apply(block).build()
    }

    /**
     * Configures cache settings for a job using the provided [CacheBuilder] configuration block.
     *
     * @param block A lambda with [CacheBuilder] receiver for defining cache settings.
     * @since 3.3.0
     */
    fun cache(block: CacheBuilder.() -> Unit) {
        cache = CacheBuilder().apply(block).build()
    }

    /**
     * Specifies a list of jobs that the current job depends on.
     * The dependent jobs must be completed successfully before the current job is executed.
     *
     * @param jobs A variable number of job names representing the dependencies for the current job.
     * @since 3.3.0
     */
    fun dependencies(vararg jobs: String) { dependencies += jobs }
    /**
     * Adds one or more jobs as dependencies (needs) for the current job.
     * These jobs must be completed before the current job starts. By default,
     * the artifacts of the dependent jobs are downloaded, and the dependency is not optional.
     *
     * @param jobs The names of the jobs to be specified as dependencies.
     * @since 3.3.0
     */
    fun needs(vararg jobs: String) {
        needs += jobs.map { NeedConfig(it, artifacts = true, optional = false, null, null, null) }
    }
    /**
     * Adds a dependency or "need" for this job in the GitLab CI/CD pipeline.
     *
     * This function allows specifying another job that the current job depends on.
     * The dependency is defined using the job name and can optionally be configured
     * further using the `NeedBuilder` DSL to define parameters such as artifacts,
     * optionality, pipeline, project, and reference.
     *
     * @param job The name of the dependent job. This is required and specifies the job
     * that the current job depends on.
     * @param block An optional configuration block using `NeedBuilder` to define
     * additional parameters for the dependency.
     * @since 3.3.0
     */
    fun need(job: String, block: NeedBuilder.() -> Unit = {}) {
        needs += NeedBuilder(job).apply(block).build()
    }

    /**
     * Configures and builds an environment with the specified name and customization block.
     *
     * @param name The name of the environment to be configured.
     * @param block A lambda with receiver to customize the environment using the EnvironmentBuilder. Defaults to an empty block.
     * @since 3.3.0
     */
    fun environment(name: String, block: EnvironmentBuilder.() -> Unit = {}) {
        environment = EnvironmentBuilder(name).apply(block).build()
    }

    /**
     * Configures retry behavior for a job, allowing you to specify the maximum number of retry attempts
     * and the conditions under which these retries should occur.
     *
     * @param max The maximum number of retry attempts. Defaults to 2 if not specified.
     * @param on Conditions (specified as strings) under which retries should be attempted.
     * @since 3.3.0
     */
    fun retry(max: Int = 2, vararg on: String) {
        retry = RetryConfig(max, on.toList())
    }

    /**
     * Configures and sets up a trigger for a specific project and branch with optional inclusion patterns and strategy.
     *
     * @param project The name of the project to associate with the trigger. Defaults to null if not specified.
     * @param branch The branch name within the project to be triggered. Defaults to null if not specified.
     * @param include A pattern or condition indicating specific items to include in the trigger. Defaults to null if not specified.
     * @param strategy The strategy to be used for the trigger configuration. Defaults to null if not specified.
     * @since 3.3.0
     */
    fun trigger(project: String? = null, branch: String? = null, include: String? = null, strategy: String? = null) {
        trigger = TriggerConfig(project, branch, include, strategy)
    }

    /**
     * Configures inheritance settings.
     *
     * @param default Specifies whether the default inheritance setting is enabled or disabled.
     *                Use `true` to enable, `false` to disable, or `null` for no specific preference.
     * @param variables Specifies whether variables should inherit their values.
     *                  Use `true` to enable, `false` to disable, or `null` for no specific preference.
     * @since 3.3.0
     */
    fun inherit(default: Boolean? = null, variables: Boolean? = null) {
        inherit = InheritConfig(default, variables)
    }

    /**
     * Prepares a release configuration with the specified parameters.
     *
     * @param tagName The name of the tag for the release. This is a required parameter and cannot be null.
     * @param description An optional description of the release providing additional context.
     * @param name An optional name for the release. If not provided, defaults to null.
     * @param ref An optional reference (commit or branch) associated with the release. If not provided, defaults to null.
     * @since 3.3.0
     */
    fun release(tagName: String, description: String? = null, name: String? = null, ref: String? = null) {
        releaseConfig = ReleaseConfig(tagName, description, name, ref)
    }

    /**
     * Stores a secret configuration in the secrets map.
     *
     * @param name The key or identifier for the secret.
     * @param vault The name of the vault where the secret is stored.
     * @param field An optional specific field within the vault containing the secret. Defaults to null.
     * @since 3.3.0
     */
    fun secret(name: String, vault: String, field: String? = null) {
        secretsConfig[name] = SecretVaultConfig(vault, field)
    }

    /**
     * Associates an ID token configuration with a specified name.
     *
     * @param name The unique identifier for the ID token configuration.
     * @param aud The audience value to be associated with the ID token.
     * @since 3.3.0
     */
    fun idToken(name: String, aud: String) {
        idTokens[name] = IdTokenConfig(aud)
    }

    /**
     * Constructs and returns a `Job` instance using the provided configuration properties.
     *
     * The method aggregates various parameters including stage, image, scripts, variables,
     * rules, tags, services, artifacts, cache, dependencies, needs, environment,
     * and other optional configurations to build a fully defined `Job` object.
     *
     * @since 3.3.0
     * @return A `Job` instance populated with the specified properties and settings.
     */
    fun build() = Job(
        stage, image, script.toList(), beforeScript.toList(), afterScript.toList(),
        variables.toMap(), rulesScope.rules.toList(), only, null,
        tags.toList(), services.toList(), artifacts, cache,
        dependencies.toList(), needs.toList(), environment, retry,
        timeout, interruptible, allowFailure, parallel, resourceGroup,
        trigger, extends, inherit, coverage, releaseConfig,
        secretsConfig.toMap(), idTokens.toMap(),
    )
}

// --- DEFAULTS BUILDER ---

/**
 * A DSL builder class for constructing the `DefaultsConfig` object, which represents
 * default configuration settings in a GitLab CI pipeline.
 *
 * The `DefaultsBuilder` provides a declarative way to configure default properties
 * such as image, before/after scripts, tags, retry behavior, timeout, and caching.
 * @since 3.3.0
 */
@GitlabCiDslMarker
class DefaultsBuilder {
    /**
     * Represents an optional image resource, typically stored as a URI or file path in string format.
     * The variable can hold null, indicating the absence of an image.
     * @since 3.3.0
     */
    var image: String? = null
    /**
     * Holds a mutable list of strings representing script content or commands
     * that are intended to be executed or processed before the main operation.
     * This can be used for initialization or pre-processing tasks.
     * @since 3.3.0
     */
    val beforeScript: MList<String> = emptyMList()
    /**
     * A mutable list of strings initialized as an empty list.
     * Represents a collection of scripts or commands that may be
     * executed after a specific process or operation completes.
     * @since 3.3.0
     */
    val afterScript: MList<String> = emptyMList()
    /**
     * Represents a mutable list of tags as a custom type `MList<String>`.
     * This variable is initialized as an empty mutable list.
     * It can be used to store and manage a collection of string-based tags.
     * @since 3.3.0
     */
    val tags: MList<String> = emptyMList()
    /**
     * Configuration for retry mechanism.
     *
     * This variable holds an instance of `RetryConfig`, which defines the behavior
     * for retrying operations, such as the maximum number of attempts or delay
     * between retries. If set to `null`, retries are disabled.
     * @since 3.3.0
     */
    private var retry: RetryConfig? = null
    /**
     * Specifies the timeout duration for a job.
     *
     * Defines the maximum amount of time that a job is allowed to run
     * before being automatically terminated. This value is optional and,
     * if not set, the default timeout defined in the CI/CD configuration
     * is used.
     * @since 3.3.0
     */
    var timeout: Duration? = null
    /**
     * Indicates whether the operation or process can be interrupted.
     *
     * This property can be set to `true` if the operation is interruptible,
     * `false` if it is not, or `null` if the interruptibility status is unspecified.
     * @since 3.3.0
     */
    var interruptible: Boolean? = null
    /**
     * A private, mutable variable to hold the configuration settings for caching.
     *
     * This variable is nullable and can store an instance of `CacheConfig`,
     * which contains the necessary information for managing and implementing
     * caching mechanisms within the application. If null, caching may be
     * treated as not configured or disabled.
     * @since 3.3.0
     */
    private var cache: CacheConfig? = null

    /**
     * Appends the provided commands to the `beforeScript` collection.
     *
     * @param cmds A variable number of string commands to be added to the `beforeScript`.
     * @since 3.3.0
     */
    fun beforeScript(vararg cmds: String) { beforeScript += cmds }
    /**
     * Appends the provided commands to the `afterScript` collection.
     *
     * @param cmds A variable number of string commands to be added to the `afterScript`.
     * @since 3.3.0
     */
    fun afterScript(vararg cmds: String) { afterScript += cmds }
    /**
     * Appends one or more tag strings to the list of tags.
     *
     * @param t A variable number of tag strings to add to the existing tags.
     * @since 3.3.0
     */
    fun tags(vararg t: String) { tags += t }
    /**
     * Configures the retry settings for a workflow job.
     *
     * This function specifies the maximum number of retry attempts and the conditions under which retries
     * should occur. If not explicitly configured, the default maximum retry count is 2.
     *
     * @param max The maximum number of retry attempts allowed. Defaults to 2.
     * @param on The conditions under which retry attempts should be made, represented as a vararg of strings.
     * @since 3.3.0
     */
    fun retry(max: Int = 2, vararg on: String) { retry = RetryConfig(max, on.toList()) }
    /**
     * Sets the timeout duration for an operation.
     *
     * @param d The duration to set as the timeout.
     * @since 3.3.0
     */
    fun timeout(d: Duration) { timeout = d }
    /**
     * Configures and builds a cache using the given block of settings.
     *
     * @param block A lambda with receiver of type `CacheBuilder` used to define the cache configuration.
     * @since 3.3.0
     */
    fun cache(block: CacheBuilder.() -> Unit) { cache = CacheBuilder().apply(block).build() }

    /**
     * Constructs and returns a new instance of `DefaultsConfig` initialized with the current properties.
     *
     * @return a `DefaultsConfig` object containing the provided configuration values.
     * @since 3.3.0
     */
    fun build() = DefaultsConfig(
        image, beforeScript.toList(), afterScript.toList(), tags.toList(),
        retry, timeout, interruptible, cache,
    )
}

// --- WORKFLOW BUILDER ---

/**
 * Builder class for defining workflow configuration in a CI/CD pipeline.
 *
 * The `WorkflowBuilder` class is part of a DSL used to construct pipeline workflows.
 * It allows users to define a series of rules that dictate the conditions and behavior
 * of the workflow through various methods and properties. The defined workflow rules
 * are then used to generate a `WorkflowConfig` object, which can be used in the pipeline
 * configuration.
 * @author Tommaso Pastorelli
 * @since 3.3.0
 */
@GitlabCiDslMarker
class WorkflowBuilder {
    /**
     * A mutable list of workflow rules used to define conditions and execution policies for a workflow.
     *
     * Each rule in the list is represented by an instance of [WorkflowRule], which contains the logical
     * conditions, execution policies, and dynamic variables to customize pipeline workflow behavior.
     *
     * This property is mutable and initially contains an empty mutable list created using the `emptyMList` utility function.
     * Rules can be added using the `rule` function or other helper methods such as `ifMergeRequest()`, `ifDefaultBranch()`,
     * or `never()` within the `WorkflowBuilder` class.
     *
     * The list of rules is eventually used to construct a [WorkflowConfig] object via the `build` function,
     * allowing configured rules to influence the behavior of CI/CD pipeline workflows.
     * @since 3.3.0
     */
    val rules = emptyMList<WorkflowRule>()

    /**
     * Adds a workflow rule to the current set of rules.
     *
     * This method is used to define a new rule within a workflow, specifying the
     * conditions, execution policy, and environment variables associated with that rule.
     *
     * @param ifCondition The conditional expression as a string. This determines whether
     *     the rule applies based on the specified condition. Defaults to `null`.
     * @param whenPolicy The execution policy for the rule, represented by an instance
     *     of [WhenPolicy]. This controls under what circumstances the rule is executed.
     *     Defaults to `null`.
     * @param variables A mapping of key-value pairs representing environment variables
     *     that are applied when the rule is triggered. Defaults to an empty map.
     * @since 3.3.0
     */
    fun rule(@Language("gitlabciexpressionlanguage") ifCondition: String? = null, whenPolicy: WhenPolicy? = null, variables: StringMap = emptyMap()) {
        rules += WorkflowRule(ifCondition, whenPolicy, variables)
    }

    /**
     * Adds a workflow rule that is applied when the GitLab pipeline is triggered by a merge request event.
     *
     * This method sets a conditional rule where the `ifCondition` evaluates to true
     * if the pipeline source (`CI_PIPELINE_SOURCE`) is equal to `merge_request_event`.
     * It is typically used in CI/CD workflows to define rules that should execute
     * exclusively in the context of merge request events.
     *
     * This method is a shorthand for calling the `rule` function with the appropriate
     * `ifCondition` for merge request triggers.
     * @since 3.3.0
     */
    fun ifMergeRequest() = rule(ifCondition = $$"$CI_PIPELINE_SOURCE == \"merge_request_event\"")
    /**
     * Adds a workflow rule that applies when the current branch matches the default branch.
     *
     * This method is a shorthand for adding a conditional rule to the workflow to
     * ensure that the associated pipeline steps or actions are executed only if the
     * branch being processed is the default branch (e.g., "main" or "master").
     *
     * The condition for this rule is based on the `CI_COMMIT_BRANCH` and `CI_DEFAULT_BRANCH`
     * variables provided within the CI/CD pipeline environment.
     *
     * Within a GitLab CI/CD workflow, this is useful for tasks or jobs that should only run
     * on the default branch of the repository, such as deploying to production or running
     * specific checks.
     * @since 3.3.0
     */
    fun ifDefaultBranch() = rule(ifCondition = $$"$CI_COMMIT_BRANCH == $CI_DEFAULT_BRANCH")
    /**
     * Adds a workflow rule that is triggered when a Git tag is created in the repository.
     *
     * This function appends a new rule to the `rules` list within the `WorkflowBuilder`.
     * The rule is conditioned to apply specifically to pipeline executions
     * associated with a Git tag, as identified by the `CI_COMMIT_TAG` variable.
     *
     * By invoking this function, you can define pipeline steps or jobs that execute
     * exclusively for Git tag-related events within the CI/CD workflow.
     * @since 3.3.0
     */
    fun ifTag() = rule(ifCondition = $$"$CI_COMMIT_TAG")
    /**
     * Adds a workflow rule with the `NEVER` execution policy.
     *
     * This function defines a rule within the workflow configuration that explicitly
     * prevents the associated action or job from being executed under any circumstances.
     * It is useful for disabling specific steps or stages in a pipeline.
     *
     * The rule is based on the [WhenPolicy.NEVER] value, which signifies that the
     * action or job should never be triggered.
     * @since 3.3.0
     */
    fun never() = rule(whenPolicy = WhenPolicy.NEVER)

    /**
     * Constructs and returns an instance of `WorkflowConfig` using the current state
     * of the `rules` collection. The `rules` are converted to a `List` and passed
     * to the `WorkflowConfig` constructor.
     *
     * @return A new instance of `WorkflowConfig` containing the rules.
     * @since 3.3.0
     */
    fun build() = WorkflowConfig(rules.toList())
}

// --- PIPELINE BUILDER ---

/**
 * A builder class for constructing and configuring a GitLab CI pipeline structure.
 *
 * The `PipelineBuilder` class provides a DSL for creating pipelines with defined stages, variables,
 * workflows, defaults, includes, and jobs. It allows users to declaratively define and build
 * the structure according to specific requirements.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@GitlabCiDslMarker
class PipelineBuilder {
    /**
     * Mutable list representing the stages defined in the pipeline.
     *
     * This property stores the ordered stages of the pipeline, allowing
     * sequential execution of jobs within each stage. Stages are typically
     * added through the `stages(vararg names: String)` function, and their
     * order affects the workflow of the pipeline.
     *
     * The stages are initially defined as an empty list and can be
     * incrementally built using the provided methods in the `PipelineBuilder`.
     * @since 3.3.0
     */
    private val stages: MList<String> = emptyMList()
    /**
     * Stores a mutable map of variables, where each entry consists of a key
     * and a `VariableEntry` that defines the value, an optional description,
     * optional predefined options, and an `expand` flag.
     *
     * This map is utilized to manage variable definitions within the pipeline
     * configuration. Variables are added using the `variable` function and are
     * included as part of the pipeline when it is built via the `build` method.
     * @since 3.3.0
     */
    private val variables = emptyMMap<String, VariableEntry>()
    /**
     * Represents the workflow configuration for the pipeline.
     *
     * This property holds a nullable instance of `WorkflowConfig`, which defines
     * the behavior, rules, and conditions for the workflow execution in the CI/CD pipeline.
     *
     * Configurable through the `workflow` method of `PipelineBuilder`, this variable
     * can be set with custom workflow rules using a DSL-based block. It is included
     * in the pipeline definition when the pipeline is built.
     * @since 3.3.0
     */
    private var workflow: WorkflowConfig? = null
    /**
     * Holds the default configuration for the system.
     *
     * This variable maintains an instance of `DefaultsConfig` that can be
     * used to access or modify default settings. It is initialized as `null`
     * and should be explicitly set before usage.
     * @since 3.3.0
     */
    private var defaults: DefaultsConfig? = null
    /**
     * A mutable list storing inclusion configurations for the pipeline.
     *
     * The `includes` list is used to define various types of inclusions, such as local files,
     * remote URLs, templates, project-based inclusions, or custom components. These inclusions
     * allow external configurations or resources to be incorporated into the pipeline.
     *
     * Each element in this list is an instance of `IncludeEntry`, which encapsulates the specific
     * details of the inclusion.
     * @since 3.3.0
     */
    private val includes = emptyMList<IncludeEntry>()
    /**
     * Holds the collection of job configurations within the pipeline.
     *
     * This map associates job names with their respective `JobBuilder` instances, allowing
     * for the structured definition of pipeline jobs. Jobs can be added or retrieved using
     * their string identifiers.
     * @since 3.3.0
     */
    private val jobs = emptyMMap<String, JobBuilder>()

    /**
     * Adds one or more stage names to the pipeline's stages.
     *
     * This method allows stages to be defined or extended for the pipeline. Stages represent
     * the different phases in the pipeline execution and are processed in the order
     * they are defined.
     *
     * @param names Variable number of stage names to be added to the pipeline's stages.
     * @since 3.3.0
     */
    fun stages(vararg names: String) { stages += names }

    /**
     * Adds a variable entry to the variables map with the specified key, value, description, and options.
     *
     * @param key The unique identifier for the variable.
     * @param value The value associated with the variable.
     * @param description An optional description providing details about the variable.
     * @param options An optional list of strings representing additional options for the variable.
     * @since 3.3.0
     */
    fun variable(key: String, value: String, description: String? = null, options: List<String>? = null) {
        variables[key] = VariableEntry(value, description, options)
    }

    /**
     * Configures the workflow for the pipeline by providing a DSL block to define
     * workflow rules and policies, such as conditions for execution or restrictions.
     *
     * @param block A lambda with receiver of type `WorkflowBuilder`. This block allows
     * defining the workflow configuration, such as rules or conditions, by invoking
     * methods provided by the `WorkflowBuilder` class.
     * @since 3.3.0
     */
    fun workflow(block: ReceiverConsumer<WorkflowBuilder>) {
        workflow = WorkflowBuilder().apply(block).build()
    }

    /**
     * Configures default settings using the provided builder block.
     *
     * @param block A lambda with receiver that defines the configuration for default settings using [DefaultsBuilder].
     * @since 3.3.0
     */
    fun default(block: ReceiverConsumer<DefaultsBuilder>) {
        defaults = DefaultsBuilder().apply(block).build()
    }

    /**
     * Adds a local file inclusion to the pipeline configuration.
     *
     * This method allows specifying a local file to be included in the pipeline.
     * The file path should be relative to the project repository.
     *
     * @param file The relative path of the local file to include in the pipeline configuration.
     * @since 3.3.0
     */
    fun includeLocal(file: String) { includes += IncludeEntry(local = file) }
    /**
     * Adds a remote URL for inclusion in the pipeline's configuration.
     *
     * This method appends an entry to the list of includes, specifying a remote
     * URL from which pipeline configurations can be incorporated.
     *
     * @param url The remote URL pointing to the configuration to be included.
     * @since 3.3.0
     */
    fun includeRemote(url: String) { includes += IncludeEntry(remote = url) }
    /**
     * Includes a predefined template in the pipeline configuration.
     *
     * This method adds a reference to an external or predefined template that
     * should be included in the pipeline's configuration. Templates allow
     * the reuse of common pipeline definitions across different configurations.
     *
     * @param name The name of the template to include in the pipeline.
     * @since 3.3.0
     */
    fun includeTemplate(name: String) { includes += IncludeEntry(template = name) }
    /**
     * Includes a project configuration by specifying its project name, file path,
     * and an optional reference.
     *
     * @param project The name of the project to include.
     * @param file The file path associated with the project configuration.
     * @param ref An optional reference for the project configuration, default is null.
     * @since 3.3.0
     */
    fun includeProject(project: String, file: String, ref: String? = null) {
        includes += IncludeEntry(project = project, file = file, ref = ref)
    }
    /**
     * Adds a custom component to the pipeline configuration.
     *
     * This method allows you to include a custom component within the pipeline
     * by specifying its name. The provided component will be wrapped into an
     * `IncludeEntry` object and added to the list of includes in the pipeline.
     *
     * @param component The name of the custom component to include in the pipeline.
     * @since 3.3.0
     */
    fun includeComponent(component: String) { includes += IncludeEntry(component = component) }

    /**
     * Adds a job to the pipeline with the specified name and configuration.
     *
     * @param name The unique name of the job to be added.
     * @param block A lambda with a receiver of type [JobBuilder] used to configure the job.
     * @since 3.3.0
     */
    fun job(name: String, block: JobBuilder.() -> Unit) {
        jobs[name] = JobBuilder(name).apply(block)
    }

    /**
     * Adds a new job template to the pipeline with the specified name and configuration.
     *
     * @param name The name of the job template to be added.
     * @param block The configuration block used to define the job template.
     * @since 3.3.0
     */
    fun template(name: String, block: JobBuilder.() -> Unit) {
        jobs[".$name"] = JobBuilder(".$name").apply(block)
    }

    /**
     * Constructs and returns a new `Pipeline` instance based on the current configuration.
     *
     * This method aggregates various components such as stages, variables, workflow,
     * defaults, includes, and jobs to produce a fully configured pipeline object.
     *
     * @return A new `Pipeline` instance.
     * @since 3.3.0
     */
    fun build() = Pipeline(
        stages.toList(), variables.toMap(), workflow, defaults,
        includes.toList(), jobs.mapValues { it.value.build() },
    )
}

/**
 * Constructs and builds a GitLab CI pipeline configuration.
 *
 * @param block A lambda with receiver of type PipelineBuilder that defines the structure and content of the pipeline.
 * @return A Pipeline object representing the configured GitLab CI pipeline.
 * @since 3.3.0
 */
@Beta
fun buildGitlabCi(block: ReceiverConsumer<PipelineBuilder>): Pipeline =
    PipelineBuilder().apply(block).build()

/**
 * Initializes a GitLab CI pipeline by building and configuring it using the specified configuration block.
 * This method is annotated with `@Beta`, indicating it is in the beta stage of development and subject
 * to potential changes in the future.
 *
 * @param block A lambda function used to configure the `PipelineBuilder` instance. The `PipelineBuilder`
 * is passed as a receiver to the lambda, allowing for direct configuration.
 * @return The configured `PipelineBuilder` instance.
 * @since 3.6.4
 */
@Beta
fun initGitlabCi(block: ReceiverConsumer<PipelineBuilder>): PipelineBuilder =
    PipelineBuilder().apply(block)

// --- YAML RENDERER ---

/**
 * Converts the current pipeline configuration into its YAML representation.
 *
 * This method serializes the `Pipeline` object, including its stages, variables,
 * workflow rules, default settings, included configurations, and job definitions,
 * into a YAML-formatted string. The resulting string adheres to the YAML
 * specification and is structured according to CI/CD pipeline configuration standards.
 *
 * The YAML output includes the following components, if defined:
 *
 * - **Stages**: Specifies the sequence of stages within the pipeline.
 * - **Variables**: Declares pipeline-level variables with optional descriptions
 *   and selectable options.
 * - **Workflow**: Defines rules for when and under what conditions the pipeline
 *   should be executed.
 * - **Defaults**: Configures default settings such as Docker images, scripts, tags,
 *   retry policies, timeouts, and caching mechanisms, applied to jobs.
 * - **Includes**: Incorporates external pipeline configurations from various sources,
 *   including local files, remote URLs, templates, or project-scoped mappings.
 * - **Jobs**: Details the individual jobs within the pipeline, describing their scripts,
 *   stages, and other specific behavior.
 *
 * Note:
 * - This function includes experimental features and is marked with the `@Beta`
 *   annotation, indicating that it may undergo changes in future versions.
 *
 * @receiver The `Pipeline` object to be serialized into YAML.
 * @return A `Yaml` object containing the serialized YAML representation of the pipeline.
 * @since 3.3.0
 */
@OptIn(Beta::class)
fun Pipeline.toYaml() = Yaml(buildString {
    // Stages
    if (stages.isNotEmpty()) {
        appendLine("stages:")
        stages.forEach { appendLine("  - $it") }
        appendLine()
    }

    // Variables
    if (variables.isNotEmpty()) {
        appendLine("variables:")
        variables.forEach { [key, entry] ->
            if (entry.description.isNotNull() || entry.options.isNotNull()) {
                appendLine("  $key:")
                appendLine("    value: \"${entry.value}\"")
                entry.description?.let { appendLine("    description: \"$it\"") }
                entry.options?.let { opts ->
                    appendLine("    options:")
                    opts.forEach { appendLine("      - \"$it\"") }
                }
            } else {
                appendLine("  $key: \"${entry.value}\"")
            }
        }
        appendLine()
    }

    // Workflow
    workflow?.let { wf ->
        appendLine("workflow:")
        appendLine("  rules:")
        wf.rules.forEach { rule ->
            append("    - ")
            val parts: MList<String> = emptyMList()
            rule.ifCondition?.let { parts += "if: '$it'" }
            rule.`when`?.let { parts += "when: ${it.yaml}" }
            appendLine(parts.joinToString("\n      "))
            rule.variables.forEach { [k, v] -> appendLine("        $k: \"$v\"") }
        }
        appendLine()
    }

    // Defaults
    defaults?.let { d ->
        appendLine("default:")
        d.image?.let { appendLine("  image: $it") }
        if (d.beforeScript.isNotEmpty()) {
            appendLine("  before_script:")
            d.beforeScript.forEach { appendLine("    - $it") }
        }
        if (d.afterScript.isNotEmpty()) {
            appendLine("  after_script:")
            d.afterScript.forEach { appendLine("    - $it") }
        }
        if (d.tags.isNotEmpty()) {
            appendLine("  tags:")
            d.tags.forEach { appendLine("    - $it") }
        }
        d.retry?.let { r ->
            appendLine("  retry:")
            appendLine("    max: ${r.max}")
            if (r.`when`.isNotEmpty()) {
                appendLine("    when:")
                r.`when`.forEach { appendLine("      - $it") }
            }
        }
        d.timeout?.let { appendLine("  timeout: ${it.toCiString()}") }
        d.interruptible?.let { appendLine("  interruptible: $it") }
        d.cache?.let { renderCache(it, indent = 2) }
        appendLine()
    }

    // Includes
    if (includes.isNotEmpty()) {
        appendLine("include:")
        includes.forEach { inc ->
            when {
                inc.local.isNotNull() -> appendLine("  - local: '${inc.local}'")
                inc.remote.isNotNull() -> appendLine("  - remote: '${inc.remote}'")
                inc.template.isNotNull() -> appendLine("  - template: ${inc.template}")
                inc.component.isNotNull() -> appendLine("  - component: '${inc.component}'")
                inc.project.isNotNull() -> {
                    appendLine("  - project: '${inc.project}'")
                    inc.ref?.let { appendLine("    ref: '$it'") }
                    inc.file?.let { appendLine("    file: '${it}'") }
                }
            }
        }
        appendLine()
    }

    // Jobs
    jobs.forEach { [name, job] -> renderJob(name, job) }
})

/**
 * Renders the provided job configuration into a string representation using the `StringBuilder`.
 *
 * @param name The name of the job to be rendered.
 * @param job The `Job` object containing the details of the job configuration.
 * @since 3.3.0
 */
private fun StringBuilder.renderJob(name: String, job: Job) {
    appendLine("$name:")
    job.extends?.let { appendLine("  extends: $it") }
    job.stage?.let { appendLine("  stage: $it") }
    job.image?.let { appendLine("  image: $it") }

    if (job.variables.isNotEmpty()) {
        appendLine("  variables:")
        job.variables.forEach { [k, v] -> appendLine("    $k: \"$v\"") }
    }

    if (job.tags.isNotEmpty()) {
        appendLine("  tags:")
        job.tags.forEach { appendLine("    - $it") }
    }

    if (job.services.isNotEmpty()) {
        appendLine("  services:")
        job.services.forEach { svc ->
            if (svc.alias.isNull() && svc.command.isEmpty() && svc.variables.isEmpty()) {
                appendLine("    - $svc.name")
            } else {
                appendLine("    - name: ${svc.name}")
                svc.alias?.let { appendLine("      alias: $it") }
                if (svc.command.isNotEmpty()) {
                    appendLine("      command:")
                    svc.command.forEach { appendLine("        - $it") }
                }
                if (svc.variables.isNotEmpty()) {
                    appendLine("      variables:")
                    svc.variables.forEach { [k, v] -> appendLine("        $k: \"$v\"") }
                }
            }
        }
    }

    if (job.beforeScript.isNotEmpty()) {
        appendLine("  before_script:")
        job.beforeScript.forEach { appendLine("    - $it") }
    }

    if (job.script.isNotEmpty()) {
        appendLine("  script:")
        job.script.forEach { appendLine("    - $it") }
    }

    if (job.afterScript.isNotEmpty()) {
        appendLine("  after_script:")
        job.afterScript.forEach { appendLine("    - $it") }
    }

    // Rules
    if (job.rules.isNotEmpty()) {
        appendLine("  rules:")
        job.rules.forEach { rule ->
            append("    - ")
            val parts: MList<String> = emptyMList()
            rule.ifCondition?.let { parts += "if: '$it'" }
            rule.`when`?.let { parts += "when: ${it.yaml}" }
            rule.allowFailure?.let { parts += "allow_failure: $it" }

            if (rule.changes.isNotEmpty() || rule.exists.isNotEmpty() || rule.variables.isNotEmpty()) {
                if (parts.isNotEmpty()) appendLine(parts.first())
                else appendLine()
                (-1)(parts).forEach { appendLine("      $it") }
                if (rule.changes.isNotEmpty()) {
                    appendLine("      changes:")
                    rule.changes.forEach { appendLine("        - $it") }
                }
                if (rule.exists.isNotEmpty()) {
                    appendLine("      exists:")
                    rule.exists.forEach { appendLine("        - $it") }
                }
                if (rule.variables.isNotEmpty()) {
                    appendLine("      variables:")
                    rule.variables.forEach { [k, v] -> appendLine("        $k: \"$v\"") }
                }
            } else {
                appendLine(parts.joinToString("\n      "))
            }
        }
    }

    // Needs
    if (job.needs.isNotEmpty()) {
        val allSimple = job.needs.all { it.artifacts && !it.optional && it.pipeline.isNull() && it.project.isNull() }
        if (allSimple) {
            appendLine("  needs:")
            job.needs.forEach { appendLine("    - ${it.job}") }
        } else {
            appendLine("  needs:")
            job.needs.forEach { need ->
                appendLine("    - job: ${need.job}")
                if (!need.artifacts) appendLine("      artifacts: false")
                if (need.optional) appendLine("      optional: true")
                need.pipeline?.let { appendLine("      pipeline: $it") }
                need.project?.let { appendLine("      project: $it") }
                need.ref?.let { appendLine("      ref: $it") }
            }
        }
    }

    if (job.dependencies.isNotEmpty()) {
        appendLine("  dependencies:")
        job.dependencies.forEach { appendLine("    - $it") }
    }

    job.artifacts?.let { a ->
        appendLine("  artifacts:")
        a.name?.let { appendLine("    name: \"$it\"") }
        if (a.paths.isNotEmpty()) {
            appendLine("    paths:")
            a.paths.forEach { appendLine("      - $it") }
        }
        if (a.exclude.isNotEmpty()) {
            appendLine("    exclude:")
            a.exclude.forEach { appendLine("      - $it") }
        }
        if (a.reports.isNotEmpty()) {
            appendLine("    reports:")
            a.reports.forEach { [type, path] -> appendLine("      $type: $path") }
        }
        a.expireIn?.let { appendLine("    expire_in: ${it.toCiString()}") }
        a.`when`?.let { appendLine("    when: ${it.yaml}") }
        if (a.untracked) appendLine("    untracked: true")
    }

    job.cache?.let { renderCache(it, indent = 2) }

    job.environment?.let { env ->
        appendLine("  environment:")
        appendLine("    name: ${env.name}")
        env.url?.let { appendLine("    url: $it") }
        env.action?.let { appendLine("    action: ${it.yaml}") }
        env.autoStopIn?.let { appendLine("    auto_stop_in: ${it.toCiString()}") }
        env.onStop?.let { appendLine("    on_stop: $it") }
        env.kubernetes?.let { k ->
            appendLine("    kubernetes:")
            k.namespace?.let { appendLine("      namespace: $it") }
        }
    }

    job.retry?.let { r ->
        appendLine("  retry:")
        appendLine("    max: ${r.max}")
        if (r.`when`.isNotEmpty()) {
            appendLine("    when:")
            r.`when`.forEach { appendLine("      - $it") }
        }
    }

    job.timeout?.let { appendLine("  timeout: ${it.toCiString()}") }
    job.interruptible?.let { appendLine("  interruptible: $it") }
    if (job.allowFailure) appendLine("  allow_failure: true")
    job.parallel?.let { appendLine("  parallel: $it") }
    job.resourceGroup?.let { appendLine("  resource_group: $it") }
    job.coverage?.let { appendLine("  coverage: '/$it/'") }

    job.trigger?.let { t ->
        appendLine("  trigger:")
        t.project?.let { appendLine("    project: $it") }
        t.branch?.let { appendLine("    branch: $it") }
        t.include?.let { appendLine("    include: $it") }
        t.strategy?.let { appendLine("    strategy: $it") }
    }

    if (job.secretsConfig.isNotEmpty()) {
        appendLine("  secrets:")
        job.secretsConfig.forEach { [name, cfg] ->
            appendLine("    $name:")
            appendLine("      vault: ${cfg.vault}")
            cfg.field?.let { appendLine("      field: $it") }
        }
    }

    if (job.idTokens.isNotEmpty()) {
        appendLine("  id_tokens:")
        job.idTokens.forEach { [name, cfg] ->
            appendLine("    $name:")
            appendLine("      aud: ${cfg.aud}")
        }
    }

    appendLine()
}

/**
 * Renders the cache configuration to the YAML format.
 *
 * This function appends a YAML-formatted representation of the provided cache configuration
 * to the current StringBuilder instance. The rendered output includes details such as
 * cache keys, paths, policies, and other attributes specified in the `CacheConfig` instance.
 *
 * @param cache The caching configuration instance containing the details to render.
 * @param indent The level of indentation to apply to the YAML output. Determines the
 *               number of spaces prepended to each line of the rendered output.
 * @since 3.3.0
 */
private fun StringBuilder.renderCache(cache: CacheConfig, indent: Int) {
    val pad = "  ".repeat(indent)
    appendLine("${pad}cache:")
    cache.key?.let { appendLine("$pad  key: $it") }
    if (cache.keyFiles.isNotEmpty()) {
        appendLine("$pad  key:")
        appendLine("$pad    files:")
        cache.keyFiles.forEach { appendLine("$pad      - $it") }
    }
    if (cache.paths.isNotEmpty()) {
        appendLine("$pad  paths:")
        cache.paths.forEach { appendLine("$pad    - $it") }
    }
    cache.policy?.let { appendLine("$pad  policy: ${it.yaml}") }
    if (cache.untracked) appendLine("$pad  untracked: true")
    if (cache.unprotect) appendLine("$pad  unprotect: true")
    cache.`when`?.let { appendLine("$pad  when: ${it.yaml}") }
    if (cache.fallbackKeys.isNotEmpty()) {
        appendLine("$pad  fallback_keys:")
        cache.fallbackKeys.forEach { appendLine("$pad    - $it") }
    }
}

/**
 * Converts the `Duration` instance to a human-readable string representation, formatting it
 * into hours and minutes where applicable.
 *
 * @return A string representing the duration in hours and/or minutes. For durations equal to or
 * greater than an hour, the result is presented as "X hours" if the duration is a whole number
 * of hours, or as "Xh Ym" if there are additional minutes. For durations less than an hour,
 * the result is formatted as "X minutes".
 * @since 3.3.0
 */
@OptIn(RiskyApproximationOfTemporal::class)
private fun Duration.toCiString(): String {
    val totalMinutes = toMinutes().toLong()
    return when {
        totalMinutes >= 60 && totalMinutes % 60 == 0L -> "${totalMinutes / 60} hours"
        totalMinutes >= 60 -> "${totalMinutes / 60}h ${totalMinutes % 60}m"
        else -> "$totalMinutes minutes"
    }
}

// --- CONVENIENCE ---

/**
 * Writes the serialized YAML representation of the pipeline to the specified file path.
 * The pipeline is converted to a YAML format using the `toYaml` method, and the resulting
 * content is written to the provided file path.
 *
 * @param path The file path where the YAML representation of the pipeline is to be written.
 *             The path must be writable, and existing content will be overwritten.
 * @since 3.3.0
 */
@OptIn(Beta::class)
fun Pipeline.writeYaml(path: Path) {
    path.writeText(toYaml().value)
}

/**
 * Writes the pipeline configuration in YAML format to the specified writer and flushes the writer.
 *
 * @param writer The writer to which the YAML representation of the pipeline will be written.
 * @since 3.3.0
 */
@OptIn(Beta::class)
fun Pipeline.writeTo(writer: Writer) {
    writer.write(toYaml().value)
    writer.flush()
}