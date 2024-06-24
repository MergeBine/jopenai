package com.mergebine.jopenai;

import org.jetbrains.annotations.NotNull;

import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation can be applied to a package, class or method to indicate that the class fields,
 * method return types and parameters in that element are not null by default unless there is:
 * An explicit null annotation
 * The method overrides a method in a superclass (in which case the annotation
 * of the corresponding parameter in the superclass applies) there is a
 * default parameter annotation applied to a more tightly nested element.
 * <p>
 * https://stancalau.ro/java-package-nullability-contract/
 */
@Documented
@NotNull
@TypeQualifierDefault({
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.PACKAGE,
        ElementType.PARAMETER,
        ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NonNullByDefault {
}
