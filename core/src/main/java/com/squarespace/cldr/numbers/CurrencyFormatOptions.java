package com.squarespace.cldr.numbers;


/**
 * Options to select and customize a currency format.
 */
public class CurrencyFormatOptions extends NumberFormatOptions<CurrencyFormatOptions> {

  private static final CurrencyFormatStyle DEFAULT_STYLE = CurrencyFormatStyle.SYMBOL;

  protected CurrencyFormatStyle style;

  public CurrencyFormatOptions() {
    this(DEFAULT_STYLE);
  }

  public CurrencyFormatOptions(CurrencyFormatStyle style) {
    setStyle(style);
  }

  /**
   * Reset the options to their defaults.
   */
  public void reset() {
    this.style = DEFAULT_STYLE;
    super.reset();
  }

  public CurrencyFormatStyle style() {
    return style;
  }

  public CurrencyFormatOptions setStyle(CurrencyFormatStyle style) {
    this.style = style == null ? DEFAULT_STYLE : style;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append("CurrencyFormatOptions(");
    repr(buf, ", ");
    buf.append(')');
    return buf.toString();
  }

  /**
   * Format this object's properties using the given separator.
   */
  public void repr(StringBuilder buf, String sep) {
    if (style != null) {
      buf.append("style=").append(style);
    }
    super.repr(buf, sep);
  }

}
