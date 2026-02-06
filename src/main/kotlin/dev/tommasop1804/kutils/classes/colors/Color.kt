package dev.tommasop1804.kutils.classes.colors

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.colors.Color.Companion.ofRGB
import dev.tommasop1804.kutils.classes.constants.TextCase
import dev.tommasop1804.kutils.classes.constants.TextCase.Companion.convertCase
import dev.tommasop1804.kutils.classes.numbers.Hex
import dev.tommasop1804.kutils.classes.numbers.Percentage
import dev.tommasop1804.kutils.classes.numbers.Percentage.Companion.FULL
import dev.tommasop1804.kutils.classes.numbers.Percentage.Companion.ZERO_PERCENT
import dev.tommasop1804.kutils.classes.tuple.Quadruple
import dev.tommasop1804.kutils.classes.tuple.Quintuple
import dev.tommasop1804.kutils.exceptions.NoMatchingFormatException
import dev.tommasop1804.kutils.exceptions.ValidationFailedException
import jakarta.persistence.AttributeConverter
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.io.Serial
import java.io.Serializable
import java.util.*
import kotlin.math.*
import kotlin.reflect.KProperty

/**
 * Represents a color with red, green, blue, and alpha components, along with
 * utility methods for converting the color into various color models and formats.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = Color.Companion.Serializer::class)
@JsonDeserialize(using = Color.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Color.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Color.Companion.OldDeserializer::class)
@Suppress("unused", "localVariableName", "kutils_substring_as_get_intprogression", "kutils_collection_declaration", "kutils_tuple_declaration", "kutils_take_as_int_invoke")
class Color internal constructor(var red: Int, var green: Int, var blue: Int, var alpha: Percentage = FULL): Serializable, Comparable<Color> {
    companion object {
        /**
         * A unique identifier for serializable classes used to verify compatibility
         * during the deserialization process. It ensures that a loaded class
         * is compatible with the serialized object.
         *
         * Any modification to the class that is incompatible with the serialized
         * form should result in a change to the serialVersionUID value to prevent
         * deserialization errors.
         *
         * @since 1.0.0
         */
        @Serial
        private const val serialVersionUID = 1L

        /**
         * The constant BLACK represents the color black with RGB values of (0, 0, 0).
         *
         * BLACK is a static, pre-defined color in the [Color] class and is often
         * used as a default or base color in various graphical or color manipulation contexts.
         *
         *  * Red: 0
         *  * Green: 0
         *  * Blue: 0
         *  * Alpha: 255 (default opacity when applicable)
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val BLACK: Color = ofRGB(0, 0, 0)

        /**
         * Represents the color white with its RGB components set to maximum values (255, 255, 255).
         *
         * The WHITE constant is used to convey pure white color in various operations, such as
         * rendering, color manipulation, or gradients.
         *
         *  * Red (R): 255
         *  * Green (G): 255
         *  * Blue (B): 255
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val WHITE: Color = ofRGB(255, 255, 255)

        /**
         * Represents the color red with an RGB value of (255, 0, 0).
         *
         * This is a constant predefined color representing the pure red shade
         * on the RGB color model with maximum intensity in the red channel
         * and no intensity in the green or blue channels.
         *
         * Use this constant wherever a standard red color is required.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val RED: Color = ofRGB(255, 0, 0)

        /**
         * Represents the color green in the RGB color model with a red value of 0,
         * green value of 255, and blue value of 0.
         *
         * This constant is a predefined instance of [Color] representing
         * the standard green color.
         *
         *  * Red: 0
         *  * Green: 255
         *  * Blue: 0
         *
         * Use this constant to refer to the color green when working with the
         * [Color] class, ensuring consistency and avoiding manual RGB value creation.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val GREEN: Color = ofRGB(0, 255, 0)

        /**
         * A predefined constant representing the color blue.
         *
         * The RGB representation of this color is (0, 0, 255), which reflects a maximum
         * intensity of the blue component while the red and green components are set to 0.
         *
         *  * Red value: 0
         *  * Green value: 0
         *  * Blue value: 255
         *
         * This color can be used across various contexts where a standard blue color
         * is required.
         *
         * @since 1.0.0
         */
        val BLUE: Color = ofRGB(0, 0, 255)

        /**
         * Represents the color yellow with an RGB value of (255, 255, 0).
         *
         * Yellow is a primary color in the additive RGB color model and is often
         * associated with brightness, optimism, and energy.
         *
         * This constant can be used in applications requiring a standard yellow color definition.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val YELLOW: Color = ofRGB(255, 255, 0)

        /**
         * Represents the color CYAN, a bright shade of blue-green
         * with RGB values (0, 255, 255).
         *
         * This predefined color is immutable and commonly used in
         * various graphical and UI designs to model the color cyan.
         *
         * CYAN is created using the [ofRGB]
         * method, where red is 0, green is 255, and blue is 255.
         *
         * @see ofRGB
         * @see Color
         *
         * @since 1.0.0
         */
        val CYAN: Color = ofRGB(0, 255, 255)

        /**
         * Represents the color magenta with an RGB value of (255, 0, 255).
         *
         * This is a standard predefined color in the RGB color model, characterized by its maximum red and blue
         * intensities and no contribution from the green component. Magenta is often used in graphic design,
         * printing, and digital imagery.
         *
         * This color can be used as a constant to avoid manually specifying the RGB values for magenta.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val MAGENTA: Color = ofRGB(255, 0, 255)

        /**
         * Represents a predefined color constant for gray, with an RGB value of (128, 128, 128).
         *
         * This color is a medium shade of gray and can be used to represent neutral or balanced tones.
         *
         *  * Red component: 128
         *  * Green component: 128
         *  * Blue component: 128
         *
         * It is part of the standard color palette in the `Color` class and can be utilized
         * directly without creating a new instance.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val GRAY: Color = ofRGB(128, 128, 128)

        /**
         * Represents a predefined color constant for light gray with RGB values (192, 192, 192).
         *
         * Light gray is a neutral color that appears as a lighter shade of gray, often used
         * for subtle backgrounds or to convey a sense of neutrality in designs.
         *
         *  * Red: 192
         *  * Green: 192
         *  * Blue: 192
         *
         * This constant is immutable and can be used where a predefined light gray color is needed.
         *
         * @since 1.0.0
         */
        val LIGHT_GRAY: Color = ofRGB(192, 192, 192)

        /**
         * Represents the dark gray color with predefined RGB values (64, 64, 64).
         *
         * This constant can be used to represent a standard dark gray color in various
         * contexts where color manipulation or representation is required.
         *
         *  * Red component: 64
         *  * Green component: 64
         *  * Blue component: 64
         *
         * The predefined RGB values ensure consistent representations across different
         * usages.
         *
         * @since 1.0.0
         */
        val DARK_GRAY: Color = ofRGB(64, 64, 64)

        /**
         * Represents the color light red with RGB values of (255, 128, 128).
         *
         * This constant provides a predefined light shade of red, useful for
         * UI design, graphic applications, or any functionality requiring a
         * soft red color.
         *
         *  * Red: 255
         *  * Green: 128
         *  * Blue: 128
         *
         * Use this constant for consistency and readability when referencing
         * this specific color shade.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val LIGHT_RED: Color = ofRGB(255, 128, 128)

        /**
         * Represents a light green color with RGB values of (128, 255, 128).
         *
         * This color is a static predefined instance in the `Color` class.
         * It can be used to create visually appealing interfaces or graphics
         * where light green hues are required.
         *
         * Properties:
         *
         *  * Red: 128
         *  * Green: 255
         *  * Blue: 128
         *
         * This color can be utilized in various color transformations and gradient
         * generations provided by the `Color` class.
         *
         * @see ofRGB
         * @see Color.generateGradient
         * @since 1.0.0
         */
        val LIGHT_GREEN: Color = ofRGB(128, 255, 128)

        /**
         * Represents a predefined color constant for light blue.
         *
         * This color is defined with RGB values of (75, 75, 255),
         * which creates a soft, light shade of blue.
         * This constant can be utilized wherever a light blue shade is needed.
         *
         * Usage of this constant ensures consistency in the representation
         * of the light blue color across various components or visual elements.
         *
         * @since 1.0.0
         */
        val LIGHT_BLUE: Color = ofRGB(75, 75, 255)

        /**
         * A predefined color constant representing a light yellow shade.
         *
         * The RGB composition of this color is:
         *
         *  * Red: 255
         *  * Green: 255
         *  * Blue: 128
         *
         * This color is defined as a lighter variant of yellow, offering a softer and
         * less intense appearance compared to standard yellow.
         *
         * Can be used in various applications such as UI design, graphics rendering,
         * or anywhere a predefined light yellow shade is needed.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val LIGHT_YELLOW: Color = ofRGB(255, 255, 128)

        /**
         * Represents the LIGHT_CYAN color value, defined as an RGB color with the following components:
         *
         *  * Red: 128
         *  * Green: 255
         *  * Blue: 255
         *
         * This color is a lightened version of cyan, making it suitable for applications requiring
         * a softer and more pastel-like appearance for design or visualization purposes.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val LIGHT_CYAN: Color = ofRGB(128, 255, 255)

        /**
         * Represents a constant color with a light magenta hue.
         *
         * The RGB components of this color are:
         *
         *  * Red: 255
         *  * Green: 128
         *  * Blue: 255
         *
         * This color can be used for styling, visualization, or any scenario requiring
         * a predefined light magenta shade.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val LIGHT_MAGENTA: Color = ofRGB(255, 128, 255)

        /**
         * Represents the color Dark Red with an RGB value of (139, 0, 0).
         *
         * DARK_RED is a predefined constant in the `Color` class and is commonly used
         * for rendering elements with a deep red tone.
         *
         *  * Red component: 139
         *  * Green component: 0
         *  * Blue component: 0
         *
         * @see ofRGB
         * @see Color
         *
         * @since 1.0.0
         */
        val DARK_RED: Color = ofRGB(139, 0, 0)

        /**
         * Represents the dark green color with the RGB value (0, 100, 0).
         *
         * This predefined constant can be used to access a standard dark green color.
         *
         *  * Red: 0
         *  * Green: 100
         *  * Blue: 0
         *
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val DARK_GREEN: Color = ofRGB(0, 100, 0)

        /**
         * Represents a constant color defined as "Dark Blue."
         *
         * This color is represented in the RGB color model with:
         *
         *  * Red: 0
         *  * Green: 0
         *  * Blue: 139
         *
         * Commonly used for themes, designs, and visual elements requiring a darker blue shade.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val DARK_BLUE: Color = ofRGB(0, 0, 139)

        /**
         * Represents the color "Dark Yellow" with RGB values of (204, 204, 0).
         *
         * This constant provides a predefined darkened version of the yellow color, useful for design and styling purposes,
         * such as when a deeper or muted shade of yellow is required.
         *
         * The RGB composition is as follows:
         *
         *  * Red: 204
         *  * Green: 204
         *  * Blue: 0
         *
         * @since 1.0.0
         */
        val DARK_YELLOW: Color = ofRGB(204, 204, 0)

        /**
         * Represents a predefined color constant for dark cyan.
         *
         * The color is created using the RGB color model with the following values:
         *
         *  * **Red:** 0
         *  * **Green:** 139
         *  * **Blue:** 139
         *
         * This color can be utilized for styling and rendering purposes where a dark shade
         * of cyan is required.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val DARK_CYAN: Color = ofRGB(0, 139, 139)

        /**
         * Represents a constant predefined color with the RGB values (139, 0, 139).
         *
         * DARK_MAGENTA is a darker shade of magenta, offering a rich and deep color tone.
         *
         * This color can be utilized to add depth or contrast in graphical applications,
         * user interface designs, or any context where precise color customization is required.
         *
         * **Key Properties:**
         *
         *  * **Red:** 139
         *  * **Green:** 0
         *  * **Blue:** 139
         *
         * @see .ofRGB
         * @since 1.0.0
         */
        val DARK_MAGENTA: Color = ofRGB(139, 0, 139)

        /**
         * Represents a light orange color with the RGB value of (255, 165, 0).
         *
         * LIGHT_ORANGE can be used to represent a softer and less saturated orange shade,
         * commonly observed in pastel color palettes or used to create warm and inviting designs.
         *
         *  * Red: 255
         *  * Green: 165
         *  * Blue: 0
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val LIGHT_ORANGE: Color = ofRGB(255, 165, 0)

        /**
         * Represents the color orange with RGB values (255, 140, 0).
         *
         * This color is commonly associated with warmth, energy, and enthusiasm.
         * It is derived using the [ofRGB] factory method,
         * ensuring valid RGB values within the range of 0 to 255.
         *
         * Usage in visual design often conveys attention and vibrancy, making it a popular
         * choice for dynamic and engaging applications.
         *
         *  * Red Component: 255
         *  * Green Component: 140
         *  * Blue Component: 0
         *
         * @see .ofRGB
         * @since 1.0.0
         */
        val ORANGE: Color = ofRGB(255, 140, 0)

        /**
         * A predefined color constant representing the shade of orange commonly used to denote
         * confidential or restricted information in visual designs or user interfaces.
         *
         * This color is represented by the RGB values (252, 65, 34).
         *
         * @since 1.0.0
         */
        val CONFIDENTIAL_ORANGE: Color = ofRGB(252, 65, 34)

        /**
         * Represents a constant color defined as "PINK" with the RGB values of (255, 192, 203).
         *
         * This color is typically associated with a light shade of pink often used for aesthetic
         * or decorative purposes, providing a soft and warm appearance.
         *
         * The RGB values for PINK are:
         *
         *  * Red: 255
         *  * Green: 192
         *  * Blue: 203
         *
         * This constant can be used directly to reference the color in various graphical or color-based
         * functionalities, ensuring consistency and convenience when working with predefined colors.
         *
         * @since 1.0.0
         */
        val PINK: Color = ofRGB(255, 192, 203)

        /**
         * Represents the color brown with an RGB value of (139, 69, 19).
         *
         * The BROWN color is commonly associated with the natural color of wood, earth, and other organic materials.
         *
         *  * **Red:** 139
         *  * **Green:** 69
         *  * **Blue:** 19
         *
         * This color can be used in applications requiring predefined color constants.
         *
         * @since 1.0.0
         */
        val BROWN: Color = ofRGB(139, 69, 19)

        /**
         * Represents the color GOLD with an RGB value of (255, 215, 0).
         *
         * This constant is predefined to facilitate usage of the gold color in applications
         * where a consistent and standardized representation is required.
         *
         * Key features of GOLD:
         *
         *  * Red component: 255
         *  * Green component: 215
         *  * Blue component: 0
         *
         * Can be utilized in various operations such as color manipulation, gradient generation,
         * and transformation to alternate color models.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val GOLD: Color = ofRGB(255, 215, 0)

        /**
         * Represents the color silver using an RGB value of (192, 192, 192).
         *
         * This constant is commonly used to define a standard silver color in
         * various graphical user interface components or styling.
         *
         *  * Red: 192
         *  * Green: 192
         *  * Blue: 192
         *
         * @see Color
         * @since 1.0.0
         */
        val SILVER: Color = ofRGB(192, 192, 192)

        /**
         * Represents the color Plum, a shade with RGB values of (221, 160, 221).
         *
         * This is a static and immutable color constant provided for convenience and reuse.
         *
         *  * Red component: 221
         *  * Green component: 160
         *  * Blue component: 221
         *
         * @since 1.0.0
         */
        val PLUM: Color = ofRGB(221, 160, 221)

        /**
         * Represents the color "Ivory" with RGB values of (255, 255, 240).
         *
         * This static constant provides a predefined color object
         * for applications requiring a soft beige-like color representation.
         *
         *  * Red: 255
         *  * Green: 255
         *  * Blue: 240
         *
         * @since 1.0.0
         */
        val IVORY: Color = ofRGB(255, 255, 240)

        /**
         * A constant representing the color TEAL.
         *
         * This is defined as a combination of red, green, and blue components
         * with values (0, 128, 128) respectively in the RGB color model.
         *
         *  * Red: 0
         *  * Green: 128
         *  * Blue: 128
         *
         * @since 1.0.0
         */
        val TEAL: Color = ofRGB(0, 128, 128)

        /**
         * Represents the color LIME, characterized by its RGB components of (204, 255, 0).
         *
         * LIME is a bright green color, often associated with vibrancy and energy.
         * This constant is defined with full green intensity, no red, and no blue.
         *
         * Use this predefined color in applications where LIME is required without
         * explicitly specifying its RGB values.
         *
         * @since 1.0.0
         */
        val LIME: Color = ofRGB(204, 255, 0)

        /**
         * Represents the TAN color as a constant with RGB values (210, 180, 140).
         *
         * TAN is a pale shade of brown resembling tanned leather. It is commonly used
         * in applications requiring neutral and earthy tones.
         *
         *  * Red: 210
         *  * Green: 180
         *  * Blue: 140
         *
         * The color value can be utilized in various graphical or design contexts
         * where consistency and predefined colors are needed.
         *
         * @since 1.0.0
         */
        val TAN: Color = ofRGB(210, 180, 140)

        /**
         * Represents the color NAVY with an RGB value of (0, 0, 128).
         *
         * NAVY is a dark shade of blue, often associated with traditional navy uniforms.
         * The color is defined using the RGB model, where:
         *
         *  * Red: 0
         *  * Green: 0
         *  * Blue: 128
         *
         * This constant is immutable and commonly used to represent nautical themes or to distinguish dark blue in design systems.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val NAVY: Color = ofRGB(0, 0, 128)

        /**
         * Represents the chocolate color with an RGB value of (210, 105, 30).
         *
         * The color chocolate is a rich, earthy brown shade commonly used in design and art
         * to evoke warmth, comfort, and reliability. It can also be used to represent elements
         * inspired by nature or food.
         *
         * This color object is immutable and can be used in applications requiring specific color definitions.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val CHOCOLATE: Color = ofRGB(210, 105, 30)

        /**
         * Represents the CORAL color with RGB values (255, 127, 80).
         *
         * CORAL is a predefined color constant in the `Color` class that provides
         * a distinct appearance resembling the natural coral hue. This color is often
         * used to represent vibrant and warm elements in visual designs.
         *
         * The RGB values of CORAL are:
         *
         *  * Red: 255
         *  * Green: 127
         *  * Blue: 80
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val CORAL: Color = ofRGB(255, 127, 80)

        /**
         * Represents the color Slate Blue.
         *
         * This color is defined using the RGB color model with the following component values:
         *
         *  * Red: 106
         *  * Green: 90
         *  * Blue: 205
         *
         * Slate Blue is a shade of blue with a subtle violet undertone, commonly used in visual applications
         * for its sophisticated and calming appearance.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val SLATE_BLUE: Color = ofRGB(106, 90, 205)

        /**
         * A predefined constant for the color "Medium Violet Red," represented by the RGB components
         * (199, 21, 133).
         *
         * This color is a moderate shade of violet-red, often used to represent vibrancy, energy,
         * and creativity in design.
         *
         * To utilize this color, it can be referred to directly using this constant in the Color class.
         *
         *  * Red: 199
         *  * Green: 21
         *  * Blue: 133
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val MEDIUM_VIOLET_RED: Color = ofRGB(199, 21, 133)

        /**
         * Represents the color "Seashell" with an RGB value of (255, 245, 238).
         *
         * This color is part of the predefined color palette and is often used to represent a pale, soft beige tone,
         * resembling the appearance of seashells.
         *
         *  * Red: 255
         *  * Green: 245
         *  * Blue: 238
         *
         * This color can be utilized in various applications such as graphical user interfaces, data visualizations,
         * or any context where a predefined soft and neutral color tone is required.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val SEASHELL: Color = ofRGB(255, 245, 238)

        /**
         * Represents the color "Snow" with an RGB composition of (255, 250, 250).
         *
         * This constant can be used to denote a soft, nearly white color in applications such as UI design,
         * graphics rendering, and other areas where specific color definitions are required.
         *
         *  * Red component: 255
         *  * Green component: 250
         *  * Blue component: 250
         *
         * This color provides a subtle tone, often utilized for backgrounds or highlighting elements where a
         * gentle and non-distracting aesthetic is desired.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val SNOW: Color = ofRGB(255, 250, 250)

        /**
         * A constant representing the color Beige with an RGB value of (245, 245, 220).
         *
         * Beige is a pale, light brown color that resembles sand or parchment. It is often
         * used in design and art to convey neutrality, warmth, or a sense of calmness.
         *
         * Can be utilized in contexts where a soft, earthy tone is desired.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val BEIGE: Color = ofRGB(245, 245, 220)

        /**
         * Represents the color Khaki with an approximate RGB composition of
         *
         *  * Red: 240
         *  * Green: 230
         *  * Blue: 140
         *
         * Khaki is a pale light brown color typically associated with earthy tones
         * and is often used in designs to evoke a sense of nature, neutrality, or warmth.
         * It is commonly employed in themes related to military camouflage, fashion, and
         * natural settings.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val KHAKI: Color = ofRGB(240, 230, 140)

        /**
         * Represents the predefined color "Peach Puff" with an RGB composition of (255, 218, 185).
         * This color is often associated with a warm, light, and pastel tone, resembling the soft hue of a peach's flesh.
         *
         *  * Red: 255
         *  * Green: 218
         *  * Blue: 185
         *
         * Use this constant to apply the "Peach Puff" color in various graphical or UI elements where
         * predefined standardized colors are required.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val PEACH_PUFF: Color = ofRGB(255, 218, 185)

        /**
         * Represents the color Wheat, a shade of light brown with RGB components (245, 222, 179).
         *
         * This color is commonly used in design and visualization for its subtle and soft appearance.
         * It is predefined in the `Color` class for convenience when working with this specific hue.
         *
         *  * **Red Component:** 245
         *  * **Green Component:** 222
         *  * **Blue Component:** 179
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val WHEAT: Color = ofRGB(245, 222, 179)

        /**
         * Represents the color Lavender with an RGB value of (230, 230, 250).
         *
         * Lavender is a light shade of purple, often associated with calmness and tranquility.
         *
         * This constant can be used to reference the predefined Lavender color in various
         * graphical or visual applications where consistent color representation is desired.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val LAVENDER: Color = ofRGB(230, 230, 250)

        /**
         * Represents the Fuchsia color, a vibrant magenta shade with full red and blue channels.
         *
         * The RGB values for Fuchsia are as follows:
         *
         *  * Red: 255
         *  * Green: 0
         *  * Blue: 255
         *
         * This is a predefined constant color representation for usage across various
         * color manipulation and representation contexts.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val FUCHSIA: Color = ofRGB(255, 0, 255)

        /**
         * Represents the color "Lavender Blush" with RGB values of (255, 240, 245).
         *
         * This constant is part of the predefined color palette and provides a soft, pastel pink color
         * often associated with delicate, floral tones. It is suitable for use in various design and
         * UI elements where a subtle pink shade is desired.
         *
         *  * **Red:** 255
         *  * **Green:** 240
         *  * **Blue:** 245
         *
         * Can be created using the `ofRGB(int red, int green, int blue)` factory method
         * or accessed directly as a predefined constant.
         *
         * @see ofRGB
         * @see Color
         *
         * @since 1.0.0
         */
        val LAVENDER_BLUSH: Color = ofRGB(255, 240, 245)

        /**
         * Represents the color "Old Lace" with an RGB value of (253, 245, 230).
         *
         * Old Lace is a very light, almost off-white shade with a subtle warm undertone,
         * often used to depict soft, elegant, or vintage themes in design.
         *
         *  * Red: 253
         *  * Green: 245
         *  * Blue: 230
         *
         * This color constant can be utilized in graphics or UI design where predefined
         * color standards are needed.
         *
         * @see ofRGB
         * @since 1.0.0
         */
        val OLD_LACE: Color = ofRGB(253, 245, 230)

        /**
         * Predefined color constant representing "Medium Aquamarine".
         *
         * This color is defined using the RGB color model with the following values:
         *
         *  * Red: 102
         *  * Green: 205
         *  * Blue: 170
         *
         * It is a medium shade of aquamarine, providing a soft greenish-blue tone.
         *
         * @since 1.0.0
         */
        val MEDIUM_AQUAMARINE: Color = ofRGB(102, 205, 170)

        /**
         * A constant representing the color AQUA with an RGB value of (0, 255, 255).
         *
         * AQUA is a bright cyan color often associated with water or a light
         * bluish-green tone. This constant can be used wherever a predefined
         * aqua/cyan color is required in the application.
         *
         * @since 1.0.0
         */
        val AQUA: Color = ofRGB(0, 255, 255)

        /**
         * A constant representing the color Medium Slate Blue with an RGB value of (123, 104, 238).
         *
         * Medium Slate Blue is a shade of blue that is primarily used in user interfaces or graphic design
         * to convey a vibrant and rich visual appearance.
         *
         *  * Red Component: 123
         *  * Green Component: 104
         *  * Blue Component: 238
         *
         * @since 1.0.0
         */
        val MEDIUM_SLATE_BLUE: Color = ofRGB(123, 104, 238)

        /**
         * Represents the color hot pink, defined by the RGB values (255, 105, 180).
         *
         * This constant is part of a predefined set of colors and provides a vibrant shade of pink
         * often used in graphical and UI design to attract attention or convey a playful tone.
         *
         * @since 1.0.0
         */
        val HOT_PINK: Color = ofRGB(255, 105, 180)

        /**
         * Represents the color Salmon with an RGB value of (250, 128, 114).
         *
         * It is a shade of pinkish-orange and is often used in user interfaces or graphic designs
         * to convey warmth, friendliness, or creativity. The RGB values denote its intensity
         * on the red, green, and blue color channels respectively.
         *
         *  * Red: 250
         *  * Green: 128
         *  * Blue: 114
         *
         * @since 1.0.0
         */
        val SALMON: Color = ofRGB(250, 128, 114)

        /**
         * A constant representing the color "Lavender Purple" with its RGB value (230, 168, 230).
         *
         * This color can be used in creating graphical user interfaces, visual elements,
         * or for any other application requiring a predefined lavender-purple shade.
         *
         * @since 1.0.0
         */
        val LAVENDER_PURPLE: Color = ofRGB(230, 168, 230)

        /**
         * Parses a string representation of a color into a `Color` object.
         * The method supports various color formats such as `rgba`, `rgb`, `hsla`, `hsl`,
         * `hsva`, `hsv`, `cmyka`, `cmyk`, `hwba`, `hwb`, `hex`, `xyz`, and `lab`.
         *
         * @param input the string representation of the color to be parsed. It should be
         * in a valid color format
         * @return the [Result] containing the `Color` object created from the given input string.
         * @throws dev.tommasop1804.kutils.exceptions.MalformedInputException if the input string is not in a valid color format
         * @throws NoMatchingFormatException if the input string does not match any supported color format
         * @since 1.0.0
         */
        infix fun parse(input: String) = runCatching {
            val s = -input.trim()
            when {
                s.startsWith("rgba") -> ColorParser.parseRgba(s)
                s.startsWith("rgb") -> ColorParser.parseRgb(s)
                s.startsWith("hsla") -> ColorParser.parseHsla(s)
                s.startsWith("hsl") -> ColorParser.parseHsl(s)
                s.startsWith("hsva") -> ColorParser.parseHsva(s)
                s.startsWith("hsv") -> ColorParser.parseHsv(s)
                s.startsWith("hsba") -> ColorParser.parseHsba(s)
                s.startsWith("hsb") -> ColorParser.parseHsb(s)
                s.startsWith("cmyka") -> ColorParser.parseCmyka(s)
                s.startsWith("cmyk") -> ColorParser.parseCmyk(s)
                s.startsWith("hwba") -> ColorParser.parseHwba(s)
                s.startsWith("hwb") -> ColorParser.parseHwb(s)
                s.startsWith("#") || s.startsWith("0x") -> ColorParser.parseHex(s)
                s.startsWith("xyz") -> ColorParser.parseXyz(s)
                s.startsWith("lab") -> ColorParser.parseLab(s)
                else -> throw NoMatchingFormatException("Color format not supported: $input")
            }
        }

        /**
         * Creates a new Color instance from the specified RGB components.
         *
         * @param red The red component of the color, must be in the range 0-255.
         * @param green The green component of the color, must be in the range 0-255.
         * @param blue The blue component of the color, must be in the range 0-255.
         * @return A new Color instance with the specified RGB values.
         * @throws ValidationFailedException If any of the input values are outside the range of 0-255.
         * @since 1.0.0
         */
        fun ofRGB(red: Int, green: Int, blue: Int): Color {
            if (red !in 0..255 || green !in 0..255 || blue !in 0..255)
                throw ValidationFailedException("Color RGB components must be between 0 and 255.")
            return Color(red, green, blue)
        }
        /**
         * Creates a representation of a color using its RGB components provided as a Triple.
         *
         * @param color A Triple containing the red, green, and blue components of the color.
         * Each value in the triple must be an integer within the range 0-255.
         * @throws ValidationFailedException If any of the input values are outside the range of 0-255.
         * @return A new Color instance with the specified RGB values.
         * @since 1.0.0
         */
        infix fun ofRGB(color: Int3) = ofRGB(color.first, color.second, color.third)

        /**
         * Creates a new color instance from the specified red, green, blue, and alpha components.
         *
         * @param red The red component of the color, in the range [0, 255].
         * @param green The green component of the color, in the range [0, 255].
         * @param blue The blue component of the color, in the range [0, 255].
         * @param alpha The alpha (transparency) component of the color.
         * @return A new instance of the Color class with the specified RGBA components.
         * @throws ValidationFailedException If any of the components is outside the allowed range [0, 255].
         * @since 1.0.0
         */
        fun ofRGBA(red: Int, green: Int, blue: Int, alpha: Percentage): Color {
            if (red !in 0..255 || green !in 0..255 || blue !in 0..255 || alpha.isOverflowing)
                throw ValidationFailedException("Color RGBA components must be between 0 and 255 for colors and between 0 and 1 for alpha.")
            return Color(red, green, blue, alpha.round())
        }
        /**
         * Combines the RGBA color components into a single color representation using a quadruple input.
         *
         * @param color A quadruple containing four values:
         *              - `first`: The red component of the color (0-255).
         *              - `second`: The green component of the color (0-255).
         *              - `third`: The blue component of the color (0-255).
         *              - `fourth`: The alpha component of the color as a percentage.
         * @since 1.0.0
         */
        @JvmName("ofRGBAQuadrupleWithPercentage")
        infix fun ofRGBA(color: Quadruple<Int, Int, Int, Percentage>) = ofRGBA(color.first, color.second, color.third, color.fourth)

        /**
         * Creates a new color instance from the specified red, green, blue, and alpha components.
         *
         * @param red The red component of the color, in the range [0, 255].
         * @param green The green component of the color, in the range [0, 255].
         * @param blue The blue component of the color, in the range [0, 255].
         * @param alpha The alpha (transparency) component of the color, in the range [0, 255].
         * @return A new instance of the Color class with the specified RGBA components.
         * @throws ValidationFailedException If any of the components is outside the allowed range [0, 255].
         * @since 1.0.0
         */
        fun ofRGBA(red: Int, green: Int, blue: Int, alpha: Int): Color {
            if (red !in 0..255 || green !in 0..255 || blue !in 0..255 || alpha !in 0..255)
                throw ValidationFailedException("Color RGBA components must be between 0 and 255.")
            return Color(red, green, blue, Percentage(alpha / 255.0, true))
        }
        /**
         * Combines the RGBA color components into a single color representation using a quadruple input.
         *
         * @param color A quadruple containing four values:
         *              - `first`: The red component of the color (0-255).
         *              - `second`: The green component of the color (0-255).
         *              - `third`: The blue component of the color (0-255).
         *              - `fourth`: The alpha component of the color as a percentage (0.0-255).
         * @since 1.0.0
         */
        infix fun ofRGBA(color: Int4) = ofRGBA(color.first, color.second, color.third, color.fourth)

        /**
         * Converts a hexadecimal color string into a [Color] object.
         * The hexadecimal string can be in the formats: `#RGB`, `#RRGGBB`, or `#RRGGBBAA`.
         * It supports optional leading `#`.
         *
         * @param hex The hexadecimal string representation of the color.
         * @return A [Color] instance with the corresponding red, green, blue, and optionally alpha values.
         * @since 1.0.0
         */
        infix fun ofHEX(hex: String): Color {
            var hex = hex
            hex.validateInputFormat("^#?[0-9A-Fa-f]{3}$|^#?[0-9A-Fa-f]{6}$|^#?[0-9A-Fa-f]{8}$".toRegex(), "Invalid HEX format: $hex")

            hex = hex.replaceFirst("#".toRegex(), "")
            if (hex.length == 3) hex = hex[0].toString() + hex[0] + hex[1] + hex[1] + hex[2] + hex[2]

            return Color(
                hex.take(2).toInt(16),
                hex.substring(2, 4).toInt(16),
                hex.substring(4, 6).toInt(16),
                Percentage(if (hex.length == 8) hex.substring(6, 8).toInt(16).coerceIn(0, 255) / 255.0 else 1.0, true)
            )
        }
        /**
         * Converts a hexadecimal color hex into a [Color] object.
         * The hexadecimal string can be in the formats: `#RGB`, `#RRGGBB`, or `#RRGGBBAA`.
         *
         * @param hex The hexadecimal string representation of the color.
         * @return A [Color] instance with the corresponding red, green, blue, and optionally alpha values.
         * @since 1.0.0
         */
        infix fun ofHEX(hex: Hex): Color {
            var hex = hex.toString(Hex.HexSymbol.NONE)

            if (hex.length == 3) hex = hex[0].toString() + hex[0] + hex[1] + hex[1] + hex[2] + hex[2]

            return Color(
                hex.take(2).toInt(16),
                hex.substring(2, 4).toInt(16),
                hex.substring(4, 6).toInt(16),
                Percentage(if (hex.length == 8) hex.substring(6, 8).toInt(16).coerceIn(0, 255) / 255.0 else 1.0, true)
            )
        }

        /**
         * Creates a new Color instance from a HEXA (hexadecimal with alpha) string.
         *
         * The input string must be in the format `RRGGBBAA`, optionally prefixed with `#`.
         * The method validates the input format and throws an exception if the string is invalid.
         *
         * @param hex A string representing the color in HEXA format. Must follow the pattern `^#?[0-9A-Fa-f]{8}$`.
         * @return A Color instance constructed from the provided HEXA string.
         * @throws IllegalArgumentException If the provided string does not match the required HEXA format.
         * @since 1.0.0
         */
        infix fun ofHEXA(hex: String): Color {
            hex.validateInputFormat("^#?[0-9A-Fa-f]{8}$".toRegex(), "Invalid HEX format")
            return ofHEX(hex)
        }
        /**
         * Creates a new Color instance from a HEXA (hexadecimal with alpha).
         *
         * The input string must be in the format `RRGGBBAA`.
         * The method validates the input format and throws an exception if the string is invalid.
         *
         * @param hex A string representing the color in HEXA format. Must follow the pattern `^#?[0-9A-Fa-f]{8}$`.
         * @return A Color instance constructed from the provided HEXA string.
         * @throws IllegalArgumentException If the provided string does not match the required HEXA format.
         * @since 1.0.0
         */
        infix fun ofHEXA(hex: Hex) = ofHEX(hex)

        /**
         * Creates a color defined by the HSL (Hue, Saturation, Lightness) color model.
         *
         * @param hue The hue component of the color, specified in degrees (0.0 to 360.0).
         * @param saturation The saturation component of the color, specified as a Percentage.
         * @param lightness The lightness component of the color, specified as a Percentage.
         * @return A new instance of Color with the corresponding RGB and alpha values derived from the HSL input.
         * @since 1.0.0
         */
        fun ofHSL(hue: Double, saturation: Percentage, lightness: Percentage) = ofHSLA(hue, saturation, lightness, FULL)
        /**
         * Creates a color representation in the HSL color space using the given SHL (saturation, hue, lightness) components.
         *
         * @param color A Triple of Doubles where:
         * - First value represents saturation.
         * - Second value represents hue.
         * - Third value represents lightness.
         * @return A new instance of Color with the corresponding RGB and alpha values derived from the HSL input.
         * @since 1.0.0
         */
        infix fun ofSHL(color: Triple<Double, Percentage, Percentage>) = ofHSL(color.first, color.second, color.third)

        /**
         * Creates a color instance from the given HSLA (hue, saturation, lightness, alpha) values.
         * The hue value is normalized within 0-360 degrees. The saturation, lightness, and alpha values
         * are clamped within the range of 0.0 to 1.0.
         *
         * @param hue The hue component of the color, representing the angle in degrees on the color wheel.
         *            It is automatically wrapped within the 0-360 range.
         * @param saturation The saturation component of the color, representing the intensity of the color.
         *                   It must be between 0 (gray) and 100 (full color).
         * @param lightness The lightness component of the color, representing the brightness of the color.
         *                  It must be between 0 (black) and 100 (white).
         * @param alpha The alpha component of the color, representing the transparency.
         *              It must be between 0 (completely transparent) and 100 (completely opaque).
         * @return A new instance of Color with the corresponding RGB and alpha values derived from the HSLA input.
         * @since 1.0.0
         */
        fun ofHSLA(hue: Double, saturation: Percentage, lightness: Percentage, alpha: Percentage): Color {
            val saturation = saturation.toDouble(true)
            val lightness = lightness.toDouble(true)
            val c = (1 - abs(2 * lightness - 1)) * saturation
            val x = c * (1 - abs((hue / 60) % 2 - 1))
            val m = lightness - c / 2

            val rgb = when (hue) {
                in 0.0..60.0 -> Triple(c, x, 0.0)
                in 60.0..120.0 -> Triple(x, c, 0.0)
                in 120.0..180.0 -> Triple(0.0, c, x)
                in 180.0..240.0 -> Triple(0.0, x, c)
                in 240.0..300.0 -> Triple(x, 0.0, c)
                in 300.0..360.0 -> Triple(c, 0.0, x)
                else -> Triple(0.0, 0.0, 0.0)
            }

            return Color(
                ((rgb.first + m) * 255).toInt(),
                ((rgb.second + m) * 255).toInt(),
                ((rgb.third + m) * 255).toInt(),
                alpha
            )
        }
        /**
         * Creates a color instance from the provided HSLA (Hue, Saturation, Lightness, Alpha) components.
         *
         * @param color A quadruple where the first value represents the hue (H) in degrees,
         * the second value is the saturation (S) as a percentage,
         * the third value is lightness (L) as a percentage,
         * and the fourth value is the alpha (A) value representing opacity.
         * @return A new instance of Color with the corresponding RGB and alpha values derived from the HSLA input.
         * @since 1.0.0
         */
        infix fun ofHSLA(color: Quadruple<Double, Percentage, Percentage, Percentage>) = ofHSLA(color.first, color.second, color.third, color.fourth)

        /**
         * Converts HSV (Hue, Saturation, Value) color components to a [Color] object.
         *
         * @param hue The hue component of the color, in degrees (0.0 to 360.0).
         * @param saturation The saturation component of the color, a Percentage.
         * @param value The value (brightness) component of the color, a Percentage.
         * @return A [Color] object representing the RGB equivalent of the specified HSV values.
         * @since 1.0.0
         */
        fun ofHSV(hue: Double, saturation: Percentage, value: Percentage): Color {
            val h = hue % 360
            val s = min(1.0, max(0.0, saturation.toDouble(true)))
            val v = min(1.0, max(0.0, value.toDouble(true)))

            val c = v * s; val x = c * (1 - abs((h / 60) % 2 - 1)); val m = v - c
            var r = 0.0; var g = 0.0; var b = 0.0

            when (h) {
                in 0.0..<60.0 -> {
                    r = c; g = x; b = 0.0
                }
                in 60.0..<120.0 -> {
                    r = x; g = c; b = 0.0
                }
                in 120.0..<180.0 -> {
                    r = 0.0; g = c; b = x
                }
                in 180.0..<240.0 -> {
                    r = 0.0; g = x; b = c
                }
                in 240.0..<300.0 -> {
                    r = x; g = 0.0; b = c
                }
                in 300.0..<360.0 -> {
                    r = c; g = 0.0; b = x
                }
            }

            r = (r + m) * 255
            g = (g + m) * 255
            b = (b + m) * 255

            return Color(r.toInt(), g.toInt(), b.toInt())
        }
        /**
         * Creates a representation of a color based on the HSV (Hue, Saturation, Value) model.
         *
         * @param color A Triple containing the Hue, Saturation, and Value components of the color.
         *              Hue is represented as a Double in the range [0.0, 360.0],
         *              Saturation and Value are represented as Percentages.
         * @return A Color object representing the RGB equivalent of the specified HSV values.
         * @since 1.0.0
         */
        infix fun ofHSV(color: Triple<Double, Percentage, Percentage>) = ofHSV(color.first, color.second, color.third)

        /**
         * Converts HSVA (Hue, Saturation, Value, Alpha) color components to a [Color] object.
         *
         *
         * @param hue The hue component of the color, in degrees (0.0 to 360.0).
         * @param saturation The saturation component of the color, a Percentage.
         * @param value The value (brightness) component of the color, a Percentage.
         * @param alpha The alpha (transparency) component of the color, a Percentage.
         * @return A [Color] object representing the RGBA equivalent of the specified HSVA values.
         * @since 1.0.0
         */
        fun ofHSVA(hue: Double, saturation: Percentage, value: Percentage, alpha: Percentage): Color {
            val color = ofHSV(hue, saturation, value)
            return Color(color.red, color.green, color.blue, alpha)
        }
        /**
         * Creates a new color instance based on the provided HSVA (Hue, Saturation, Value, Alpha) components.
         *
         * - The first value corresponds to the hue (0.0 to 360.0 degrees).
         * - The second value corresponds to the saturation (Percentage).
         * - The third value corresponds to the value (brightness) (Percentage).
         * - The fourth value corresponds to the alpha (transparency) (Percentage).
         *
         * @param color A quadruple of doubles representing hue, saturation, value, and alpha components.
         * @return A Color object representing the RGBA equivalent of the specified HSVA values.
         * @since 1.0.0
         */
        infix fun ofHSVA(color: Quadruple<Double, Percentage, Percentage, Percentage>) = ofHSVA(color.first, color.second, color.third, color.fourth)

        /**
         * Converts HSB (Hue, Saturation, Brightness) values to a Color object.
         *
         * @param hue The hue component of the color, in degrees from 0 to 360.
         * @param saturation The saturation component of the color.
         * @param brightness The brightness component of the color.
         * @return A Color object representing the corresponding RGB color.
         * @since 1.0.0
         */
        fun ofHSB(hue: Double, saturation: Percentage, brightness: Percentage): Color {
            val h = hue / 60.0
            val i = floor(h).toInt()
            val f = h - i

            val saturation = saturation.toDouble(true)
            val brightness = brightness.toDouble(true)

            val p = brightness * (1 - saturation)
            val q = brightness * (1 - saturation * f)
            val t = brightness * (1 - saturation * (1 - f))

            val (r, g, b) = when (i % 6) {
                0 -> Triple(brightness, t, p)
                1 -> Triple(q, brightness, p)
                2 -> Triple(p, brightness, t)
                3 -> Triple(p, q, brightness)
                4 -> Triple(t, p, brightness)
                5 -> Triple(brightness, p, q)
                else -> Triple(0.0,0.0,0.0)
            }

            return Color((r * 255).roundToInt(), (g * 255).roundToInt(), (b * 255).roundToInt())
        }
        /**
         * Converts HSB (Hue, Saturation, Brightness) values to a Color object.
         *
         * @param color a Triple containing hue, saturation, and brightness components.
         * @return A Color object representing the corresponding RGB color.
         * @since 1.0.0
         */
        infix fun ofHSB(color: Triple<Double, Percentage, Percentage>) = ofHSB(color.first, color.second, color.third)

        /**
         * Creates a color instance using HSBA (Hue, Saturation, Brightness, Alpha) model.
         *
         * @param hue The hue component of the color, typically in the range of 0.0 to 360.0.
         * @param saturation The saturation component of the color.
         * @param brightness The brightness component of the color.
         * @param alpha The alpha component of the color.
         * @return A Color instance corresponding to the specified HSBA values.
         * @since 1.0.0
         */
        fun ofHSBA(hue: Double, saturation: Percentage, brightness: Percentage, alpha: Percentage): Color {
            val color = ofHSB(hue, saturation, brightness)
            return Color(color.red, color.green, color.blue, alpha)
        }
        /**
         * Converts the given color representation in HSBA format to another format using the specified components.
         *
         * @param color A Double4 object representing the HSBA color,
         * where the first value corresponds to hue, the second to saturation,
         * the third to brightness, and the fourth to alpha.
         * @since 1.0.0
         */
        infix fun ofHSBA(color: Quadruple<Double, Percentage, Percentage, Percentage>) = ofHSBA(color.first, color.second, color.third, color.fourth)

        /**
         * Creates a new `Color` instance based on the given CMYK (Cyan, Magenta, Yellow, Key/Black) color values.
         *
         * @param cyan the cyan component of the color
         * @param magenta the magenta component of the color
         * @param yellow the yellow component of the color
         * @param key the key (black) component of the color
         * @return a new instance of `Color` representing the RGB equivalent of the input CMYK values
         * @since 1.0.0
         */
        fun ofCMYK(cyan: Percentage, magenta: Percentage, yellow: Percentage, key: Percentage): Color {
            val c = min(1.0, max(0.0, cyan.toDouble(true)))
            val m = min(1.0, max(0.0, magenta.toDouble(true)))
            val y = min(1.0, max(0.0, yellow.toDouble(true)))
            val k = min(1.0, max(0.0, key.toDouble(true)))

            val r = ((1 - min(1.0, c * (1 - k) + k)) * 255).roundToInt()
            val g = ((1 - min(1.0, m * (1 - k) + k)) * 255).roundToInt()
            val b = ((1 - min(1.0, y * (1 - k) + k)) * 255).roundToInt()

            return Color(r, g, b)
        }
        /**
         * Converts a CMYK color represented as a quadruple of doubles into its corresponding color format.
         *
         * - first: Cyan component of the color.
         * - second: Magenta component of the color.
         * - third: Yellow component of the color.
         * - fourth: Key (black) component of the color.
         * @param color A quadruple containing the CMYK values.
         * @return a new instance of Color representing the RGB equivalent of the input CMYK values
         * @since 1.0.0
         */
        infix fun ofCMYK(color: MonoQuadruple<Percentage>) = ofCMYK(color.first, color.second, color.third, color.fourth)

        /**
         * Creates a `Color` instance from CMYK (Cyan, Magenta, Yellow, Key/Black) and an Alpha (transparency) value.
         * The CMYK values are used to calculate the RGB components, and the Alpha value is included in the result.
         *
         * @param cyan The cyan component of the color, represented as a value between 0.0 and 1.0.
         * @param magenta The magenta component of the color.
         * @param yellow The yellow component of the color.
         * @param key The black component of the color.
         * @param alpha The alpha (transparency) value of the color.
         * @return A `Color` instance with the calculated RGB values and the specified alpha value.
         * @since 1.0.0
         */
        fun ofCMYKA(cyan: Percentage, magenta: Percentage, yellow: Percentage, key: Percentage, alpha: Percentage): Color {
            val color = ofCMYK(cyan, magenta, yellow, key)
            return Color(color.red, color.green, color.blue, alpha)
        }
        /**
         * Converts a color represented by a quintuple of CMYKA (Cyan, Magenta, Yellow, Key/Black, Alpha) values
         * into the desired representation by delegating to another method.
         *
         * - First element is the cyan component
         * - Second element is the magenta component
         * - Third element is the yellow component
         * - Fourth element is the key/black component
         * - Fifth element is the alpha component
         * @param color A Quintuple of Double values representing the CMYKA color components.
         * @return A Color instance with the calculated RGB values and the specified alpha value.
         * @since 1.0.0
         */
        infix fun ofCMYKA(color: MonoQuintuple<Percentage>) = ofCMYKA(color.first, color.second, color.third, color.fourth, color.fifth)

        /**
         * Creates a color instance from the provided HWB (hue, whiteness, blackness) values.
         * The hue value is normalized within 0-360 degrees. Whiteness and blackness values
         * are clamped within the range of 0.0 to 1.0. The derived color is computed based on
         * an HSLA representation with saturation and lightness values determined by the given
         * whiteness and blackness inputs.
         *
         * @param hue The hue component of the color, representing the angle in degrees on the color wheel.
         *            It is automatically wrapped within the 0-360 range.
         * @param whiteness The whiteness component of the color, representing the white blending factor.
         *                  It must be between 0 (no whiteness) and 100 (completely white).
         * @param blackness The blackness component of the color, representing the black blending factor.
         *                  It must be between 0 (no blackness) and 100 (completely black).
         * @return A new instance of Color corresponding to the normalized HWB input values.
         * @since 1.0.0
         */
        fun ofHWB(hue: Double, whiteness: Percentage, blackness: Percentage): Color {
            val hue = ((hue % 360) + 360) % 360 / 360.0

            val i = floor(hue * 6).toInt()
            val f = hue * 6 - i
            val q = 1 - f

            var r = 0.0
            var g = 0.0
            var b = 0.0

            when (i % 6) {
                0 -> { r = 1.0; g = f; b = 0.0 }
                1 -> { r = q; g = 1.0; b = 0.0 }
                2 -> { r = 0.0; g = 1.0; b = f
                }
                3 -> { r = 0.0; g = q; b = 1.0 }
                4 -> {
                    r = f; g = 0.0; b = 1.0 }
                5 -> { r = 1.0; g = 0.0; b = q }
            }

            val whiteness = whiteness.toDouble(true)
            val blackness = blackness.toDouble(true)

            val factor = 1 - whiteness - blackness
            r = r * factor + whiteness
            g = g * factor + whiteness
            b = b * factor + whiteness

            return Color(
                (r * 255).roundToInt().coerceIn(0, 255),
                (g * 255).roundToInt().coerceIn(0, 255),
                (b * 255).roundToInt().coerceIn(0, 255)
            )
        }
        /**
         * Creates a new instance of the HWB color model using the provided color components.
         *
         * @param color A Triple where the first value represents the hue (H) in degrees,
         * the second value represents the whiteness (W) as a percentage (0.0 to 1.0),
         * and the third value represents the blackness (B) as a percentage (0.0 to 1.0).
         * @return An instance representing the specified HWB color.
         * @since 1.0.0
         */
        infix fun ofHWB(color: Triple<Double, Percentage, Percentage>) = ofHWB(color.first, color.second, color.third)

        /**
         * Creates a new Color instance based on HWBA (Hue, Whiteness, Blackness, Alpha) color model.
         *
         * @param hue The hue value of the color, in degrees. Should typically be in the range [0, 360).
         * @param whiteness The whiteness value of the color, as a percentage between 0 and 100.
         * @param blackness The blackness value of the color, as a percentage between 0 and 100.
         * @param alpha The alpha (opacity) value of the color, as a percentage between 0 and 100, where 0 is fully transparent and 100 is fully opaque.
         * @return A new Color instance with the specified HWBA values.
         * @since 1.0.0
         */
        fun ofHWBA(hue: Double, whiteness: Percentage, blackness: Percentage, alpha: Percentage): Color {
            val color = ofHWB(hue, whiteness, blackness)
            return Color(color.red, color.green, color.blue, alpha)
        }
        /**
         * Creates a color instance using the HWBA (Hue, Whiteness, Blackness, Alpha) color model.
         *
         * @param color A quadruple representing hue, whiteness, blackness, and alpha values.
         * - First value represents the hue (H), typically in degrees [0, 360).
         * - Second value represents the whiteness (W), typically in the range [0, 100].
         * - Third value represents the blackness (B), typically in the range [0, 100].
         * - Fourth value represents the alpha (A), typically in the range [0, 100], where 0 is fully transparent and 100 is fully opaque.
         * @return A new Color instance with the specified HWBA values.
         * @since 1.0.0
         */
        infix fun ofHWBA(color: Quadruple<Double, Percentage, Percentage, Percentage>) = ofHWBA(color.first, color.second, color.third, color.fourth)

        /**
         * Converts CIE LAB color space values to an RGB color.
         *
         * @param lightness The lightness component of the LAB color, expected to be in the range [0.0, 100.0].
         * @param a The green-red component of the LAB color, typically ranging from [-128.0, 127.0].
         * @param b The blue-yellow component of the LAB color, typically ranging from [-128.0, 127.0].
         * @return A [Color] object representing the converted RGB color, with each component (red, green, blue)
         *         constrained within the range [0, 255].
         * @since 1.0.0
         */
        @Suppress("localVariableName")
        fun ofLAB(lightness: Percentage, a: Double, b: Double): Color {
            val refX = 95.047
            val refY = 100.0
            val refZ = 108.883

            val lightness = lightness.toDouble()

            var y = (lightness + 16.0) / 116.0
            var x = a / 500.0 + y
            var z = y - b / 200.0

            val x3 = x.pow(3.0)
            val y3 = y.pow(3.0)
            val z3 = z.pow(3.0)

            x = if (x3 > 0.008856) x3 else (x - 16.0 / 116.0) / 7.787
            y = if (y3 > 0.008856) y3 else (y - 16.0 / 116.0) / 7.787
            z = if (z3 > 0.008856) z3 else (z - 16.0 / 116.0) / 7.787

            val X = refX * x
            val Y = refY * y
            val Z = refZ * z

            var r = X * 0.032406 + Y * -0.015372 + Z * -0.004986
            var g = X * -0.009689 + Y * 0.018758 + Z * 0.000415
            var b = X * 0.000557 + Y * -0.002040 + Z * 0.010570

            fun gammaCorrect(v: Double) = if (v > 0.0031308) 1.055 * v.pow(1.0 / 2.4) - 0.055 else 12.92 * v

            r = gammaCorrect(r).coerceIn(0.0, 1.0)
            g = gammaCorrect(g).coerceIn(0.0, 1.0)
            b = gammaCorrect(b).coerceIn(0.0, 1.0)

            return Color(
                (r * 255).roundToInt(),
                (g * 255).roundToInt(),
                (b * 255).roundToInt()
            )
        }
        /**
         * Creates a color representation in the LAB color space using the provided color components.
         * This method utilizes the `Double3` tuple to define the LAB values and passes them
         * for further processing.
         *
         * @param color A `Double3` tuple where `first`, `second`, and `third` represent the
         * lightness (L*), a*, and b* components of the LAB color model, respectively.
         * @since 1.0.0
         */
        infix fun ofLAB(color: Triple<Percentage, Double, Double>) = ofLAB(color.first, color.second, color.third)

        /**
         * Creates a new Color instance from a single RGB integer value.
         *
         * The integer value is expected to contain the red, green, blue, and optionally alpha components
         * combined into a single 32-bit integer. The bits are interpreted as follows:
         * - The highest 8 bits (bits 24-31) represent the alpha (transparency) channel.
         * - The next 8 bits (bits 16-23) represent the red component.
         * - The next 8 bits (bits 8-15) represent the green component.
         * - The lowest 8 bits (bits 0-7) represent the blue component.
         *
         * @param rgb The integer encoding the red, green, blue, and alpha color components.
         * @return A new Color instance with the specified components.
         * @since 1.0.0
         */
        infix fun ofRGBInt(rgb: Int): Color = Color(((rgb shr 16) and 0xFF), ((rgb shr 8) and 0xFF), (rgb and 0xFF), Percentage(((rgb shr 24) and 0xFF) / 255.0, true))

        /**
         * Converts a given Java AWT Color instance to a Color instance.
         *
         * @param javaColor The Java AWT Color instance to convert.
         * @return A new Color instance with the same red, green, blue, and alpha values as the given Java AWT Color.
         * @since 1.0.0
         */
        infix fun fromJavaColor(javaColor: java.awt.Color): Color = Color(javaColor.red, javaColor.green, javaColor.blue, Percentage(javaColor.alpha.coerceIn(0, 255) / 255.0, true))

        /**
         * Returns a `Color` object corresponding to the provided case-insensitive color name.
         *
         * @param name The case-insensitive name of the color to look up.
         * @return The `Color` object associated with the provided name, or `null` if no matching color is found.
         * @since 1.0.0
         */
        infix fun ofName(name: String): Color? = when (name.lowercase(Locale.getDefault())) {
            "black" -> BLACK
            "white" -> WHITE
            "red" -> RED
            "green" -> GREEN
            "blue" -> BLUE
            "yellow" -> YELLOW
            "cyan" -> CYAN
            "magenta" -> MAGENTA
            "silver" -> SILVER
            "gray" -> GRAY
            "light gray" -> LIGHT_GRAY
            "dark gray" -> DARK_GRAY
            "light red" -> LIGHT_RED
            "light green" -> LIGHT_GREEN
            "light blue" -> LIGHT_BLUE
            "light yellow" -> LIGHT_YELLOW
            "light cyan" -> LIGHT_CYAN
            "light magenta" -> LIGHT_MAGENTA
            "dark red" -> DARK_RED
            "dark green" -> DARK_GREEN
            "dark blue" -> DARK_BLUE
            "dark yellow" -> DARK_YELLOW
            "dark cyan" -> DARK_CYAN
            "dark magenta" -> DARK_MAGENTA
            "light orange" -> LIGHT_ORANGE
            "orange" -> ORANGE
            "confidential orange" -> CONFIDENTIAL_ORANGE
            "pink" -> PINK
            "brown" -> BROWN
            "gold" -> GOLD
            "plum" -> PLUM
            "ivory" -> IVORY
            "teal" -> TEAL
            "lime" -> LIME
            "tan" -> TAN
            "navy" -> NAVY
            "chocolate" -> CHOCOLATE
            "coral" -> CORAL
            "slate blue" -> SLATE_BLUE
            "medium violet red" -> MEDIUM_VIOLET_RED
            "seashell" -> SEASHELL
            "snow" -> SNOW
            "beige" -> BEIGE
            "khaki" -> KHAKI
            "peach puff" -> PEACH_PUFF
            "wheat" -> WHEAT
            "lavender" -> LAVENDER
            "fuchsia" -> FUCHSIA
            "lavender blush" -> LAVENDER_BLUSH
            "old lace" -> OLD_LACE
            "medium aquamarine" -> MEDIUM_AQUAMARINE
            "aqua" -> AQUA
            "medium slate blue" -> MEDIUM_SLATE_BLUE
            "hot pink" -> HOT_PINK
            "salmon" -> SALMON
            "lavender purple" -> LAVENDER_PURPLE
            else -> null
        }

        /**
         * Converts a given Pantone color code into its corresponding HEX color representation.
         *
         * _Pantone Code_ means the code formatted as `##-####`, not for example `106 C` or `542 U`.
         *
         * @param pantoneCode The Pantone color code to convert. Must be a valid Pantone code.
         * @return The HEX color representation of the specified Pantone code.
         * @since 1.0.0
         */
        infix fun ofPantoneCode(pantoneCode: String): Color? {
            val hex = PantoneConverter.byCode(pantoneCode)?.second
            return if (hex.isNull()) null else ofHEX(hex)
        }

        /**
         * Retrieves a `Color` object based on the provided Pantone name.
         *
         * The Pantone name is converted to the specified text case before looking up the associated color.
         *
         * @param pantoneName The name of the Pantone color to be converted to a `Color` object.
         * @param textCase The text case format in which the Pantone name should be converted
         *        before lookup. Defaults to `TextCase.STANDARD`.
         * @return The `Color` object corresponding to the given Pantone name, or `null` if the
         *         name cannot be converted to a valid color.
         * @since 1.0.0
         */
        fun ofPantoneName(pantoneName: String, textCase: TextCase = TextCase.STANDARD): Color? {
            val hex = PantoneConverter.byName(pantoneName.convertCase(textCase, TextCase.KEBAB_CASE))
            return if (hex.isNull()) null else ofHEX(hex)
        }

        private fun testColorValueRange(red: Int, green: Int, blue: Int, alpha: Percentage) {
            var rangeError = false
            var badComponentString = ""

            if (alpha.isOverflowing) {
                rangeError = true
                badComponentString = "$badComponentString Alpha"
            }
            if (red !in 0..255) {
                rangeError = true
                badComponentString = "$badComponentString Red"
            }
            if (green !in 0..255) {
                rangeError = true
                badComponentString = "$badComponentString Green"
            }
            if (blue !in 0..255) {
                rangeError = true
                badComponentString = "$badComponentString Blue"
            }
            validate(!rangeError) { "Color parameter outside of expected range: $badComponentString" }
        }

        /**
         * Sorts a collection of colors by their luminosity in ascending order.
         *
         * @param colors An iterable collection of `Color` objects to be sorted by luminosity.
         * @return A list of `Color` objects sorted by their luminosity.
         * @since 1.0.0
         */
        fun sortByLuminosity(colors: Iterable<Color>): List<Color> = colors.sortedBy { it.getLuminosity() }.toList()

        /**
         * Generates a gradient of colors between the specified start and end colors, divided into the given number of steps.
         *
         * @param startColor the starting color of the gradient.
         * @param endColor the ending color of the gradient.
         * @param steps the number of steps in the gradient, including the start and end colors.
         * @return a list of colors representing the gradient from startColor to endColor.
         * @since 1.0.0
         */
        fun generateGradient(startColor: Color, endColor: Color, steps: Int): List<Color> {
            val gradient = mutableListOf<Color>()
            val startRed: Int = startColor.red
            val startGreen: Int = startColor.green
            val startBlue: Int = startColor.blue
            val endRed: Int = endColor.red
            val endGreen: Int = endColor.green
            val endBlue: Int = endColor.blue

            for (i in 0..<steps) {
                val t = i.toDouble() / (steps - 1)
                val red = (startRed + (endRed - startRed) * t).toInt()
                val green = (startGreen + (endGreen - startGreen) * t).toInt()
                val blue = (startBlue + (endBlue - startBlue) * t).toInt()
                gradient.add(Color(red, green, blue))
            }
            return gradient
        }

        class Serializer : ValueSerializer<Color>() {
            override fun serialize(value: Color, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.toString())
            }
        }

        class Deserializer : ValueDeserializer<Color>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = ofHEX(p.string)
        }

        class OldSerializer : JsonSerializer<Color>() {
            override fun serialize(value: Color, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeString(value.toString())
            }
        }

        class OldDeserializer : JsonDeserializer<Color>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext) = ofHEX(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<Color?, String?> {
            override fun convertToDatabaseColumn(attribute: Color?) = attribute?.toString()
            override fun convertToEntityAttribute(dbData: String?) = dbData?.let { ofHEX(it) }
        }
    }

    /**
     * Creates a new instance of a Color object with the specified red, green, blue, and alpha values.
     * The original values are used for any parameters not provided.
     *
     * @param red The red component value for the new Color. Defaults to the current red value.
     * @param green The green component value for the new Color. Defaults to the current green value.
     * @param blue The blue component value for the new Color. Defaults to the current blue value.
     * @param alpha The alpha component value for the new Color. Defaults to the current alpha value.
     * @return A new Color instance with the specified or original properties.
     * @since 1.0.0
     */
    fun copy(red: Int = this.red, green: Int = this.green, blue: Int = this.blue, alpha: Percentage = this.alpha): Color = Color(red, green, blue, alpha)

    /**
     * Converts the color values (red, green, blue, alpha) into an integer representation.
     *
     *
     * @return An integer representation of the color, where alpha is stored in the highest 8 bits,
     * red in the next 8 bits, green in the next 8 bits, and blue in the lowest 8 bits.
     * @since 1.0.0
     */
    fun toInt(): Int {
        testColorValueRange(red, green, blue, alpha)
        return (((alpha.toDouble(true) * 255).roundToInt() and 0xFF) shl 24) or ((red and 0xFF) shl 16) or ((green and 0xFF) shl 8) or (blue and 0xFF)
    }

    /**
     * Converts the color values (red, green, blue, alpha) into an integer representation.
     *
     *
     * @return An integer representation of the color, where alpha is stored in the highest 8 bits,
     * red in the next 8 bits, green in the next 8 bits, and blue in the lowest 8 bits.
     * @since 1.0.0
     */
    fun toLong(): Long = toInt().toLong()

    /**
     * Converts the color values (red, green, blue, alpha) into an integer representation.
     *
     *
     * @return An integer representation of the color, where alpha is stored in the highest 8 bits,
     * red in the next 8 bits, green in the next 8 bits, and blue in the lowest 8 bits.
     * @since 1.0.0
     */
    fun toFloat(): Float = toInt().toFloat()

    /**
     * Converts the color values (red, green, blue, alpha) into an integer representation.
     *
     *
     * @return An integer representation of the color, where alpha is stored in the highest 8 bits,
     * red in the next 8 bits, green in the next 8 bits, and blue in the lowest 8 bits.
     * @since 1.0.0
     */
    fun toDouble(): Double = toInt().toDouble()

    /**
     * Converts the color components to an RGB representation.
     *
     * This method returns a `Triple` containing the red, green, and blue
     * components, respectively, as its values.
     *
     * @return A `Triple` of integers representing the red, green, and blue
     * values in the current color instance.
     * @since 1.0.0
     */
    fun toRGB() = Triple(red, green, blue)

    /**
     * Converts the current color representation to an RGBA quadruple.
     *
     * This method combines the red, green, blue, and alpha channel values into
     * a Quadruple, representing the color in the RGBA format.
     *
     * @return A Quadruple containing the red, green, blue, and alpha channel values.
     * @since 1.0.0
     */
    fun toRGBA() = Quadruple(red, green, blue, alpha)

    /**
     * Converts the current string representation of the object to its HEX format.
     *
     * This function utilizes the `toString` method of the `Model.HEX` type for conversion,
     * and returns the result as a HEX object.
     *
     * @return An object of type `Hex` containing the HEX representation of the current instance.
     * @since 1.0.0
     */
    fun toHEX() = Hex(toString(Model.HEX))

    /**
     * Converts the current instance's string representation into a hexadecimal format.
     * It utilizes the `Model.HEXA` conversion configuration during the transformation.
     *
     * @return A `Hex` object representing the hexadecimal formatted value.
     *
     * @since 1.0.0
     */
    fun toHEXA() = Hex(toString(Model.HEXA))

    /**
     * Converts the color representation to the HSL (Hue, Saturation, Lightness) color model.
     *
     * @return A [Triple] containing the HSL values where the first element is hue (0-360),
     *         the second is saturation, and the third is lightness.
     * @since 1.0.0
     */
    fun toHSL(): Triple<Double, Percentage, Percentage> {
        val hsla = toHSLA()
        return Triple(hsla.first, hsla.second, hsla.third)
    }
    /**
     * Converts the color represented by the RGB components and alpha value to the HSLA (Hue, Saturation, Lightness, Alpha) color space.
     *
     * This method calculates the hue, saturation, and lightness of the color based on its RGB values and alpha transparency.
     *
     * @return A quadruple where:
     * - First value represents hue (in degrees, range: 0.0 to 360.0)
     * - Second value is saturation
     * - Third value is lightness
     * - Fourth value is alpha
     * @since 1.0.0
     */
    fun toHSLA(): Quadruple<Double, Percentage, Percentage, Percentage> {
        val r = red / 255.0
        val g = green / 255.0
        val b = blue / 255.0

        val max = max(r, max(g, b))
        val min = min(r, min(g, b))
        val deltaMaxMin = max - min

        val h = when {
            deltaMaxMin == 0.0 -> 0.0
            max == r -> 60 * (((g - b) / deltaMaxMin) % 6)
            max == g -> 60 * (((b - r) / deltaMaxMin) + 2)
            max == b -> 60 * (((r - g) / deltaMaxMin) + 4)
            else -> 0.0
        }
        val l = (max + min) / 2.0
        val s = when {
            deltaMaxMin == 0.0 -> 0.0
            else -> (deltaMaxMin / (1.0 - abs(2.0 * l - 1.0)))
        }

        return Quadruple(h, Percentage(s, true), Percentage(l, true), alpha)
    }

    /**
     * Converts RGB color components to HSV (Hue, Saturation, Value) color space.
     *
     * @return A [Triple] representing the HSV color, where the first value is the hue (in degrees, 0 to 360),
     *         the second value is the saturation and the third value is the
     *         value or brightness.
     * @since 1.0.0
     */
    fun toHSV(): Triple<Double, Percentage, Percentage> {
        val r = red / 255.0f; val g = green / 255.0f; val b = blue / 255.0f
        val max = max(r, max(g, b))
        val min = min(r, min(g, b))
        var h: Float; val s: Float

        val d = max - min
        s = if (max == 0f) 0f else d / max

        if (max == min) h = 0f // achromatic
        else {
            h = when (max) {
                r -> (g - b) / d + (if (g < b) 6 else 0)
                g -> (b - r) / d + 2
                else -> (r - g) / d + 4
            }
            h /= 6f
        }
        return Triple((h * 360).toDouble(), Percentage(s, true), Percentage(max, true))
    }
    /**
     * Converts the current color representation to its HSVA (Hue, Saturation, Value, Alpha) equivalent.
     *
     * @return A Quadruple containing the hue, saturation, value, and alpha components of the color
     * @since 1.0.0
     */
    fun toHSVA(): Quadruple<Double, Percentage, Percentage, Percentage> {
        val hsv = toHSV()
        return Quadruple(hsv.first, hsv.second, hsv.third, alpha)
    }

    /**
     * Converts the red, green, and blue color components to the HSB (Hue, Saturation, Brightness) color model.
     *
     * @return A [Triple] containing the hue (H) as a Double between 0.0 and 360.0,
     * the saturation (S), and the brightness (B)
     * @since 1.0.0
     */
    fun toHSB(): Triple<Double, Percentage, Percentage> {
        val r = red / 255.0
        val g = green / 255.0
        val b = blue / 255.0

        val maxVal = max(max(r ,g) ,b)
        val minVal = min(min(r, g), b)
        val delta = maxVal - minVal

        val h = when {
            delta == 0.0 -> 0.0
            maxVal == r -> (60 * ((g - b) / delta) + 360) % 360
            maxVal == g -> (60 * ((b - r) / delta) + 120) % 360
            else -> (60 * ((r - g) / delta) + 240) % 360
        }

        val s = if (maxVal == 0.0) 0.0 else delta / maxVal

        return Triple(h, Percentage(s, true), Percentage(maxVal, true))
    }
    /**
     * Converts the current color instance to the HSBA color model.
     * HSBA stands for Hue, Saturation, Brightness, and Alpha, where alpha represents the opacity level.
     *
     * @return A Quadruple containing the hue, saturation, brightness, and alpha components of the color
     * @since 1.0.0
     */
    fun toHSBA(): Quadruple<Double, Percentage, Percentage, Percentage> {
        val hsb = toHSB()
        return Quadruple(hsb.first, hsb.second, hsb.third, alpha)
    }

    /**
     * Converts the RGB color components to CMYK (Cyan, Magenta, Yellow, Black) color model.
     *
     * @return A Quadruple representing the CMYK components of the color.
     *         The first value is Cyan, the second is Magenta,
     *         the third is Yellow, and the fourth is Black.
     * @since 1.0.0
     */
    fun toCMYK(): MonoQuadruple<Percentage> {
        val r = red / 255.0f; val g = green / 255.0f; val b = blue / 255.0f
        val k = 1 - max(r, max(g, b))
        if (k == 1f) return Quadruple(ZERO_PERCENT, ZERO_PERCENT, ZERO_PERCENT, FULL) // black
        val c = (1 - r - k) / (1 - k)
        val m = (1 - g - k) / (1 - k)
        val y = (1 - b - k) / (1 - k)
        return Quadruple(Percentage(c, true), Percentage(m, true), Percentage(y, true), Percentage(k, true))
    }
    /**
     * Converts the current color to the CMYKA color model representation.
     *
     * The function calculates the Cyan, Magenta, Yellow, Black (Key), and Alpha values
     * of the color and returns them as a quintuple of Percentage values.
     *
     * @return A quintuple containing the Cyan, Magenta, Yellow, Black, and Alpha
     *         components of the color.
     * @since 1.0.0
     */
    fun toCMYKA(): MonoQuintuple<Percentage> {
        val cmyk = toCMYK()
        return Quintuple(cmyk.first, cmyk.second, cmyk.third, cmyk.fourth, alpha)
    }

    /**
     * Converts the current color to the HWB (Hue-Whiteness-Blackness) color model.
     *
     * HWB is a cylindrical-coordinate representation of points in an RGB color model.
     * It is often used as an alternative to HSV as it separates the whiteness and blackness components.
     *
     * @return A [Triple] representing the HWB color values. The first component is the hue (in degrees),
     * the second component is the whiteness percentage, and the third component is the blackness percentage.
     * @since 1.0.0
     */
    fun toHWB(): Triple<Double, Percentage, Percentage> {
        val rn = red / 255.0
        val gn = green / 255.0
        val bn = blue / 255.0

        val maxVal = maxOf(rn, gn, bn)
        val minVal = minOf(rn, gn, bn)
        val delta = maxVal - minVal

        val h = when {
            delta == 0.0 -> 0.0
            maxVal == rn -> (60 * ((gn - bn) / delta) + 360) % 360
            maxVal == gn -> (60 * ((bn - rn) / delta) + 120) % 360
            else -> (60 * ((rn - gn) / delta) + 240) % 360
        }

        val bl = 1 - maxVal

        return Triple(h, Percentage(minVal, true), Percentage(bl, true))
    }
    /**
     * Converts the color to the HWBA (Hue, Whiteness, Blackness, Alpha) color model.
     *
     * The resulting array contains:
     * - Hue (degrees) at index 0
     * - Whiteness (percentage) at index 1
     * - Blackness (percentage) at index 2
     * - Alpha (transparency as a percentage) at index 3
     *
     *
     * @return A `Quadruple` representing the HWBA components of the color.
     * @since 1.0.0
     */
    fun toHWBA(): Quadruple<Double, Percentage, Percentage, Percentage> {
        val hwb = toHWB()
        return Quadruple(hwb.first, hwb.second, hwb.third, alpha)
    }

    /**
     * Converts the RGB color values to the CIE 1931 XYZ color space.
     *
     * The conversion applies a linear transformation based on the standard
     * RGB to XYZ transformation matrix.
     *
     * @return A [Triple] containing the X, Y, and Z components of the color in the CIE 1931 XYZ color space.
     * @since 1.0.0
     */
    fun toXYZ(): MonoTriple<Percentage> {
        var r = (red / 255f).toDouble()
        var g = (green / 255f).toDouble()
        var b = (blue / 255f).toDouble()

        r = if (r > 0.04045f) ((r + 0.055) / 1.055).pow(2.4) else r / 12.92f
        g = if (g > 0.04045f) ((g + 0.055) / 1.055).pow(2.4) else g / 12.92f
        b = if (b > 0.04045f) ((b + 0.055) / 1.055).pow(2.4) else b / 12.92f

        val x = r * 0.4124564f + g * 0.3575761f + b * 0.1804375f
        val y = r * 0.2126729f + g * 0.7151522f + b * 0.0721750f
        val z = r * 0.0193339f + g * 0.1191920f + b * 0.9503041f

        return Triple(Percentage(x, true), Percentage(y, true), Percentage(z, true))
    }

    /**
     * Converts color values to the CIE LAB color space.
     *
     * The method takes the current color values, internally converts them
     * to the CIE XYZ color space, and then transforms them into LAB space
     * using the appropriate formulas and reference white values.
     *
     * @return A [Triple] containing the LAB color representation, where the first value
     * represents the lightness (L), the second value represents the green-red axis (A),
     * and the third value represents the blue-yellow axis (B).
     * @since 1.0.0
     */
    fun toLAB(): Triple<Percentage, Double, Double> {
        val xyz = toXYZ()

        var x = xyz.first.toDouble(true) / 0.95047f
        var y = xyz.second.toDouble(true)
        var z = xyz.third.toDouble(true) / 1.08883f

        x = if (x > 0.008856f) x.pow((1 / 3f).toDouble()) else (x * 903.3f + 16) / 116f
        y = if (y > 0.008856f) y.pow((1 / 3f).toDouble()) else (y * 903.3f + 16) / 116f
        z = if (z > 0.008856f) z.pow((1 / 3f).toDouble()) else (z * 903.3f + 16) / 116f

        val l = (116 * y) - 16
        val a = 500 * (x - y)
        val b = 200 * (y - z)

        return Triple(Percentage(l), a, b)
    }

    /**
     * Converts the current `Color` instance to a `java.awt.Color` object.
     *
     *
     * @return A `java.awt.Color` representation of the current `Color`.
     * @since 1.0.0
     */
    fun toJavaColor() = java.awt.Color(red, green, blue, (alpha.toDouble(true) * 255).roundToInt())

    /**
     * Converts the current hex color representation of the object to its corresponding Pantone color code.
     *
     * @return The Pantone color code as a String if a match is found, otherwise null.
     * @since 1.0.0
     */
    fun toPantoneCode(): String? = PantoneConverter.byHex(toString())

    /**
     * Converts the current object to a Pantone color name in the specified text case.
     *
     * @param textCase The desired text case for the resulting Pantone name. Defaults to `TextCase.LOWER_CASE`.
     * @return The Pantone color name as a string in the specified text case, or `null` if no matching Pantone name is found.
     * @since 1.0.0
     */
    fun toPantoneName(textCase: TextCase = TextCase.LOWER_CASE): String? = PantoneConverter.byHexGiveName(toString())?.convertCase(
        TextCase.KEBAB_CASE, textCase)

    /**
     * Converts various color models into a map representation.
     *
     * This method provides a mapping of color model names to their corresponding
     * representations generated by specific conversion functions. The supported
     * models include RGB, RGBA, HEX, HEXA, HSL, HSLA, HSV, HSVA, CMYK, CMYKA, HWB,
     * HWBA, XYZ, LAB, Pantone code, and Pantone name.
     *
     * @return A map where the keys are the string identifiers of color models
     * (e.g., "rgb", "rgba", "hex"), and the values are the respective converted
     * representations for those models.
     * @since 1.0.0
     */
    fun toModelMap() = mapOf(
        "rgb" to toRGB(),
        "rgba" to toRGBA(),
        "hex" to toHEX(),
        "hexa" to toHEXA(),
        "hsl" to toHSL(),
        "hsla" to toHSLA(),
        "hsv" to toHSV(),
        "hsva" to toHSVA(),
        "hsb" to toHSB(),
        "hsba" to toHSBA(),
        "cmyk" to toCMYK(),
        "cmyka" to toCMYKA(),
        "hwb" to toHWB(),
        "hwba" to toHWBA(),
        "xyz" to toXYZ(),
        "lab" to toLAB(),
        "pantoneCode" to toPantoneCode(),
        "pantoneName" to toPantoneName()
    )

    /**
     * Converts the RGB color components and the alpha component into a map.
     * The map contains entries for "red", "green", "blue", and "alpha".
     *
     * @return a map where the keys are the component names ("red", "green", "blue", "alpha")
     *         and the values are their respective values.
     * @since 1.0.0
     */
    fun toComponentMap() = mapOf(
        "red" to red,
        "green" to green,
        "blue" to blue,
        "alpha" to alpha
    )

    /**
     * Delegation function to retrieve the value associated with the given property name
     * by combining the results from `toComponentMap()` and `toModelMap()`. The property name
     * is used as the key to fetch the value.
     *
     * - `red` - TYPE: `Int`
     * - `green` - TYPE: `Int`
     * - `blue` - TYPE: `Int`
     * - `alpha` - TYPE: `Percentage`
     * - `alpha` - TYPE: `Percentage`
     * - `rgb` - TYPE: `Triple<Int, Int, Int>`
     * - `rgba` - TYPE: `Quadruple<Int, Int, Int, Percentage>`
     * - `hex` - TYPE: `Hex`
     * - `hexa` - TYPE: `Hex`
     * - `hsl` - TYPE: `Triple<Double, Percentage, Percentage>`
     * - `hsla` - TYPE: `Quadruple<Double, Percentage, Percentage, Percentage>`
     * - `hsv` - TYPE: `Triple<Double, Percentage, Percentage>`
     * - `hsva` - TYPE: `Quadruple<Double, Percentage, Percentage, Percentage>`
     * - `hsb` - TYPE: `Triple<Double, Percentage, Percentage>`
     * - `hsba` - TYPE: `Quadruple<Double, Percentage, Percentage, Percentage>`
     * - `cmyk` - TYPE: `Quadruple<Percentage, Percentage, Percentage, Percentage>`
     * - `cmyka` - TYPE: `Quintuple<Percentage, Percentage, Percentage, Percentage, Percentage>`
     * - `hwb` - TYPE: `Triple<Double, Percentage, Percentage>`
     * - `hwba` - TYPE: `Quadruple<Double, Percentage, Percentage, Percentage>`
     * - `xyz` - TYPE: `Triple<Percentage, Percentage, Percentage>`
     * - `lab` - TYPE: `Triple<Percentage, Double, Double>`
     * - `pantoneCode` - TYPE: `Int`
     * - `pantoneName` - TYPE: `String?`
     *
     * @param thisRef the reference to the object containing the delegated property, can be null
     * @param property the metadata of the delegated property
     * @return the value corresponding to the property name in the combined map
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>): R = (toComponentMap() + toModelMap() + mapOf("r" to red, "g" to green, "b" to blue, "a" to alpha))
        .getValue(property.name) as R

    /**
     * Converts a color representation to its string equivalent based on the specified color model.
     *
     * @param model The color model to determine the string format (e.g., RGB, RGBA, HSL, etc.).
     * @param decimal A flag indicating whether to use decimal values (true) or rounded/integer values (false). Defaults to true.
     * @return A formatted string representing the color in the specified model.
     * @since 1.0.0
     */
    fun toString(model: Model, decimal: Boolean = true): String {
        return when (decimal) {
            true -> when (model) {
                Model.RGB -> String.format(Locale.of("en", "us"), "rgb(%d, %d, %d)", red, green, blue)
                Model.RGBA -> String.format(Locale.of("en", "us"), "rgba(%d, %d, %d, %d)", red, green, blue, (alpha.toDouble(true) * 255).roundToInt())
                Model.HEX -> -String.format(Locale.of("en", "us"), "#%02X%02X%02X", red, green, blue)
                Model.HEXA -> -String.format(Locale.of("en", "us"), "#%02X%02X%02X%02X", red, green, blue, (alpha.toDouble(true) * 255).roundToInt())
                Model.HSL -> String.format(Locale.of("en", "us"), "hsl(%.1f, %.1f%%, %.1f%%)", toHSL().first, toHSL().second.toDouble(), toHSL().third.toDouble())
                Model.HSLA -> String.format(Locale.of("en", "us"), "hsla(%.1f, %.1f%%, %.1f%%, %d%%)", toHSL().first, toHSL().second.toDouble(), toHSL().third.toDouble(), alpha.roundToInt())
                Model.HSV -> String.format(Locale.of("en", "us"), "hsv(%.1f, %.1f%%, %.1f%%)", toHSV().first, toHSV().second.toDouble(), toHSV().third.toDouble())
                Model.HSVA -> String.format(Locale.of("en", "us"), "hsva(%.1f, %.1f%%, %.1f%%, %d%%)", toHSV().first, toHSV().second.toDouble(), toHSV().third.toDouble(), alpha.roundToInt())
                Model.HSB -> String.format(Locale.of("en", "us"), "hsb(%.1f, %.1f%%, %.1f%%)", toHSB().first, toHSB().second.toDouble(), toHSB().third.toDouble())
                Model.HSBA -> String.format(Locale.of("en", "us"), "hsba(%.1f, %.1f%%, %.1f%%, %d%%)", toHSB().first, toHSB().second.toDouble(), toHSB().third.toDouble(), alpha.roundToInt())
                Model.CMYK -> String.format(Locale.of("en", "us"), "cmyk(%.2f%%, %.2f%%, %.2f%%, %.2f%%)", toCMYK().first.toDouble(), toCMYK().second.toDouble(), toCMYK().third.toDouble(), toCMYK().fourth.toDouble())
                Model.CMYKA -> String.format(Locale.of("en", "us"), "cmyka(%.2f%%, %.2f%%, %.2f%%, %.2f%%, %d%%)", toCMYK().first.toDouble(), toCMYK().second.toDouble(), toCMYK().third.toDouble(), toCMYK().fourth.toDouble(), alpha.roundToInt())
                Model.HWB -> String.format(Locale.of("en", "us"), "hwb(%.1f, %.1f%%, %.1f%%)", toHWB().first, toHWB().second.toDouble(), toHWB().third.toDouble())
                Model.HWBA -> String.format(Locale.of("en", "us"), "hwba(%.1f, %.1f%%, %.1f%%, %d%%)", toHWB().first, toHWB().second.toDouble(), toHWB().third.toDouble(), alpha.roundToInt())
                Model.XYZ -> String.format(Locale.of("en", "us"), "xyz(%.2f%%, %.2f%%, %.2f%%)", toXYZ().first.toDouble(), toXYZ().second.toDouble(), toXYZ().third.toDouble())
                Model.LAB -> String.format(Locale.of("en", "us"), "lab(%.2f, %.2f, %.2f)", toLAB().first.toDouble(), toLAB().second, toLAB().third)
            }
            false -> when (model) {
                Model.RGB -> String.format(Locale.of("en", "us"), "rgb(%d, %d, %d)", red, green, blue)
                Model.RGBA -> String.format(Locale.of("en", "us"), "rgba(%d, %d, %d, %d)", red, green, blue, (alpha.toDouble(true) * 255).roundToInt())
                Model.HEX -> -String.format(Locale.of("en", "us"), "#%02X%02X%02X", red, green, blue)
                Model.HEXA -> -String.format(Locale.of("en", "us"), "#%02X%02X%02X%02X", red, green, blue, (alpha.toDouble(true) * 255).roundToInt())
                Model.HSL -> {
                    val hsl = toHSL()
                    String.format(Locale.of("en", "us"), "hsl(%d, %d%%, %d%%)", hsl.first.roundToInt(), hsl.second.roundToInt(), hsl.third.roundToInt())
                }
                Model.HSLA -> {
                    val hsl = toHSL()
                    String.format(Locale.of("en", "us"), "hsla(%d, %d%%, %d%%, %d%%)", hsl.first.roundToInt(), hsl.second.roundToInt(), hsl.third.roundToInt(), alpha.roundToInt())
                }
                Model.HSV -> {
                    val hsv = toHSV()
                    String.format(Locale.of("en", "us"), "hsv(%d, %d%%, %d%%)", hsv.first.roundToInt(), hsv.second.roundToInt(), hsv.third.roundToInt())
                }
                Model.HSVA -> {
                    val hsv = toHSV()
                    String.format(Locale.of("en", "us"), "hsva(%d, %d%%, %d%%, %d%%)", hsv.first.roundToInt(), hsv.second.roundToInt(), hsv.third.roundToInt(), alpha.roundToInt())
                }
                Model.HSB -> {
                    val hsb = toHSB()
                    String.format(Locale.of("en", "us"), "hsb(%d, %d%%, %d%%)", hsb.first.roundToInt(), hsb.second.roundToInt(), hsb.third.roundToInt())
                }
                Model.HSBA -> {
                    val hsb = toHSB()
                    String.format(Locale.of("en", "us"), "hsba(%d, %d%%, %d%%, %d%%)", hsb.first.roundToInt(), hsb.second.roundToInt(), hsb.third.roundToInt(), alpha.roundToInt())
                }
                Model.CMYK -> {
                    val cmyk = toCMYK()
                    String.format(Locale.of("en", "us"), "cmyk(%d%%, %d%%, %d%%, %d%%)", cmyk.first.roundToInt(), cmyk.second.roundToInt(), cmyk.third.roundToInt(), cmyk.fourth.roundToInt())
                }
                Model.CMYKA -> {
                    val cmyk = toCMYK()
                    String.format(Locale.of("en", "us"), "cmyka(%d%%, %d%%, %d%%, %d%%, %d%%)", cmyk.first.roundToInt(), cmyk.second.roundToInt(), cmyk.third.roundToInt(), cmyk.fourth.roundToInt(), alpha.roundToInt())
                }
                Model.HWB -> {
                    val hwb = toHWB()
                    String.format(Locale.of("en", "us"), "hwb(%d, %d%%, %d%%)", hwb.first.roundToInt(), hwb.second.roundToInt(), hwb.third.roundToInt())
                }
                Model.HWBA -> {
                    val hwb = toHWB()
                    String.format(Locale.of("en", "us"), "hwba(%d, %d%%, %d%%, %d%%)", hwb.first.roundToInt(), hwb.second.roundToInt(), hwb.third.roundToInt(), alpha.roundToInt())
                }
                Model.XYZ -> {
                    val xyz = toXYZ()
                    String.format(Locale.of("en", "us"), "xyz(%d%%, %d%%, %d%%)", xyz.first.roundToInt(), xyz.second.roundToInt(), xyz.third.roundToInt())
                }
                Model.LAB -> {
                    val lab = toLAB()
                    String.format(Locale.of("en", "us"), "lab(%d, %d, %d)", lab.first.roundToInt(), lab.second.roundToInt(), lab.third.roundToInt())
                }
            }

        }
    }

    /**
     * Returns a string representation of the color object.
     *
     * Delegates to the `toString(model: Model)` method to generate the string.
     * The output format depends on the alpha transparency value of the color:
     * - If the alpha value is 255 (fully opaque), the `Model.HEX` format is used.
     * - If the alpha value is not 255 (contains transparency), the `Model.HEXA` format is used.
     *
     *
     * @return The string representation of the color in the appropriate format.
     * @since 1.0.0
     */
    override fun toString() = if (alpha.isNotFull) toString(Model.HEXA) else toString(Model.HEX)

    /**
     * Component function that retrieves the `red` property of the object.
     * This function allows the use of destructuring declarations to access
     * the `red` component of an instance.
     *
     * @return The `red` value of the object.
     * @since 1.0.0
     */
    operator fun component1() = red
    /**
     * Provides the second component of the object, typically representing the `green` value in a structured object.
     *
     * This function allows the destructuring of the object to access its `green` property directly.
     *
     * @return The `green` component of the object.
     * @since 1.0.0
     */
    operator fun component2() = green
    /**
     * Operator function that enables destructuring declarations to access the third component.
     * Specifically, this function returns the `blue` property.
     *
     * @return The third component of the object, typically represented by the `blue` property.
     *
     * @since 1.0.0
     */
    operator fun component3() = blue
    /**
     * Operator function component4 used for destructuring declarations.
     * Returns the fourth component of the data structure.
     *
     * @return The value stored in the `alpha` property.
     * @since 1.0.0
     */
    operator fun component4() = alpha

    /**
     * Compares this Color object to another object to determine if they are equal.
     *
     * The equality check first evaluates if the two objects are the same instance.
     * If not, it verifies the compatibility of the `other` object, following which it checks
     * for equivalence between the color components: `red`, `green`, `blue`, and `alpha`.
     *
     *
     * @param other The object to compare with this Color instance.
     * @return `true` if the objects are equal in terms of their attributes, otherwise `false`.
     * @since 1.0.0
     */
    override operator fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other.isNull()) return false
        if (other is java.awt.Color) return red == other.red && green == other.green && blue == other.blue && (alpha.toDouble(true) * 255).roundToInt() == other.alpha
        if (javaClass != other.javaClass) return false
        val color = other as Color
        return red == color.red && green == color.green && blue == color.blue && alpha == color.alpha
    }

    /**
     * Computes a hash code for the color instance based on its RGBA components.
     *
     *
     * @return An integer representing the hash code of this color instance.
     * @since 1.0.0
     */
    override fun hashCode(): Int {
        var result = red
        result = 31 * result + green
        result = 31 * result + blue
        result = 31 * result + (alpha * 255).toInt()
        return result
    }

    /**
     * Compares this color to another color with a given tolerance for each color component.
     * Determines if the differences in the red, green, blue, and alpha components
     * between the two colors are within the specified tolerance.
     *
     *
     * @param other The color to compare with.
     * @param tollerance The allowed maximum difference for each color component.
     * @return `true` if all components (red, green, blue, alpha) differ within the specified tolerance, `false` otherwise.
     * @since 1.0.0
     */
    fun override(other: Color, tollerance: Int) = abs(red - other.red) <= tollerance
            && abs(green - other.green) <= tollerance
            && abs(blue - other.blue) <= tollerance
            && abs(alpha.toDouble(true) - other.alpha.toDouble(true)) <= tollerance

    /**
     * Retrieves the luminosity of the color, represented as the lightness (L) component
     * from the HSL (Hue, Saturation, Lightness) color model.
     *
     *
     * @return The luminosity (lightness) of the color as a percentage value from 0.0 to 100.0.
     * @since 1.0.0
     */
    fun getLuminosity(): Percentage = toHSL().third

    /**
     * Adjusts the lightness of the color by the specified amount.
     * This operation ensures that the lightness remains within the valid range of 0.0 to 1.0.
     *
     *
     * @param amount The amount by which to increase the lightness.
     *               Positive values make the color lighter, while negative values make it darker.
     * @return A new `Color` instance with the adjusted lightness.
     * @since 1.0.0
     */
    fun lighten(amount: Double): Color {
        val hsl = toHSL()
        return ofHSL(hsl.first, hsl.second, Percentage(min(1.0, (hsl.third.toDouble(true) + amount)), true))
    }

    /**
     * Darkens the current color by reducing its lightness component in the HSL color model.
     * The amount specified determines the degree to which the color's lightness is decreased.
     *
     *
     * @param amount The amount by which to reduce the lightness component.
     *               This value should be a non-negative number where larger values represent greater darkening.
     * @return A new color with the lightness component decreased by the specified amount.
     *         If the resulting lightness is less than 0, it will be clamped to 0.
     * @since 1.0.0
     */
    fun darken(amount: Double): Color {
        val hsl = toHSL()
        return ofHSL(hsl.first, hsl.second, Percentage(max(0.0, (hsl.third.toDouble(true) - amount)), true))
    }

    /**
     * Inverts the color by subtracting the RGB components of the color
     * from 255, while preserving the alpha channel.
     *
     *
     * @return A new Color instance with the RGB components inverted
     *         and the same alpha value as the original color.
     * @since 1.0.0
     */
    fun invert(): Color = Color(255 - red, 255 - green, 255 - blue, alpha)

    /**
     * Unary minus operator function that inverts the current instance.
     * This operator is typically used to reverse or negate the current state
     * of the object it is applied to, as defined by the implementation of the `invert` method.
     *
     * @return The inverted representation of the current instance.
     * @since 1.0.0
     */
    operator fun not() = invert()

    /**
     * Compares this color object with the specified other color object for order.
     * Comparison is based on the long representation of the colors.
     *
     *
     * @param other the color object to be compared with this color.
     * @return a negative integer, zero, or a positive integer as this color is less than,
     * equal to, or greater than the specified other color.
     * @since 1.0.0
     */
    override operator fun compareTo(other: Color): Int = toLong().compareTo(other.toLong())

    /**
     * Combines this color with another color by averaging the corresponding RGBA components.
     *
     * @param other The color to combine with this color.
     * @return A new color instance with averaged RGBA components.
     * @since 1.0.0
     */
    operator fun plus(other: Color) = ofRGBA(
        (red + other.red) / 2,
        (green + other.green) / 2,
        (blue + other.blue) / 2,
        (alpha + other.alpha) / 2
    )

    /** Represents different color models used in color representation
     * and manipulation in various systems and formats.
     *
     * This enumeration defines a list of color models, each providing
     * a specific approach to defining and interpreting color in a
     * computational or graphic context. These include RGB-based models,
     * alpha channel extensions, and other specialized models like CMYK
     * or LAB.
     *
     *  * **RGB and RGBA:** Red-Green-Blue models with optional Alpha channel for transparency.
     *  * **HSL and HSLA:** Hue-Saturation-Lightness models with optional Alpha transparency.
     *  * **HSV and HSVA:** Hue-Saturation-Value models with optional Alpha channel.
     *  * **CMYK and CMYKA:** Cyan-Magenta-Yellow-Black models, often used in printing, with optional Alpha transparency.
     *  * **HWB and HWBA:** Hue-Whiteness-Blackness models with optional Alpha channel.
     *  * **HEX and HEXA:** Hexadecimal RGB notations with optional Alpha channel.
     *  * **XYZ:** A color space representing human vision using tristimulus values.
     *  * **LAB:** A color space focused on color perception, separating lightness and chromatic components.
     *
     * @since 1.0.0
     */
    enum class Model {
        /**
         * Represents the RGB (Red-Green-Blue) color model.
         *
         * This color model is based on three primary additive colors: red, green,
         * and blue. It combines these colors in various intensities to produce a wide
         * range of colors. The RGB model is commonly used in digital displays,
         * computer graphics, and image manipulation due to its alignment with the way
         * human vision perceives color through light.
         *
         * In this model:
         *
         *  * Red, green, and blue values are typically represented as intensities
         * in the range of 0-255 in 8-bit or 0.0-1.0 in normalized forms.
         *  * It forms the foundation for other extended color models, such as RGBA
         * (which adds transparency).
         *
         * @since 1.0.0
         */
        RGB,

        /**
         * Represents the RGBA color model, which extends the RGB (Red-Green-Blue) model
         * by including an Alpha component for transparency.
         *
         * This model is widely used in digital imaging and graphics applications,
         * allowing colors to have varying levels of opacity. The Alpha channel specifies
         * the transparency of the color, with a value range typically between 0 (completely transparent)
         * and 1 (completely opaque).
         *
         * The RGBA model is useful for layering multiple graphics or images,
         * enabling blending and overlay effects with transparency.
         *
         * @since 1.0.0
         */
        RGBA,

        /**
         * Represents the Hue-Saturation-Lightness (HSL) color model.
         *
         * The HSL color model defines colors based on three key components:
         *
         *  * **Hue:** The type of color perceived, represented as an angle
         * within the 360° range on the color wheel (e.g., 0° for red, 120° for green, 240° for blue).
         *  * **Saturation:** The intensity or purity of the color, represented as a percentage (0% being gray and 100% being fully saturated).
         *  * **Lightness:** The brightness of the color, also represented as a percentage
         * (0% being black, 100% being white, and 50% being pure color).
         *
         * This model is primarily used in digital graphics and image editing tools for its
         * intuitive manipulation of color properties, allowing easier adjustments such as
         * changing brightness or saturation while preserving the base color.
         *
         * @since 1.0.0
         */
        HSL,

        /**
         * Represents the HSLA color model, which stands for Hue, Saturation,
         * Lightness, and Alpha.
         *
         * The HSLA model is an extension of the HSL color model with an additional
         * alpha channel to define the transparency or opacity of the color. It is often
         * used in graphics and web development to specify colors with varying levels of
         * opacity.
         *
         *  * **Hue:** The color type or shade, represented as an angle in degrees (0-360).
         *  * **Saturation:** The intensity or purity of the color, expressed as a percentage (0%-100%).
         *  * **Lightness:** The brightness or luminance of the color, expressed as a percentage (0%-100%).
         *  * **Alpha:** The transparency level of the color, ranging from 0 (completely transparent) to 1 (completely opaque).
         *
         * @since 1.0.0
         */
        HSLA,

        /**
         * Represents the HSV color model, which stands for Hue, Saturation, and Value.
         *
         * HSV is a cylindrical coordinate representation of colors in an RGB color space.
         * It is commonly used for color selection and adjustments because it is closer to
         * how humans perceive and interpret colors.
         *
         *  * **Hue:** The color type, expressed as a degree on the color wheel (0-360),
         * where 0 is red, 120 is green, and 240 is blue.
         *  * **Saturation:** The intensity or purity of the color, ranging from 0% (gray)
         * to 100% (full color).
         *  * **Value:** The brightness of the color, ranging from 0% (black) to 100% (full brightness).
         *
         * This model is particularly useful in applications such as graphic design, digital
         * image processing, and other contexts where intuitive control over color is beneficial.
         *
         * @since 1.0.0
         */
        HSV,

        /**
         * Represents the HSVA color model, an extension of the HSV (Hue, Saturation, Value) model
         * that includes an Alpha channel to define transparency.
         *
         * The HSVA model is commonly used in graphics editing, digital design, and game development
         * due to its intuitive representation of colors and easy manipulation of transparency.
         * Each component in the model describes:
         *
         *  * **Hue:** The color type, expressed as an angle on the color wheel (0-360 degrees).
         *  * **Saturation:** The intensity or purity of the color, ranging from full color (1.0) to grayscale (0.0).
         *  * **Value:** The brightness of the color, ranging from black (0.0) to full color intensity (1.0).
         *  * **Alpha:** The transparency level, ranging from fully transparent (0.0) to fully opaque (1.0).
         *
         *
         * @since 1.0.0
         */
        HSVA,

        /**
         * Represents the CMYK color model, which stands for Cyan, Magenta, Yellow, and Black.
         *
         * The CMYK model is commonly used in color printing and is based on the subtractive color process,
         * where colors are created by combining different levels of each primary component. It defines colors
         * as percentages of the four base inks (cyan, magenta, yellow, and black) rather than using light intensity
         * as in RGB-based models.
         *
         *  * **Cyan:** The percentage of cyan ink.
         *  * **Magenta:** The percentage of magenta ink.
         *  * **Yellow:** The percentage of yellow ink.
         *  * **Black:** The percentage of black ink, often used to enhance dark tones and details.
         *
         * This model is particularly well-suited for physical mediums, such as paper and fabric, where
         * light absorption and reflection define the resulting color.
         *
         * @since 1.0.0
         */
        CMYK,

        /**
         * Represents the CMYK color model with an additional Alpha channel for transparency.
         *
         * The CMYKA model is an extension of the CMYK (Cyan-Magenta-Yellow-Black) color model,
         * commonly used in printing and other applications where subtractive color mixing is essential.
         * The added Alpha channel allows for the specification of transparency, enhancing versatility
         * in graphic design, compositing, and rendering tasks.
         *
         * @since 1.0.0
         */
        CMYKA,

        /**
         * Represents the Hue-Whiteness-Blackness (HWB) color model.
         *
         * The HWB model is a cylindrical-coordinate representation of colors,
         * designed as an alternative to the Hue-Saturation-Value (HSV) color model.
         * It explicitly includes terms for the amount of white and black present in a color.
         *
         *
         *  * **Hue:** Specifies the color type and is measured in degrees on the color wheel (0 to 360).
         *  * **Whiteness:** Represents the percentage of white mixed into the color (0% to 100%).
         *  * **Blackness:** Represents the percentage of black mixed into the color (0% to 100%).
         *
         * Primarily used for its intuitive representation of colors, HWB is often implemented
         * in web design, graphics software, and other systems requiring color adjustments.
         *
         * @since 1.0.0
         */
        HWB,

        /**
         * Represents the HWBA (Hue-Whiteness-Blackness-Alpha) color model.
         *
         * The HWBA color model is an extension of the HWB (Hue-Whiteness-Blackness)
         * model, incorporating an additional Alpha channel to represent transparency.
         * This model is well-suited for describing colors in a way similar to
         * human perception while also offering control over opacity.
         *
         *  * The **Hue** determines the type of color (e.g., red, blue).
         *  * The **Whiteness** represents the proportion of white in the color.
         *  * The **Blackness** represents the proportion of black in the color.
         *  * The **Alpha** defines the transparency level, where 1 is fully opaque,
         * and 0 is completely transparent.
         *
         * This model is particularly useful for digital graphics or applications
         * requiring both color and transparency adjustments.
         *
         * @since 1.0.0
         */
        HWBA,

        /**
         * Represents the HEX color model, which utilizes hexadecimal notation
         * for specifying RGB colors. HEX color codes are widely used in web
         * design and other digital applications for their concise and
         * standardized representation of color values.
         *
         * The HEX model encodes the Red, Green, and Blue color components
         * as pairs of hexadecimal digits, resulting in a 6-character string
         * prefixed with a hash symbol (#). It offers a clean, human-readable
         * format for colors in digital media.
         *
         * @since 1.0.0
         */
        HEX,

        /**
         * Represents the hexadecimal RGBA color model.
         *
         * The HEXA model extends the HEX model by including an additional component
         * for alpha transparency. Colors are represented in a string format with
         * a combination of six hexadecimal digits for red, green, and blue components,
         * followed by two additional digits that define the alpha (transparency) channel.
         *
         * This model is widely used in digital design, web development, and
         * graphics rendering where color and transparency are defined in a compact,
         * human-readable form.
         *
         * @since 1.0.0
         */
        HEXA,

        /**
         * Represents the XYZ color model, a color space based on human vision using tristimulus values.
         *
         * The XYZ model defines colors in terms of three variables (X, Y, and Z),
         * which correspond to a linear combination of observer sensitivity curves.
         * It serves as a foundation for other color spaces and ensures device-independent
         * representation of color, often used in color science and colorimetry.
         *
         * @since 1.0.0
         */
        XYZ,

        /**
         * Represents the LAB color model, which is designed to approximate human vision and is used
         * for accurate color reproduction and comparison.
         * @since 1.0.0
         */
        LAB,

        /**
         * HSB (Hue, Saturation, Brightness) is an alias of HSV. Provided for convenience.
         * @since 1.0.0
         */
        HSB,

        /**
         * HSBA extends HSB with Alpha (alias of HSVA).
         * @since 1.0.0
         */
        HSBA
    }
}