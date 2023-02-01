package com.pixar02.papi.expansion.argument;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Pattern;

public enum ArgumentSplitType {
  NO_OMIT("_"),
  OMIT_PARENTHESIS("_(?![^(]*\\))"),
  OMIT_SQUARE_BRACKET("_(?![^[]*])"),
  OMIT_ANGLE_BRACKET("_(?![^{]*})");

  public final static @Nonnull ArgumentSplitType DEFAULT = ArgumentSplitType.OMIT_ANGLE_BRACKET;

  private final @Nonnull String patternString;

  private @Nullable Pattern cachedPattern;

  ArgumentSplitType(@Nonnull String patternString) {
    this.patternString = patternString;
  }

  public @Nonnull Pattern getPattern() {
    if (this.cachedPattern == null) {
      this.cachedPattern = Pattern.compile(patternString);
    }
    return this.cachedPattern;
  }
}
