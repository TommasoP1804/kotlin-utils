package dev.tommasop1804.kutils.classes.coding

import dev.tommasop1804.kutils.DOT
import dev.tommasop1804.kutils.StringList
import dev.tommasop1804.kutils.classes.constants.TextCase
import dev.tommasop1804.kutils.classes.constants.TextCase.Companion.convertCase
import dev.tommasop1804.kutils.equalsIgnoreCase
import dev.tommasop1804.kutils.minus
import dev.tommasop1804.kutils.unaryMinus
import dev.tommasop1804.kutils.unaryPlus
import java.time.Year
import kotlin.collections.map

/**
 * Enum representing major programming and markup languages with relevant information.
 *
 * @property displayName The common name of the language
 * @property yearCreated The year when the language was first released
 * @property paradigms The main paradigms supported by the language
 * @property fileExtensions Common file extensions used for this language
 * @property description Brief description or notable features of the language
 * @property category The category of the language, based on its popularity and status
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
enum class Language(
    val displayName: String,
    val yearCreated: Year,
    val paradigms: StringList,
    val fileExtensions: StringList,
    val description: String,
    val category: Category
) {
    JAVA(
        "Java",
        1995,
        listOf("Object-Oriented", "Imperative", "Generic"),
        listOf("java", "class", "jar"),
        "A general-purpose, class-based, object-oriented language designed for portability and cross-platform compatibility.",
        Category.PROGRAMMING
    ),
    KOTLIN(
        "Kotlin",
        2011,
        listOf("Object-Oriented", "Functional", "Imperative"),
        listOf("kt", "kts", "ktm"),
        "A cross-platform, statically typed, general-purpose language with type inference, designed to interoperate with Java.",
        Category.PROGRAMMING
    ),
    PYTHON(
        "Python",
        1991,
        listOf("Object-Oriented", "Imperative", "Functional", "Procedural"),
        listOf("py", "pyc", "pyd", "pyo"),
        "An interpreted, high-level, general-purpose language emphasizing code readability with significant indentation.",
        Category.PROGRAMMING
    ),
    JAVASCRIPT(
        "JavaScript",
        1995,
        listOf("Object-Oriented", "Functional", "Event-Driven"),
        listOf("js", "mjs", "cjs"),
        "A high-level, interpreted scripting language that conforms to the ECMAScript specification, primarily used for web development.",
        Category.PROGRAMMING
    ),
    TYPESCRIPT(
        "TypeScript",
        2012,
        listOf("Object-Oriented", "Functional", "Static Typing"),
        listOf("ts", "tsx"),
        "A strict syntactical superset of JavaScript that adds optional static typing and class-based object-oriented programming.",
        Category.PROGRAMMING
    ),
    CSHARP(
        "C#",
        2000,
        listOf("Object-Oriented", "Structured", "Functional", "Generic"),
        listOf("cs"),
        "A multi-paradigm language developed by Microsoft as part of the .NET initiative, designed for the Common Language Infrastructure.",
        Category.PROGRAMMING
    ),
    CPP(
        "C++",
        1985,
        listOf("Object-Oriented", "Procedural", "Generic"),
        listOf("cpp", "cc", "cxx", "h", "hpp"),
        "An extension of the C language that adds object-oriented features, used for systems programming, game development, and performance-critical applications.",
        Category.PROGRAMMING
    ),
    C(
        "C",
        1972,
        listOf("Procedural", "Imperative", "Structured"),
        listOf("c", "h"),
        "A general-purpose, procedural language supporting structured programming, lexical variable scope, and recursion.",
        Category.PROGRAMMING
    ),
    PHP(
        "PHP",
        1995,
        listOf("Object-Oriented", "Procedural", "Functional"),
        listOf("php"),
        "A server-side scripting language designed primarily for web development but also used as a general-purpose programming language.",
        Category.PROGRAMMING
    ),
    RUBY(
        "Ruby",
        1995,
        listOf("Object-Oriented", "Functional", "Imperative"),
        listOf("rb", "rbw"),
        "A dynamic, open source programming language with a focus on simplicity and productivity, with an elegant syntax.",
        Category.PROGRAMMING
    ),
    SWIFT(
        "Swift",
        2014,
        listOf("Object-Oriented", "Functional", "Protocol-Oriented"),
        listOf("swift"),
        "A general-purpose, multi-paradigm language developed by Apple for iOS, macOS, watchOS, and tvOS development.",
        Category.PROGRAMMING
    ),
    GO(
        "Go",
        2009,
        listOf("Concurrent", "Imperative", "Structured"),
        listOf("go"),
        "A statically typed, compiled language designed at Google, syntactically similar to C but with memory safety, garbage collection, and CSP-style concurrency.",
        Category.PROGRAMMING
    ),
    RUST(
        "Rust",
        2010,
        listOf("Concurrent", "Functional", "Imperative", "Structured"),
        listOf("rs"),
        "A multi-paradigm language focused on performance and safety, especially safe concurrency, supporting functional and imperative-procedural paradigms.",
        Category.PROGRAMMING
    ),
    SCALA(
        "Scala",
        2004,
        listOf("Object-Oriented", "Functional"),
        listOf("scala", "sc"),
        "A strong statically typed language that combines object-oriented and functional programming, designed to be concise and elegant.",
        Category.PROGRAMMING
    ),
    HASKELL(
        "Haskell",
        1990,
        listOf("Functional", "Pure", "Lazy"),
        listOf("hs", "lhs"),
        "A purely functional language with strong static typing, type inference, and lazy evaluation.",
        Category.PROGRAMMING
    ),
    SQL(
        "SQL",
        1974,
        listOf("Declarative"),
        listOf("sql"),
        "A domain-specific language used for managing data in relational database management systems.",
        Category.QUERY
    ),
    R(
        "R",
        1993,
        listOf("Functional", "Object-Oriented", "Procedural"),
        listOf("r", "R"),
        "A language and environment for statistical computing and graphics, widely used among statisticians and data miners.",
        Category.PROGRAMMING
    ),
    PERL(
        "Perl",
        1987,
        listOf("Procedural", "Object-Oriented", "Functional"),
        listOf("pl", "pm", "t"),
        "A family of high-level, general-purpose, interpreted, dynamic programming languages, known for text processing capabilities.",
        Category.PROGRAMMING
    ),
    DART(
        "Dart",
        2011,
        listOf("Object-Oriented", "Functional"),
        listOf("dart"),
        "A client-optimized language for fast apps on any platform, developed by Google and used for Flutter framework.",
        Category.PROGRAMMING
    ),
    GROOVY(
        "Groovy",
        2003,
        listOf("Object-Oriented", "Functional", "Dynamic"),
        listOf("groovy", "gvy", "gy", "gsh"),
        "An optionally typed and dynamic language for the Java platform, designed to enhance developer productivity with a concise, familiar, and easy-to-learn syntax.",
        Category.PROGRAMMING
    ),
    ASSEMBLY(
        "Assembly",
        1949,
        listOf("Procedural", "Low-Level"),
        listOf("asm", "s"),
        "A low-level programming language in which there is a strong correspondence between the instructions and the architecture's machine code.",
        Category.PROGRAMMING
    ),
    OBJECTIVE_C(
        "Objective-C",
        1984,
        listOf("Object-Oriented", "Reflective"),
        listOf("m", "h"),
        "An object-oriented programming language that adds Smalltalk-style messaging to the C programming language, primarily used for iOS and macOS development before Swift.",
        Category.PROGRAMMING
    ),
    COBOL(
        "COBOL",
        1959,
        listOf("Procedural", "Imperative"),
        listOf("cob", "cbl", "cpy"),
        "A compiled English-like computer programming language designed for business use, still widely used in legacy applications deployed on mainframe computers.",
        Category.PROGRAMMING
    ),
    FORTRAN(
        "Fortran",
        1957,
        listOf("Procedural", "Imperative", "Array-based"),
        listOf("f", "for", "f90", "f95", "f03", "f08"),
        "A general-purpose, compiled language especially suited to numeric computation and scientific computing, the first commercially available high-level programming language.",
        Category.PROGRAMMING
    ),
    LISP(
        "Lisp",
        1958,
        listOf("Functional", "Procedural", "Reflective"),
        listOf("lisp", "lsp", "l", "cl", "el"),
        "The second-oldest high-level programming language still in use, with fully parenthesized prefix notation and known for its powerful macro system.",
        Category.PROGRAMMING
    ),
    CLOJURE(
        "Clojure",
        2007,
        listOf("Functional", "Dynamic", "Concurrent"),
        listOf("clj", "cljs", "cljc", "edn"),
        "A modern dialect of Lisp that targets the Java Virtual Machine, emphasizing functional programming and designed for concurrency.",
        Category.PROGRAMMING
    ),
    ERLANG(
        "Erlang",
        1986,
        listOf("Functional", "Concurrent", "Distributed"),
        listOf("erl", "hrl"),
        "A general-purpose, concurrent, functional language used to build massively scalable soft real-time systems with high availability requirements.",
        Category.PROGRAMMING
    ),
    ELIXIR(
        "Elixir",
        2011,
        listOf("Functional", "Concurrent", "Process-oriented"),
        listOf("ex", "exs"),
        "A functional, concurrent, general-purpose language that runs on the BEAM virtual machine used to implement the Erlang language.",
        Category.PROGRAMMING
    ),
    JULIA(
        "Julia",
        2012,
        listOf("Multiple Dispatch", "Procedural", "Functional", "Meta"),
        listOf("jl"),
        "A high-level, high-performance language for technical computing, with syntax similar to MATLAB and performance comparable to C.",
        Category.PROGRAMMING
    ),
    MATLAB(
        "MATLAB",
        1984,
        listOf("Array", "Object-Oriented", "Procedural"),
        listOf("m", "mat"),
        "A proprietary multi-paradigm programming language and numeric computing environment, widely used for matrix manipulations, plotting of functions and data, and algorithm implementation.",
        Category.PROGRAMMING
    ),
    PASCAL(
        "Pascal",
        1970,
        listOf("Imperative", "Procedural", "Structured"),
        listOf("pas", "pp"),
        "An imperative and procedural programming language designed to encourage good programming practices using structured programming and data structuring.",
        Category.PROGRAMMING
    ),
    DELPHI(
        "Delphi",
        1995,
        listOf("Object-Oriented", "Event-Driven", "Visual"),
        listOf("pas", "dpr", "dpk"),
        "An integrated development environment for rapid application development of desktop, mobile, web, and console software, using a dialect of Object Pascal.",
        Category.PROGRAMMING
    ),
    BASH(
        "Bash",
        1989,
        listOf("Scripting", "Command Line"),
        listOf("sh", "bash"),
        "A Unix shell and command language written by Brian Fox for the GNU Project as a free software replacement for the Bourne shell.",
        Category.PROGRAMMING
    ),
    POWERSHELL(
        "PowerShell",
        2006,
        listOf("Scripting", "Object-Oriented", "Functional"),
        listOf("ps1", "psm1", "psd1"),
        "A task automation and configuration management framework from Microsoft, consisting of a command-line shell and associated scripting language.",
        Category.PROGRAMMING
    ),
    VBA(
        "VBA",
        1993,
        listOf("Event-Driven", "Object-Oriented", "Procedural"),
        listOf("bas", "cls", "frm"),
        "Visual Basic for Applications, an implementation of Microsoft's event-driven programming language Visual Basic embedded in Microsoft Office applications.",
        Category.PROGRAMMING
    ),
    LUA(
        "Lua",
        1993,
        listOf("Procedural", "Object-Oriented", "Functional"),
        listOf("lua"),
        "A lightweight, high-level, multi-paradigm programming language designed primarily for embedded use in applications, known for its efficiency and simplicity.",
        Category.PROGRAMMING
    ),
    PROLOG(
        "Prolog",
        1972,
        listOf("Logic", "Declarative"),
        listOf("pl", "pro", "p"),
        "A logic programming language associated with artificial intelligence and computational linguistics, based on formal logic.",
        Category.PROGRAMMING
    ),
    ADA(
        "Ada",
        1980,
        listOf("Multi-paradigm", "Structured", "Object-Oriented", "Concurrent"),
        listOf("ada", "ads", "adb"),
        "A structured, statically typed, imperative, and object-oriented high-level programming language, designed for large, long-lived applications where reliability and efficiency are essential.",
        Category.PROGRAMMING
    ),
    SCHEME(
        "Scheme",
        1975,
        listOf("Functional", "Procedural"),
        listOf("scm", "ss"),
        "A dialect of Lisp that follows a minimalist design philosophy, emphasizing the use of functional programming where feasible.",
        Category.PROGRAMMING
    ),
    FSHARP(
        "F#",
        2005,
        listOf("Functional", "Object-Oriented", "Imperative"),
        listOf("fs", "fsi", "fsx"),
        "A functional-first, general purpose, strongly typed, multi-paradigm programming language that encompasses functional, imperative, and object-oriented programming methods.",
        Category.PROGRAMMING
    ),
    VISUAL_BASIC(
        "Visual Basic .NET",
        2001,
        listOf("Object-Oriented", "Event-Driven", "Declarative"),
        listOf("vb"),
        "An object-oriented programming language implemented on the .NET Framework, known for its simple syntax and used for developing Windows applications.",
        Category.PROGRAMMING
    ),
    ABAP(
        "ABAP",
        1983,
        listOf("Procedural", "Object-Oriented"),
        listOf("abap"),
        "A high-level programming language created by SAP for developing business applications in the SAP environment.",
        Category.PROGRAMMING
    ),
    APEX(
        "Apex",
        2007,
        listOf("Object-Oriented", "Procedural"),
        listOf("cls", "trigger"),
        "A proprietary programming language developed by Salesforce.com for their Force.com platform, syntactically similar to Java.",
        Category.PROGRAMMING
    ),
    CRYSTAL(
        "Crystal",
        2014,
        listOf("Object-Oriented", "Concurrent"),
        listOf("cr"),
        "A programming language with Ruby-like syntax and static type checking, designed for high performance and safety.",
        Category.PROGRAMMING
    ),
    D(
        "D",
        2001,
        listOf("Multi-paradigm", "Object-Oriented", "Imperative", "Functional"),
        listOf("d"),
        "A general-purpose programming language with static typing, systems-level access, and C-like syntax, designed to combine the power and high performance of C and C++ with the programmer productivity of modern languages.",
        Category.PROGRAMMING
    ),
    HACK(
        "Hack",
        2014,
        listOf("Gradual Typing", "Object-Oriented"),
        listOf("php", "hh"),
        "A programming language for HHVM (HipHop Virtual Machine) created by Facebook as a dialect of PHP with static typing features.",
        Category.PROGRAMMING
    ),
    OCAML(
        "OCaml",
        1996,
        listOf("Functional", "Imperative", "Object-Oriented"),
        listOf("ml", "mli"),
        "A general-purpose, multi-paradigm programming language which extends the Caml dialect of ML with object-oriented features.",
        Category.PROGRAMMING
    ),
    RACKET(
        "Racket",
        1994,
        listOf("Functional", "Procedural", "Object-Oriented"),
        listOf("rkt"),
        "A general-purpose, multi-paradigm programming language in the Lisp/Scheme family, designed as a platform for programming language design and implementation.",
        Category.PROGRAMMING
    ),
    SMALLTALK(
        "Smalltalk",
        1972,
        listOf("Object-Oriented", "Reflective"),
        listOf("st"),
        "One of the first object-oriented programming languages, known for its elegant and simple syntax, and pioneering the MVC software pattern.",
        Category.PROGRAMMING
    ),
    SOLIDITY(
        "Solidity",
        2014,
        listOf("Object-Oriented", "Contract-Oriented"),
        listOf("sol"),
        "A statically-typed programming language designed for developing smart contracts that run on the Ethereum Virtual Machine.",
        Category.PROGRAMMING
    ),
    COFFEESCRIPT(
        "CoffeeScript",
        2009,
        listOf("Functional", "Object-Oriented"),
        listOf("coffee"),
        "A programming language that transcompiles to JavaScript, adding syntactic sugar inspired by Ruby, Python and Haskell to enhance JavaScript's brevity and readability.",
        Category.PROGRAMMING
    ),
    VHDL(
        "VHDL",
        1983,
        listOf("Dataflow", "Procedural"),
        listOf("vhd", "vhdl"),
        "VHSIC (Very High Speed Integrated Circuit) Hardware Description Language, used in electronic design automation to describe digital and mixed-signal systems.",
        Category.PROGRAMMING
    ),
    VERILOG(
        "Verilog",
        1984,
        listOf("Procedural", "Dataflow", "Structural"),
        listOf("v", "vh"),
        "A hardware description language used to model electronic systems, particularly digital circuits and systems.",
        Category.HARDWARE_DESCRIPTION
    ),
    ACTIONSCRIPT(
        "ActionScript",
        1998,
        listOf("Object-Oriented", "Imperative"),
        listOf("as"),
        "An object-oriented programming language originally developed for controlling Adobe Flash animations and applications.",
        Category.PROGRAMMING
    ),
    BASIC(
        "BASIC",
        1964,
        listOf("Procedural", "Imperative"),
        listOf("bas", "basic"),
        "Beginner's All-purpose Symbolic Instruction Code, a family of high-level programming languages designed to be easy to learn and use.",
        Category.PROGRAMMING
    ),
    LOGO(
        "Logo",
        1967,
        listOf("Functional", "Educational"),
        listOf("logo"),
        "An educational programming language designed to teach programming concepts, known for its 'turtle graphics' used for drawing pictures on screen.",
        Category.PROGRAMMING
    ),
    SCRATCH(
        "Scratch",
        2003,
        listOf("Visual", "Educational", "Event-Driven"),
        listOf("sb", "sb2", "sb3"),
        "A block-based visual programming language and website targeted primarily at children to help learn programming concepts without needing to worry about syntax.",
        Category.PROGRAMMING
    ),
    FORTH(
        "Forth",
        1970,
        listOf("Stack-Oriented", "Procedural", "Reflective"),
        listOf("fth", "4th"),
        "A structured, imperative, reflective, stack-based programming language and environment, used in embedded systems and boot loaders.",
        Category.PROGRAMMING
    ),
    ALGOL(
        "ALGOL",
        1958,
        listOf("Imperative", "Procedural", "Structured"),
        listOf("alg", "algol"),
        "One of the earliest high-level programming languages that greatly influenced many other languages, known for its use of block structure and nested scopes.",
        Category.PROGRAMMING
    ),
    EIFFEL(
        "Eiffel",
        1986,
        listOf("Object-Oriented", "Contract-Based"),
        listOf("e"),
        "An object-oriented programming language designed by Bertrand Meyer that emphasizes software quality, reusability, and the design by contract methodology.",
        Category.PROGRAMMING
    ),
    IDRIS(
        "Idris",
        2009,
        listOf("Functional", "Dependent Types"),
        listOf("idr"),
        "A purely functional programming language with dependent types, designed for general purpose programming with a focus on correctness and verification.",
        Category.PROGRAMMING
    ),
    TURING(
        "Turing",
        1982,
        listOf("Imperative", "Procedural"),
        listOf("t", "tu"),
        "A Pascal-like programming language developed for teaching programming, named after computer scientist Alan Turing.",
        Category.PROGRAMMING
    ),
    REBOL(
        "REBOL",
        1997,
        listOf("Functional", "Object-Oriented", "Metaprogramming"),
        listOf("r", "reb", "rebol"),
        "Relative Expression-Based Object Language, a lightweight, reflective language designed for distributed computing and network communications.",
        Category.PROGRAMMING
    ),
    TCLTK(
        "Tcl/Tk",
        1988,
        listOf("Imperative", "Procedural", "Event-Driven"),
        listOf("tcl", "tk"),
        "Tool Command Language with its graphical user interface toolkit, designed to be embedded into applications and for rapid prototyping.",
        Category.PROGRAMMING
    ),
    POSTSCRIPT(
        "PostScript",
        1982,
        listOf("Stack-Based", "Concatenative"),
        listOf("ps", "eps"),
        "A page description language in electronic and desktop publishing, known for its use in defining the appearance of text and graphics on printed pages.",
        Category.PAGE_DESCRIPTION
    ),
    APPLESCRIPT(
        "AppleScript",
        1993,
        listOf("Scripting", "Event-Driven"),
        listOf("applescript", "scpt"),
        "A scripting language created by Apple to control and automate applications on macOS, designed to use natural language elements for ease of use.",
        Category.PROGRAMMING
    ),
    LABVIEW(
        "LabVIEW",
        1986,
        listOf("Dataflow", "Visual", "Graphical"),
        listOf("vi", "lvproj"),
        "Laboratory Virtual Instrument Engineering Workbench, a visual programming language from National Instruments used for data acquisition, instrument control, and industrial automation.",
        Category.PROGRAMMING
    ),

    // Non-programming languages (markup, data serialization, etc.)
    HTML(
        "HTML",
        1993,
        listOf("Markup", "Declarative"),
        listOf("html", "htm"),
        "HyperText Markup Language, the standard markup language for documents designed to be displayed in a web browser.",
        Category.MARKUP
    ),
    CSS(
        "CSS",
        1996,
        listOf("Style Sheet", "Declarative"),
        listOf("css"),
        "Cascading Style Sheets, a style sheet language used for describing the presentation of a document written in HTML or XML.",
        Category.STYLE
    ),
    XML(
        "XML",
        1998,
        listOf("Markup", "Declarative"),
        listOf("xml"),
        "Extensible Markup Language, a markup language that defines a set of rules for encoding documents in a format that is both human-readable and machine-readable.",
        Category.MARKUP
    ),
    JSON(
        "JSON",
        2001,
        listOf("Data Serialization", "Declarative"),
        listOf("json"),
        "JavaScript Object Notation, an open standard file format and data interchange format that uses human-readable text to store and transmit data objects.",
        Category.DATA_SERIALIZATION
    ),
    YAML(
        "YAML",
        2001,
        listOf("Data Serialization", "Declarative"),
        listOf("yaml", "yml"),
        "YAML Ain't Markup Language, a human-readable data serialization language commonly used for configuration files and data exchange between languages with different data structures.",
        Category.DATA_SERIALIZATION
    ),
    MARKDOWN(
        "Markdown",
        2004,
        listOf("Markup", "Lightweight"),
        listOf("md", "markdown"),
        "A lightweight markup language with plain text formatting syntax designed to be converted to HTML and other formats.",
        Category.MARKUP
    ),
    TOML(
        "TOML",
        2013,
        listOf("Data Serialization", "Configuration"),
        listOf("toml"),
        "Tom's Obvious, Minimal Language, a configuration file format designed to be easy to read and write with a simple key-value syntax.",
        Category.DATA_SERIALIZATION
    ),
    SVG(
        "SVG",
        2001,
        listOf("Vector Graphics", "XML-based"),
        listOf("svg"),
        "Scalable Vector Graphics, an XML-based vector image format for two-dimensional graphics with support for interactivity and animation.",
        Category.MARKUP
    ),

    // Additional less common programming languages
    BRAINFUCK(
        "Brainfuck",
        1993,
        listOf("Esoteric", "Minimalist"),
        listOf("bf"),
        "An esoteric programming language created to challenge and amuse programmers, notable for its extreme minimalism with only eight commands.",
        Category.ESOTERIC
    ),
    WHITESPACE(
        "Whitespace",
        2003,
        listOf("Esoteric"),
        listOf("ws"),
        "An esoteric programming language that uses only whitespace characters (space, tab, and line feed) as syntax, making programs appear invisible when viewed with most text editors.",
        Category.ESOTERIC
    ),
    INTERCAL(
        "INTERCAL",
        1972,
        listOf("Esoteric"),
        listOf("i", "int"),
        "Compiler Language With No Pronounceable Acronym, one of the first esoteric programming languages, designed to be completely different from any other programming language.",
        Category.ESOTERIC
    ),
    MALBOLGE(
        "Malbolge",
        1998,
        listOf("Esoteric"),
        listOf("mal"),
        "An esoteric programming language designed to be as difficult to program in as possible, named after the eighth circle of hell in Dante's Inferno.",
        Category.ESOTERIC
    ),
    BEFUNGE(
        "Befunge",
        1993,
        listOf("Esoteric", "Two-dimensional"),
        listOf("bf"),
        "A two-dimensional esoteric programming language where programs are laid out on a grid and execution can proceed in any cardinal direction.",
        Category.ESOTERIC
    ),
    PIET(
        "Piet",
        2001,
        listOf("Esoteric", "Visual"),
        listOf("ppm"),
        "An esoteric programming language in which programs look like abstract paintings, with program flow determined by changes in hue and brightness between adjacent pixels.",
        Category.ESOTERIC
    ),
    LOLCODE(
        "LOLCODE",
        2007,
        listOf("Esoteric"),
        listOf("lol"),
        "An esoteric programming language inspired by lolcat memes, with syntax designed to resemble the language used in image macros featuring cats.",
        Category.ESOTERIC
    ),
    CHEF(
        "Chef",
        2002,
        listOf("Esoteric"),
        listOf("chef"),
        "An esoteric programming language designed to make programs look like cooking recipes, with ingredients as variables and cooking instructions as operations.",
        Category.ESOTERIC
    ),
    COW(
        "COW",
        2003,
        listOf("Esoteric"),
        listOf("cow"),
        "An esoteric programming language whose programs consist of variations of the word 'moo', inspired by the distinctive sound made by bovines.",
        Category.ESOTERIC
    ),
    UNLAMBDA(
        "Unlambda",
        1999,
        listOf("Esoteric", "Functional"),
        listOf("unl"),
        "An esoteric functional programming language based on combinatory logic, designed to be as impractical as possible while still being Turing-complete.",
        Category.ESOTERIC
    ),

    // Additional modern programming languages
    HAXE(
        "Haxe",
        2005,
        listOf("Object-Oriented", "Functional", "Cross-platform"),
        listOf("hx"),
        "A high-level, cross-platform programming language that can compile to multiple target languages including JavaScript, PHP, C++, Java, and Python.",
        Category.PROGRAMMING
    ),
    NIM(
        "Nim",
        2008,
        listOf("Imperative", "Functional", "Object-Oriented"),
        listOf("nim"),
        "A statically typed compiled systems programming language with a focus on efficiency, expressiveness, and elegance, drawing inspiration from Python, Ada, and Modula.",
        Category.PROGRAMMING
    ),
    ZIG(
        "Zig",
        2016,
        listOf("Imperative", "Procedural"),
        listOf("zig"),
        "A general-purpose programming language designed for robustness, optimality, and maintainability, with a focus on providing direct access to low-level features.",
        Category.PROGRAMMING
    ),
    PURESCRIPT(
        "PureScript",
        2013,
        listOf("Functional", "Strongly-typed"),
        listOf("purs"),
        "A strongly-typed functional programming language that compiles to JavaScript, similar to Haskell but designed for the web platform.",
        Category.PROGRAMMING
    ),
    ELM(
        "Elm",
        2012,
        listOf("Functional", "Declarative"),
        listOf("elm"),
        "A domain-specific programming language for declaratively creating web browser-based graphical user interfaces, focused on usability, performance, and robustness.",
        Category.PROGRAMMING
    ),
    REASON(
        "Reason",
        2016,
        listOf("Functional", "Object-Oriented"),
        listOf("re", "rei"),
        "A syntax extension for OCaml created by Facebook, designed to make OCaml more accessible to JavaScript programmers.",
        Category.PROGRAMMING
    ),
    WASM(
        "WebAssembly",
        2017,
        listOf("Low-level", "Assembly"),
        listOf("wasm", "wat"),
        "A binary instruction format for a stack-based virtual machine, designed as a portable target for compilation of high-level languages like C, C++, and Rust.",
        Category.INSTRUCTION_FORMAT
    ),

    // Additional markup and data formats
    GRAPHQL(
        "GraphQL",
        2015,
        listOf("Query Language", "Data Manipulation"),
        listOf("graphql", "gql"),
        "A query language for APIs and a runtime for executing those queries with existing data, providing a more efficient and powerful alternative to REST.",
        Category.QUERY
    ),
    LATEX(
        "LaTeX",
        1984,
        listOf("Markup", "Document Preparation"),
        listOf("tex", "latex"),
        "A document preparation system especially suited for scientific and mathematical documents, based on the TeX typesetting system.",
        Category.MARKUP
    ),
    RESTRUCTUREDTEXT(
        "reStructuredText",
        2002,
        listOf("Markup", "Lightweight"),
        listOf("rst"),
        "A lightweight markup language designed to be both readable in plain text form and convertible to other formats like HTML, PDF, and ePub.",
        Category.MARKUP
    ),
    ASCIIDOC(
        "AsciiDoc",
        2002,
        listOf("Markup", "Lightweight"),
        listOf("adoc", "asciidoc"),
        "A lightweight markup language for writing documentation, articles, books, web pages, and presentations, similar to Markdown but with more features.",
        Category.MARKUP
    ),
    INI(
        "INI",
        1985,
        listOf("Configuration", "Key-Value"),
        listOf("ini", "cfg", "conf"),
        "A simple configuration file format consisting of key-value pairs organized into sections, commonly used in Windows and many other systems.",
        Category.DATA_SERIALIZATION
    ),
    CSV(
        "CSV",
        1972,
        listOf("Data", "Tabular"),
        listOf("csv"),
        "Comma-Separated Values, a simple file format used to store tabular data where each line represents a row and fields are separated by commas.",
        Category.DATA_SERIALIZATION
    ),
    TSV(
        "TSV",
        1993,
        listOf("Data", "Tabular"),
        listOf("tsv", "tab"),
        "Tab-Separated Values, a simple file format similar to CSV but using tabs as field separators, which can handle data containing commas more easily.",
        Category.DATA_SERIALIZATION
    ),
    PROTOBUF(
        "Protocol Buffers",
        2008,
        listOf("Data Serialization", "Interface Definition"),
        listOf("proto"),
        "A language-neutral, platform-neutral, extensible mechanism for serializing structured data, developed by Google for efficient data storage and interchange.",
        Category.DATA_SERIALIZATION
    ),
    THRIFT(
        "Thrift",
        2007,
        listOf("Interface Definition", "RPC"),
        listOf("thrift"),
        "A software framework for scalable cross-language services development, originally developed at Facebook, allowing definition of data types and service interfaces.",
        Category.DATA_SERIALIZATION
    ),
    AVRO(
        "Avro",
        2009,
        listOf("Data Serialization", "Schema-based"),
        listOf("avro", "avsc"),
        "A data serialization system developed within Apache's Hadoop project, providing rich data structures, compact binary format, and container files for storing persistent data.",
        Category.DATA_SERIALIZATION
    ),
    PARQUET(
        "Parquet",
        2013,
        listOf("Columnar Storage", "Data Format"),
        listOf("parquet"),
        "A columnar storage file format designed for efficient data storage and retrieval, part of the Apache Hadoop ecosystem and optimized for use with complex data processing frameworks.",
        Category.DATA_SERIALIZATION
    ),
    ORC(
        "ORC",
        2013,
        listOf("Columnar Storage", "Data Format"),
        listOf("orc"),
        "Optimized Row Columnar, a columnar storage file format providing efficient compression and improved performance when processing large data sets in Apache Hive.",
        Category.DATA_SERIALIZATION
    ),

    REGEX(
        "RegEx",
        1951,
        listOf("Pattern Matching", "Text Processing"),
        listOf("regex", "regexp"),
        "Regular Expressions, a sequence of characters that define a search pattern, mainly for use in pattern matching with strings.",
        Category.PATTERN_MATCHING
    ),

    KATEX(
        "KaTeX",
        2013,
        listOf("Markup", "Mathematical Notation"),
        listOf("tex", "katex"),
        "A fast, easy-to-use JavaScript library for TeX math rendering on the web, developed by Khan Academy as a faster alternative to MathJax.",
        Category.MARKUP
    );

    companion object {
        /**
         * A list of display names for all languages (programming, markup, data serialization, etc.).
         *
         * This property retrieves the `displayName` attribute from all entries of the `Language` class
         * and maps them into a list of strings.
         *
         * This can be used to provide a collection of human-readable names associated with
         * different languages defined in the containing `Language` class.
         *
         * @return A list of strings representing the display names of all languages.
         * @since 1.0.0
         */
        val nameList: StringList
            get() = entries.map(Language::displayName)

        /**
         * Finds the first language entry that matches the given file extension.
         *
         * The search is case-insensitive and scans through the list of file extensions
         * associated with each entry to locate a match. This works for both programming
         * languages and non-programming languages (markup, data serialization, etc.).
         *
         * @param extension The file extension to search for, without the leading dot (e.g., "java", "kt", "html", "json").
         * @since 1.0.0
         */
        infix fun ofFileExtension(extension: String) = entries.find { -(extension - Char.DOT) in it.fileExtensions }

        /**
         * Finds an entry in the `entries` list based on the given display name, ignoring case considerations.
         *
         * This function searches the `entries` collection for an element whose `displayName` matches
         * the provided [name] string, without considering case differences.
         *
         * @param name The display name to search for in the `entries` collection.
         * @return The entry from the `entries` list that matches the provided display name, or `null` if no match is found.
         * @since 1.0.0
         */
        infix fun ofName(name: String) = entries.find { it.displayName equalsIgnoreCase name }

        /**
         * Searches for a language entry with a name matching the provided value,
         * while accounting for the specified text case. This works for both programming
         * languages and non-programming languages (markup, data serialization, etc.).
         *
         * @param name the input string representing the language name to search for (e.g., "Java", "HTML", "JSON")
         * @param textCase the text case to consider when searching for a match, defaults to [dev.tommasop1804.kutils.classes.constants.TextCase.STANDARD]
         * @return the matching language entry if found, or `null` otherwise
         * @since 1.0.0
         */
        fun ofName(name: String, textCase: TextCase = TextCase.STANDARD) =
            entries.find { +it.displayName == name.convertCase(textCase, TextCase.UPPER_CASE) }

        /**
         * Filters the entries of the class to include only those that belong to the specified category.
         *
         * This function allows retrieving all entries that match a given category by comparing
         * the `category` property of each entry against the provided `category` parameter.
         * The result is returned as a set of matching entries.
         *
         * @param category The category used to filter the entries. Must be a valid `Category` value.
         * @return A set of entries that belong to the specified category.
         * @since 1.0.0
         */
        infix fun byCategory(category: Category) = entries.filter { it.category == category }.toSet()
    }

    constructor(
        displayName: String,
        yearCreated: Int,
        paradigms: StringList,
        fileExtensions: StringList,
        description: String,
        category: Category
    ) : this(
        displayName,
        dev.tommasop1804.kutils.Year(yearCreated),
        paradigms,
        fileExtensions,
        description,
        category
    )

    /**
     * Represents a classification of programming-related categories.
     *
     * This enumeration defines various types of programming and scripting languages,
     * as well as description and query formats, categorized by their primary usage or design intent.
     *
     * @since 1.0.0
     */
    enum class Category {
        PROGRAMMING,
        PAGE_DESCRIPTION,
        INSTRUCTION_FORMAT,
        MARKUP,
        STYLE,
        DATA_SERIALIZATION,
        QUERY,
        HARDWARE_DESCRIPTION,
        ESOTERIC,
        PATTERN_MATCHING
    }
}