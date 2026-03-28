@file:Suppress("unused")
@file:Since("3.3.0")

package dev.tommasop1804.kutils.dsl.dockercompose

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.coding.*
import dev.tommasop1804.kutils.classes.time.*
import dev.tommasop1804.kutils.classes.time.Duration.Companion.asSecondsOfDuration
import java.io.Writer
import java.nio.file.Path
import kotlin.io.path.writeText

@DslMarker
annotation class DockerComposeDslMarker

// --- MODEL ---

/**
 * Represents a Docker Compose file configuration.
 *
 * @property version The version of the Docker Compose file format.
 * @property services A map of service names to their respective configurations.
 * @property volumes A map of volume names to their respective configurations, or `null` for default configurations.
 * @property networks A map of network names to their respective configurations, or `null` for default configurations.
 * @property secrets A map of secret names to their respective configurations.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class ComposeFile(
    val version: String?,
    val services: Map<String, Service>,
    val volumes: Map<String, VolumeConfig?>,
    val networks: Map<String, NetworkConfig?>,
    val secrets: Map<String, SecretConfig>
)

/**
 * Represents a service configuration in a docker-compose file.
 *
 * @property image The image to use for the service. This can be a Docker Hub image or a custom image.
 * @property build Specifies build configuration for the service, including context, Dockerfile, and build arguments.
 * @property command Override the default command for the image.
 * @property entrypoint Override the default entry point for the image.
 * @property containerName The custom name for the container.
 * @property restart Defines the restart policy for the service (e.g., no, always, on-failure).
 * @property ports A list of port mappings between the host and the container.
 * @property expose A list of ports to expose from the container without mapping them to the host.
 * @property environment Environment variables to set in the container as key-value pairs.
 * @property envFile A list of files containing environment variables to set in the container.
 * @property volumes A list of volume mappings between the host and the container.
 * @property dependsOn Specifies service dependencies and their conditions before starting the service.
 * @property healthcheck Defines a health check configuration for the service.
 * @property networks A list of networks the service is connected to.
 * @property labels Metadata labels as key-value pairs to apply to the service.
 * @property deploy Deployment configuration for the service, including replicas and resource limits.
 * @property workingDir Overrides the working directory for the container.
 * @property user Specifies the user that runs the container process.
 * @property extraHosts Additional host-to-IP mappings for the container.
 * @property logging Logging configuration for the service, including the logging driver and options.
 * @property secrets A list of secrets to mount into the container.
 * @property profiles A list of profiles that this service is associated with.
 * @property tmpfs A list of tmpfs mounts for the container.
 * @property ulimits User limit settings for the container, such as open files or processes.
 * @property sysctls Kernel parameters to set in the container.
 * @property capAdd A list of capabilities to add to the container.
 * @property capDrop A list of capabilities to remove from the container.
 * @property privileged Specifies whether the container should run in privileged mode.
 * @property readOnly Specifies whether the container's root filesystem is read-only.
 * @property stdinOpen Keep the container's standard input (stdin) open.
 * @property tty Allocate a pseudo-TTY for the container.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class Service(
    val image: String?,
    val build: BuildConfig?,
    val command: StringList,
    val entrypoint: String?,
    val containerName: String?,
    val restart: RestartPolicy?,
    val ports: StringList,
    val expose: StringList,
    val environment: StringMap,
    val envFile: StringList,
    val volumes: StringList,
    val dependsOn: Map<String, DependsOnCondition>,
    val healthcheck: Healthcheck?,
    val networks: StringList,
    val labels: StringMap,
    val deploy: DeployConfig?,
    val workingDir: String?,
    val user: String?,
    val extraHosts: StringList,
    val logging: LoggingConfig?,
    val secrets: StringList,
    val profiles: StringList,
    val tmpfs: StringList,
    val ulimits: Map<String, UlimitConfig>,
    val sysctls: StringMap,
    val capAdd: StringList,
    val capDrop: StringList,
    val privileged: Boolean,
    val readOnly: Boolean,
    val stdinOpen: Boolean,
    val tty: Boolean
)

/**
 * Represents the build configuration for a service, including details about
 * the context directory, Dockerfile, build arguments, target stage, and cache sources.
 *
 * @property context The build context directory. This is typically the directory
 * where the Docker build command is executed.
 * @property dockerfile The path to the Dockerfile relative to the context. If null, the default
 * Dockerfile in the context directory is used.
 * @property args A map of build-time arguments to pass to the Docker build process.
 * These arguments can be referenced in the Dockerfile using ARG instructions.
 * @property target The build target stage to use, as specified in a multi-stage Dockerfile.
 * If null, the final stage of the Dockerfile is used.
 * @property cacheFrom A list of image names to consider as cache sources during the build.
 * These images are used to speed up the build process by reusing layers.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class BuildConfig(
    val context: String,
    val dockerfile: String?,
    val args: StringMap,
    val target: String?,
    val cacheFrom: StringList
)

/**
 * Represents a health check configuration for a system or service.
 *
 * @property test the command or script to execute for checking the health.
 * @property interval the time interval between two consecutive health checks.
 * @property timeout the maximum duration a health check is allowed to run before being considered failed.
 * @property retries the number of times a failed health check should be retried before marking it as unhealthy.
 * @property startPeriod an optional duration to delay the start of health checks, allowing the system or service some time to initialize.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class Healthcheck(
    val test: StringList,
    val interval: Duration,
    val timeout: Duration,
    val retries: Int,
    val startPeriod: Duration?
)

/**
 * Represents the deployment configuration for a service.
 *
 * @property replicas The desired number of service replicas. If null, the number of replicas is not specified.
 * @property resources The resource configuration (e.g., limits and reservations) for the service.
 * @property restartPolicy The restart policy to apply for the service in case of failures or other restart triggers.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class DeployConfig(
    val replicas: Int?,
    val resources: Resources?,
    val restartPolicy: DeployRestartPolicy?
)

/**
 * Represents resource configurations for a deployable entity, including both resource limits
 * and reservations. Limits specify the maximum amount of resources that can be used,
 * while reservations define the minimum guaranteed resources.
 *
 * @property limits The maximum resource limits, such as CPU and memory, specified as a [ResourceSpec].
 * @property reservations The guaranteed minimum resources, such as CPU and memory, specified as a [ResourceSpec].
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class Resources(
    val limits: ResourceSpec?,
    val reservations: ResourceSpec?
)

/**
 * Represents the specifications for computing resources such as CPU and memory.
 *
 * This class models resource constraints or allocations, and is typically used
 * in defining limits or reservations for computational resources.
 *
 * @property cpus The amount of CPU resources to allocate or limit. This is represented
 * as a string, typically indicating the number of CPUs or CPU shares,
 * and can be null to imply no specific value is set.
 * @property memory The amount of memory to allocate or limit. This is represented
 * as a string, typically indicating the memory size (e.g., "512MiB", "2GiB"),
 * and can be null to imply no specific value is set.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class ResourceSpec(
    val cpus: String?,
    val memory: String?
)

/**
 * Represents the restart policy for a deployment process.
 *
 * @property condition Specifies the condition under which the deployment should be restarted.
 *                     Common values include "always", "on-failure", or "none".
 * @property delay Defines the delay between restart attempts. If null, no delay is defined.
 * @property maxAttempts Indicates the maximum number of restart attempts allowed. If null,
 *                       the number of attempts is unlimited.
 * @property window Specifies the time window within which the restart attempts are monitored.
 *                  If null, no specific monitoring window is applied.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class DeployRestartPolicy(
    val condition: String,
    val delay: Duration?,
    val maxAttempts: Int?,
    val window: Duration?
)

/**
 * Configuration for logging options in a containerized service.
 *
 * This class defines the logging driver and associated options used to configure
 * the logging behavior for a service. The logging driver specifies the mechanism
 * used to capture log output, while the options provide additional configuration
 * details for the selected driver.
 *
 * @property driver The logging driver used for capturing log output.
 * @property options A map of key-value pairs providing specific configuration
 *                   options for the selected logging driver.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class LoggingConfig(
    val driver: String,
    val options: StringMap
)

/**
 * Represents the configuration for a volume in a Docker Compose file.
 *
 * This class encapsulates the properties required to define a volume, including
 * the driver, driver-specific options, whether the volume is external or not,
 * and its name.
 *
 * @property driver The name of the driver to be used for the volume, or null if not specified.
 * @property driverOpts A map of key-value pairs representing options for the volume driver.
 * @property external Indicates whether the volume is externally managed outside of Docker Compose.
 * @property name The name of the volume, or null if it should be auto-generated or inferred by Docker Compose.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class VolumeConfig(
    val driver: String?,
    val driverOpts: StringMap,
    val external: Boolean,
    val name: String?
)

/**
 * Represents the configuration for a network in a Docker Compose file.
 *
 * This data class defines the properties used for specifying network-related
 * configurations, including driver, external usage, name, and IPAM (IP Address
 * Management) configurations.
 *
 * @property driver The network driver to use. It determines the functionality and behavior
 *                  of the network (e.g., "bridge", "overlay").
 * @property external Indicates whether the network is externally managed. If true, Docker
 *                    Compose will use an existing network rather than creating a new one.
 * @property name The name of the network. If not specified, Docker Compose generates
 *                a default name based on the project.
 * @property ipam An optional IPAM configuration that defines how IP addresses
 *                and related settings are managed for the network.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class NetworkConfig(
    val driver: String?,
    val external: Boolean,
    val name: String?,
    val ipam: IpamConfig?
)

/**
 * Represents the configuration for IP Address Management (IPAM) used within a network.
 *
 * This class is used to define the IPAM settings, including the driver responsible
 * for managing IP address allocation and a list of specific IPAM pools that define
 * subnets and gateways.
 *
 * @property driver The name of the IPAM driver used for managing IP allocation.
 * Can be null if no specific driver is defined.
 * @property config A list of `IpamPool` objects, each describing a subnet and an
 * optional gateway for the IP allocation.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class IpamConfig(
    val driver: String?,
    val config: List<IpamPool>
)

/**
 * Represents an IP Address Management (IPAM) pool configuration, defining a subnet and an optional gateway.
 *
 * This class is typically used to specify the range of IP addresses and a gateway for network
 * configuration in containerized environments, such as with Docker Compose.
 *
 * @property subnet The subnet in CIDR notation (e.g., "192.168.1.0/24") that defines the IP address range.
 * @property gateway The optional gateway IP address for the subnet. If null, no gateway is set for this pool.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class IpamPool(
    val subnet: String,
    val gateway: String?
)

/**
 * Represents the configuration for a secret in a Docker Compose setup.
 *
 * This class specifies the details of a secret that can be either defined
 * by a file on the local filesystem or managed externally.
 *
 * @property file The path to the file containing the secret data. If null, no file is associated.
 * @property external Indicates whether the secret is externally managed. If true, the secret will not be created by the Compose file.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class SecretConfig(
    val file: String?,
    val external: Boolean
)

/**
 * Represents a configuration for setting ulimit values in a container.
 *
 * Ulimit is a mechanism to control resources available to processes started by a user.
 * This configuration allows specifying separate soft and hard limits.
 *
 * @property soft The soft limit, which is the value that the kernel enforces for the corresponding resource.
 *                It can be modified up to the hard limit without requiring special privileges.
 * @property hard The hard limit, which acts as an upper bound for the soft limit
 *                and can be raised only by a privileged user.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
data class UlimitConfig(val soft: Long, val hard: Long)

/**
 * Represents the policy that governs the restart behavior of a container.
 *
 * A `RestartPolicy` defines the conditions under which a container should be restarted
 * by the container orchestration engine. The policy determines whether a container is restarted
 * unconditionally, never, or based on failure conditions.
 *
 * @property yaml The YAML representation of the restart policy.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
enum class RestartPolicy(val yaml: String) {
    /**
     * Represents a restart policy where no automatic restarts are performed.
     * @since 3.3.0
     */
    NO("no"),
    /**
     * Indicates that the service will always restart regardless of the exit status.
     * @since 3.3.0
     */
    ALWAYS("always"),
    /**
     * Represents a restart policy where the service is restarted only if it exits with a non-zero status.
     * Typically used to ensure resiliency for tasks that may fail due to transient issues.
     * @since 3.3.0
     */
    ON_FAILURE("on-failure"),
    /**
     * Represents a restart policy where the service will not restart
     * if it has been explicitly stopped. This policy is used to prevent
     * automatic restarts unless the container was stopped unintentionally.
     * @since 3.3.0
     */
    UNLESS_STOPPED("unless-stopped"),
}

/**
 * Represents the conditions that can be applied to a dependent service in a containerized
 * application environment. These conditions determine the state a service must reach
 * before another service can start or proceed.
 *
 * @property yaml The YAML representation of the condition used in configuration files.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
enum class DependsOnCondition(val yaml: String) {
    /**
     * Represents the condition indicating that a service has started.
     *
     * Used in scenarios where the execution of a dependent service or resource
     * requires another service to reach the "started" state before proceeding.
     * @since 3.3.0
     */
    SERVICE_STARTED("service_started"),
    /**
     * Represents a condition where a dependent service is considered healthy.
     *
     * This condition is used to signify that a required service has passed its
     * health checks and is ready to be utilized by other services or components
     * within the system. It is a part of the `DependsOnCondition` enumeration
     * which defines various states that a dependent service can exhibit.
     *
     * @since 3.3.0
     */
    SERVICE_HEALTHY("service_healthy"),
    /**
     * Represents the condition where a service has completed successfully.
     * This is used as a dependency condition in scenarios where successful
     * completion of a service is required.
     * @since 3.3.0
     */
    SERVICE_COMPLETED_SUCCESSFULLY("service_completed_successfully"),
}

// --- BUILDERS ---

/**
 * Builder class for configuring and creating a [Healthcheck] instance in the Docker Compose DSL.
 *
 * This class provides a set of properties and methods for defining the behavior of a health check
 * for a service. It includes options for customization such as the health check command, interval,
 * timeout, number of retries, and an optional start period.
 * @author Tommaso Pastorelli
 * @since 3.3.0
 */
@DockerComposeDslMarker
class HealthcheckBuilder {
    /**
     * Specifies the command to be used for the health check.
     * This can be used to define custom health check commands
     * for services in a Docker Compose configuration.
     * @since 3.3.0
     */
    var test: StringMList = emptyMList()
    /**
     * The time to wait between health check executions.
     *
     * This duration specifies the interval at which health checks will be performed.
     * It should be configured based on system requirements and expected health check response times.
     * The default value is 30 seconds.
     * @since 3.3.0
     */
    var interval: Duration = 30.asSecondsOfDuration()
    /**
     * Specifies the maximum duration allowed for a health check to complete.
     * If the health check does not finish within this period, it will be considered failed.
     * The default value is 30 seconds.
     * @since 3.3.0
     */
    var timeout: Duration = 30.asSecondsOfDuration()
    /**
     * Specifies the maximum number of retry attempts allowed for the health check.
     * This value determines how many times the health check will retry before considering it a failure.
     *
     * Default value is set to 3.
     * @since 3.3.0
     */
    var retries: Int = 3
    /**
     * Specifies the optional grace period before starting health check retries.
     * This allows a service to initialize without being marked as unhealthy.
     *
     * A null value indicates no start period is defined.
     * @since 3.3.0
     */
    var startPeriod: Duration? = null

    /**
     * Adds the specified commands to the `test` property by concatenating them.
     *
     * @param command The commands to be appended to the `test` property.
     * @since 3.3.0
     */
    fun test(vararg command: String) {
        command.forEach { test += it }
    }
    /**
     * Builds a [Healthcheck] instance using the current configuration of the `HealthcheckBuilder`.
     *
     * @return A new [Healthcheck] instance with the specified properties.
     * @since 3.3.0
     */
    fun build() = Healthcheck(test, interval, timeout, retries, startPeriod)
}

/**
 * A builder class for configuring resource specifications such as CPU and memory.
 *
 * This is primarily used within the context of Docker Compose DSL for defining resource limits
 * or reservations, typically in conjunction with `DeployBuilder`'s `limits` or `reservations` functions.
 *
 * Instances of this builder allow fluent configuration of the CPU and memory resources
 * and produce a `ResourceSpec` object upon building.
 *
 * Annotated with `@DockerComposeDslMarker` to ensure scope control in DSL usage.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@DockerComposeDslMarker
class ResourceSpecBuilder {
    /**
     * Specifies the CPU resources available for the container.
     *
     * This property defines a string representation of the CPU limit or reservation associated with the container's resources.
     * It can be used to configure how much CPU capacity is allocated to a specific service within a Docker Compose configuration.
     *
     * A null value indicates that no specific CPU resource allocation is set.
     * @since 3.3.0
     */
    var cpus: String? = null
    /**
     * Defines the memory limit for a resource.
     *
     * This variable specifies the amount of memory allocated for the resource,
     * represented as a string. The value is optional and can be null, indicating
     * that no memory limit is explicitly set.
     * @since 3.3.0
     */
    var memory: String? = null

    /**
     * Constructs a `ResourceSpec` instance using the provided CPU and memory configuration.
     *
     * This method evaluates the `cpus` and `memory` properties of the `ResourceSpecBuilder`
     * to determine if a `ResourceSpec` should be created. If either `cpus` or `memory`
     * is not `null`, a `ResourceSpec` will be returned with the provided values; otherwise,
     * `null` will be returned.
     *
     * @return A `ResourceSpec` object if `cpus` or `memory` is not null, otherwise `null`.
     * @since 3.3.0
     */
    fun build() = if (cpus.isNotNull() || memory.isNotNull()) ResourceSpec(cpus, memory) else null
}

/**
 * Builder class for configuring deployment settings in a Docker Compose file.
 *
 * This class provides functions to define the number of replicas, resource limits,
 * resource reservations, and the restart policy for a service.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@DockerComposeDslMarker
class DeployBuilder {
    /**
     * The number of replica tasks for the service.
     *
     * This property specifies the desired number of instances of a service
     * running in a distributed system. When set, it controls the scaling
     * level of the service by defining the number of task replicas.
     * It can be set via the [replicas] function in the DSL or directly
     * assigned a value.
     *
     * A value of `null` indicates that no specific number of replicas has
     * been defined, allowing the system to determine the behavior.
     * @since 3.3.0
     */
    var replicas: Int? = null
    /**
     * Holds a mutable configuration for resource limits during the build phase.
     *
     * This variable is assigned using the [limits] function to configure resource constraints
     * such as CPUs and memory associated with a deployment. The [ResourceSpecBuilder] instance
     * allows for defining these specifications in a DSL-style block.
     *
     * When the [build] function is invoked, the [limitsBuilder] is used to generate a [ResourceSpec]
     * object which represents the finalized resource limits.
     *
     * It is nullable to indicate that limits may be optional, and its value remains null until set
     * via the [limits] method.
     * @since 3.3.0
     */
    private var limitsBuilder: ResourceSpecBuilder? = null
    /**
     * A builder for configuring resource reservations for a deployment.
     *
     * This variable holds an instance of `ResourceSpecBuilder` which is used
     * to define CPU and memory requirements for reserving resources in the deployment.
     * It is typically configured via the `reservations` function within the `DeployBuilder` DSL.
     *
     * If no reservations are specified, this variable remains null.
     * @since 3.3.0
     */
    private var reservationsBuilder: ResourceSpecBuilder? = null
    /**
     * Defines the restart policy configuration for a deployment.
     *
     * This property holds an instance of [DeployRestartPolicy], which specifies the
     * conditions under which a service should be restarted, along with optional parameters
     * such as delay, maximum retry attempts, and reset window duration.
     *
     * The restart policy is typically configured using the `restartPolicy` function within
     * the enclosing [DeployBuilder] class, allowing fine-tuned control over service
     * behavior during failure scenarios.
     * @since 3.3.0
     */
    private var restartPolicy: DeployRestartPolicy? = null

    /**
     * Configures resource limits for the deployment.
     *
     * @param block A lambda with `ResourceSpecBuilder` as the receiver
     *              where resource limits such as `cpus` and `memory` can be specified.
     * @since 3.3.0
     */
    fun limits(block: ReceiverConsumer<ResourceSpecBuilder>) {
        limitsBuilder = ResourceSpecBuilder().apply(block)
    }

    /**
     * Configures the resource reservations for the deployment.
     *
     * @param block A lambda with receiver used to configure the resource reservations
     *              by setting CPU and memory requirements.
     * @since 3.3.0
     */
    fun reservations(block: ReceiverConsumer<ResourceSpecBuilder>) {
        reservationsBuilder = ResourceSpecBuilder().apply(block)
    }

    /**
     * Configures the restart policy for the deployment.
     *
     * @param condition Specifies the condition under which the service should be restarted
     * (e.g., "none", "on-failure", or "any").
     * @param delay The delay between restarts, if applicable. Defaults to `null` if not specified.
     * @param maxAttempts The maximum number of restart attempts allowed. Defaults to `null` for unlimited attempts.
     * @param window The time window for evaluating restart attempts. Defaults to `null` if not specified.
     * @since 3.3.0
     */
    fun restartPolicy(condition: String, delay: Duration? = null, maxAttempts: Int? = null, window: Duration? = null) {
        restartPolicy = DeployRestartPolicy(condition, delay, maxAttempts, window)
    }

    /**
     * Builds a DeployConfig instance based on the current configuration of the DeployBuilder.
     *
     * @return A DeployConfig object containing the configured number of replicas, resource limits
     * and reservations, and restart policy. Any non-configured properties will be null.
     * @since 3.3.0
     */
    fun build() = DeployConfig(
        replicas,
        Resources(limitsBuilder?.build(), reservationsBuilder?.build()),
        restartPolicy,
    )
}

/**
 * Configures logging options for a service in a Docker Compose configuration.
 *
 * This builder allows the customization of logging settings, including the logging driver
 * and specific driver-related options. It is typically used in the context of defining
 * a service's logging requirements within the `logging` block of a service definition.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@DockerComposeDslMarker
class LoggingBuilder {
    /**
     * The logging driver to be used for the configuration.
     *
     * This property specifies the name of the logging driver, which determines how log messages
     * generated by the container are handled. Common values for this property include "json-file"
     * and "syslog", but it can be customized as needed depending on the logging backend being used.
     *
     * By default, this property is set to "json-file".
     * @since 3.3.0
     */
    var driver: String = "json-file"
    /**
     * Represents a mutable map for configuring logging driver options.
     *
     * The `options` property is primarily used to store key-value pairs
     * defining parameters for a logging driver, such as `max-size` or `max-file`.
     * Values can be added or modified using the `option` function in the containing class.
     *
     * This map is empty by default, but can be populated dynamically
     * before being passed to the `LoggingConfig`.
     * @since 3.3.0
     */
    val options: StringMMap = emptyMMap()

    /**
     * Sets a logging option by specifying a key-value pair.
     *
     * This method allows adding a custom logging option to the configuration,
     * which is stored as a key-value pair in the `options` map. It can be
     * used to specify additional parameters for configuring logging behavior.
     *
     * @param key The name of the logging option to be set.
     * @param value The value associated with the logging option.
     * @since 3.3.0
     */
    fun option(key: String, value: String) { options[key] = value }
    /**
     * Sets the maximum size for log files.
     *
     * This method configures the "max-size" option for the logging settings,
     * specifying the maximum size a log file can reach before it is rotated.
     *
     * @param size The maximum size of the log file. This should be a string representation
     * of the size, such as "10m" for 10 megabytes.
     * @since 3.3.0
     */
    fun maxSize(size: String) { option("max-size", size) }
    /**
     * Configures the `max-file` logging option, specifying the maximum number
     * of log files to retain before overwriting old files. This is typically
     * used in conjunction with the `max-size` option to manage log rotation.
     *
     * @param count The maximum number of log files to retain. Must be provided
     *              as a string. For example: `"5"` to retain up to 5 old log files.
     * @since 3.3.0
     */
    fun maxFile(count: String) { option("max-file", count) }

    /**
     * Builds and returns a new instance of `LoggingConfig` using the current
     * configuration of the `LoggingBuilder`.
     *
     * This method creates the `LoggingConfig` object by combining the `driver`
     * and `options` configured in the `LoggingBuilder`. The returned object can
     * be used to define logging configuration within a Docker Compose service.
     *
     * @return A new instance of `LoggingConfig` containing the configured driver and options.
     * @since 3.3.0
     */
    fun build() = LoggingConfig(driver, options.toMap())
}

/**
 * A builder class for constructing a `BuildConfig` instance used in defining build-related configurations for Docker Compose services.
 *
 * This builder allows users to specify the context directory, Dockerfile path, build arguments, target stage,
 * and cache sources when configuring the build process for a Docker service.
 *
 * @constructor Initializes the builder with the provided build context directory.
 * @param context The build context directory.
 * @author Tommaso Pastorelli
 * @since 3.3.0
 */
@DockerComposeDslMarker
class BuildConfigBuilder(private val context: String) {
    /**
     * Specifies the path to the Dockerfile used for building the Docker image.
     *
     * This property can be set to define a custom path to a Dockerfile,
     * which overrides the default behavior of using a `Dockerfile` located in the build context's root directory.
     *
     * If null, the build process assumes the default Dockerfile is used.
     * @since 3.3.0
     */
    var dockerfile: String? = null
    /**
     * Mutable map for storing build-time arguments passed to a Docker build process.
     *
     * This map allows defining key-value pairs that are passed as build arguments
     * during the container image build. It supports adding or modifying entries and
     * is used to configure Docker build settings programmatically.
     *
     * The keys in this map represent the argument names, and the corresponding values
     * represent their associated values. These arguments are commonly leveraged to
     * provide values for ARG instructions within Dockerfiles or dynamically customize
     * build behavior.
     * @since 3.3.0
     */
    val args: StringMMap = emptyMMap()
    /**
     * Specifies the build stage target in a multi-stage Docker build.
     *
     * When set, this property determines which stage of the Dockerfile
     * to use as the build target. This is useful in multi-stage builds
     * to define a specific stage for efficient image creation.
     *
     * If null, the default behavior is to build the final stage in the Dockerfile.
     * @since 3.3.0
     */
    var target: String? = null
    /**
     * Represents a collection of image names used as build cache sources during the Docker build process.
     *
     * The `cacheFrom` list specifies the external images that can be used as sources for the Docker build cache.
     * These images are typically pulled from a registry and are utilized to optimize and speed up the build process by
     * reusing cached layers.
     *
     * This property is mutable and can be updated through the `cacheFrom(vararg images: String)` function.
     * @since 3.3.0
     */
    val cacheFrom: StringMList = emptyMList()

    /**
     * Adds a build-time argument to the Docker build process.
     *
     * This method allows specifying key-value pairs that are passed as `ARG` instructions
     * to the Docker build process. The arguments provided can be used within the Dockerfile.
     *
     * @param key The name of the build argument.
     * @param value The value of the build argument.
     * @since 3.3.0
     */
    fun arg(key: String, value: String) { args[key] = value }
    /**
     * Adds the specified Docker image names to the `cacheFrom` list.
     *
     * This method is used within the DSL to specify images that can be used as a build cache
     * for optimized Docker builds. Each image provided in the parameter will be appended
     * to the existing `cacheFrom` list in the builder.
     *
     * @param images Vararg parameter representing the names of Docker images to be added to the cache.
     * @since 3.3.0
     */
    fun cacheFrom(vararg images: String) { cacheFrom += images }

    /**
     * Builds a `BuildConfig` instance using the current state of the builder.
     *
     * @return A `BuildConfig` object containing the specified context, dockerfile, build arguments, target,
     *         and cache configuration.
     * @since 3.3.0
     */
    fun build() = BuildConfig(context, dockerfile, args.toMap(), target, cacheFrom.toList())
}

/**
 * A DSL builder class for defining and configuring a service within a Docker Compose setup.
 * Allows specification of various service properties such as image, ports, environment variables,
 * volumes, and network configurations.
 *
 * @param name The name of the service being defined.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@DockerComposeDslMarker
class ServiceBuilder(val name: String) {
    /**
     * Specifies the Docker image to be used for creating a container.
     *
     * The `image` property represents the identifier of the container image, which can include
     * the image name, tag, or digest. If not explicitly set, the property defaults to `null`.
     * This property is optional and can be dynamically configured.
     * @since 3.3.0
     */
    var image: String? = null
    /**
     * Holds the configuration details necessary for building the application.
     * The variable is nullable and may not always be initialized.
     * @since 3.3.0
     */
    private var buildConfig: BuildConfig? = null
    /**
     * Specifies the command to be executed inside the container.
     *
     * The `command` variable represents a custom runtime command for overriding
     * the default command defined within the container's image. If set, this
     * command will be used when the container starts.
     *
     * Setting this variable to null uses the default command from the image.
     * @since 3.3.0
     */
    var command: StringMList = emptyMList()
    /**
     * Specifies the custom entry point for the service container.
     *
     * This variable defines the command that will be executed as the container's
     * entry point. If set, it overrides the default entry point defined in the
     * container's image. It can be used to customize the behavior of the container
     * at startup.
     *
     * When uninitialized or set to null, the image's default entry point is used.
     * @since 3.3.0
     */
    var entrypoint: String? = null
    /**
     * Specifies a custom name for the container.
     *
     * This property allows you to define a specific name for the container
     * instead of relying on the default name generated by the system.
     * Useful for identifying or managing containers in a clear and
     * organized manner, especially in complex setups with multiple containers.
     * @since 3.3.0
     */
    var containerName: String? = null
    /**
     * Configures the restart policy for the service.
     *
     * This property determines how the service container should be restarted
     * in various scenarios, such as failure or manual intervention.
     *
     * Acceptable values are defined in the `RestartPolicy` enum:
     * - `NO`: The container will not restart automatically.
     * - `ALWAYS`: The container will restart unconditionally.
     * - `ON_FAILURE`: The container will restart only when it exits with a non-zero status.
     * - `UNLESS_STOPPED`: The container will restart unless explicitly stopped.
     *
     * If not specified, the default value is `null`, meaning no specific restart policy is applied.
     * @since 3.3.0
     */
    var restart: RestartPolicy? = null
    /**
     * Configures the working directory for the service container.
     *
     * This property specifies the directory in the container's filesystem
     * where the service will operate. Setting this value ensures that commands
     * executed in the container will use the specified directory as the current working directory.
     *
     * Use `null` to indicate that the working directory is not explicitly set.
     * @since 3.3.0
     */
    var workingDir: String? = null
    /**
     * Specifies the username or user ID to use within the container runtime.
     *
     * This variable allows configuring the user context under which the container
     * processes will execute. It can be set to a username, a UID, or left as null,
     * in which case the container defaults to its pre-configured user settings.
     *
     * A user can be specified via the `user` function in the `ServiceBuilder` class.
     * @since 3.3.0
     */
    var user: String? = null
    /**
     * Specifies whether the container should run with privileged mode enabled.
     *
     * When `true`, the container is granted elevated privileges, allowing it to perform
     * operations that are restricted in unprivileged mode, such as accessing all devices
     * on the host or modifying certain kernel parameters.
     *
     * Defaults to `false`.
     * @since 3.3.0
     */
    var privileged: Boolean = false
    /**
     * Specifies whether the container is set to be read-only.
     *
     * When enabled, the container's filesystem will be mounted as read-only,
     * preventing modifications to the filesystem at runtime. This is commonly
     * used to enhance security by preventing unintended or unauthorized changes
     * to the container's data.
     * @since 3.3.0
     */
    var readOnly: Boolean = false
    /**
     * Indicates whether the STDIN (standard input) stream for the container should remain open.
     *
     * When set to `true`, the container's standard input remains open, even if no data is being sent to it.
     * This is useful for scenarios where interactive input might be required or when using containers
     * that expect input from the user or another process.
     *
     * Default value: `false`
     * @since 3.3.0
     */
    var stdinOpen: Boolean = false
    /**
     * Indicates whether a pseudo-TTY (teletypewriter) is allocated for the container.
     *
     * When set to `true`, a TTY will be created and attached to the container,
     * allowing for interactive terminal interaction. This is commonly useful
     * when running containers that require direct user input or terminal-based
     * applications.
     *
     * Default value is `false`.
     * @since 3.3.0
     */
    var tty: Boolean = false

    /**
     * Holds the list of port mappings for the service.
     *
     * This mutable list stores port configurations that define how container ports
     * are mapped to host ports. Ports are specified as strings in the format
     * `hostPort:containerPort`. For example, `8080:80` maps port 8080 on the host
     * to port 80 in the container.
     *
     * The `ports` list is used in service configuration, allowing users to specify
     * multiple port mappings for a container. New port mappings can be added
     * dynamically through methods such as `ports` and `port`.
     * @since 3.3.0
     */
    private val ports: StringMList = emptyMList()
    /**
     * A list of container ports to be exposed by the service.
     *
     * This property holds the ports on the container that will be made accessible
     * when the service is created. Ports added to this list will be exposed but not
     * bound to any specific host port.
     *
     * The values within the list are represented as strings and typically specify
     * container port numbers (e.g., "8080", "80"). This property is populated
     * through the `expose` function.
     * @since 3.3.0
     */
    private val expose: StringMList = emptyMList()
    /**
     * A private variable that represents the environment configuration.
     *
     * The `environment` map holds string-based key-value pairs, which can be used
     * to store and retrieve environment-specific settings or configurations.
     * It is initialized as an empty map using `emptyMMap()`.
     * @since 3.3.0
     */
    private val environment: StringMMap = emptyMMap()
    /**
     * A mutable list of environment file paths used for configuring the service's runtime environment.
     *
     * Each entry in this list represents the path to a file containing environment variables.
     * These environment variables are applied to the service container during the service startup.
     *
     * This property is initialized as an empty mutable list and can be updated using the `envFile` function.
     * The contents of this list are incorporated when building the service configuration.
     * @since 3.3.0
     */
    private val envFiles: StringMList = emptyMList()
    /**
     * A mutable list that holds volume mappings for the service configuration.
     *
     * This property is used to define and manage the volume bindings for containers
     * in the context of a service. Each volume mapping represents a binding from
     * the host to the container or other types of volume associations.
     *
     * Examples of usage include adding a single volume, mounting named volumes,
     * and managing multiple volume mappings for the service.
     * @since 3.3.0
     */
    private val volumeMappings: StringMList = emptyMList()
    /**
     * Represents a mapping of dependent services and their associated conditions required for starting these services.
     *
     * The `dependsOn` property is a mutable map where the key is the name of a dependent service,
     * and the value is a `DependsOnCondition`, specifying the condition under which the service should be started.
     *
     * This property is primarily used to define service dependencies in the context of
     * constructing a configuration for a service, ensuring that specific services are started successfully
     * or meet defined criteria before the current service is launched.
     *
     * By default, this map is initialized as empty, meaning there are no service dependencies initially.
     * @since 3.3.0
     */
    private val dependsOn = emptyMMap<String, DependsOnCondition>()
    /**
     * Stores the health check configuration for the service being built.
     *
     * This variable holds an instance of [Healthcheck] that defines the parameters
     * for verifying the health status of a container, such as test commands, retry policies,
     * time intervals, and timeouts.
     *
     * It can be configured using the `healthcheck` method in the `ServiceBuilder` DSL. When not set,
     * the health check configuration will not be included in the service definition.
     * @since 3.3.0
     */
    private var healthcheck: Healthcheck? = null
    /**
     * Represents a mutable list of network names associated with the service.
     *
     * The `networksList` is used to store the names of networks that the service is
     * connected to. This property can be modified by adding network names when configuring
     * the service. It defaults to an empty mutable list.
     * @since 3.3.0
     */
    private val networksList: StringMList = emptyMList()
    /**
     * A mutable map representing the labels associated with a service.
     *
     * Labels are key-value pairs used to add metadata to the service definition,
     * which can be used for organization, filtering, or other operational purposes.
     *
     * This map is initialized as empty and can be populated or modified
     * using relevant methods such as `label` and `labels` in the containing class.
     * @since 3.3.0
     */
    private val labels: StringMMap = emptyMMap()
    /**
     * Configuration for deployment settings of the service.
     *
     * This property holds an optional `DeployConfig` that encapsulates
     * deployment-specific configurations such as replicas, resource constraints,
     * and restart policies. It can be set using the `deploy` function.
     * @since 3.3.0
     */
    private var deploy: DeployConfig? = null
    /**
     * Stores a list of additional host-to-IP address mappings for the service.
     *
     * This property is used to configure extra host entries in the container's
     * `/etc/hosts` file, allowing services to resolve custom hostnames to specific
     * IP addresses.
     *
     * The entries are typically added via the `extraHost(host, ip)` method, where
     * each host and IP address pair is formatted as `<hostname>:<ip>`.
     * @since 3.3.0
     */
    private val extraHosts: StringMList = emptyMList()
    /**
     * Configuration for logging options used in the `ServiceBuilder`.
     *
     * This property holds an optional `LoggingConfig` instance that defines
     * the logging driver and associated options for a service.
     *
     * It can be set using the `logging` builder method, allowing the customization
     * of logging behavior for services such as defining the log driver (e.g., "json-file")
     * and driver-specific options.
     * @since 3.3.0
     */
    private var logging: LoggingConfig? = null
    /**
     * Represents a mutable list of secret names associated with the service configuration.
     *
     * This list is used for defining secrets that the service needs access to.
     * New secrets can be added to this list via the `secrets` method, and its
     * contents are included when building the final service configuration.
     * @since 3.3.0
     */
    private val secretsList: StringMList = emptyMList()
    /**
     * A mutable list of profiles initialized as empty.
     * The list is intended to hold profile-related data represented as strings.
     * @since 3.3.0
     */
    private val profiles: StringMList = emptyMList()
    /**
     * Represents a mutable list of paths for `tmpfs` mounts in the service configuration.
     *
     * `tmpfs` mounts are used to store data temporarily in memory rather than writing it to a filesystem.
     * This property holds all `tmpfs` paths configured for the service.
     * The list can be modified by invoking the `tmpfs(vararg paths: String)` function.
     * @since 3.3.0
     */
    private val tmpfs: StringMList = emptyMList()
    /**
     * Maintains a mapping of ulimit configurations for the service.
     *
     * This property holds an immutable map of string keys to corresponding `UlimitConfig` values,
     * where each key represents the name of a specific ulimit
     * (e.g., "nofile" for open files or "nproc" for processes),
     * and the value specifies the soft and hard limits through the `UlimitConfig` data structure.
     *
     * The property is initialized using the `emptyMMap` utility, ensuring it begins as an empty,
     * type-safe mutable map. This allows for dynamic addition of ulimit configurations as needed.
     *
     * The ulimits can define resource restrictions or quotas for the container's runtime.
     * @since 3.3.0
     */
    private val ulimits: MMap<String, UlimitConfig> = emptyMMap()
    /**
     * Represents a mutable map of sysctl (system control) configurations for a container.
     *
     * This property is used to store and manage system-level kernel parameter configurations
     * that can be set within a container's runtime environment. Sysctl settings allow fine-tuned
     * control over various kernel parameters, such as networking or system performance configurations.
     *
     * Keys represent the specific sysctl parameter names, and their corresponding values represent
     * the configured values for those parameters.
     *
     * By default, this map is initialized as empty.
     * @since 3.3.0
     */
    private val sysctls: StringMMap = emptyMMap()
    /**
     * Represents a mutable list of capability additions for a specific context where
     * additional permissions or capabilities may be required. The list is initialized
     * as empty and can be modified as needed.
     * @since 3.3.0
     */
    private val capAdd: StringMList = emptyMList()
    /**
     * Holds a list of Linux capabilities to be dropped from the container.
     *
     * This property is initialized as an empty mutable list and can be modified
     * to include specific capabilities that should be explicitly removed from the container.
     * Modifying this list alters the capabilities configuration of the container
     * when it is built using the `ServiceBuilder`.
     * @since 3.3.0
     */
    private val capDrop: StringMList = emptyMList()
    /**
     * Builds the configuration using the provided context and configuration block.
     *
     * @param context The context to initialize the configuration, default is `.`.
     * @param block A lambda to configure the `BuildConfigBuilder`.
     * @since 3.3.0
     */
    fun build(context: String = String.DOT, block: ReceiverConsumer<BuildConfigBuilder> = {}) {
        buildConfig = BuildConfigBuilder(context).apply(block).build()
    }

    /**
     * Adds the provided commands to the list of existing commands.
     *
     * @param command A vararg parameter that takes one or more strings representing the commands to be added.
     * @since 3.3.0
     */
    fun commands(vararg command: String) {
        this.command += command
    }
    /**
     * Configures port mappings for the service by adding one or more host-to-container port mappings.
     *
     * @param mappings One or more strings that represent port mappings in the format "hostPort:containerPort"
     * or "hostPort:containerPort/protocol". For example, "8080:80" or "8080:80/tcp".
     * @since 3.3.0
     */
    fun ports(vararg mappings: String) { ports += mappings }
    /**
     * Maps a port on the host to a port in the container.
     *
     * @param host The port number on the host machine.
     * @param container The port number in the container.
     * @since 3.3.0
     */
    fun port(host: Int, container: Int) { ports += "$host:$container" }
    /**
     * Exposes the specified container ports.
     *
     * @param containerPorts A variable number of strings representing the ports to expose.
     * @since 3.3.0
     */
    fun expose(vararg containerPorts: String) { expose += containerPorts }

    /**
     * Adds multiple key-value pairs to the environment configuration.
     *
     * @param pairs Vararg parameter representing key-value pairs of type String2
     *              to be added to the environment.
     * @since 3.3.0
     */
    fun environment(vararg pairs: String2) { environment += pairs }
    /**
     * Adds or updates an environment variable for the service with the specified key and value.
     *
     * @param key The name of the environment variable to be added or updated.
     * @param value The value to assign to the environment variable.
     * @since 3.3.0
     */
    fun env(key: String, value: String) { environment[key] = value }
    /**
     * Adds one or more file paths to the list of environment files used by the service.
     *
     * This method allows specifying external files that define environment variables
     * for the service. Each provided file should be a valid path to a text file
     * containing key-value pairs representing environment variables.
     *
     * @param files One or more file paths representing environment files.
     * @since 3.3.0
     */
    fun envFile(vararg files: String) { envFiles += files }

    /**
     * Adds a volume mapping to the service configuration.
     *
     * @param mapping A string representing a volume mapping in the format "hostPath:containerPath".
     * @since 3.3.0
     */
    fun volume(mapping: String) { volumeMappings += mapping }
    /**
     * Maps a named volume to a specific mount path within the container.
     *
     * @param named The name of the volume to be used for mapping.
     * @param mountPath The path inside the container where the volume will be mounted.
     * @since 3.3.0
     */
    fun volume(named: String, mountPath: String) { volumeMappings += "$named:$mountPath" }
    /**
     * Adds the specified volume mappings to the existing list of volume mappings.
     *
     * @param mappings A variable number of volume mapping strings to be added.
     */
    fun volumes(vararg mappings: String) { volumeMappings += mappings }
    /**
     * Configures one or more temporary file systems (tmpfs) for the container.
     * This method maps the specified paths to temporary file systems.
     *
     * @param paths Paths to be mounted as temporary file systems inside the container.
     * @since 3.3.0
     */
    fun tmpfs(vararg paths: String) { tmpfs += paths }

    /**
     * Specifies the services that this service depends on.
     * Each dependent service is assigned a condition that it must meet,
     * in this case, the condition is `SERVICE_STARTED`.
     *
     * @param services The names of the services that this service depends on.
     * @since 3.3.0
     */
    fun dependsOn(vararg services: String) {
        services.forEach { dependsOn[it] = DependsOnCondition.SERVICE_STARTED }
    }
    /**
     * Specifies that the current service depends on another service with an optional condition.
     * This indicates the service dependency and ensures the specified service is managed
     * to meet the assigned condition before proceeding.
     *
     * @param service The name of the service that the current service depends on.
     * @param condition The condition that needs to be met for the dependency.
     *                  It can be one of the predefined conditions in `DependsOnCondition`.
     * @since 3.3.0
     */
    fun dependsOn(service: String, condition: DependsOnCondition) {
        dependsOn[service] = condition
    }

    /**
     * Configures the healthcheck settings for a service.
     *
     * This method allows you to define healthcheck properties such as the command to run,
     * interval, timeout, retries, and start period using a DSL-style builder.
     * The defined healthcheck settings are used to monitor the service's health.
     *
     * @param block The configuration block for setting up the healthcheck using a
     *              [HealthcheckBuilder]. Use this block to define healthcheck parameters.
     * @since 3.3.0
     */
    fun healthcheck(block: HealthcheckBuilder.() -> Unit) {
        healthcheck = HealthcheckBuilder().apply(block).build()
    }

    /**
     * Defines the networks that this service should be connected to.
     * Updates the `networksList` field by adding the specified networks.
     *
     * @param nets The names of networks to associate with this service. Can accept multiple network names as vararg arguments.
     * @since 3.3.0
     */
    fun networks(vararg nets: String) { networksList += nets }
    /**
     * Adds the given pairs of labels to the existing collection.
     *
     * @param pairs A variable number of String2 pairs to be added to the labels.
     * @since 3.3.0
     */
    fun labels(vararg pairs: String2) { labels += pairs }
    /**
     * Adds or updates a label for the current service configuration.
     *
     * Labels are used to apply metadata to the service, which can be used by orchestration
     * tools or for organizational purposes.
     *
     * @param key The key of the label to add or update.
     * @param value The value associated with the label key.
     * @since 3.3.0
     */
    fun label(key: String, value: String) { labels[key] = value }

    /**
     * Configures the deployment settings for the service.
     *
     * This function allows you to specify deployment-related configurations such as
     * the number of replicas, resource limits, reservations, and restart policies.
     *
     * @param block A lambda with a receiver of type `DeployBuilder`. Use this
     *              lambda to set deployment configurations.
     * @since 3.3.0
     */
    fun deploy(block: DeployBuilder.() -> Unit) {
        deploy = DeployBuilder().apply(block).build()
    }

    /**
     * Adds a mapping of a hostname to its corresponding IP address to the extra hosts list.
     *
     * @param host The hostname to be mapped.
     * @param ip The IP address associated with the hostname.
     * @since 3.3.0
     */
    fun extraHost(host: String, ip: String) { extraHosts += "$host:$ip" }

    /**
     * Configures logging settings for a service.
     *
     * This method allows specifying the logging configuration by leveraging a DSL provided
     * by the [LoggingBuilder] class. It enables customization of the logging driver and options
     * such as maximum log size or file count.
     *
     * @param block A lambda with a receiver of type [LoggingBuilder] used to configure the logging settings.
     * @since 3.3.0
     */
    fun logging(block: LoggingBuilder.() -> Unit) {
        logging = LoggingBuilder().apply(block).build()
    }

    /**
     * Adds one or more secret names to the service configuration.
     * This method appends the provided secret names to the current list of secrets
     * associated with the service being defined.
     *
     * @param names The names of the secrets to add. Each name represents a secret that will be
     *              included in the service configuration.
     * @since 3.3.0
     */
    fun secrets(vararg names: String) { secretsList += names }
    /**
     * Adds the provided profile names to the existing profiles collection.
     *
     * @param names A variable number of profile names to be added.
     * @since 3.3.0
     */
    fun profiles(vararg names: String) { profiles += names }

    /**
     * Sets the ulimit configuration for a specified resource.
     *
     * @param name The name of the resource for which the limits are being set.
     * @param soft The soft limit for the resource, which is the value that the operating system enforces.
     * @param hard The hard limit for the resource, which acts as an upper bound for the soft limit.
     * @since 3.3.0
     */
    fun ulimit(name: String, soft: Long, hard: Long) { ulimits[name] = UlimitConfig(soft, hard) }
    /**
     * Adds or updates a sysctl key-value pair in the service configuration.
     *
     * Sysctl settings are used to specify kernel parameters at runtime, providing
     * control over various aspects of the operating system's behavior.
     *
     * @param key The name of the sysctl setting to be added or updated.
     * @param value The value to associate with the specified sysctl key.
     * @since 3.3.0
     */
    fun sysctl(key: String, value: String) { sysctls[key] = value }
    /**
     * Adds one or more Linux capabilities to the container.
     *
     * Linux capabilities allow fine-grained control over specific privileged operations.
     *
     * @param caps The capabilities to be added to the container. Each capability should be specified as a string.
     * @since 3.3.0
     */
    fun capAdd(vararg caps: String) { capAdd += caps }
    /**
     * Drops the specified capabilities by adding them to the list of capabilities to be dropped.
     *
     * @param caps A vararg parameter representing the names of the capabilities to be dropped.
     * @since 3.3.0
     */
    fun capDrop(vararg caps: String) { capDrop += caps }

    /**
     * Constructs and returns a new instance of the Service class with the provided configuration parameters.
     *
     * This method aggregates various settings necessary to define a service and initializes it with
     * the specified attributes such as image, build configuration, commands, environment variables,
     * volume mappings, network settings, and several others to produce a fully configured service instance.
     *
     * @return A fully initialized Service instance with the specified configuration.
     * @since 3.3.0
     */
    fun build() = Service(
        image, buildConfig, command, entrypoint, containerName, restart,
        ports.toList(), expose.toList(), environment.toMap(), envFiles.toList(),
        volumeMappings.toList(), dependsOn.toMap(), healthcheck,
        networksList.toList(), labels.toMap(), deploy, workingDir, user,
        extraHosts.toList(), logging, secretsList.toList(), profiles.toList(),
        tmpfs.toList(), ulimits.toMap(), sysctls.toMap(),
        capAdd.toList(), capDrop.toList(), privileged, readOnly, stdinOpen, tty,
    )
}

/**
 * A DSL builder for configuring volume properties in a Docker Compose setup.
 *
 * The `VolumeBuilder` class provides functionality to define and manage the configuration
 * of Docker volumes, including options for volume drivers, driver-specific parameters,
 * external volume management, and naming.
 *
 * Use this builder in the context of a Docker Compose configuration to customize
 * volume settings as per your requirements. Instances of this builder are typically
 * configured using the `volume` function of a higher-level builder.
 *
 * @property name The name of the volume being configured.
 * @constructor Creates a new instance of the `VolumeBuilder` with the specified volume name.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@DockerComposeDslMarker
class VolumeBuilder(val name: String) {
    /**
     * Specifies the driver for the Docker volume configuration.
     *
     * This property determines the underlying volume driver that will be used to manage the volume
     * in the corresponding Docker Compose configuration. If not explicitly set, the default volume
     * driver provided by Docker will be used. The driver defines how and where the volume data is stored.
     *
     * A value of `null` indicates that no specific driver has been configured.
     * @since 3.3.0
     */
    var driver: String? = null
    /**
     * A map of driver-specific options associated with a volume in a Docker Compose configuration.
     *
     * This property stores key-value pairs that define specific parameters or settings for the volume's driver.
     * It is used to customize the behavior of the volume driver based on the provided options.
     *
     * The map is mutable and allows adding or modifying driver options dynamically via the `driverOpt` function.
     *
     * @since 3.3.0
     */
    val driverOpts: StringMMap = emptyMMap()
    /**
     * Indicates whether the volume is managed externally outside of the Docker Compose setup.
     *
     * When set to `true`, the volume is considered to be external, and Docker Compose will not attempt
     * to create or manage it. This is useful for referencing pre-existing volumes managed by the Docker
     * host or an external storage system.
     *
     * Defaults to `false`, meaning the volume is created and managed by Docker Compose.
     * @since 3.3.0
     */
    var external: Boolean = false
    /**
     * The name of the volume to be used in the Docker Compose configuration.
     *
     * This property specifies a custom name for the Docker volume. If left as `null`,
     * Docker will assign a default name to the volume when the configuration is applied.
     * It is optional and can be set based on the desired naming convention.
     * @since 3.3.0
     */
    var volumeName: String? = null

    /**
     * Adds a driver-specific option to the volume configuration.
     *
     * This method allows specifying custom options for the volume driver by
     * associating a key with a value. These options are passed to the driver
     * during the volume creation process.
     *
     * @param key The option key to be used for the driver-specific configuration.
     * @param value The value corresponding to the specified option key.
     * @since 3.3.0
     */
    fun driverOpt(key: String, value: String) { driverOpts[key] = value }

    /**
     * Builds and returns a `VolumeConfig` instance using the current state of the `VolumeBuilder`.
     *
     * This method consolidates the properties defined in the `VolumeBuilder` object, such as
     * `driver`, `driverOpts`, `external`, and `volumeName`, into a `VolumeConfig` object.
     *
     * @return A `VolumeConfig` instance containing the configured values for the volume.
     * @since 3.3.0
     */
    fun build() = VolumeConfig(driver, driverOpts.toMap(), external, volumeName)
}

/**
 * A builder class for defining and constructing a network configuration in the context of Docker Compose.
 *
 * This class is part of a DSL for specifying the properties and behavior of a network, including driver,
 * external settings, custom naming, and IPAM (IP Address Management) settings. The resulting configuration
 * can be used for defining the networking setup in a Docker Compose file.
 *
 * @constructor Creates a NetworkBuilder instance with the specified network name.
 * @param name The default name for the network being configured.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@DockerComposeDslMarker
class NetworkBuilder(val name: String) {
    /**
     * Defines the driver plugin to use for the network configuration.
     *
     * The driver determines how the network operates and what features it supports.
     * For example, commonly used network drivers include "bridge", "host", or "overlay".
     * If not specified, the default driver will be used.
     *
     * This property can be set directly or through the `driver` function within the DSL context.
     * @since 3.3.0
     */
    var driver: String? = null
    /**
     * Indicates whether the network is an externally defined network.
     *
     * When set to `true`, the network is managed externally and will not be created
     * or modified by the current configuration. This can be useful for linking to
     * pre-existing networks outside the scope of the Docker Compose configuration.
     * @since 3.3.0
     */
    var external: Boolean = false
    /**
     * Specifies the name of the network to be created or used.
     *
     * If set, this value overrides the default name generated for the network.
     * It can be used to reference an existing network when the `external` property is set to `true`.
     * @since 3.3.0
     */
    var networkName: String? = null
    /**
     * Represents the IP Address Management (IPAM) configuration for a network.
     *
     * This variable holds an optional `IpamConfig` instance that specifies the
     * configuration for IP allocation, including the driver and any custom
     * IPAM pools defined for the network. It is configured via the `ipam`
     * function of the `NetworkBuilder` class.
     * @since 3.3.0
     */
    private var ipam: IpamConfig? = null

    /**
     * Configures the IP Address Management (IPAM) settings for the network being built.
     * This method allows customization of IPAM settings such as driver and IP allocation pools.
     *
     * @param block A lambda with receiver of type `IpamBuilder` used to define IPAM configuration.
     * @since 3.3.0
     */
    fun ipam(block: ReceiverConsumer<IpamBuilder>) {
        ipam = IpamBuilder().apply(block).build()
    }

    /**
     * Builds and returns a `NetworkConfig` instance based on the current state of the `NetworkBuilder`.
     *
     * This method consolidates the configuration properties such as `driver`, `external`,
     * `networkName`, and `ipam` defined within the builder and creates an immutable
     * `NetworkConfig` instance to be used by the Docker Compose system. The returned
     * `NetworkConfig` represents the fully configured network definition.
     *
     * @return A `NetworkConfig` object containing the constructed network configuration.
     * @since 3.3.0
     */
    fun build() = NetworkConfig(driver, external, networkName, ipam)
}

/**
 * A builder class for defining and constructing IP Address Management (IPAM) configurations
 * to be used in the context of Docker Compose networks.
 *
 * This class allows specifying the IPAM driver and a set of IP address pools using a DSL-like structure.
 *
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@DockerComposeDslMarker
class IpamBuilder {
    /**
     * Specifies the IPAM (IP Address Management) driver to be used.
     *
     * The driver determines the mechanism used for IP address allocation within a Docker
     * network configuration. If not explicitly set, the default IPAM driver will be used.
     *
     * Setting a custom driver allows for advanced network configurations or integration
     * with external network plugins.
     *
     * This property is optional.
     * @since 3.3.0
     */
    var driver: String? = null
    /**
     * A mutable list that holds `IpamPool` instances, representing the IP address pools
     * that are associated with the IPAM configuration.
     *
     * This list is used to define subnet and gateway pairs that are part of the
     * network IP range for Docker Compose configurations.
     *
     * Items can be added to this list using the `subnet` function in the `IpamBuilder` class.
     * When `build` is called on the associated `IpamBuilder` instance, the contents of
     * this list are used to create an `IpamConfig` object.
     * @since 3.3.0
     */
    val pools = emptyMList<IpamPool>()

    /**
     * Adds a subnet configuration to the IPAM (IP Address Management) pools.
     *
     * @param subnet The CIDR notation of the subnet to be added.
     * @param gateway Optional. The gateway IP address associated with the subnet.
     * @since 3.3.0
     */
    fun subnet(subnet: String, gateway: String? = null) {
        pools += IpamPool(subnet, gateway)
    }

    /**
     * Constructs and returns an `IpamConfig` object using the current state of the builder.
     *
     * This method finalizes the IPAM configuration by combining the driver and IPAM pools
     * defined in the builder into an immutable `IpamConfig` instance. The resulting configuration
     * can then be applied to a network in a Docker Compose setup.
     *
     * @return An `IpamConfig` instance containing the configured IPAM driver and pools.
     * @since 3.3.0
     */
    fun build() = IpamConfig(driver, pools.toList())
}

/**
 * A builder class for constructing a secret configuration in a Docker Compose file.
 *
 * This class allows the configuration of Docker secrets using DSL-style syntax.
 * Secrets can be defined with either a file path or marked as external.
 *
 * @constructor Creates a `SecretBuilder` with the specified name for the secret.
 *
 * @property file An optional file path to use for the secret content.
 * @property external A flag indicating whether the secret is external or not.
 * @property name The name of the secret.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@DockerComposeDslMarker
class SecretBuilder(val name: String) {
    /**
     * Represents a file path or name as a nullable string.
     * This variable can hold the name or path of a file, or remain null if no file is specified.
     * @since 3.3.0
     */
    var file: String? = null
    /**
     * Indicates whether the secret is managed externally.
     *
     * When set to `true`, the secret is expected to already exist externally and will not be created
     * or modified by the current configuration.
     * @since 3.3.0
     */
    var external: Boolean = false

    /**
     * Builds a `SecretConfig` instance using the current properties of the `SecretBuilder`.
     *
     * This method finalizes the configuration of a Docker secret by creating a new `SecretConfig`
     * object from the properties `file` and `external`. The resulting configuration can be used
     * in a Docker Compose setup to define a secret.
     *
     * @return A `SecretConfig` object containing the secret's configuration details.
     * @since 3.3.0
     */
    fun build() = SecretConfig(file, external)
}

// --- TOP-LEVEL COMPOSE BUILDER ---

/**
 * Builder class for defining and constructing a Docker Compose configuration file.
 *
 * This class provides a DSL-like API to configure various components of a Docker Compose file,
 * including services, volumes, networks, and secrets. It also supports specifying the version
 * of the Docker Compose file.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@DockerComposeDslMarker
class DockerComposeBuilder {
    /**
     * Represents the version information as a nullable string.
     *
     * This variable is used to store version details, which can be null if the
     * version is not defined or unavailable. Typically, it might represent
     * the version of an application, library, or module.
     * @since 3.3.0
     */
    var version: String? = null
    /**
     * A mutable map representing the collection of services configured for the Docker Compose setup.
     *
     * This map holds service definitions where each key is a `String` representing the service name,
     * and the value is a `ServiceBuilder` which provides a configurable builder for defining
     * the properties and settings of the associated service.
     *
     * Used internally by the `DockerComposeBuilder` to store and manage service configurations before
     * generating the final Compose file.
     * @since 3.3.0
     */
    private val services = emptyMMap<String, ServiceBuilder>()
    /**
     * A mutable map storing volume configurations used in the Docker Compose setup.
     *
     * Each entry in this map associates a volume name (as a `String`) with an optional
     * `VolumeBuilder` instance. The `VolumeBuilder` is used to define and customize
     * the configuration of the corresponding volume, including options like driver,
     * driver-specific parameters, external volume management, and naming.
     *
     * The map is populated using the `volume` function, where volumes can be registered
     * and configured. When building a `ComposeFile`, the contents of this map are transformed
     * into the final volume definitions.
     * @since 3.3.0
     */
    private val volumes = emptyMMap<String, VolumeBuilder?>()
    /**
     * A mutable map used to store network configurations.
     *
     * The keys are strings representing network identifiers or names,
     * while the values are instances of `NetworkBuilder` or null.
     *
     * This map allows the creation, modification, and lookup of
     * network-related objects during application runtime.
     * @since 3.3.0
     */
    private val networks = emptyMMap<String, NetworkBuilder?>()
    /**
     * A mutable map that stores secret definitions within the Docker Compose DSL.
     *
     * This property is used to define and manage secrets within a `DockerComposeBuilder`.
     * Each entry in the map associates a secret name (key) with its configuration (value).
     * Secrets can be configured using the `secret` function, and their definitions
     * will be included in the final `ComposeFile` generated by the `build` function.
     * @since 3.3.0
     */
    private val secrets = emptyMMap<String, SecretBuilder>()

    /**
     * Sets the version of the object.
     *
     * @param v The version string to be set.
     * @since 3.3.0
     */
    fun version(v: String) { version = v }

    /**
     * Configures and registers a service with the specified name.
     *
     * @param name The name of the service to be registered.
     * @param block A lambda with a receiver of type ServiceBuilder, used to configure the service.
     * @since 3.3.0
     */
    fun service(name: String, block: ReceiverConsumer<ServiceBuilder>) {
        services[name] = ServiceBuilder(name).apply(block)
    }

    /**
     * Configures and registers a volume with the specified name in the Docker Compose setup.
     *
     * This method allows defining a volume by its name and optionally applying further configuration using
     * a `VolumeBuilder`. The provided lambda `block` receives an instance of `VolumeBuilder` as its receiver,
     * enabling fluent configuration of the volume's properties.
     *
     * @param name The name of the volume to be created or configured.
     * @param block A lambda with a receiver of type `VolumeBuilder`, used to define volume-specific options. Optional and may be null.
     * @since 3.3.0
     */
    fun volume(name: String, block: ReceiverConsumer<VolumeBuilder>? = null) {
        volumes[name] = block?.let { VolumeBuilder(name).apply(it) }
    }

    /**
     * Configures a network with the given name and applies provided settings using a builder.
     *
     * @param name The name of the network to be configured.
     * @param block A lambda with a receiver of type `NetworkBuilder` to configure the network. Optional and may be null.
     * @since 3.3.0
     */
    fun network(name: String, block: ReceiverConsumer<NetworkBuilder>? = null) {
        networks[name] = block?.let { NetworkBuilder(name).apply(it) }
    }

    /**
     * Configures and registers a secret with the specified name in the Docker Compose setup.
     *
     * This method allows defining a secret using its name and applying further configuration
     * using a `SecretBuilder`. The provided lambda `block` receives an instance of `SecretBuilder`
     * as its receiver, enabling DSL-style configuration of the secret's properties.
     *
     * @param name The name of the secret to be created or configured.
     * @param block A lambda with a receiver of type `SecretBuilder`, used to define secret-specific options.
     */
    fun secret(name: String, block: ReceiverConsumer<SecretBuilder>) {
        secrets[name] = SecretBuilder(name).apply(block)
    }

    /**
     * Constructs a `ComposeFile` instance with the provided configuration data.
     *
     * The method creates and returns a `ComposeFile` object by aggregating
     * and transforming the internal properties such as `version`, `services`,
     * `volumes`, `networks`, and `secrets`. Each property is processed and built
     * through their respective `.build()` methods, where applicable.
     *
     * @return A fully constructed `ComposeFile` object with all components initialized.
     * @since 3.3.0
     */
    fun build() = ComposeFile(
        version,
        services.mapValues { it.value.build() },
        volumes.mapValues { it.value?.build() },
        networks.mapValues { it.value?.build() },
        secrets.mapValues { it.value.build() },
    )
}

/**
 * Configures and builds a Docker Compose file using the provided DSL block.
 *
 * @param block A lambda that configures the DockerComposeBuilder instance.
 * @return A ComposeFile object that represents the constructed Docker Compose configuration.
 * @since 3.3.0
 * @author Tommaso Pastorelli
 */
@Beta
fun buildDockerCompose(block: ReceiverConsumer<DockerComposeBuilder>): ComposeFile =
    DockerComposeBuilder().apply(block).build()

// --- YAML RENDER ---

/**
 * Converts the current `ComposeFile` instance into its YAML representation.
 *
 * This method generates a textual representation of the `ComposeFile` object in YAML format,
 * adhering to the structure commonly used for Docker Compose configuration files. The YAML
 * output includes sections such as `version`, `services`, `volumes`, `networks`, and `secrets`.
 *
 * ### Processed Sections:
 * - **`version`**: If a version is defined, it will be included as the top-level property.
 * - **`services`**: Outputs all services defined in the `services` map, including their respective
 *   configurations. Each service is rendered with its name and related properties.
 * - **`volumes`**: If any volumes are defined, the method renders them along with their respective
 *   configurations, including options for drivers, external status, and driver-specific options.
 * - **`networks`**: Networks will be rendered with details such as driver type, external status, and IPAM
 *   configurations, including subnet and gateway information if applicable.
 * - **`secrets`**: Renders a `secrets` section detailing the file path or external flag for each secret
 *   defined.
 *
 * This method takes into account null or empty values for properties, ensuring that only relevant
 * and necessary configuration details are included in the resulting YAML.
 *
 * **Annotations:**
 * - The method is marked with `@OptIn(Beta::class)`, suggesting that it relies on experimental or
 *   beta features which may change over time.
 * @since 3.3.0
 */
@OptIn(Beta::class)
fun ComposeFile.toYaml() = Yaml(buildString {
    version?.let { appendLine("version: \"$it\"") ; appendLine() }

    if (services.isNotEmpty()) {
        appendLine("services:")
        services.forEach { [name, svc] -> renderService(name, svc) }
    }

    if (volumes.isNotEmpty()) {
        appendLine()
        appendLine("volumes:")
        volumes.forEach { [name, cfg] ->
            if (cfg.isNull() || (cfg.driver.isNull() && !cfg.external && cfg.driverOpts.isEmpty())) {
                appendLine("  $name:")
            } else {
                appendLine("  $name:")
                cfg.driver?.let { appendLine("    driver: $it") }
                if (cfg.external) appendLine("    external: true")
                cfg.name?.let { appendLine("    name: $it") }
                if (cfg.driverOpts.isNotEmpty()) {
                    appendLine("    driver_opts:")
                    cfg.driverOpts.forEach { [k, v] -> appendLine("      $k: \"$v\"") }
                }
            }
        }
    }

    if (networks.isNotEmpty()) {
        appendLine()
        appendLine("networks:")
        networks.forEach { [name, cfg] ->
            if (cfg.isNull()) {
                appendLine("  $name:")
            } else {
                appendLine("  $name:")
                cfg.driver?.let { appendLine("    driver: $it") }
                if (cfg.external) appendLine("    external: true")
                cfg.name?.let { appendLine("    name: $it") }
                cfg.ipam?.let { ipam ->
                    appendLine("    ipam:")
                    ipam.driver?.let { appendLine("      driver: $it") }
                    if (ipam.config.isNotEmpty()) {
                        appendLine("      config:")
                        ipam.config.forEach { pool ->
                            appendLine("        - subnet: ${pool.subnet}")
                            pool.gateway?.let { appendLine("          gateway: $it") }
                        }
                    }
                }
            }
        }
    }

    if (secrets.isNotEmpty()) {
        appendLine()
        appendLine("secrets:")
        secrets.forEach { [name, cfg] ->
            appendLine("  $name:")
            cfg.file?.let { appendLine("    file: $it") }
            if (cfg.external) appendLine("    external: true")
        }
    }
})

/**
 * Renders the representation of a service in a configuration file format.
 *
 * @receiver The StringBuilder used for appending the rendered output.
 * @param name The name of the service to be rendered.
 * @param svc An instance of the Service class containing all the properties of the service.
 * @since 3.3.0
 */
private fun StringBuilder.renderService(name: String, svc: Service) {
    appendLine("  $name:")
    svc.image?.let { appendLine("    image: $it") }

    svc.build?.let { b ->
        if (b.dockerfile.isNull() && b.args.isEmpty() && b.target.isNull() && b.cacheFrom.isEmpty()) {
            appendLine("    build: ${b.context}")
        } else {
            appendLine("    build:")
            appendLine("      context: ${b.context}")
            b.dockerfile?.let { appendLine("      dockerfile: $it") }
            b.target?.let { appendLine("      target: $it") }
            if (b.args.isNotEmpty()) {
                appendLine("      args:")
                b.args.forEach { [k, v] -> appendLine("        $k: \"$v\"") }
            }
            if (b.cacheFrom.isNotEmpty()) {
                appendLine("      cache_from:")
                b.cacheFrom.forEach { appendLine("        - $it") }
            }
        }
    }

    svc.containerName?.let { appendLine("    container_name: $it") }
    svc.command.ifNotEmpty { appendLine("    command: ${let { list -> if (list.isSingleElement) "\"${list.first()}\"" else list.map { "\"$it\"" } }}") }
    svc.entrypoint?.let { appendLine("    entrypoint: $it") }
    svc.restart?.let { appendLine("    restart: ${it.yaml}") }
    svc.workingDir?.let { appendLine("    working_dir: $it") }
    svc.user?.let { appendLine("    user: \"$it\"") }
    if (svc.privileged) appendLine("    privileged: true")
    if (svc.readOnly) appendLine("    read_only: true")
    if (svc.stdinOpen) appendLine("    stdin_open: true")
    if (svc.tty) appendLine("    tty: true")

    if (svc.ports.isNotEmpty()) {
        appendLine("    ports:")
        svc.ports.forEach { appendLine("      - \"$it\"") }
    }
    if (svc.expose.isNotEmpty()) {
        appendLine("    expose:")
        svc.expose.forEach { appendLine("      - \"$it\"") }
    }

    if (svc.environment.isNotEmpty()) {
        appendLine("    environment:")
        svc.environment.forEach { [k, v] -> appendLine("      $k: \"$v\"") }
    }
    if (svc.envFile.isNotEmpty()) {
        appendLine("    env_file:")
        svc.envFile.forEach { appendLine("      - $it") }
    }

    if (svc.volumes.isNotEmpty()) {
        appendLine("    volumes:")
        svc.volumes.forEach { appendLine("      - $it") }
    }
    if (svc.tmpfs.isNotEmpty()) {
        appendLine("    tmpfs:")
        svc.tmpfs.forEach { appendLine("      - $it") }
    }

    if (svc.dependsOn.isNotEmpty()) {
        val allStarted = svc.dependsOn.values.all { it == DependsOnCondition.SERVICE_STARTED }
        if (allStarted) {
            appendLine("    depends_on:")
            svc.dependsOn.keys.forEach { appendLine("      - $it") }
        } else {
            appendLine("    depends_on:")
            svc.dependsOn.forEach { [svcName, cond] ->
                appendLine("      $svcName:")
                appendLine("        condition: ${cond.yaml}")
            }
        }
    }

    svc.healthcheck?.let { hc ->
        appendLine("    healthcheck:")
        appendLine("      test: [${hc.test.joinToString { "\"$it\"" } }]")
        appendLine("      interval: ${hc.interval.toIsoString()}")
        appendLine("      timeout: ${hc.timeout.toIsoString()}")
        appendLine("      retries: ${hc.retries}")
        hc.startPeriod?.let { appendLine("      start_period: ${it.toIsoString()}") }
    }

    if (svc.networks.isNotEmpty()) {
        appendLine("    networks:")
        svc.networks.forEach { appendLine("      - $it") }
    }
    if (svc.labels.isNotEmpty()) {
        appendLine("    labels:")
        svc.labels.forEach { [k, v] -> appendLine("      $k: \"$v\"") }
    }

    svc.deploy?.let { d ->
        appendLine("    deploy:")
        d.replicas?.let { appendLine("      replicas: $it") }
        d.resources?.let { res ->
            appendLine("      resources:")
            res.limits?.let { l ->
                appendLine("        limits:")
                l.cpus?.let { appendLine("          cpus: \"$it\"") }
                l.memory?.let { appendLine("          memory: $it") }
            }
            res.reservations?.let { r ->
                appendLine("        reservations:")
                r.cpus?.let { appendLine("          cpus: \"$it\"") }
                r.memory?.let { appendLine("          memory: $it") }
            }
        }
    }

    if (svc.extraHosts.isNotEmpty()) {
        appendLine("    extra_hosts:")
        svc.extraHosts.forEach { appendLine("      - \"$it\"") }
    }

    svc.logging?.let { log ->
        appendLine("    logging:")
        appendLine("      driver: ${log.driver}")
        if (log.options.isNotEmpty()) {
            appendLine("      options:")
            log.options.forEach { [k, v] -> appendLine("        $k: \"$v\"") }
        }
    }

    if (svc.secrets.isNotEmpty()) {
        appendLine("    secrets:")
        svc.secrets.forEach { appendLine("      - $it") }
    }
    if (svc.profiles.isNotEmpty()) {
        appendLine("    profiles:")
        svc.profiles.forEach { appendLine("      - $it") }
    }

    if (svc.ulimits.isNotEmpty()) {
        appendLine("    ulimits:")
        svc.ulimits.forEach { [name, cfg] ->
            appendLine("      $name:")
            appendLine("        soft: ${cfg.soft}")
            appendLine("        hard: ${cfg.hard}")
        }
    }
    if (svc.sysctls.isNotEmpty()) {
        appendLine("    sysctls:")
        svc.sysctls.forEach { [k, v] -> appendLine("      $k: $v") }
    }
    if (svc.capAdd.isNotEmpty()) {
        appendLine("    cap_add:")
        svc.capAdd.forEach { appendLine("      - $it") }
    }
    if (svc.capDrop.isNotEmpty()) {
        appendLine("    cap_drop:")
        svc.capDrop.forEach { appendLine("      - $it") }
    }
}

// --- CONVENIENCE ---

/**
 * Converts the Duration object to an ISO-8601 compliant string representation.
 *
 * The resulting string represents the duration in seconds, suffixed with the letter 's'.
 *
 * @return A string representation of the duration in ISO-8601 format.
 * @since 3.3.0
 */
@OptIn(RiskyApproximationOfTemporal::class)
private fun Duration.toIsoString(): String {
    val totalSeconds = toSeconds().toLong()
    return "${totalSeconds}s"
}

/**
 * Writes the YAML representation of the current ComposeFile instance to the specified file path.
 *
 * @param path The file path where the YAML content should be written.
 * @since 3.3.0
 */
@OptIn(Beta::class)
fun ComposeFile.writeYaml(path: Path) {
    path.writeText(toYaml().value)
}

/**
 * Writes the YAML representation of this ComposeFile to the provided [Writer].
 *
 * The YAML content is encoded as a string, written to the [Writer], and then flushed.
 *
 * @param writer The [Writer] to which the YAML content will be written.
 * @since 3.3.0
 */
@OptIn(Beta::class)
fun ComposeFile.writeTo(writer: Writer) {
    writer.write(toYaml().value)
    writer.flush()
}