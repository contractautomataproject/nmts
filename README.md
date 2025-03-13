# NMTS Refinement Checker


This repository contains an implementation of the Non-reducible Modal Transition Systems (NMTS) refinement.
The class `src/main/java/io/github/contractautomata/nmts/NMTSRefinement.java` contains the implementation of the refinement, following the algorithms presented in the paper "Non-reducible Modal Transition Systems" (currently submitted for publication). 
This class is the executable tool that can be used to check whether two NMTS are in NMTS refinement relation.

The class `src/main/java/io/github/contractautomata/nmts/Main.java` is an executable running the NMTS refinement on the examples in the paper.
For each example involving the NMTS refinement, the reachability closed sets as well as the refinement relations (when existing) are reported.
All NMTSs are located in the folder `src/main/resources`.

The contract automata library (CATLib) is used to store and load the NMTSs as modal contract automata. 
Furthermore, the synchronous product used by the NMTS refinement for computing the reachability closed sets is computed using the composition function of CATLib.


### Getting started

The easiest way to start is to download the released jar files of this repository (see Releases in the right column).
Both jar files are runnable from command-line, and they only require a Java distribution compatible with version 17.  
The file `NMTSRefinement.jar` is the NMTS refinement checker tool. The file packages the executable class `src/main/java/io/github/contractautomata/nmts/NMTSRefinement.java` with all its dependencies and resources. 
This is an example of execution of the tool (assuming that the executable and the NMTS files are located in the same folder):

```console
> java -jar NMTSRefinement.jar Fig8_T.data Fig8_U.data
--------------------------------------------------------
 NMTS Refinement Checker
--------------------------------------------------------
Usage:
    java -jar NMTSRefinement.jar <file1> <file2>

Description:
    This tool checks whether the first file (file1) is an NMTS refinement of the second file (file2).

Arguments:
    <file1>   Path to the first NMTS file.
    <file2>   Path to the second NMTS file.

Fig8_T.data is an NMTS refinement of Fig8_U.data
Refinement relation : [[t1, t1], [t, t], [t3, t3], [ti, ti], [t2, t2]]
```

The file `NMTS-1.0-SNAPSHOT.jar` is packaging the executable class `src/main/java/io/github/contractautomata/nmts/Main.java` with all its dependencies and resources.

To execute it type:
```console
> java -jar NMTS-1.0-SNAPSHOT.jar
```

By running this executable, the following output will be printed at console:

```console
Figure 2:
Reachability Closed sets of S: [[[s5]], [[s]], [[s2], [s1]], [[s4], [s3]]]
S is an NMTS: false
Reachability Closed sets of T:[[[t]], [[t5]], [[t2], [t1]], [[t4], [t3]]]
T is an NMTS: false
Warning: NMTS Refinement is not checking whether the operands are NMTS or MTS.
S <n T : false

Figure 4:
Reachability Closed sets of S: [[[s]], [[s2], [s1]], [[s5]], [[s4], [s3]]]
S is an NMTS: true
Reachability Closed sets of T:[[[t3], [t4], [t5]], [[t2], [t1]], [[t]], [[t6]]]
T is an NMTS: true
S <n T : false

Reachability Closed sets of I: [[[i3], [i4]], [[i]], [[i2], [i1]], [[i5]]]
I is an NMTS: true
Reachability Closed sets of S:[[[s]], [[s2], [s1]], [[s5]], [[s4], [s3]]]
S is an NMTS: true
I <n S : true
R_(I <n S) : [[i2, s2], [i3, s3], [i1, s1], [i5, s5], [i4, s4], [i, s]]

Reachability Closed sets of I: [[[i3], [i4]], [[i]], [[i2], [i1]], [[i5]]]
I is an NMTS: true
Reachability Closed sets of T:[[[t3], [t4], [t5]], [[t2], [t1]], [[t]], [[t6]]]
T is an NMTS: true
I <n T : false

Figure 6:
Reachability Closed sets of S: [[[s]], [[s1], [s2]], [[s3], [s4]]]
S is an NMTS: false
Reachability Closed sets of T:[[[s2], [s1]], [[s]], [[s3], [s4]]]
T is an NMTS: true
Warning: NMTS Refinement is not checking whether the operands are NMTS or MTS.
S <n T : false

Figure 7:
Reachability Closed sets of S: [[[s]], [[s2], [s1]], [[s3]]]
S is an NMTS: true
Reachability Closed sets of T:[[[t1]], [[t2]], [[t]]]
T is an NMTS: true
S <n T : false
Reachability Closed sets of T: [[[t1]], [[t2]], [[t]]]
T is an NMTS: true
Reachability Closed sets of S:[[[s]], [[s2], [s1]], [[s3]]]
S is an NMTS: true
T <n S : false

Figure 8:
Reachability Closed sets of S: [[[si]], [[s2]], [[s], [s1]]]
S is an NMTS: true
Reachability Closed sets of T:[[[t]], [[t1], [t2]], [[ti]], [[t3]]]
T is an NMTS: true
S <n T : false
Reachability Closed sets of T: [[[t]], [[t1], [t2]], [[ti]], [[t3]]]
T is an NMTS: true
Reachability Closed sets of U:[[[u2]], [[u1], [u]], [[ui]]]
U is an NMTS: true
T <n U : true
R_(T <n U) : [[ti, ui], [t, u], [t1, u], [t3, u2], [t2, u1]]
