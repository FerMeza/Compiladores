# Compilers

Files, notes, exercises and projects from the compiler class.

## Description

The folders "Assignment 2," "Assignment 7," and "Lexical Analyzer" contain the lexical analysis and simplified versions of the Compiler's Lexical Analyzer. The exercises are related to syntactic and semantic analysis. The "Compiler" folder contains the lexical analyzer and the syntactic analyzer for the subset of the GO programming language specified in the "Gramatica2021-Final.pdf" file located in the same folder.

## Deploy and test compiler

To test this on Linux on the Compiler folder run:

```
javac -d . src/compilador/*.java
```

This would generate all the .class files, test in the same directory with the next command:

```
java compilador.Main <file_name>
```

\<file_name\> should be subsituted by prueba.f or any other file that follows the rules of the file "Gramatica2021-Final.pdf"

## Technologies Used

Java, JFlex

## Acknowledgements

I would like to express my gratitude to my former professor, Ulises, and my teammates, Paulina Aguilar Pérez and Bryan Áviles Martínez. This repository would not have been possible without their contributions. Additionally, I would like to point the JFlex tool for simplifying the creation of lexical analyzers in Java.