
squarespace-cldr - Java CLDR code generation.

WARNING: This code is a work in progress and the API may change.

See: http://cldr.unicode.org/

[![Build Status](https://travis-ci.org/Squarespace/cldr.svg?branch=master)](https://travis-ci.org/Squarespace/cldr)
[![Coverage Status](https://coveralls.io/repos/github/Squarespace/cldr/badge.svg?branch=master)](https://coveralls.io/github/Squarespace/cldr?branch=master)

License: [Apache 2.0](LICENSE)

### Resolving Locales

```java
CLDR.Locale locale = CLDR.get().resolve("zh-CN");
System.out.println(locale);
```

> "zh-Hant-CN"

```java
locale = CLDR.get().resolve("zh-HK");
System.out.println(locale);
System.out.printf("%s / %s / %s\n", locale.language(), locale.script(), locale.territory());
```
> "zh-Hant-HK"

> "zh / Hant / HK"

```java
locale = CLDR.get().resolve("sr");
System.out.println(locale);
```

> "sr-Cyrl-RS"

```java
locale = CLDR.get().resolve(java.util.Locale.CANADA_FRENCH);
System.out.println(locale);
```

> fr-Latn-CA

```java
locale = CLDR.get().resolve(java.util.Locale.JAPANESE);
System.out.println(locale);
```

> ja-Jpan-JP

```java
locale = CLDR.get().resolve("en_XY");
System.out.println(locale);
```
> "en-Latn-XY"

```java
locale = CLDR.get().resolve("und-Zzzz-ZZ");
System.out.println(locale);
```
> "en-Latn-US"

#### Minimize by removing likely subtags

```java
locale = CLDR.get().resolve("en");
System.out.println(locale);
```
> "en-Latn-US"

```java
locale = CLDR.get().minimize(locale);
System.out.println(locale);
```
> "en"

### Enhanced Language Matching

This implements [enhanced language matching](http://www.unicode.org/reports/tr35/tr35.html#EnhancedLanguageMatching) which, given a list of desired locales, tries to find the best match among the application's supported locales.

```java
LanguageMatcher matcher = new LanguageMatcher("es-419, es-ES, es-PT");
CLDR.Locale locale = matcher.match("es-AR");
System.out.println(locale);
```
> "es-419"

```java
locale = new LanguageMatcher("es, es-419, es-PT").match("es-MX");
```
> "es-419"

```java
locale = new LanguageMatcher("es, es-419, es-MX").match("es-PT");
```
> "es"

```java
locale = new LanguageMatcher("es-419, es-PT, es-MX").match("es");
```
> "es-PT"

```java
locale = new LanguageMatcher("en, en-GU, en-IN, en-GB").match("en-VI");
```
> "en"

```java
locale = new LanguageMatcher("en, en-GU, en-IN, en-GB").match("en-AU");
```
> "en-GB"

```java
locale = new LanguageMatcher("en-US, en-GU, en-IN, en-GB").match("en-019");
```
> "en-US"

```java
locale = new LanguageMatcher("en-US, en-GU, en-IN, en-GB").match("en-150");
```
> "en-GB"

### Accessing Formatters

Below are some examples of resolving a locale to get a reference to a
formatter.  Formatters are identified by their bundle identifier.

You can use a pre-defined locale directly:

```java
NumberFormatter f = CLDR.get().getNumberFormatter(CLDR.Locale.en_US);
System.out.println(f.bundleId());
```
> "en"

.. or resolve a Java format locale string:
```java
CLDR.Locale locale = CLDR.get().resolve("en_US");
f = CLDR.get().getNumberFormatter(locale);
System.out.println(f.bundleId());
```
> "en"

.. or resolve a Java locale object:
```java
locale = CLDR.get().resolve(java.util.Locale.US);
f = CLDR.get().getNumberFormatter(locale);
System.out.println(f.bundleId());
```
> "en"

.. or resolve a BCP 47 language tag:
```java
locale = CLDR.get().resolve("en-Latn-US");
f = CLDR.get().getNumberFormatter(locale);
System.out.println(f.bundleId());
```
> "en"


### Date and Time formatting

```java
StringBuilder buffer = new StringBuilder();

long epoch = 1288648500000L;
ZoneId zoneId = ZoneId.of("America/New_York");
ZonedDateTime datetime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epoch), zoneId);
CalendarFormatter f = CLDR.get().getCalendarFormatter(CLDR.Locale.en_US);
CalendarFormatOptions options = new CalendarFormatOptions();
f.format(datetime, options, buffer);
System.out.println(buffer);
```

> "11/1/2010"

```java
options = new CalendarFormatOptions().setDateSkeleton(CalendarSkeleton.GyMMMd);
f.format(datetime, options, buffer);
```

> "Nov 1, 2010 AD"

```java
options = new CalendarFormatOptions().setDateFormat(CalendarFormat.MEDIUM);
f.format(datetime, options, buffer);
```

> "Nov 1, 2010"

```java
options = new CalendarFormatOptions().setTimeFormat(CalendarFormat.MEDIUM);
f.format(datetime, options, buffer);
```

> "5:55:00 PM"

```java
options = new CalendarFormatOptions().setWrapperFormat(CalendarFormat.MEDIUM);
f.format(datetime, options, buffer);
```

> "Nov 1, 2010, 5:55:00 PM"

```java
epoch = 1288598100000L;
datetime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epoch), zoneId);
options = new CalendarFormatOptions().setWrapperFormat(CalendarFormat.FULL);
f.format(datetime, options, buffer);
```

> "Monday, November 1, 2010 at 3:55:00 AM Eastern Daylight Time"

```java
zoneId = ZoneId.of("America/Los_Angeles");
datetime = datetime.withZoneSameInstant(zoneId);
f.format(datetime, options, buffer);
```

> "Monday, November 1, 2010 at 12:55:00 AM Pacific Daylight Time"


#### Formatting individual date and time fields

```java
f.formatField(datetime, "EEEE", buffer);
```
> "Monday"

```java
f.formatField(datetime, "LLLL", buffer);
```
> "November"


#### Formatting date time intervals

```java
long epoch = 1288648500000L;
ZoneId zoneId = ZoneId.of("America/New_York");
ZonedDateTime start = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epoch), zoneId);
ZonedDateTime end = start.withYear(2020).withMonth(12);

CalendarFormatter f = CLDR.get().getCalendarFormatter(CLDR.Locale.en_US);
f.format(start, end, DateTimeIntervalSkeleton.y, buffer);
System.out.println(buffer);
```
> "2010 – 2020"

```java
f.format(start, end, DateTimeIntervalSkeleton.yMMM, buffer);
```
> "Nov 2010 – Dec 2020"

```java
end = start.withMonth(12);
f.format(start, end, DateTimeIntervalSkeleton.yMMM, buffer);
```
> "Nov – Dec 2010"

```java
end = start.withDayOfMonth(23);
f.format(start, end, DateTimeIntervalSkeleton.yMMMd, buffer);
```
> "Nov 1 – 23, 2010"

```java
end = start.withHour(22);
f.format(start, end, DateTimeIntervalSkeleton.hmv, buffer);
```
> "5:55 – 10:55 PM ET"

```java
start = start.withMinute(1);
end = start.withMinute(27);
f.format(start, end, DateTimeIntervalSkeleton.Hm, buffer);
```
> "17:01 – 17:27"

A helper method exists to find the field of greatest difference, which will help
you choose whether to use a date or time skeleton:

```java
end = start.withMonth(12);
DateTimeField field = CalendarUtils.fieldOfGreatestDifference(start, end);
System.out.println(field);
```
> MONTH


### Decimal number formatting

```java
StringBuilder buffer = new StringBuilder();
NumberFormatter f = CLDR.get().getNumberFormatter(CLDR.Locale.en_US);
BigDecimal n = BigDecimal.valueOf(Math.PI);
DecimalFormatOptions options = new DecimalFormatOptions();
f.formatDecimal(n, buffer, options);
System.out.println(buffer);
```

> "3.142"

```java
options = new DecimalFormatOptions().setMaximumFractionDigits(5);
f.formatDecimal(n, buffer, options);
```

> "3.14159"

```java
options = new DecimalFormatOptions().setRoundMode(NumberRoundMode.FLOOR);
f.formatDecimal(n, buffer, options);
```

> "3.141"

```java
n = new BigDecimal("10000");

options = new DecimalFormatOptions().setMinimumFractionDigits(2).setGrouping(true);
f.formatDecimal(n, buffer, options);
```

> "10,000.00"

```java
n = new BigDecimal("0.5");

options = new DecimalFormatOptions().setStyle(DecimalFormatStyle.PERCENT);
f.formatDecimal(n, buffer, options);
```

> "50%"

#### Compact forms

```java
n = new BigDecimal("999.95");

options = new DecimalFormatOptions(DecimalFormatStyle.LONG);
f.formatDecimal(n, buffer, options);
```

> "1 thousand"

```java
CLDR.get().getNumberFormatter(CLDR.Locale.fr).formatDecimal(n, buffer, options);
```

> "1 millier"

Pattern is chosen using plural category of the visible number.

```java
for (String num : new String[] { "1000", "1200", "2000", "5000" }) {
    n = new BigDecimal(num);
    CLDR.get().getNumberFormatter(CLDR.Locale.pl).formatDecimal(n, buffer, options);
}
```

> "1 tysiąc"

> "1,2 tysiąca"

> "2 tysiące"

> "5 tysięcy"

### Unit formatting

```java
StringBuilder buffer = new StringBuilder();
UnitFormatOptions options = new UnitFormatOptions();
NumberFormatter f = CLDR.get().getNumberFormatter(CLDR.Locale.en_US);

UnitConverter converter = CLDR.get().getUnitConverter(CLDR.Locale.en_CA);
System.out.println(converter.measurementSystem());
```

> METRIC

```java
converter = CLDR.get().getUnitConverter(CLDR.Locale.en_GB);
System.out.println(converter.measurementSystem());
```

> UK


```java
converter = CLDR.get().getUnitConverter(CLDR.Locale.en_US);
System.out.println(converter.measurementSystem());
```

> US

```java
UnitValue value = new UnitValue(("1234567", Unit.FOOT);
UnitValue converted = converter.convert(value, Unit.KILOMETER);
f.formatUnit(converted, buffer, options);
System.out.println(converted);
System.out.println(buffer)
```

> UnitValue(376.2960216, KILOMETER)

> "376.3km"

```java
value = new UnitValue("125.785", Unit.MEGABYTE);
f.formatUnit(value, buffer, options);
```

> "125.8MB"

```java
converted = converter.convert(value, Unit.GIGABYTE);
f.formatUnit(converted, buffer, options);
System.out.println(converted);
System.out.println(buffer);
```

> UnitValue(0.1228369140625, GIGABYTE)

> "0.1GB"

```java
options.setGrouping(true);
value = new UnitValue("112233445566778899", Unit.BYTE);
converted = converter.bytes(value);
f.formatUnit(converted, buffer, options);
```

> "102,075.7TB"

```java
options = new UnitFormatOptions(UnitFormat.LONG).setGrouping(true);
f = CLDR.get().getNumberFormatter(CLDR.Locale.fr);
f.formatUnit(converted, buffer, options);
```

> "102075,7 téraoctets"


#### Unit sequence formatting

```java
StringBuilder buffer = new StringBuilder();
UnitFormatOptions longOptions = new UnitFormatOptions(UnitFormat.LONG);
UnitFormatOptions narrowOptions = new UnitFormatOptions(UnitFormat.NARROW);

UnitConverter converter = CLDR.get().getUnitConverter(CLDR.Locale.en_US);
UnitValue degrees = new UnitValue("13.536613", Unit.DEGREE);
List<UnitValue> angle = converter.sequence(degrees, UnitConstants.ANGLE_FACTORS);
System.out.println(angle);
```

> [UnitValue(13, DEGREE), UnitValue(32, ARC_MINUTE), UnitValue(11.8068, ARC_SECOND)]

```java
NumberFormatter fmt = CLDR.get().getNumberFormatter(CLDR.Locale.en_US);
fmt.formatUnits(angle, buffer, longOptions);
```

> "13 degrees 32 arcminutes 11.8 arcseconds"

```java
buffer.setLength(0);
fmt.formatUnits(angle, buffer, narrowOptions);
```

> "13° 32′ 11.8″"

```java
buffer.setLength(0);
UnitValue days = new UnitValue("753", Unit.DAY);
List<UnitValue> duration = converter.sequence(days, UnitConstants.DURATION_FACTORS);
fmt.formatUnits(duration, buffer, longOptions);
```

> "2 years 22 days 12 hours 21 minutes 36 seconds"

```java
fmt.formatUnits(duration, buffer, narrowOptions);
```

> "2y 22d 12h 21m 36s"

```java
fmt = CLDR.get().getNumberFormatter(CLDR.Locale.ko);
fmt.formatUnits(duration, buffer, narrowOptions);
```

> "2년 22일 12시간 21분 36초"

#### Formatting with custom unit factor sets

```java
fmt = CLDR.get().getNumberFormatter(CLDR.Locale.en_US);
days = new UnitValue("753.35", Unit.DAY);
UnitFactorSet durationFactors = new UnitFactorSet(UnitFactors.DURATION, Unit.WEEK, Unit.HOUR);
duration = converter.sequence(days, durationFactors);
System.out.println(duration);
```

> [UnitValue(107, WEEK), UnitValue(104.4, HOUR)]

```java
fmt.formatUnits(duration, buffer, longOptions);
```
> "107 weeks 104.4 hours"

```java
UnitValue inches = new UnitValue("1234567", Unit.INCH);
UnitFactorSet lengthFactors = new UnitFactorSet(UnitFactors.LENGTH, Unit.MILE, Unit.FOOT);
List<UnitValue> length = converter.sequence(inches, lengthFactors);
System.out.println(length);
```

> [UnitValue(19, MILE), UnitValue(2560.583333333333, FOOT)]

```java
fmt.formatUnits(length, buffer, narrowOptions);
```

> "19mi 2560.6′"

#### Switching on Metric vs English

```java
for (CLDR.Locale locale : new CLDR.Locale[] { CLDR.Locale.en_CA, CLDR.Locale.en_US }) {
    converter = CLDR.get().getUnitConverter(locale);
    if (converter.measurementSystem().usesMetric(UnitCategory.LENGTH)) {
        lengthFactors = UnitFactorSets.LENGTH;
    } else {
        lengthFactors = UnitFactorSets.LENGTH_ENGLISH;
    }
    length = converter.sequence(inches, lengthFactors);

    buffer.append(locale).append(' ');
    fmt.formatUnits(length, buffer, longOptions);
    buffer.append('\n');
}
```
> "en-CA 31 kilometers 358 meters 0.2 centimeters"

> "en-US 19 miles 853 yards 1 foot 7 inches"


### Currency formatting

```java
StringBuilder buffer = new StringBuilder();
NumberFormatter f = CLDR.get().getNumberFormatter(CLDR.Locale.en_US);

BigDecimal n = new BigDecimal("1");
CurrencyFormatOptions options = new CurrencyFormatOptions();
f.formatCurrency(n, CLDR.Currency.USD, buffer, options);
System.out.println(buffer);
```

> "$1.00"

```java
n = new BigDecimal("-1");
options = new CurrencyFormatOptions(CurrencyFormatStyle.ACCOUNTING);
f.formatCurrency(n, CLDR.Currency.USD, buffer, options);
```

> "($1.00)"

```java
n = new BigDecimal("69900");
options = new CurrencyFormatOptions(CurrencyFormatStyle.NAME);
f.formatCurrency(n, CLDR.Currency.USD, buffer, options);
```

> "69,900.00 US dollars"

```java
options = new CurrencyFormatOptions(CurrencyFormatStyle.CODE);
f.formatCurrency(n, CLDR.Currency.USD, buffer, options);
```

> "69,900.00 USD"

```java
n = new BigDecimal("1.491");
options = new CurrencyFormatOptions().setRoundMode(NumberRoundMode.CEIL);
f.formatCurrency(n, CLDR.Currency.USD, buffer, options);
```

> "$1.50"

```java
options = new CurrencyFormatOptions(CurrencyFormatStyle.SHORT)
    .setFormatMode(NumberFormatMode.SIGNIFICANT_MAXFRAC)
    .setMaximumFractionDigits(1);
n = new BigDecimal("1200");
f.formatCurrency(n, CLDR.Currency.USD, buffer, options);
```

> "$1.2K"

```java
n = new BigDecimal("999999");
f.formatCurrency(n, CLDR.Currency.USD, buffer, options);
```

> "$1M"

```java
f.formatCurrency(n, CLDR.Currency.EUR, buffer, options);
```

> "€1M"


### Message Formatter

The `MessageFormat` class allows you to extract localized strings from your application
with inline formatting of typed arguments.

The types available are: `plural`, `number`, `currency`, `datetime`, `datetime-interval` and `unit`.



```java
ZoneId tzNewYork = ZoneId.of("America/New_York");
ZoneId tzParis = ZoneId.of("Europe/Paris");
StringBuilder buf = new StringBuilder();

String format = "Transmission of {0 unit compact:bytes} "
    + "took {1 unit in:second sequence:hour,minute,second format:long}";

MessageFormat msg = new MessageFormat(CLDR.Locale.en_US, tzNewYork, format);
MessageArgs args = MessageArgs.newBuilder()
    .add("1234567890")
    .add("12345")
    .build();
msg.format(args, buf);
System.out.println(buf);
```

> "Transmission of 1.1GB took 3 hours 25 minutes 45 seconds"

```java
msg = new MessageFormat(CLDR.Locale.fr_FR, tzParis, format);
msg.format(args, buf);
```

> "Transmission of 1,1 Go took 3 heures 25 minutes 45 secondes"

#### Positional or named arguments

This demonstrates plural, currency and datetime arguments.

```java
format = "The total for the {count, plural, one{product} other{# products}} "
    + "you ordered is {amount currency} "
    + "on {2 datetime wrap:full}.";

msg = new MessageFormat(CLDR.Locale.en_US, tzNewYork, format);
StringMessageArg amount = new StringMessageArg("1234.56");
amount.setCurrency("USD");
args = MessageArgs.newBuilder()
    .add("count", "1")
    .add("amount", amount)
    .add("1498584124000")
    .build();
msg.format(args, buf);
```
> The total for the product you ordered is $1,234.56 on
> Tuesday, June 27, 2017 at 1:22:04 PM Eastern Daylight Time.

```java
msg = new MessageFormat(CLDR.Locale.en_US, tzNewYork, format);
args = MessageArgs.newBuilder()
    .add("count", "23")
    .add("amount", amount)
    .add("1498584124000")
    .build();
msg.format(args, buf);
```

> The total for the 23 products you ordered is $1,234.56 on
> Tuesday, June 27, 2017 at 1:22:04 PM Eastern Daylight Time.


#### Date time intervals

```java
format = "The event takes place from {0;1 datetime-interval MMMd} and we hope to "
    + "raise {2 currency style:short} for our foundation.";

msg = new MessageFormat(CLDR.Locale.en_US, tzNewYork, format);
amount = new StringMessageArg("999990");
amount.setCurrency("EUR");
args = MessageArgs.newBuilder()
    .add("1509647217000")
    .add("1509819011000")
    .add(amount)
    .build();
msg.format(args, buf);
```

> "The event takes place from Nov 2 – 4 and we hope to raise €1M for our foundation."

```java
format = "Congratulations, you came in {0 selectordinal one{#st} two{#nd} few{#rd} other{#th}} place, "
    + "up from {1 selectordinal one{#st} two{#nd} few{#rd} other{#th}}, "
    + "that's quite an improvement!";
msg = new MessageFormat(CLDR.Locale.en_US, tzNewYork, format);
args = MessageArgs.newBuilder()
    .add("3")
    .add("27")
    .build();
msg.format(args, buf);
```

> "Congratulations, you came in 3rd place, up from 27th, that's quite an improvement!"

