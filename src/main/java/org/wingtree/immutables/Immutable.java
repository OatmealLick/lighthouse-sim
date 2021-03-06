package org.wingtree.immutables;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation defining style of code generation performed by Immutables
 */
@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
@JsonSerialize
@JsonDeserialize
@Value.Style(
        get = {"is*", "get*"},
        init = "with*",
        typeAbstract = {"*"},
        visibility = Value.Style.ImplementationVisibility.PUBLIC,
        depluralize = true,
        allParameters = true)
public @interface Immutable
{
}
