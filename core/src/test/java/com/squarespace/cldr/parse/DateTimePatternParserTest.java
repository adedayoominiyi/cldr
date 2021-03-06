package com.squarespace.cldr.parse;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.squarespace.cldr.parse.DateTimePatternParser;
import com.squarespace.cldr.parse.FieldPattern.Field;
import com.squarespace.cldr.parse.FieldPattern.Node;
import com.squarespace.cldr.parse.FieldPattern.Text;
import com.squarespace.cldr.parse.WrapperPatternParser;


public class DateTimePatternParserTest {

  private static final DateTimePatternParser DATETIME_PARSER = new DateTimePatternParser();
  private static final WrapperPatternParser WRAPPER_PARSER = new WrapperPatternParser();

  private static final Node[] EMPTY = new Node[]{};

  @Test
  public void testParse() {
    assertPattern("y/M/d 'at' h:m",
        field('y', 1), text("/"), field('M', 1), text("/"), field('d', 1),
        text(" at "),
        field('h', 1), text(":"), field('m', 1));
  }

  @Test
  public void testParseWrapper() {
    assertWrapper("{1} 'at' {0}", field('1', 0), text(" at "), field('0', 0));
  }

  private void assertPattern(String pattern, Node ...expected) {
    Node[] actual = DATETIME_PARSER.parse(pattern).toArray(EMPTY);
    assertEquals(expected, actual);
  }

  private void assertWrapper(String pattern, Node ...expected) {
    System.out.println(WRAPPER_PARSER.parseWrapper(pattern));
    Node[] actual = WRAPPER_PARSER.parseWrapper(pattern).toArray(EMPTY);

    assertEquals(expected, actual);
  }

  private Node text(String text) {
    return new Text(text);
  }

  private Node field(char ch, int width) {
    return new Field(ch, width);
  }

}
