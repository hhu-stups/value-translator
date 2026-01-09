# Value Translator

Library to translate **Classical B** values into Java objects.

This library provides a Java representation for B value types, i.e. sets,
strings, numbers, etc.
It **translates** B expressions to Java -- without evaluating them.

## Installation 

### Snapshot Versions

`implementation "de.hhu.stups:value-translator:0.2.3-SNAPSHOT"`

Snapshot versions are available in the [Sonatype snapshots repository](https://central.sonatype.com/repository/maven-snapshots/).
To use a snapshot version you need to add the snapshots repository to your build, e.g. using Gradle:

```groovy
repositories {
    mavenCentral()
    maven {
        name = "Sonatype snapshots"
        url = "https://central.sonatype.com/repository/maven-snapshots/"
    }
}
```

### Releases 

**Current Version**: 0.2.2

`implementation "de.hhu.stups:value-translator:0.2.2"`

Releases are published on [Maven Central](https://central.sonatype.com/artifact/de.hhu.stups/value-translator).

## Usage

The central interface of *Value Translator* is the [`Translator`](src/main/java/de/hhu/stups/prob/translator/Translator.java) class in the
`de.hhu.stups.prob.translator` package.
This class provides a static `translate(String expr)` method. This method
expects a valid snippet of **B** and returns a Java representation of the
snippet.

Each class, representing a value or a collection of values, provides means to
translate the represented values to Java objects.

### Examples

**Values**:

```java
BNumber number = Translator.translate("5");
number.intValue(); // 5
```

**Collections**:

*Value Translator* supports several collection types, e.g. sets:

```java
BSet<BNumber> bSet = Translator.translate("{1,2,3,2}");
System.out.println(bSet); // {1,2,3}
bSet.stream().mapToInt(BNumber::intValue).sum(); // 6
```

Some collections types are provided as interpretations of sets, e.g. functions,
these are computed and validated at runtime:

```java
BSet<BTuple<BNumber, BAtom>> set = Translator.translate("{(1,a), (2, b), (3,c)}");
Map<Integer, String> map = set
                            .asFunction(BNumber.class, BAtom.class)
                            .toMap(BNumber::intValue, BAtom::stringValue);
```

## Types

*Value Translator* exposes B types on two levels. The translator itself supports and returns the following types:

* Set ([`BSet`](src/main/java/de/hhu/stups/prob/translator/BSet.java)) for sets, functions, relations and sequences (independently of the used notation).
* Boolean ([`BBoolean`](src/main/java/de/hhu/stups/prob/translator/BBoolean.java))
* Integer ([`BNumber`](src/main/java/de/hhu/stups/prob/translator/BNumber.java))
* Real number ([`BReal`](src/main/java/de/hhu/stups/prob/translator/BReal.java))
* Record ([`BRecord`](src/main/java/de/hhu/stups/prob/translator/BRecord.java))
* Atom ([`BAtom`](src/main/java/de/hhu/stups/prob/translator/BAtom.java)) used for identifiers and the names of enumerated sets.
* String ([`BString`](src/main/java/de/hhu/stups/prob/translator/BString.java))
* Tuple ([`BTuple`](src/main/java/de/hhu/stups/prob/translator/BTuple.java)) for tuples and maplets. 
* Symbolic ([`BSymbolic`](src/main/java/de/hhu/stups/prob/translator/BSymbolic.java)) for symbolic values of any type. 

### Relations, Functions and Sequences

Sets provide methods to interpret theses as either relations, sequences or
functions. All of these interpretations represent additional constraints on the
elements of a set. The constraints are validated when the reinterpretation
methods are invoked. 

These interpretations are represented by the following types:

* Sequence ([`BSequence`](src/main/java/de/hhu/stups/prob/translator/interpretations/BSequence.java))
* Relations ([`BRelation`](src/main/java/de/hhu/stups/prob/translator/interpretations/BRelation.java))
* Function ([`BFunction`](src/main/java/de/hhu/stups/prob/translator/interpretations/BFunction.java))


**Note**: All value classes implement the `BValue` interface.

## Reinterpreting Sets

Relations, functions and sequences are translated as sets.
*Value Translator* provides means to reinterpret a set as either of these
types.

All mapping methods are provided in two variants, with and without arguments.
In some cases, for instance when chaining calls, it might be necessary to pass
the types of the values in a collection as arguments.

Relations, functions and sequences are sets with additional constraints. These 
constraints, which are not exhaustive, are validated on reinterpretation
and incur additional runtime costs.

### Relations

Functions are sets of pairs.

#### Constraints

* All elements must be tuples.

#### Set methods to create a relation

* `<K,V>asRelation()`
* `<K,V>asRelation(Class<K> domainType, Class<V> rangeType)`

### Functions

Functions are sets of pairs, where the first element of each pair is unique in
the set.

#### Constraints

* All elements must be tuples.
* The first element of each tuple is unique in the set.

#### Set methods to create a function

* `<K,V>asFunction()`
* `<K,V>asFunction(Class<K> domainType, Class<V> rangeType)`

### Sequences

A function where the domain is the range of number from `1` to `n`, where `n`
is the cardinality of the set.

#### Constraints

* All elements must be tuples.
* The first element of each tuple is an instance of BNumber.

#### Set methods to create a sequence

* `<K>asSequence()`
* `<K,V>asSequence(Class<K> domainType, Class<V> rangeType)`

## Extracting values from BValues

All B Types provide methods to convert them to Java objects or collections of objects.

```java
BNumber number = Translator.translate("5");
number.intValue(); // 5
```

Collections Types, i.e. sets, sequences, relations and functions provide 
methods to get a collection of BValue objects or, provided with a method to
extract the values, a collection of arbitrary objects. 

Records provide a map of key value pairs that map a Java String to a BValue.

Tuples provide access to the two BValues they contain.

#### Value Types

* [`BAtom`](src/main/java/de/hhu/stups/prob/translator/BAtom.java): `BAtom.stringValue()`
* [`BSymbolic`](src/main/java/de/hhu/stups/prob/translator/BSymbolic.java): `BSymbolic.stringValue()`
* [`BBoolean`](src/main/java/de/hhu/stups/prob/translator/BBoolean.java): `BBoolean.booleanValue()`
* [`BNumber`](src/main/java/de/hhu/stups/prob/translator/BNumber.java): `BNumber.intValue()`, `BNumber.longValue()`, `BNumber.bigIntegerValue()`
* [`BReal`](src/main/java/de/hhu/stups/prob/translator/BReal.java): `BNumber.floatValue()`, `BNumber.doubleValue()`
* [`BString`](src/main/java/de/hhu/stups/prob/translator/BString.java): `BString.stringValue()`

#### Collection Types

Collection types support generics to reduce the number of necessary casts.

**[`BSet`](src/main/java/de/hhu/stups/prob/translator/BSet.java)**

* `BSet.<T>toSet()`: a set of BValue typed objects.

**[`BFunction`](src/main/java/de/hhu/stups/prob/translator/interpretations/BFunction.java)**

* `BFunction.<K, V>toMap()`: a map of BValue to BValue pairs
* `BFunction.<K, V>toMap(Function keyMapper, Function valueMapper)`: a map containing the
    results of applying `keyMapper` to each key and `valueMapper` to the
    corresponding value.

**[`BRelation`](src/main/java/de/hhu/stups/prob/translator/interpretations/BRelation.java)**

* `BRelation.<K,V>toRelationalMap()`: map containing BValues as keys and a list of
    BValues for that key.
* `BRelation.<K, V>toRelationalMap(Function keyMapper, Function valueMapper)`: a map
    containing as keys the result of applying `keyMapper` to each key. For each 
    key the map contains the list of results of applying `valueMapper` to each
    value. Elements are grouped by the computed keys after applying `keyMapper`.

**[`BSequence`](src/main/java/de/hhu/stups/prob/translator/interpretations/BSequence.java)**

* `BSequence.<K>toList()` list of BValues. The list preserves the sequence order.
    The resulting list is zero-indexed.
* `BSequence.<K>toList(Function mapper)` list containing the results of applying
    `mapper` to each value in the sequence. The resulting list preserves the
    sequence order. The resulting list is zero-indexed.

**[`BTuple`](src/main/java/de/hhu/stups/prob/translator/BTuple.java)**

* `Tuple.<T>getFirst()`: BValue, first component of the tuple
* `Tuple.<T>getSecond()`: BValue, second component of the tuple

**[`BRecord`](src/main/java/de/hhu/stups/prob/translator/BRecord.java)**

* `BRecord.<String, BValue>toMap()`

## Future work

* [ ] Additional validations for relation, function and sequence reinterpretations
* [ ] Support for ProB/ProB 2.0 Internal AST Representation
* [ ] Improve the interface for records (extractors, generics)

## License

MIT License, see [LICENSE](LICENSE) for details.
