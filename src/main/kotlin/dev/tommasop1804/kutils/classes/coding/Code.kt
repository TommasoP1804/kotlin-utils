package dev.tommasop1804.kutils.classes.coding

import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.asSingleList
import dev.tommasop1804.kutils.exceptions.CompilationException
import dev.tommasop1804.kutils.exceptions.RequiredParameterException
import dev.tommasop1804.kutils.toEnumConst
import tools.jackson.core.JsonGenerator
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.databind.node.ObjectNode
import java.io.File
import java.io.StringWriter
import java.net.URI
import java.net.URLClassLoader
import javax.script.ScriptEngineManager
import javax.tools.*

/**
 * Represents a piece of code with associated attributes such as its value, language, and length.
 *
 * This class provides functionality to access a character at a specific index and retrieve a subsequence
 * of the code content.
 *
 * @property value The content of the code as a string.
 * @property language The programming language of the code.
 * @property length The length of the code content.
 * @since 1.0.0
 */
@Suppress("unused")
@JsonSerialize(using = Code.Companion.Serializer::class)
@JsonDeserialize(using = Code.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Code.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Code.Companion.OldDeserializer::class)
open class Code(open val value: String, val language: Language) : CharSequence {
    /**
     * Represents the length of the string stored in the `value` property of the Code class.
     * This property provides the number of characters in the string.
     *
     * @since 1.0.0
     */
    override val length get() = value.length

    /**
     * Secondary constructor for the `Code` class.
     *
     * This constructor initializes a `Code` object using an existing `Code` instance.
     * It extracts the `value` and `language` from the provided `Code` object and
     * passes them to the primary constructor of the class.
     *
     * @param code The `Code` instance from which to initialize the new `Code` object.
     * @since 1.0.0
     */
    constructor(code: Code) : this(code.value, code.language)

    companion object {
        /**
         * Processes the provided Java source code and returns a Code object using the specified Java language.
         *
         * @param code the Java source code to be processed
         * @since 1.0.0
         */
        fun java(@org.intellij.lang.annotations.Language("java") code: String) = Code(code, Language.JAVA)

        /**
         * Constructs a Code object with the specified Kotlin source code.
         *
         * @param code The Kotlin source code to be encapsulated within the Code object.
         * @return A new Code object containing the provided Kotlin source code.
         * @since 1.0.0
         */
        fun kotlin(@org.intellij.lang.annotations.Language("kotlin") code: String) = Code(code, Language.KOTLIN)

        /**
         * Creates an instance of the Code class initialized with the given Python code.
         *
         * @param code The Python code to be represented as a Code object.
         * @since 1.0.0
         */
        fun python(@org.intellij.lang.annotations.Language("Py") code: String) = Code(code, Language.PYTHON)

        /**
         * Creates a new instance of the `Code` class with the specified JavaScript code and language set to JavaScript.
         *
         * @param code The JavaScript code to be wrapped in the `Code` instance.
         * @return An instance of `Code` with the provided JavaScript code.
         * @since 1.0.0
         */
        fun javaScript(@org.intellij.lang.annotations.Language("JavaScript") code: String) = Code(code, Language.JAVASCRIPT)

        /**
         * Creates an instance of the `Code` class with TypeScript as the specified language.
         *
         * @param code The TypeScript code to be encapsulated within the `Code` instance.
         * @since 1.0.0
         */
        fun typeScript(@org.intellij.lang.annotations.Language("TypeScript") code: String) = Code(code, Language.TYPESCRIPT)

        /**
         * Constructs a `Code` instance representing a string of C# code.
         *
         * @param code The C# code as a string. Must follow valid C# syntax.
         * @return A `Code` object containing the provided C# code and its associated metadata.
         * @since 1.0.0
         */
        fun cSharp(@org.intellij.lang.annotations.Language("C#") code: String) = Code(code, Language.CSHARP)

        /**
         * Creates a representation of C++ code as an instance of the Code class.
         *
         * @param code The C++ source code to be represented.
         * @since 1.0.0
         */
        fun cpp(@org.intellij.lang.annotations.Language("c++") code: String) = Code(code, Language.CPP)

        /**
         * Creates a new instance of the `Code` class with the given C language code snippet.
         *
         * @param code A string representing a snippet of code written in the C programming language.
         * @since 1.0.0
         */
        fun c(@org.intellij.lang.annotations.Language("C") code: String) = Code(code, Language.C)

        /**
         * Creates a new instance of the `Code` class with the specified PHP code as its content.
         *
         * @param code The PHP code provided as a string, which will be used to initialize a `Code` object.
         * @return A `Code` object initialized with the given PHP code and specified language.
         * @since 1.0.0
         */
        fun php(@org.intellij.lang.annotations.Language("PHP") code: String) = Code(code, Language.PHP)

        /**
         * Executes a given Ruby code snippet within the context of a defined language processing environment.
         *
         * @param code A string containing the Ruby code to be executed or processed.
         * @since 1.0.0
         */
        fun ruby(@org.intellij.lang.annotations.Language("Ruby") code: String) = Code(code, Language.RUBY)

        /**
         * Creates a new instance of the `Code` class with the given Swift code and language set to Swift.
         *
         * @param code The Swift source code as a string.
         * @since 1.0.0
         */
        fun swift(@org.intellij.lang.annotations.Language("swift") code: String) = Code(code, Language.SWIFT)

        /**
         * Creates a `Code` instance representing Go code with the provided string content.
         *
         * @param code A string containing Go code. This parameter must conform to Go language syntax.
         * @return A new `Code` object with the specified Go code and its language set as Go.
         * @since 1.0.0
         */
        fun go(@org.intellij.lang.annotations.Language("go") code: String) = Code(code, Language.GO)

        /**
         * Wraps the provided Rust code into a Code object with the specified Rust language context.
         *
         * @param code The Rust code to be wrapped.
         * @since 1.0.0
         */
        fun rust(@org.intellij.lang.annotations.Language("rust") code: String) = Code(code, Language.RUST)

        /**
         * Creates an instance of the `Code` class with the provided Scala code and assigns the language as `SCALA`.
         *
         * @param code The source code written in Scala to be encapsulated in the `Code` object.
         * @return A new `Code` object representing the provided Scala source code.
         * @since 1.0.0
         */
        fun scala(@org.intellij.lang.annotations.Language("scala") code: String) = Code(code, Language.SCALA)

        /**
         * Creates a Code object for the provided Haskell code snippet.
         *
         * @param code The Haskell code to be encapsulated within the Code object.
         * @return A Code object representing the given Haskell code with its associated language metadata.
         * @since 1.0.0
         */
        fun haskell(@org.intellij.lang.annotations.Language("Haskell") code: String) = Code(code, Language.HASKELL)

        /**
         * Constructs a SQL-specific code representation.
         *
         * @param code The SQL code string to be represented by the `Code` object.
         * @since 1.0.0
         */
        fun sql(@org.intellij.lang.annotations.Language("sql") code: String) = Code(code, Language.SQL)

        /**
         * Constructs a Code object using the provided R language script.
         *
         * @param code A string representing the R script to be encapsulated in a Code object.
         * @return A Code object containing the provided R script.
         * @since 1.0.0
         */
        fun r(@org.intellij.lang.annotations.Language("R") code: String) = Code(code, Language.R)

        /**
         * Executes the given Perl code as a string and wraps it in a `Code` object
         * with Perl as the specified language.
         *
         * @param code The Perl code as a string to be executed or processed.
         * @return A `Code` object containing the provided Perl code and its associated language.
         * @since 1.0.0
         */
        fun perl(@org.intellij.lang.annotations.Language("Perl") code: String) = Code(code, Language.PERL)

        /**
         * Creates a Dart code object from the provided source code.
         *
         * @param code The Dart source code to be encapsulated within a `Code` object.
         * @return A new instance of the `Code` class representing the Dart code.
         * @since 1.0.0
         */
        fun dart(@org.intellij.lang.annotations.Language("Dart") code: String) = Code(code, Language.DART)

        /**
         * Creates an instance of the Code class initialized with the provided Groovy code.
         *
         * @param code A string containing Groovy code. Annotated to specify the expected language is Groovy.
         * @return A Code instance containing the Groovy code and language type set to Groovy.
         * @since 1.0.0
         */
        fun groovy(@org.intellij.lang.annotations.Language("Groovy") code: String) = Code(code, Language.GROOVY)

        /**
         * Constructs a `Code` object encapsulating the provided assembly language code.
         *
         * @param code The assembly language code to be wrapped in the `Code` object.
         * @since 1.0.0
         */
        fun assembly(@org.intellij.lang.annotations.Language("Assembly") code: String) = Code(code, Language.ASSEMBLY)

        /**
         * Constructs a Code object containing Objective-C code with its associated language type.
         *
         * @param code A string representing the Objective-C source code.
         * @return A Code object with the provided Objective-C source code and its language set to OBJECTIVE_C.
         * @since 1.0.0
         */
        fun objectiveC(@org.intellij.lang.annotations.Language("Objective-C") code: String) = Code(code, Language.OBJECTIVE_C)

        /**
         * Constructs a new instance of `Code` containing COBOL source code.
         *
         * @param code The COBOL source code to be encapsulated in the `Code` instance.
         * @since 1.0.0
         */
        fun cobol(@org.intellij.lang.annotations.Language("COBOL") code: String) = Code(code, Language.COBOL)

        /**
         * Constructs a Code object containing the specified Fortran source code.
         *
         * @param code The Fortran source code to be encapsulated in the Code object.
         * @since 1.0.0
         */
        fun fortran(@org.intellij.lang.annotations.Language("Fortran") code: String) = Code(code, Language.FORTRAN)

        /**
         * Constructs a `Code` object using the provided Lisp code string.
         *
         * @param code A string representing Lisp code, annotated with the "Lisp" language identifier.
         * @return A `Code` object initialized with the specified Lisp code.
         * @since 1.0.0
         */
        fun lisp(@org.intellij.lang.annotations.Language("Lisp") code: String) = Code(code, Language.LISP)

        /**
         * Wraps the given Clojure code in a `Code` object with the `CLOJURE` language type.
         *
         * @param code The Clojure code to be encapsulated within the `Code` object.
         * @return A `Code` instance with the specified Clojure code and its language type set to `CLOJURE`.
         * @since 1.0.0
         */
        fun clojure(@org.intellij.lang.annotations.Language("Clojure") code: String) = Code(code, Language.CLOJURE)

        /**
         * Converts a given string of Erlang code to a `Code` object using the Erlang language context.
         *
         * @param code A string representation of Erlang code. Must be valid Erlang code.
         * @return A `Code` object encapsulating the provided code, identified as Erlang code.
         * @since 1.0.0
         */
        fun erlang(@org.intellij.lang.annotations.Language("Erlang") code: String) = Code(code, Language.ERLANG)

        /**
         * Creates a new instance of `Code` initialized with the given Elixir code string.
         *
         * @param code A string containing the Elixir code to be wrapped.
         * @since 1.0.0
         */
        fun elixir(@org.intellij.lang.annotations.Language("Elixir") code: String) = Code(code, Language.ELIXIR)

        /**
         * Constructs an instance of the Code class with the specified Julia code input.
         *
         * @param code The Julia code represented as a string.
         * @since 1.0.0
         */
        fun julia(@org.intellij.lang.annotations.Language("Julia") code: String) = Code(code, Language.JULIA)

        /**
         * Constructs a MATLAB code representation with the specified code string.
         *
         * @param code A string containing MATLAB code to be wrapped within a specified language construct.
         * @since 1.0.0
         */
        fun matlab(@org.intellij.lang.annotations.Language("MATLAB") code: String) = Code(code, Language.MATLAB)

        /**
         * Wraps Pascal code into a `Code` instance with Pascal as the specified language.
         *
         * @param code The source code written in Pascal to be wrapped into a `Code` object.
         * @since 1.0.0
         */
        fun pascal(@org.intellij.lang.annotations.Language("pascal") code: String) = Code(code, Language.PASCAL)

        /**
         * Converts a given Delphi code string into a `Code` object with the specified language type.
         *
         * @param code The Delphi code as a string. It is expected to conform to the Delphi language syntax.
         * @since 1.0.0
         */
        fun delphi(@org.intellij.lang.annotations.Language("delphi") code: String) = Code(code, Language.DELPHI)

        /**
         * Creates an instance of the `Code` class with the provided Bash script content.
         *
         * @param code A string containing the Bash script to be wrapped as a `Code` object.
         * @since 1.0.0
         */
        fun bash(@org.intellij.lang.annotations.Language("Bash") code: String) = Code(code, Language.BASH)

        /**
         * Executes the provided PowerShell code by wrapping it in a Code object with PowerShell language settings.
         *
         * @param code The PowerShell script to be executed as a String.
         * @since 1.0.0
         */
        fun powershell(@org.intellij.lang.annotations.Language("PowerShell") code: String) = Code(code, Language.POWERSHELL)

        /**
         * Creates a `Code` object for the provided VBA (Visual Basic for Applications) code snippet.
         *
         * @param code A string representing the VBA code to be wrapped by the `Code` object.
         * @return A `Code` object initialized with the provided VBA code and the VBA language.
         * @since 1.0.0
         */
        fun vba(@org.intellij.lang.annotations.Language("vba") code: String) = Code(code, Language.VBA)

        /**
         * Creates a new instance of the `Code` class with the specified Lua code and marks its language as Lua.
         *
         * @param code The Lua code to be wrapped in the `Code` object. It must be a valid Lua script.
         * @return A new `Code` object encapsulating the provided Lua code and its language.
         * @since 1.0.0
         */
        fun lua(@org.intellij.lang.annotations.Language("Lua") code: String) = Code(code, Language.LUA)

        /**
         * Constructs a `Code` instance representing a Prolog program.
         *
         * @param code A string containing the Prolog source code. The code is expected to adhere to the Prolog language syntax.
         * @since 1.0.0
         */
        fun prolog(@org.intellij.lang.annotations.Language("Prolog") code: String) = Code(code, Language.PROLOG)

        /**
         * Constructs an instance of the `Code` class containing the provided Ada programming
         * language source code. The language is explicitly set to `Language.ADA`.
         *
         * @param code The Ada source code to be encapsulated within the `Code` instance.
         * @since 1.0.0
         */
        fun ada(@org.intellij.lang.annotations.Language("Ada") code: String) = Code(code, Language.ADA)

        /**
         * Constructs a `Code` object representing Scheme language code.
         *
         * @param code A string containing the Scheme code to be encapsulated in the `Code` object.
         * @since 1.0.0
         */
        fun scheme(@org.intellij.lang.annotations.Language("Scheme") code: String) = Code(code, Language.SCHEME)

        /**
         * Constructs a `Code` instance with the provided F# code and associates it with the F# language type.
         *
         * @param code The F# code as a string. It represents the source code to be encapsulated by the `Code` instance.
         *
         * @since 1.0.0
         */
        fun fSharp(@org.intellij.lang.annotations.Language("F#") code: String) = Code(code, Language.FSHARP)

        /**
         * Creates a `Code` instance with the provided Visual Basic code and specifies the language as Visual Basic.
         *
         * @param code A string containing the Visual Basic code to be wrapped in a `Code` object.
         * @since 1.0.0
         */
        fun visualBasic(@org.intellij.lang.annotations.Language("Visual Basic") code: String) = Code(code, Language.VISUAL_BASIC)

        /**
         * Constructs a new instance of `Code` with the provided ABAP code.
         *
         * @param code The ABAP code as a string to be encapsulated in the `Code` object.
         * @since 1.0.0
         */
        fun abap(@org.intellij.lang.annotations.Language("ABAP") code: String) = Code(code, Language.ABAP)

        /**
         * Creates a new instance of the `Code` object for the given Apex code string.
         *
         * @param code The Apex code string to be wrapped in a `Code` object.
         * @return A new `Code` object initialized with the provided Apex code.
         * @since 1.0.0
         */
        fun apex(@org.intellij.lang.annotations.Language("Apex") code: String) = Code(code, Language.APEX)

        /**
         * Creates a `Code` object representing the provided Crystal language code.
         *
         * @param code The Crystal code as a string.
         * @since 1.0.0
         */
        fun crystal(@org.intellij.lang.annotations.Language("Crystal") code: String) = Code(code, Language.CRYSTAL)

        /**
         * Creates a new D language code representation using the provided code string.
         *
         * @param code The D language code as a string.
         * @since 1.0.0
         */
        fun d(@org.intellij.lang.annotations.Language("D") code: String) = Code(code, Language.D)

        /**
         * Creates and returns a new Code instance based on the provided Hack language code snippet.
         *
         * @param code The Hack language code snippet for which the Code object is created.
         * @since 1.0.0
         */
        fun hack(@org.intellij.lang.annotations.Language("Hack") code: String) = Code(code, Language.HACK)

        /**
         * Constructs an instance of `Code` with the provided OCaml code string and specifies the language as OCaml.
         *
         * @param code The OCaml source code to be encapsulated in the `Code` instance.
         * @since 1.0.0
         */
        fun ocaml(@org.intellij.lang.annotations.Language("OCaml") code: String) = Code(code, Language.OCAML)

        /**
         * Creates a Racket code representation using the specified string and language.
         *
         * @param code The Racket code to be represented as a string. It should adhere to the Racket syntax.
         * @since 1.0.0
         */
        fun racket(@org.intellij.lang.annotations.Language("Racket") code: String) = Code(code, Language.RACKET)

        /**
         * Creates a new instance of the `Code` class using the provided Smalltalk code string.
         *
         * @param code The Smalltalk code as a string. This parameter must be a valid Smalltalk
         * code snippet that adheres to the language syntax rules.
         * @since 1.0.0
         */
        fun smalltalk(@org.intellij.lang.annotations.Language("Smalltalk") code: String) = Code(code, Language.SMALLTALK)

        /**
         * Creates a Code object representing Solidity code.
         *
         * @param code The Solidity source code as a string.
         * @return A Code object containing the provided Solidity code and its associated language information.
         * @since 1.0.0
         */
        fun solidity(@org.intellij.lang.annotations.Language("Solidity") code: String) = Code(code, Language.SOLIDITY)

        /**
         * Compiles and constructs a CoffeeScript code representation.
         *
         * @param code The CoffeeScript code as a string. The code should follow the valid syntax rules of CoffeeScript.
         * @return Returns an instance of Code containing the provided CoffeeScript code and its metadata.
         * @since 1.0.0
         */
        fun coffeeScript(@org.intellij.lang.annotations.Language("CoffeeScript") code: String) = Code(code, Language.COFFEESCRIPT)

        /**
         * Constructs a VHDL code representation.
         *
         * @param code The VHDL code as a string. This parameter must contain valid VHDL code syntax.
         * @since 1.0.0
         */
        fun vhdl(@org.intellij.lang.annotations.Language("VHDL") code: String) = Code(code, Language.VHDL)

        /**
         * Creates a Code object with the specified Verilog code and assigns it the Verilog language type.
         *
         * @param code The Verilog code as a string.
         * @since 1.0.0
         */
        fun verilog(@org.intellij.lang.annotations.Language("Verilog") code: String) = Code(code, Language.VERILOG)

        /**
         * Executes the provided ActionScript code and encapsulates it within a Code object.
         *
         * @param code The ActionScript code to be executed. Must be a valid ActionScript syntax.
         * @since 1.0.0
         */
        fun actionScript(@org.intellij.lang.annotations.Language("ActionScript") code: String) = Code(code, Language.ACTIONSCRIPT)

        /**
         * Creates a new instance of the `Code` class with the specified Basic language code string.
         *
         * @param code The Basic language source code as a string.
         * @since 1.0.0
         */
        fun basic(@org.intellij.lang.annotations.Language("Basic") code: String) = Code(code, Language.BASIC)

        /**
         * Executes the given Forth code and returns a corresponding Code object.
         *
         * @param code the Forth language code provided as a string
         * @return a Code object representing the given Forth code with the appropriate language setting
         * @since 1.0.0
         */
        fun forth(@org.intellij.lang.annotations.Language("Forth") code: String) = Code(code, Language.FORTH)

        /**
         * Constructs a `Code` object specifically configured for the ALGOL programming language.
         *
         * @param code The source code written in the ALGOL programming language.
         * @since 1.0.0
         */
        fun algol(@org.intellij.lang.annotations.Language("Algol") code: String) = Code(code, Language.ALGOL)

        /**
         * This method creates a `Code` object using the provided Eiffel source code.
         *
         * @param code Eiffel source code that will be encapsulated in the `Code` object.
         * It must be a valid string representation of the Eiffel programming language.
         * @return Returns a `Code` object initialized with the Eiffel source code.
         * @since 1.0.0
         */
        fun eiffel(@org.intellij.lang.annotations.Language("Eiffel") code: String) = Code(code, Language.EIFFEL)

        /**
         * Creates a new instance of Code object with the given Idris code and associated language.
         *
         * @param code The string representing the Idris code to encapsulate.
         * @since 1.0.0
         */
        fun idris(@org.intellij.lang.annotations.Language("Idris") code: String) = Code(code, Language.IDRIS)

        /**
         * Creates a `Code` object using the provided Turing code string and specifies the language as Turing.
         *
         * @param code The source code string written in the Turing programming language.
         * @since 1.0.0
         */
        fun turing(@org.intellij.lang.annotations.Language("Turing") code: String) = Code(code, Language.TURING)

        /**
         * Creates a `Code` object representing Rebol code.
         *
         * @param code The Rebol code as a string.
         * @return A `Code` object initialized with the specified Rebol code and using the `REBOL` language type.
         * @since 1.0.0
         */
        fun rebol(@org.intellij.lang.annotations.Language("Rebol") code: String) = Code(code, Language.REBOL)

        /**
         * Executes the provided Tcl/Tk code.
         *
         * @param code The Tcl/Tk script to be executed.
         * @since 1.0.0
         */
        fun tcltk(@org.intellij.lang.annotations.Language("Tcl/Tk") code: String) = Code(code, Language.TCLTK)

        /**
         * Constructs a `Code` object containing the provided PostScript code.
         *
         * @param code The PostScript code to be wrapped into a `Code` object.
         * This parameter must be a syntactically valid PostScript language fragment.
         * @since 1.0.0
         */
        fun postScript(@org.intellij.lang.annotations.Language("PostScript") code: String) = Code(code, Language.POSTSCRIPT)

        /**
         * Executes the given AppleScript code.
         *
         * @param code The AppleScript code to be executed. Must be a valid AppleScript language string.
         * @since 1.0.0
         */
        fun appleScript(@org.intellij.lang.annotations.Language("AppleScript") code: String) = Code(code, Language.APPLESCRIPT)

        /**
         * Creates a `Code` instance with the specified LabVIEW code and language type.
         *
         * @param code The string representation of the LabVIEW code.
         * @since 1.0.0
         */
        fun labview(@org.intellij.lang.annotations.Language("LabVIEW") code: String) = Code(code, Language.LABVIEW)

        /**
         * Compiles the provided Brainfuck code into an executable representation.
         *
         * @param code The Brainfuck code to be processed. It must be syntactically correct
         *             and adhere to the conventions of the Brainfuck language.
         * @return A Code instance that contains the compiled Brainfuck code and its language metadata.
         * @since 1.0.0
         */
        // Additional less common programming languages
        fun brainfuck(@org.intellij.lang.annotations.Language("Brainfuck") code: String) = Code(code, Language.BRAINFUCK)

        /**
         * Constructs a `Code` instance using the provided Whitespace language source code.
         *
         * @param code The source code written in Whitespace language.
         * @since 1.0.0
         */
        fun whitespace(@org.intellij.lang.annotations.Language("Whitespace") code: String) = Code(code, Language.WHITESPACE)

        /**
         * Parses and encapsulates the provided INTERCAL code into a `Code` object with INTERCAL as the specified language.
         *
         * @param code The INTERCAL code to be processed. It must be written in the INTERCAL programming language.
         * @return A `Code` object encapsulating the provided code string and defining its language as INTERCAL.
         * @since 1.0.0
         */
        fun intercal(@org.intellij.lang.annotations.Language("INTERCAL") code: String) = Code(code, Language.INTERCAL)

        /**
         * Creates a Code object for the specified Malbolge code string.
         *
         * @param code The Malbolge code as a string. The code must comply with the Malbolge language syntax.
         * @return A Code object configured with the provided Malbolge code and set to use the Malbolge language.
         * @since 1.0.0
         */
        fun malbolge(@org.intellij.lang.annotations.Language("Malbolge") code: String) = Code(code, Language.MALBOLGE)

        /**
         * Creates a new instance of the `Code` class with the specified Befunge code and language.
         *
         * @param code A string containing Befunge source code.
         * @return An instance of the `Code` class representing the provided Befunge code.
         * @since 1.0.0
         */
        fun befunge(@org.intellij.lang.annotations.Language("Befunge") code: String) = Code(code, Language.BEFUNGE)

        /**
         * Executes the provided Piet programming language code.
         *
         * @param code the Piet code to be processed
         * @since 1.0.0
         */
        fun piet(@org.intellij.lang.annotations.Language("Piet") code: String) = Code(code, Language.PIET)

        /**
         * Parses a given LOLCODE snippet.
         *
         * @param code The LOLCODE source code as a string. This should be written according to the syntax
         *             rules of the LOLCODE programming language.
         * @return A Code object containing the parsed source code and its language metadata.
         * @since 1.0.0
         */
        fun lolcode(@org.intellij.lang.annotations.Language("LOLCODE") code: String) = Code(code, Language.LOLCODE)

        /**
         * Interprets the provided Chef programming language code and initializes it into a `Code` object.
         *
         * @param code The input code written in the Chef programming language.
         * @since 1.0.0
         */
        fun chef(@org.intellij.lang.annotations.Language("Chef") code: String) = Code(code, Language.CHEF)

        /**
         * Encodes the provided code in the COW esolang syntax and encapsulates it within a `Code` object.
         *
         * @param code The string containing the COW esolang code to be processed.
         * @since 1.0.0
         */
        fun cow(@org.intellij.lang.annotations.Language("COW") code: String) = Code(code, Language.COW)

        /**
         * Converts the given Unlambda code into a `Code` object associated with the `UNLAMBDA` language.
         *
         * @param code the Unlambda code as a string input
         * @return a `Code` object containing the provided Unlambda code and its associated language
         * @since 1.0.0
         */
        fun unlambda(@org.intellij.lang.annotations.Language("Unlambda") code: String) = Code(code, Language.UNLAMBDA)

        /**
         * Compiles the provided Haxe code and creates a `Code` object with it, specifying Haxe as the language.
         *
         * @param code the Haxe source code to be wrapped in a `Code` object.
         * @since 1.0.0
         */
        // Additional modern programming languages
        fun haxe(@org.intellij.lang.annotations.Language("Haxe") code: String) = Code(code, Language.HAXE)

        /**
         * Creates a Code object for the Nim programming language.
         *
         * @param code the source code in Nim language.
         * @return a Code object containing the provided Nim source code.
         * @since 1.0.0
         */
        fun nim(@org.intellij.lang.annotations.Language("Nim") code: String) = Code(code, Language.NIM)

        /**
         * Executes the provided Zig code and initializes it with the specified language context.
         *
         * @param code the Zig code to be executed or processed
         * @since 1.0.0
         */
        fun zig(@org.intellij.lang.annotations.Language("Zig") code: String) = Code(code, Language.ZIG)

        /**
         * Creates a `Code` object initialized with the provided PureScript source code string.
         *
         * @param code the PureScript source code as a string
         * @return a `Code` object initialized with the given source code and marked with the `Language.PURESCRIPT` identifier
         * @since 1.0.0
         */
        fun pureScript(@org.intellij.lang.annotations.Language("PureScript") code: String) = Code(code, Language.PURESCRIPT)

        /**
         * A function to create a Code object with Elm language support.
         *
         * @param code A string containing valid Elm code.
         * @return A Code object initialized with the provided Elm code and language type.
         * @since 1.0.0
         */
        fun elm(@org.intellij.lang.annotations.Language("Elm") code: String) = Code(code, Language.ELM)

        /**
         * Constructs a `Code` object with the provided Reason language code.
         *
         * @param code The source code written in the Reason programming language.
         * @since 1.0.0
         */
        fun reason(@org.intellij.lang.annotations.Language("Reason") code: String) = Code(code, Language.REASON)

        /**
         * Compiles and executes the given WebAssembly (WASM) code.
         *
         * @param code The WebAssembly code to be executed. The code must be written in WebAssembly text format and follow the expected syntax and structure for valid WASM modules
         * .
         * @since 1.0.0
         */
        fun wasm(@org.intellij.lang.annotations.Language("WebAssembly") code: String) = Code(code, Language.WASM)

        /**
         * Compiles a given string written in the Logo programming language into a `Code` object.
         *
         * @param code The source code as a string in the Logo programming language.
         * @since 1.0.0
         */
        // Educational programming languages
        fun logo(@org.intellij.lang.annotations.Language("Logo") code: String) = Code(code, Language.LOGO)

        /**
         * Constructs a Code object with the given Scratch programming language code.
         *
         * @param code The Scratch language code passed as a string.
         * @return A Code instance containing the provided Scratch code and associated language.
         * @since 1.0.0
         */
        fun scratch(@org.intellij.lang.annotations.Language("Scratch") code: String) = Code(code, Language.SCRATCH)

        /**
         * Converts the provided HTML code into a Code object with HTML language type.
         *
         * @param code the string containing HTML code to be processed
         * @since 1.0.0
         */
        // Non-programming languages (markup, data serialization, etc.)
        fun html(@org.intellij.lang.annotations.Language("HTML") code: String) = Code(code, Language.HTML)

        /**
         * Creates a code block with the specified CSS content.
         *
         * @param code A string containing CSS code. This parameter should strictly conform to
         * the CSS syntax.
         * @since 1.0.0
         */
        fun css(@org.intellij.lang.annotations.Language("CSS") code: String) = Code(code, Language.CSS)

        /**
         * Creates a new instance of the `Code` class with the specified XML code and associates it with XML language syntax.
         *
         * @param code The XML code to be encapsulated in the `Code` and interpreted with XML syntax highlighting.
         * @since 1.0.0
         */
        fun xml(@org.intellij.lang.annotations.Language("XML") code: String) = Code(code, Language.XML)

        /**
         * Parses and processes the given JSON code.
         *
         * @param code The JSON string to be processed.
         * @return A new Code object containing the processed JSON code and its associated language type.
         * @since 1.0.0
         */
        fun json(@org.intellij.lang.annotations.Language("JSON") code: String) = Code(code, Language.JSON)

        /**
         * Creates a Code object with the provided YAML code string.
         *
         * @param code The YAML code string to be wrapped in the Code object.
         * @since 1.0.0
         */
        fun yaml(@org.intellij.lang.annotations.Language("YAML") code: String) = Code(code, Language.YAML)

        /**
         * Converts a given Markdown-formatted string into a Code object initialized with the Markdown language.
         *
         * @param code The string containing Markdown-formatted content.
         * @return A Code object initialized with the given Markdown content and language.
         * @since 1.0.0
         */
        fun markdown(@org.intellij.lang.annotations.Language("Markdown") code: String) = Code(code, Language.MARKDOWN)

        /**
         * Constructs a `Code` object using the provided TOML-formatted string.
         *
         * @param code The TOML code as a string. This string should conform to the TOML standard.
         * @since 1.0.0
         */
        fun toml(@org.intellij.lang.annotations.Language("TOML") code: String) = Code(code, Language.TOML)

        /**
         * Creates a `Code` instance with the provided SVG code and sets the language to SVG.
         *
         * @param code The SVG code as a string. The string should be written in valid SVG format.
         * @since 1.0.0
         */
        fun svg(@org.intellij.lang.annotations.Language("SVG") code: String) = Code(code, Language.SVG)

        /**
         * Creates a `Code` object with Regex language support.
         *
         * @param code The input string that represents the regular expression.
         * @since 1.0.0
         */
        fun regex(@org.intellij.lang.annotations.Language("RegExp") code: String) = Code(code, Language.REGEX)

        /**
         * Constructs a `Code` instance using the supplied GraphQL code and sets the language to GraphQL.
         *
         * @param code The GraphQL code to be encapsulated.
         * @return A `Code` object containing the specified GraphQL code and its associated language.
         * @since 1.0.0
         */
        // Additional markup and data formats
        fun graphql(@org.intellij.lang.annotations.Language("GraphQL") code: String) = Code(code, Language.GRAPHQL)

        /**
         * Creates a new instance of `Code` with the provided LaTeX code and specifies the language as LaTeX.
         *
         * @param code The LaTeX-formatted string to be encapsulated in the `Code` instance.
         * @since 1.0.0
         */
        fun latex(@org.intellij.lang.annotations.Language("LaTeX") code: String) = Code(code, Language.LATEX)

        /**
         * Constructs a `Code` instance with the provided KaTeX string.
         * KaTeX is a fast, simple way to render LaTeX math as HTML and MathML.
         *
         * @param code The KaTeX code string to be wrapped in a `Code` object.
         * @since 1.0.0
         */
        fun katex(@org.intellij.lang.annotations.Language("KaTeX") code: String) = Code(code, Language.KATEX)

        /**
         * Creates a representation of the given reStructuredText code as a `Code` object.
         *
         * @param code The reStructuredText code as a string. Must comply with the reStructuredText format.
         * @return A `Code` object encapsulating the provided reStructuredText code and its language type.
         * @since 1.0.0
         */
        fun restructuredText(@org.intellij.lang.annotations.Language("reStructuredText") code: String) = Code(code, Language.RESTRUCTUREDTEXT)

        /**
         * Creates a Code instance representing the supplied AsciiDoc content.
         *
         * @param code The AsciiDoc-formatted string to be encapsulated within the Code object.
         *             This parameter must be a valid AsciiDoc string.
         * @since 1.0.0
         */
        fun asciidoc(@org.intellij.lang.annotations.Language("AsciiDoc") code: String) = Code(code, Language.ASCIIDOC)

        /**
         * Creates a new instance of the `Code` class with the specified INI-formatted string.
         *
         * @param code A string containing INI-formatted code. This argument should comply with the syntax rules for INI files.
         * @since 1.0.0
         */
        fun ini(@org.intellij.lang.annotations.Language("INI") code: String) = Code(code, Language.INI)

        /**
         * Parses the given CSV code and encapsulates it in a Code object along with the CSV language specifier.
         *
         * @param code the input string containing CSV-formatted data
         * @since 1.0.0
         */
        fun csv(@org.intellij.lang.annotations.Language("CSV") code: String) = Code(code, Language.CSV)

        /**
         * Parses the provided tab-separated values (TSV) content and creates a Code object.
         *
         * @param code The TSV content to be parsed.
         * @since 1.0.0
         */
        fun tsv(@org.intellij.lang.annotations.Language("TSV") code: String) = Code(code, Language.TSV)

        /**
         * Parses the provided Protocol Buffers (protobuf) code and returns a Code object
         * representing the given code in the specified language.
         *
         * @param code The Protocol Buffers (protobuf) code to be parsed. It must be a valid
         * Protocol Buffers definition as a string.
         * @return A Code object representing the given protobuf code in the specified language.
         * @since 1.0.0
         */
        fun protobuf(@org.intellij.lang.annotations.Language("Protocol Buffers") code: String) = Code(code, Language.PROTOBUF)

        /**
         * Creates a `Code` instance for the given Thrift code block.
         *
         * @param code The Thrift code as a string. This parameter expects the input to conform to the Thrift syntax
         *             as it will be used to create a `Code` object associated with the Thrift language.
         * @since 1.0.0
         */
        fun thrift(@org.intellij.lang.annotations.Language("Thrift") code: String) = Code(code, Language.THRIFT)

        /**
         * Creates an instance of Code with the specified Avro code and language.
         *
         * @param code the Avro code as a string to be wrapped in a Code object
         * @since 1.0.0
         */
        fun avro(@org.intellij.lang.annotations.Language("Avro") code: String) = Code(code, Language.AVRO)

        /**
         * Compiles the given Parquet code into an instance of the `Code` class, with the specified
         * Parquet language type.
         *
         * @param code The string representation of the Parquet code to be compiled.
         * @return A `Code` object initialized with the provided Parquet code and language type.
         * @since 1.0.0
         */
        fun parquet(@org.intellij.lang.annotations.Language("Parquet") code: String) = Code(code, Language.PARQUET)

        /**
         * Creates a new instance of the `Code` class with the specified ORC code and language.
         *
         * @param code the ORC language code as a string
         * @since 1.0.0
         */
        fun orc(@org.intellij.lang.annotations.Language("ORC") code: String) = Code(code, Language.ORC)

        class Serializer : ValueSerializer<Code>() {
            override fun serialize(value: Code, gen: JsonGenerator, ctxt: SerializationContext) {
                gen.writeStringProperty("value", value.value)
                gen.writeStringProperty("language", value.language.name)
            }
        }

        class Deserializer : ValueDeserializer<Code>() {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Code {
                val node = p.objectReadContext().readTree<ObjectNode>(p)
                return Code(
                    value = node.get("valye").asString(),
                    language = node.get("language").asString().toEnumConst()
                )
            }
        }

        class OldSerializer : JsonSerializer<Code>() {
            override fun serialize(value: Code, gen: com.fasterxml.jackson.core.JsonGenerator, serializers: SerializerProvider) {
                gen.writeStringField("code", value.value)
                gen.writeStringField("language", value.language.name)
            }
        }

        class OldDeserializer : JsonDeserializer<Code>() {
            override fun deserialize(p: com.fasterxml.jackson.core.JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Code {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
                return Code(
                    value = node.get("valye").asText(),
                    language = node.get("language").asText().toEnumConst()
                )
            }
        }
    }

    /**
     * Retrieves the character at the specified position in the string.
     *
     * @param index The zero-based index of the character to be retrieved.
     * @return The character located at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of range.
     * @since 1.0.0
     */
    override fun get(index: Int): Char = value[index]
    /**
     * Returns a new character sequence that is a subsequence of this character sequence.
     *
     * @param startIndex the start index, inclusive.
     * @param endIndex the end index, exclusive.
     * @return a character sequence starting from `startIndex` and ending at `endIndex - 1`.
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = value.subSequence(startIndex, endIndex)



    /**
     * Compiles and executes Java code dynamically at runtime. The method verifies
     * that the language is Java before proceeding to compile and execute the specified
     * Java class. The class should contain a no-argument static method named `sayHello`
     * that will be invoked during execution.
     *
     * @param className the fully qualified name of the Java class to compile and execute.
     * @throws UnsupportedOperationException if the language is not Java.
     * @throws CompilationException if the compilation fails for the provided Java class.
     * @since 1.0.0
     */
    private fun runJavaCode(className: String) {
        language == Language.JAVA || throw UnsupportedOperationException("Only java is supported.")

        val compiler: JavaCompiler = ToolProvider.getSystemJavaCompiler()
        val diagnostics = StringWriter()

        val fileManager: StandardJavaFileManager = compiler.getStandardFileManager(null, null, null)

        val outputDir = File("dynamic_classes")
        outputDir.mkdirs()
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, outputDir.asSingleList())

        val compilationUnits = JavaSourceFromString(className, value).asSingleList()

        val task = compiler.getTask(diagnostics, fileManager, null, null, null, compilationUnits)
        val success = task.call()

        if (success) {
            val classLoader = URLClassLoader(arrayOf(outputDir.toURI().toURL()))
            val clazz = classLoader.loadClass(className)

            val method = clazz.getMethod("sayHello")
            method.invoke(null)

        } else throw CompilationException(Language.JAVA, "Class: $className")

        fileManager.close()
    }

    /**
     * Executes the code written in the specified programming language.
     *
     * @param className Optional parameter specifying the name of the class to run, applicable only for Java execution.
     *        It must not be null for Java. For other languages, it is ignored.
     * @return Unit, indicating the method does not produce any result on successful execution.
     * @throws CompilationException if there is an error during the execution of the script.
     * @throws RequiredParameterException if the className is null when required for Java execution.
     * @throws UnsupportedOperationException if the provided language is not supported.
     * @since 1.0.0
     */
    fun runCode(className: String? = null) {
        when(language) {
            Language.KOTLIN, Language.JAVASCRIPT, Language.GROOVY, Language.SCALA, Language.CLOJURE, Language.TCLTK -> {
                val engine = ScriptEngineManager().getEngineByName(when(language) {
                    Language.KOTLIN -> "kotlin"
                    Language.JAVASCRIPT -> "JavaScript"
                    Language.GROOVY -> "groovy"
                    Language.SCALA -> "scala"
                    Language.CLOJURE -> "clojure"
                    else -> ""
                })
                try {
                    engine.eval(value)
                } catch (e: Exception) {
                    throw CompilationException(language, e)
                }
            }
            Language.JAVA -> runJavaCode(className ?: throw RequiredParameterException("Class name cannot be null for Java."))
            else -> throw UnsupportedOperationException("Only Kotlin, Java, JavaScript, Groovy, Scala, Clojure, TCL is supported.")
        }
    }

    /**
     * Returns a string representation of the Code object.
     *
     * @return A string that includes the value and language properties of the Code object.
     * @since 1.0.0
     */
    override fun toString(): String = "Code(value=```$value```, language=$language)"

    /**
     * Represents a Java source file provided as a string. This class extends `SimpleJavaFileObject`
     * to enable the dynamic creation and manipulation of Java source files in memory.
     *
     * @constructor Creates an instance of `JavaSourceFromString` with the specified file name and source code.
     * @param name The name of the Java file.
     * @param code The source code of the Java file as a string.
     *
     * @since 1.0.0
     */
    private class JavaSourceFromString(name: String, private val code: String) :
        SimpleJavaFileObject(
            URI.create("string:///" + name.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension),
            JavaFileObject.Kind.SOURCE
        ) {
        /**
         * Returns the character content of the source file represented by this object.
         *
         * @param ignoreEncodingErrors Indicates whether to ignore encoding errors when retrieving the content.
         * @return The content of the source file as a string.
         * @since 1.0.0
         */
        override fun getCharContent(ignoreEncodingErrors: Boolean) = code
    }
}
