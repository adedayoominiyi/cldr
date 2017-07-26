package com.squarespace.cldr.numbers;



import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;


public class NumberOperandsTest {

  @Test
  public void testParseOperands() {
    assertOperands("1", "neg=false n=1 i=1 v=0 w=0 f=0 t=0 nid=1 z=0");
    assertOperands("1.230", "neg=false n=1.230 i=1 v=3 w=2 f=230 t=23 nid=1 z=0");
    assertOperands("-5.23000", "neg=true n=5.23000 i=5 v=5 w=2 f=23000 t=23 nid=1 z=0");
    assertOperands("10230.122", "neg=false n=10230.122 i=10230 v=3 w=3 f=122 t=122 nid=5 z=0");
    assertOperands("123456789000.000", "neg=false n=123456789000.0 i=123456789000 v=3 w=0 f=0 t=0 nid=12 z=0");

    // count leading zeroes in decimal part
    assertOperands("123.0000001", "neg=false n=123.0000001 i=123 v=7 w=7 f=1 t=1 nid=3 z=6");
    assertOperands("-1.000000000000000001", "neg=true n=1.000000000000000001 i=1 v=18 w=18 f=1 t=1 nid=1 z=17");

    // ignore invalid trailing data
    assertOperands("1x...", "neg=false n=1 i=1 v=0 w=0 f=0 t=0 nid=1 z=0");
    assertOperands("1.230xyz", "neg=false n=1.230 i=1 v=3 w=2 f=230 t=23 nid=1 z=0");
    assertOperands("1.230..", "neg=false n=1.230 i=1 v=3 w=2 f=230 t=23 nid=1 z=0");
  }

  private void assertOperands(String number, String expected) {
    NumberOperands op = new NumberOperands(number);
    assertEquals(op.toString(), expected);
  }

}
