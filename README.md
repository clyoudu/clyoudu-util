# clyoudu-util

## byteformat
Formatting byte size to human readable format, or converting unit from X to Y.
- unit system: `SI/IEC`
- scale: number of decimal places
- roundingMode: round policy
- displayUnitName: display unit name if true, or display value only
Usage:
```java
System.out.println(ByteUnitFormat.B.to(ByteUnitFormat.K, 1024L, Rate.SI));//1.02K
System.out.println(ByteUnitFormat.B.to(ByteUnitFormat.K, 1024L, Rate.IEC));//1K
System.out.println(ByteUnitFormat.M.to(ByteUnitFormat.G, 4096L, Rate.IEC));//4G
System.out.println(ByteUnitFormat.M.to(ByteUnitFormat.G, 4096L));//4G
System.out.println(ByteUnitFormat.B.humanReadable(174541987L));//166.5M
System.out.println(M.humanReadable(1024L));//1G
System.out.println(M.humanReadable(1024L, false));//1
```

## consoletable
Printing formatted table to console(simple ascii table).
- lineSep: separator of each line
- verticalSep: vertical separator of each cell(one character)
- horizontalSep: horizontal separator of each cell(one character)
- joinSep: cell point(one character)
- nullPolicy: policy for null value process
- restrict: number of columns for each row/header must be the same when strict mode used
Usage:
```java
List<Cell> header = new ArrayList<Cell>(){{
    add(new Cell("name"));
    add(new Cell("email"));
    add(new Cell("tel"));
}};
List<List<Cell>> body = new ArrayList<List<Cell>>(){{
    add(new ArrayList<Cell>(){{
        add(new Cell("kat"));
        add(new Cell(Align.CENTER,"kat@gimal.com"));
        add(new Cell(Align.RIGHT,"54321"));
    }});
    add(new ArrayList<Cell>(){{
        add(new Cell("ashe"));
        add(new Cell("ashe_111@hotmail.com"));
        add(new Cell("9876543210"));
    }});
    add(new ArrayList<Cell>(){{
        add(null);
        add(new Cell(null));
        add(new Cell(Align.LEFT,"11"));
    }});
}};
//default
new ConsoleTable.ConsoleTableBuilder().addHeaders(header).addRows(body).build().print();

//no header
new ConsoleTable.ConsoleTableBuilder().addRows(body).build().print();

//restrict
header.add(new Cell("not restrict"));
//new ConsoleTable.ConsoleTableBuilder().addHeaders(header).addRows(body).restrict(false).build().print();
new ConsoleTable.ConsoleTableBuilder().addHeaders(header).addRows(body).restrict(true).build().print();

//"null"
new ConsoleTable.ConsoleTableBuilder().addHeaders(header).addRows(body).nullPolicy(NullPolicy.NULL_STRING).build().print();
new ConsoleTable.ConsoleTableBuilder().addHeaders(header).addRows(body).nullPolicy(NullPolicy.THROW).build().print();

//line sep
//new ConsoleTable.ConsoleTableBuilder().addHeaders(header).addRows(body).lineSep("\n\n").build().print();

//vertical sep & horizontal sep & join sep
//new ConsoleTable.ConsoleTableBuilder().addHeaders(header).addRows(body).verticalSep("*").horizontalSep("*").joinSep("*").build().print();

/**
 * +------+----------------------+------------+
   | name | email                | tel        |
   +------+----------------------+------------+
   | kat  |    kat@gimal.com     |      54321 |
   | ashe | ashe_111@hotmail.com | 9876543210 |
   |      |                      | 11         |
   +------+----------------------+------------+
 */
```

more similar tools:

[TableList.java](https://github.com/therealfarfetchd/crogamp/blob/master/src/com/github/mrebhan/crogamp/cli/TableList.java)

[java-ascii-table](http://bethecoder.com/applications/products/asciiTable.action)

[asciitable](https://github.com/vdmeer/asciitable)

## pakagegenerator
generate directory structure to string
- ignoreDirs: ignored directories will not appended to result
- packageCollapse: if true, empty directories in a row, like 'github/clyoudu/util', will generated as `github.clyoudu.util`
- generateFile: if true, files will appended to result
Usage:
```java
System.out.print(generate(System.getProperty("user.dir") + "/src/main/java", new HashSet<>(Arrays.asList(".idea", "target", ".git")), true, true));
System.out.println("\n\n------------------------------------------------\n");
System.out.print(generate(System.getProperty("user.dir") + "/src/main/java", new HashSet<>(Arrays.asList(".idea", "target", ".git")), false, false));
/**
 * java.github.clyoudu
   ├── byteformat
   │   └── ByteUnitFormat.java
   ├── consoletable
   │   ├── ConsoleTable.java
   │   ├── enums
   │   │   ├── Align.java
   │   │   └── NullPolicy.java
   │   ├── table
   │   │   ├── Body.java
   │   │   ├── Cell.java
   │   │   └── Header.java
   │   └── util
   │       └── PrintUtil.java
   └── packagegenerator
       └── PackageGenerator.java

   ------------------------------------------------

   java
   └── github
       └── clyoudu
           ├── byteformat
           ├── consoletable
           │   ├── enums
           │   ├── table
           │   └── util
           └── packagegenerator
 */
```