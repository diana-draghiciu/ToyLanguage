# ToyLanguage
 
A Java project that is implemented as an interpreter for a toy language. 


-Model-view-controller architectural pattern and object-oriented concepts used. 


-Provides a graphical user interface (JavaFX).


-Multi-threading implemented


-Handles exceptions


-Expressions: arithmetic (+,-,*,/), logic (&,|), relational (<,<=,==,!=,>,>=), variable, value, read from heap expressions.


-Statements: variable declaration statement, assign, compound, fork, if, open/close file, read from file, print, switch, while etc


Toy Language Evaluation (Execution):


Once a program is selected from the preloaded list, the user can view its execution step by step, being able to view the ProgramID, SymbolTable, ExecutionStack, OutTable, HeapTable, Semaphore, and FileTable.

Our mini interpreter uses several structures:


–Execution Stack (ExeStack): a stack of statements to execute the currrent program


–Table of Symbols (SymTable): a table which keeps the variables values


–Output (OutTable): that keeps all the messages printed by the toy program


-Heap (HeapTable): manages the heap memory.


-Table of Files (FileTable): a table which keeps all the files from which data is read


All these structures denote the program state (PrgState). Our interpreter can execute multiple programs but for each of them use a different PrgState structures.  At the beginning, ExeStack contains the original program, and SymTable, Heap, FileTable and Out are empty. After the evaluation has started, ExeStack contains the remaining part of the program  that must be evaluated, SymTable contains the variables (from the variable declarations statements evaluated so far) with their assigned values, OutTable contains the values printed so far, HeapTable contains the adress and the value of the variables assigned on the heap and FileTable the files opened to read from.
